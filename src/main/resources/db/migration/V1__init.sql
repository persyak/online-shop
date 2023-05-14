CREATE TABLE item (
	id SERIAL PRIMARY KEY NOT NULL,
    name varchar(255),
    price double precision not null,
    creation_date timestamp not null,
    description varchar
);

CREATE TABLE credentials (
    id SERIAL PRIMARY KEY NOT NULL,
    login varchar(255),
    password varchar(255),
    salt varchar(500),
    firstname varchar(50),
    lastname varchar(50)
);