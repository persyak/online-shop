CREATE TABLE IF NOT EXISTS credentials (
    id SERIAL PRIMARY KEY NOT NULL,
    username varchar(255),
    password varchar(255),
    firstname varchar(50),
    lastname varchar(50),
    role varchar(255)
);