CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email_verification_token VARCHAR(255),
                       email_verification_expiration_date TIMESTAMP,
                       email_verified BOOLEAN DEFAULT FALSE,
                       password_reset_token VARCHAR(255),
                       password_reset_token_expiration_date TIMESTAMP,
                       firstname VARCHAR(100),
                       lastname VARCHAR(100),
                       company VARCHAR(255),
                       position VARCHAR(255),
                       location VARCHAR(255),
                       profile_picture VARCHAR(255),
                       profile_complete BOOLEAN DEFAULT FALSE
);
CREATE TABLE posts (
                       id SERIAL PRIMARY KEY,
                       content VARCHAR(255) NOT NULL,
                       picture VARCHAR(255),
                       author_id INT NOT NULL,
                       FOREIGN KEY (author_id) REFERENCES users(id)
);