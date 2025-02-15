INSERT INTO users (username, password)
VALUES ('user1', '$2a$10$WOuZxXUOHPad.YA9INStfO/88Vrh.sqbGEoGsgw9dGTq.6cN5vJXi');

INSERT INTO todos (content, completed, created_time, last_modified_time, user_id)
VALUES ('Learn Spring Boot', true, '2024-02-13 10:00:00', '2024-02-13 10:00:00', 1),
       ('Write SQL Scripts', true, '2024-02-13 11:00:00', '2024-02-13 11:30:00', 1),
       ('Build a REST API', true, '2024-02-13 12:00:00', '2024-02-13 12:45:00', 1),
       ('Explore Hibernate', false, '2024-02-13 13:00:00', '2024-02-13 13:15:00', 1),
       ('Create a CRUD App', false, '2024-02-13 14:00:00', '2024-02-13 14:30:00', 1);

INSERT INTO properties (name, type, data, todo_id)
VALUES ('마감일', 'DATE', '{"type":"date", "start":"2025-02-10T00:00:00", "end":null}', 1),
       ('우선순위', 'SELECT', '{"type":"select", "name":"상", "color":"red"}', 1);

INSERT INTO properties (name, type, data, todo_id)
VALUES ('마감일', 'DATE', '{"type":"date", "start":"2025-02-11T00:00:00", "end":null}', 2),
       ('우선순위', 'SELECT', '{"type":"select", "name":"하", "color":"gray"}', 2);

INSERT INTO properties (name, type, data, todo_id)
VALUES ('마감일', 'DATE', '{"type":"date", "start":"2025-02-12T00:00:00", "end":null}', 3),
       ('우선순위', 'SELECT', '{"type":"select", "name":"상", "color":"red"}', 3);

INSERT INTO properties (name, type, data, todo_id)
VALUES ('마감일', 'DATE', '{"type":"date", "start":"2025-02-13T00:00:00", "end":null}', 4),
       ('우선순위', 'SELECT', '{"type":"select", "name":"하", "color":"gray"}', 4);

INSERT INTO properties (name, type, data, todo_id)
VALUES ('마감일', 'DATE', '{"type":"date", "start":"2025-02-14T00:00:00", "end":null}', 5),
       ('우선순위', 'SELECT', '{"type":"select", "name":"중", "color":"blue"}', 5);
