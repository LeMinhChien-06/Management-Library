-- Users table
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    full_name  VARCHAR(100)        NOT NULL,
    phone      VARCHAR(20),
    role       ENUM('ADMIN', 'LIBRARIAN', 'USER') DEFAULT 'USER',
    active     BOOLEAN   DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Categories table
CREATE TABLE categories
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE books
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    title              VARCHAR(255) NOT NULL,
    author             VARCHAR(255) NOT NULL,
    isbn               VARCHAR(20) UNIQUE,
    category_id        BIGINT,
    qr_code            VARCHAR(255) UNIQUE,
    total_quantity     INT       DEFAULT 0,
    available_quantity INT       DEFAULT 0,
    publication_year   INT,
    publisher          VARCHAR(255),
    description        TEXT,
    image_url          VARCHAR(500),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

-- Borrowings table
CREATE TABLE borrowings
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    borrow_date DATE   NOT NULL,
    due_date    DATE   NOT NULL,
    return_date DATE,
    status enum('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    notes       TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- borrowing_details table
CREATE TABLE borrowing_details
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    borrowing_id BIGINT NOT NULL,
    book_id      BIGINT NOT NULL,
    status       ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    FOREIGN KEY (borrowing_id) REFERENCES borrowings (id),
    FOREIGN KEY (book_id) REFERENCES books (id)
);

-- User logs table
CREATE TABLE user_logs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    action      VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    user_agent  VARCHAR(100),
    entity_id   BIGINT,
    details     TEXT,
    ip_address  VARCHAR(45),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX       idx_user_logs_user_id (user_id),
    INDEX       idx_user_logs_action (action),
    INDEX       idx_user_logs_entity (entity_type, entity_id),
    INDEX       idx_user_logs_created_at (created_at),


    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- User sessions table
CREATE TABLE user_sessions
(
    session_id       VARCHAR(36) PRIMARY KEY,
    username         VARCHAR(50) NOT NULL,
    created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at       TIMESTAMP   NOT NULL,
    active           BOOLEAN     NOT NULL DEFAULT TRUE,
    ip_address       VARCHAR(45),
    user_agent       VARCHAR(500),
    device_info      VARCHAR(100),
    last_accessed_at TIMESTAMP,

    INDEX            idx_username_active (username, active),
    INDEX            idx_session_id_active (session_id, active),
    INDEX            idx_expires_at (expires_at),
    INDEX            idx_created_at (created_at),

    FOREIGN KEY (username) REFERENCES users (username)
        ON DELETE CASCADE ON UPDATE CASCADE
);