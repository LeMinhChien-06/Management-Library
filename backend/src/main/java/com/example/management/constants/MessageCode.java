package com.example.management.constants;

import lombok.Getter;

@Getter
public enum MessageCode {

    // SUCCESS MESSAGES (2xx)
    SUCCESS("MSG_SUCCESS", "Thành công", 200),
    APPROVE_SUCCESS("MSG_SUCCESS", "Duyệt thành công", 200),
    REJECT_SUCCESS("MSG_SUCCESS", "Từ chối thành công", 200),
    CREATED_SUCCESS("MSG_CREATED_SUCCESS", "Tạo mới thành công", 201),
    UPDATED_SUCCESS("MSG_UPDATED_SUCCESS", "Cập nhật thành công", 200),
    DELETED_SUCCESS("MSG_DELETED_SUCCESS", "Xóa thành công", 200),

    // USER SUCCESS MESSAGES (2xx)
    USER_CREATED_SUCCESS("MSG_USER_CREATED_SUCCESS", "Tạo người dùng thành công", 201),
    USER_UPDATED_SUCCESS("MSG_USER_UPDATED_SUCCESS", "Cập nhật người dùng thành công", 200),
    USER_DELETED_SUCCESS("MSG_USER_DELETED_SUCCESS", "Xóa người dùng thành công", 200),
    USER_LIST_SUCCESS("MSG_USER_LIST_SUCCESS", "Lấy danh sách người dùng thành công", 200),
    USER_DETAIL_SUCCESS("MSG_USER_DETAIL_SUCCESS", "Lấy thông tin người dùng thành công", 200),

    // CATEGORY SUCCESS MESSAGES (2xx)
    CATEGORY_CREATED_SUCCESS("MSG_CATEGORY_CREATED_SUCCESS", "Tạo thể loại thành công", 201),
    CATEGORY_UPDATED_SUCCESS("MSG_CATEGORY_UPDATED_SUCCESS", "Cập nhật thể loại thành công", 200),
    CATEGORY_DELETED_SUCCESS("MSG_CATEGORY_DELETED_SUCCESS", "Xóa thể loại thành công", 200),
    CATEGORY_LIST_SUCCESS("MSG_CATEGORY_LIST_SUCCESS", "Lấy danh sách thể loại thành công", 200),
    CATEGORY_DETAIL_SUCCESS("MSG_CATEGORY_DETAIL_SUCCESS", "Lấy thông tin thể loại thành công", 200),

    // BOOK SUCCESS MESSAGES (2xx)
    BOOK_CREATED_SUCCESS("MSG_BOOK_CREATED_SUCCESS", "Tạo sách thành công", 201),
    BOOK_UPDATED_SUCCESS("MSG_BOOK_UPDATED_SUCCESS", "Cập nhật sách thành công", 200),
    BOOK_DELETED_SUCCESS("MSG_BOOK_DELETED_SUCCESS", "Xóa sách thành công", 200),
    BOOK_LIST_SUCCESS("MSG_BOOK_LIST_SUCCESS", "Lấy danh sách sách thành công", 200),
    BOOK_DETAIL_SUCCESS("MSG_BOOK_DETAIL_SUCCESS", "Lấy thông tin sách thành công", 200),
    BOOK_SEARCH_SUCCESS("MSG_BOOK_SEARCH_SUCCESS", "Tìm kiếm sách thành công", 200),
    BOOK_SEARCH_BY_CATEGORY_SUCCESS("MSG_BOOK_SEARCH_BY_CATEGORY_SUCCESS", "Tìm kiếm sách theo loại thành công", 200),

    // BORROW SUCCESS MESSAGES (2xx)
    BORROW_CREATED_SUCCESS("MSG_BOOK_CREATED_SUCCESS", "Tạo phiếu mượn sách thành công", 201),
    RETURN_BOOK_SUCCESS("MSG_RETURN_BOOK_SUCCESS", "Trả sách thành công", 200),

    // LOGIN/AUTH SUCCESS MESSAGES (2xx)
    LOGIN_SUCCESS("MSG_LOGIN_SUCCESS", "Đăng nhập thành công", 200),
    LOGOUT_SUCCESS("MSG_LOGOUT_SUCCESS", "Đăng xuất thành công", 200),
    REGISTER_SUCCESS("MSG_REGISTER_SUCCESS", "Đăng ký tài khoản thành công", 201),
    PASSWORD_CHANGED_SUCCESS("MSG_PASSWORD_CHANGED_SUCCESS", "Đổi mật khẩu thành công", 200),
    PASSWORD_RESET_SUCCESS("MSG_PASSWORD_RESET_SUCCESS", "Khôi phục mật khẩu thành công", 200),
    EMAIL_VERIFIED_SUCCESS("MSG_EMAIL_VERIFIED_SUCCESS", "Xác thực email thành công", 200),


