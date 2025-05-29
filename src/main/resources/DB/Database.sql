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
    book_id     BIGINT NOT NULL,
    borrow_date DATE   NOT NULL,
    due_date    DATE   NOT NULL,
    return_date DATE,
    status      ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    notes       TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (book_id) REFERENCES books (id)
);

-- User logs table
CREATE TABLE user_logs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    action      VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    user_agent VARCHAR(100),
    entity_id   BIGINT,
    details     TEXT,
    ip_address  VARCHAR(45),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);