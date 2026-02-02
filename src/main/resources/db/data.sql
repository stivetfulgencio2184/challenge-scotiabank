INSERT INTO students (id, name, last_name, status, age) VALUES
    (123, 'Jesús', 'Salvador', true, 33),
    (456, 'Stivet', 'Fulgencio', false, 41),
    (789, 'Mary Esther', 'Ruiz', true, 30);

INSERT INTO users (enabled, username, password) VALUES
    (true, 'jsalvador', '$#Jesus2100#$'),
    (true, 'mruiz', '$#Mary0795#$'),
    (false, 'sfulgencio', '$$Stiver2184$$');

INSERT INTO roles (name, abbreviation) VALUES
    ('Administrator', 'admin'),
    ('Database Administrator', 'dba'),
    ('User', 'usr');

INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1),
    (2, 3),
    (3, 2);