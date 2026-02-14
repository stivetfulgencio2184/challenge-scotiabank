CREATE TABLE IF NOT EXISTS students (
    pk INTEGER AUTO_INCREMENT PRIMARY KEY,
    id INTEGER,
    name VARCHAR(50),
    last_name VARCHAR(50),
    status BOOLEAN,
    age INTEGER
);

-------------------------------
-- Tables associated to RBAC --
-------------------------------
CREATE TABLE IF NOT EXISTS users (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    enabled BOOLEAN,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS roles (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    abbreviation VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);