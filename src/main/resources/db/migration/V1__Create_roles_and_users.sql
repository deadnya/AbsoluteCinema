CREATE TABLE roles (
    id UUID PRIMARY KEY,
    role VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    age INT,
    gender VARCHAR(255)
);

CREATE TABLE users_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

INSERT INTO roles (id, role) VALUES ('00000000-0000-0000-0000-000000000001', 'ADMIN');
INSERT INTO roles (id, role) VALUES ('00000000-0000-0000-0000-000000000002', 'USER');

INSERT INTO users (id, email, password, first_name, last_name, age, gender)
VALUES ('00000000-0000-0000-0000-000000000003', 'admin@admin.com', '$2a$12$7adQq6PI0ggL.mLbvTO1duFvnoD9KQ4.qq8JQF0wPh1Xykls4kbLy', 'Admin', 'User', NULL, NULL);

INSERT INTO users_roles (user_id, role_id) VALUES ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001');