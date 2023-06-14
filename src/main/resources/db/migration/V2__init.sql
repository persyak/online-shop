CREATE TABLE "user" (
    id SERIAL PRIMARY KEY NOT NULL,
    username varchar(255),
    password varchar(255),
    salt varchar(500),
    firstname varchar(50),
    lastname varchar(50)
);

--CREATE TABLE credentials (
--    id SERIAL PRIMARY KEY NOT NULL,
--    login varchar(255),
--    password varchar(255),
--    salt varchar(500),
--    firstname varchar(50),
--    lastname varchar(50)
--);