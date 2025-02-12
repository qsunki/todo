CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS todos
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL,
    created_time TIMESTAMP NOT NULL,
    last_modified_time TIMESTAMP NOT NULL,
    user_id BIGINT       NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