    // FILE OPERATION SUCCESS MESSAGES (2xx)
    FILE_UPLOAD_SUCCESS("MSG_FILE_UPLOAD_SUCCESS", "Tải file lên thành công", 201),
    FILE_DELETE_SUCCESS("MSG_FILE_DELETE_SUCCESS", "Xóa file thành công", 200),
    FILE_DOWNLOAD_SUCCESS("MSG_FILE_DOWNLOAD_SUCCESS", "Tải file xuống thành công", 200),

    // ROLE/PERMISSION SUCCESS MESSAGES (2xx)
    ROLE_CREATED_SUCCESS("MSG_ROLE_CREATED_SUCCESS", "Tạo vai trò thành công", 201),
    ROLE_UPDATED_SUCCESS("MSG_ROLE_UPDATED_SUCCESS", "Cập nhật vai trò thành công", 200),
    ROLE_DELETED_SUCCESS("MSG_ROLE_DELETED_SUCCESS", "Xóa vai trò thành công", 200),
    PERMISSION_GRANTED_SUCCESS("MSG_PERMISSION_GRANTED_SUCCESS", "Cấp quyền thành công", 200),
    PERMISSION_REVOKED_SUCCESS("MSG_PERMISSION_REVOKED_SUCCESS", "Thu hồi quyền thành công", 200),

    // CLIENT ERROR MESSAGES (4xx)
    BAD_REQUEST("ERR_BAD_REQUEST", "Yêu cầu không hợp lệ", 400),
    VALIDATION_ERROR("ERR_VALIDATION", "Dữ liệu không hợp lệ", 400),
    REQUIRED_FIELD("ERR_REQUIRED_FIELD", "Trường này là bắt buộc", 400),
    INVALID_EMAIL("ERR_INVALID_EMAIL", "Định dạng email không hợp lệ", 400),
    INVALID_PHONE("ERR_INVALID_PHONE", "Định dạng số điện thoại không hợp lệ", 400),
    PASSWORD_TOO_SHORT("ERR_PASSWORD_TOO_SHORT", "Mật khẩu phải có ít nhất 8 ký tự", 400),
    PASSWORD_TOO_WEAK("ERR_PASSWORD_TOO_WEAK", "Mật khẩu phải chứa ít nhất một chữ hoa, chữ thường và số", 400),
    PASSWORD_INVALID("MSG_PASSWORD_INVALID", "Mật khẩu không hợp lệ", 400),
    INVALID_DATE_FORMAT("ERR_INVALID_DATE_FORMAT", "Định dạng ngày tháng không hợp lệ", 400),
    INVALID_NUMBER_FORMAT("ERR_INVALID_NUMBER_FORMAT", "Định dạng số không hợp lệ", 400),
    DATA_NULL_ERROR("ERR_DATA_NULL", "Dữ liệu không được truyền vào", 400),
    USERNAME_NOT_EXIST("ERR_USERNAME_NOT_EXIST", "Tên đăng nhập không tồn tại", 400),
    REQUIRED_USERNAME("ERR_REQUIRED_USERNAME", "Tên đăng nhập không được để trống", 400),
    REQUIRED_MIN_USERNAME_MAX("ERR_REQUIRED_MIN_USERNAME_MAX", "Tên đăng nhập phải phải từ 4 đến 50 ký tự", 400),
    REQUIRED_PASSWORD("ERR_REQUIRED_PASSWORD", "Mật khẩu không được để trống", 400),
    REQUIRED_EMAIL("ERR_REQUIRED_EMAIL", "Email không được để trống", 400),
    REQUIRED_PHONE("ERR_REQUIRED_PHONE", "Số điện thoại không được để trống", 400),
    REQUIRED_FULL_NAME("ERR_REQUIRED_FULL_NAME", "Họ và tên không được để trống", 400),
    REQUIRED_PAGE_NUMBER("ERR_REQUIRED_PAGE_NUMBER", "Số trang không được để trống", 400),
    REQUIRED_PAGE_SIZE("ERR_REQUIRED_PAGE_SIZE", "Kích thước trang không được để trống", 400),
    REQUIRED_SORT_FIELD("ERR_REQUIRED_SORT_FIELD", "Trường sắp xếp không được để trống", 400),
    REQUIRED_CATEGORY_NAME("ERR_REQUIRED_CATEGORY_NAME", "Tên thể loại không được để trống", 400),
    REQUIRED_BOOK_NAME("ERR_REQUIRED_BOOK_NAME", "Tên sách không được để trống", 400),
    REQUIRED_BOOK_AUTHOR("ERR_REQUIRED_BOOK_AUTHOR", "Tên tác giả không được để trống", 400),

