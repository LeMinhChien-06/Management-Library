package com.example.management.aspect;

import com.example.management.annotation.TrackAction;
import com.example.management.constants.MessageCode;
import com.example.management.entity.User;
import com.example.management.exception.user.UserExceptions;
import com.example.management.repository.UserRepository;
import com.example.management.service.DeviceDetectionService;
import com.example.management.service.UserLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final UserLogService userLogService;
    private final DeviceDetectionService deviceDetectionService;
    private final UserRepository userRepository;
    private final ExpressionParser parser = new SpelExpressionParser();

    @AfterReturning(value = "@annotation(trackAction)", returning = "result")
    public void trackAction(JoinPoint joinPoint, TrackAction trackAction, Object result) {
        try {
            if (!trackAction.enabled()) {
                return;
            }

            User currentUser = getCurrentUser();
            if (currentUser == null) {
                log.warn("No authenticated user found for audit");
                return;
            }

            HttpServletRequest request = getCurrentRequest();
            if (request == null) {
                log.warn("No HTTP request found for audit");
                return;
            }

            String ipAddress = deviceDetectionService.extractClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");

            Long entityId = evaluateEntityId(trackAction.entityId(), joinPoint, result);
            String entityName = evaluateEntityName(trackAction.entityName(), joinPoint, result);

            String details = createDetails(trackAction, entityName);

            userLogService.logAction(currentUser, trackAction.action().name(), trackAction.entityType().name(), entityId, details, ipAddress, userAgent);

            log.debug("Audit logged: {} by {} on {}:{}", trackAction.action(), currentUser.getUsername(), trackAction.entityType(), entityId);

        } catch (Exception e) {
            log.error("Error in audit aspect", e);
        }
    }

    private User getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth != null && auth.getPrincipal() instanceof User) {
                return (User) auth.getPrincipal();
            }
            
            // Handle JWT case
            if (auth != null && auth.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) auth.getPrincipal();
                String username = jwt.getSubject(); // get username from token subject claim
                
                if (username != null) {
                    return userRepository.findByUsername(username).orElseThrow(UserExceptions::userNotFound);
                }
            }
            
        } catch (Exception e) {
            log.warn("Error getting current user", e);
        }
        return null;
    }

    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            log.warn("Error getting current request", e);
            return null;
        }
    }

    private Long evaluateEntityId(String expression, JoinPoint joinPoint, Object result) {
        if (expression == null || expression.trim().isEmpty()) {
            return null;
        }

        try {
            EvaluationContext context = createEvaluationContext(joinPoint, result);
            Expression exp = parser.parseExpression(expression);
            Object value = exp.getValue(context);

            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        } catch (Exception e) {
            log.warn("Failed to evaluate entityId expression: {}", expression, e);
        }
        return null;
    }

    private String evaluateEntityName(String expression, JoinPoint joinPoint, Object result) {
        if (expression == null || expression.trim().isEmpty()) {
            return null;
        }

        try {
            EvaluationContext context = createEvaluationContext(joinPoint, result);
            Expression exp = parser.parseExpression(expression);
            Object value = exp.getValue(context);

            return value != null ? value.toString() : null;
        } catch (Exception e) {
            log.warn("Failed to evaluate entityName expression: {}", expression, e);
        }
        return null;
    }

    private EvaluationContext createEvaluationContext(JoinPoint joinPoint, Object result) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable("arg" + i, args[i]);
        }

        context.setVariable("result", result);

        return context;
    }

    private String createDetails(TrackAction trackAction, String entityName) {
        String actionDescription = trackAction.action().getDescription();

        if (entityName != null && !entityName.trim().isEmpty()) {
            return String.format("%s: '%s'", actionDescription, entityName);
        } else {
            return actionDescription;
        }
    }
}