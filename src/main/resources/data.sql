INSERT INTO roles (name, description) VALUES ('USER', 'regular user');
INSERT INTO roles (name, description) VALUES ('ADMIN', 'super user');

INSERT INTO products (name, brand, price) VALUES ('macbook air', 'apple', 1245.89);
INSERT INTO products (name, brand, price) VALUES ('soundbar ultra', 'sony', 245.89);

INSERT INTO users (username, email, password) VALUES ('admin', 'admin@gmail.com', '$2a$10$rrFWxUsUBgEyDz7Pc/JwM.8hg5v3Bm5x3A/SENH1Wp7Lh8yAtPRIe');
INSERT INTO users_role(user_id, role_id) VALUES (1, 2);

INSERT INTO users (username, email, password) VALUES ('jhoan', 'jhoan@gmail.com', '$2a$10$7akbf6qvdkVDHFSOkHfl9OUv2p5gQgIzRq0RR1ZOi7.qswBKpIRQS');
INSERT INTO users_role(user_id, role_id) VALUES (2, 1);