    // AUTH ERROR MESSAGES (401)
    UNAUTHORIZED("ERR_UNAUTHORIZED", "Chưa xác thực", 401),
    INVALID_CREDENTIALS("ERR_INVALID_CREDENTIALS", "Thông tin đăng nhập không chính xác", 401),
    TOKEN_EXPIRED("ERR_TOKEN_EXPIRED", "Token đã hết hạn", 401),
    TOKEN_INVALID("ERR_TOKEN_INVALID", "Token không hợp lệ", 401),
    TOKEN_NOT_CREATED("ERR_TOKEN_NOT_CREATED", "Token không được tạo", 401),
    SESSION_EXPIRED("ERR_SESSION_EXPIRED", "Phiên đăng nhập đã hết hạn", 401),


    // FORBIDDEN ERROR MESSAGES (403)
    FORBIDDEN("ERR_FORBIDDEN", "Không có quyền truy cập", 403),
    ACCOUNT_DISABLED("ERR_ACCOUNT_DISABLED", "Tài khoản đã bị vô hiệu hóa", 403),
    ACCOUNT_LOCKED("ERR_ACCOUNT_LOCKED", "Tài khoản đã bị khóa", 403),
    INSUFFICIENT_PERMISSIONS("ERR_INSUFFICIENT_PERMISSIONS", "Không đủ quyền thực hiện", 403),
    PERMISSION_DENIED("ERR_PERMISSION_DENIED", "Không có quyền thực hiện thao tác này", 403),
    ACCESS_DENIED("ERR_ACCESS_DENIED", "Từ chối truy cập", 403),

    // NOT FOUND ERROR MESSAGES (404)
    NOT_FOUND("ERR_NOT_FOUND", "Không tìm thấy tài nguyên", 404),
    USER_NOT_FOUND("ERR_USER_NOT_FOUND", "Người dùng không tồn tại", 404),
    ROLE_NOT_FOUND("ERR_ROLE_NOT_FOUND", "Vai trò không tồn tại", 404),
    FILE_NOT_FOUND("ERR_FILE_NOT_FOUND", "File không tồn tại", 404),
    PAGE_NOT_FOUND("ERR_PAGE_NOT_FOUND", "Trang không tồn tại", 404),
    ENDPOINT_NOT_FOUND("ERR_ENDPOINT_NOT_FOUND", "Endpoint không tồn tại", 404),

    // CATEGORY
    CATEGORY_NOT_FOUND("ERR_CATEGORY_NOT_FOUND", "Thể loại không tồn tại", 404),

    // BOOK
    BOOK_NOT_FOUND("ERR_BOOK_NOT_FOUND", "Sách không tồn tại", 404),

    // BORROWING MƯỢN SÁCH
    BORROW_NOT_FOUND("ERR_BORROW_NOT_FOUND", "Không tìm thấy phiếu mượn", 404),
    BOOk_OUT_OF_STOCK("ERR_BOOK_OUT_OF_STOCK", "Sách hết hàng", 400),
    BORROW_BOOK_MAX("ERR_BORROW_BOOK_MAX", "Bạn chỉ được mượn tối đa 5 cuốn sách trong 1 phiếu!", 400),
    MAX_BORROW_LIMIT("ERR_MAX_BORROW_LIMIT", "Đã vượt quá giới hạn mượn sách", 400),
    ALREADY_BORROW("ERR_ALREADY_BORROW", "Bạn đã mượn sách này rồi", 400),
    INVALID_DUE_DATE("ERR_INVALID_DUE_DATE", "Ngày trả sách không vượt quá 30 ngày từ khi mượn sách", 400),
    INVALID_STATUS("ERR_INVALID_STATUS", "trạng thái không hợp lệ", 400),
    REQUIRED_DUE_DATE("ERR_REQUIRED_DUE_DATE", "Ngày trả không được để trống", 400),
    NO_BOOK_RETURN("ERR_NO_BOOK_RETURN", "Không có sách nào để trả", 400),

    // CONFLICT ERROR MESSAGES (409)
    CONFLICT("ERR_CONFLICT", "Dữ liệu đã tồn tại", 409),
    USER_ALREADY_EXISTS("ERR_USER_ALREADY_EXISTS", "Người dùng đã tồn tại", 409),
    EMAIL_ALREADY_EXISTS("ERR_EMAIL_ALREADY_EXISTS", "Email đã được sử dụng", 409),
    USERNAME_ALREADY_EXISTS("ERR_USERNAME_ALREADY_EXISTS", "Tên đăng nhập đã được sử dụng", 409),
    ROLE_ALREADY_EXISTS("ERR_ROLE_ALREADY_EXISTS", "Vai trò đã tồn tại", 409),
    PHONE_ALREADY_EXISTS("ERR_PHONE_ALREADY_EXISTS", "Số điện thoại đã được sử dụng", 409),

    // UNPROCESSABLE ENTITY (422)
    UNPROCESSABLE_ENTITY("ERR_UNPROCESSABLE_ENTITY", "Dữ liệu không thể xử lý", 422),
    INVALID_FILE_TYPE("ERR_INVALID_FILE_TYPE", "Định dạng file không được hỗ trợ", 422),
    FILE_TOO_LARGE("ERR_FILE_TOO_LARGE", "Kích thước file quá lớn", 422),
    INVALID_PAGE_SIZE("ERR_INVALID_PAGE_SIZE", "Kích thước trang không hợp lệ", 422),
    BUSINESS_RULE_VIOLATION("ERR_BUSINESS_RULE_VIOLATION", "Vi phạm quy tắc nghiệp vụ", 422),

    // TOO MANY REQUESTS (429)
    TOO_MANY_REQUESTS("ERR_TOO_MANY_REQUESTS", "Quá nhiều yêu cầu", 429),
    RATE_LIMIT_EXCEEDED("ERR_RATE_LIMIT_EXCEEDED", "Vượt quá giới hạn tần suất", 429),

    // SERVER ERROR MESSAGES (5xx)
    INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER", "Lỗi hệ thống", 500),
    DATABASE_ERROR("ERR_DATABASE", "Lỗi cơ sở dữ liệu", 500),
    CONNECTION_ERROR("ERR_CONNECTION", "Lỗi kết nối", 500),
    PROCESSING_ERROR("ERR_PROCESSING", "Lỗi xử lý dữ liệu", 500),

    // BAD GATEWAY (502)
    BAD_GATEWAY("ERR_BAD_GATEWAY", "Lỗi gateway", 502),
    EXTERNAL_SERVICE_ERROR("ERR_EXTERNAL_SERVICE", "Lỗi dịch vụ bên ngoài", 502),

    // SERVICE UNAVAILABLE (503)
    SERVICE_UNAVAILABLE("ERR_SERVICE_UNAVAILABLE", "Dịch vụ không khả dụng", 503),
    MAINTENANCE_MODE("ERR_MAINTENANCE_MODE", "Hệ thống đang bảo trì", 503),

    // GATEWAY TIMEOUT (504)
    GATEWAY_TIMEOUT("ERR_GATEWAY_TIMEOUT", "Hết thời gian chờ gateway", 504),
    TIMEOUT_ERROR("ERR_TIMEOUT", "Hết thời gian chờ", 504),
    REQUEST_TIMEOUT("ERR_REQUEST_TIMEOUT", "Hết thời gian chờ yêu cầu", 408),

    // UNSUPPORTED OPERATIONS
    UNSUPPORTED_OPERATION("ERR_UNSUPPORTED_OPERATION", "Thao tác không được hỗ trợ", 405),
    METHOD_NOT_ALLOWED("ERR_METHOD_NOT_ALLOWED", "Phương thức không được phép", 405);


    private final String code;
    private final String message;
    private final Integer statusCode;

    MessageCode(String code, String message, Integer statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public static MessageCode fromCode(String code) {
        for (MessageCode messageCode : values()) {
            if (messageCode.getCode().equals(code)) {
                return messageCode;
            }
        }
        throw new IllegalArgumentException("Unknown message code: " + code);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    public boolean isServerError() {
        return statusCode >= 500 && statusCode < 600;
    }

}
