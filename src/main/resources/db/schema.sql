CREATE TABLE IF NOT EXISTS students (
    pk INTEGER AUTO_INCREMENT PRIMARY KEY,
    id INTEGER,
    name VARCHAR(50),
    last_name VARCHAR(50),
    status BOOLEAN,
    age INTEGER
);