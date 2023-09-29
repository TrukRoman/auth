INSERT INTO authorized_user (id, email, password, first_name, last_name, role)
SELECT 1, 'admin@gmail.com', '$2a$10$WdtZgZdOkaGNj7cNq5hJbezD1I3J7YhZpMOCqmwTQsvSb38d50n/S', 'Ivan', 'Ivanov', 'ADMIN'
WHERE NOT EXISTS(
        SELECT 1 FROM authorized_user WHERE role = 'ADMIN'
    );
