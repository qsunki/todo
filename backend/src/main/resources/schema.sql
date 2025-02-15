CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS todos
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    content            VARCHAR(255) NOT NULL,
    completed          BOOLEAN      NOT NULL,
    created_time       TIMESTAMP    NOT NULL,
    last_modified_time TIMESTAMP    NOT NULL,
    user_id            BIGINT       NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS properties
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    type    VARCHAR(255) NOT NULL,
    data    JSON         NOT NULL,
    todo_id BIGINT       NULL,
    FOREIGN KEY (todo_id) REFERENCES todos (id) ON DELETE CASCADE
);
