INSERT INTO users (
    name,
    email,
    password,
    role,
    active,
    created_at,
    updated_at
)
VALUES (
           'System Admin',
           'admin@example.com',
           '$2y$10$73njTeAJXpfZ725pZIehhuqQFqBGmZ//p1UP3tDC4MCDnAej4U9ju',
           'ADMIN',
           true,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );