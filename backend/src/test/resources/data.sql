INSERT INTO users (username, password)
VALUES ('user1', '$2a$10$WOuZxXUOHPad.YA9INStfO/88Vrh.sqbGEoGsgw9dGTq.6cN5vJXi');

INSERT INTO todos (content, completed, created_time, last_modified_time, user_id)
VALUES ('Learn Spring Boot', true, '2024-02-13 10:00:00', '2024-02-13 10:00:00', 1),
       ('Write SQL Scripts', true, '2024-02-13 11:00:00', '2024-02-13 11:30:00', 1),
       ('Build a REST API', true, '2024-02-13 12:00:00', '2024-02-13 12:45:00', 1),
       ('Explore Hibernate', false, '2024-02-13 13:00:00', '2024-02-13 13:15:00', 1),
       ('Create a CRUD App', false, '2024-02-13 14:00:00', '2024-02-13 14:30:00', 1);
