CREATE DATABASE items OWNER postgres;

\c items;

CREATE TABLE item (
	id serial PRIMARY KEY,
    name varchar(255),
    price double precision not null,
    creationdate timestamp not null,
    description varchar
);

INSERT INTO item (name, price, creation_date, description)
VALUES ('vacuum-cleaner', 300, '2020-11-29 01:04:00', 'automated vaccum-cleaner Samsung');
INSERT INTO item (name, price, creation_date, description)
VALUES ('iphone 6', 400, '2016-10-11 01:04:00', 'metallic color');
INSERT INTO item (name, price, creation_date, description)
VALUES ('macbook', 400, '2021-09-11 01:04:00', 'silver macbook air');
INSERT INTO item (name, price, creation_date, description)
VALUES ('inverter', 1600, '2020-10-01 01:04:00', 'huawei inverter');

CREATE TABLE credentials (
    id SERIAL PRIMARY KEY,
    login varchar(255),
    password varchar(255),
    salt varchar(500),
    firstname varchar(50),
    lastname varchar(50)
);

INSERT INTO credentials (login, password, salt, firstname, lastname)
VALUES('atrubin', '$2a$10$YJ5K7J7u9eroZRcYeeRJmegfDE0QKif27WaXY9HTwjlcGo1xy2ika', '$2a$10$YJ5K7J7u9eroZRcYeeRJme', 'Tolik', 'Trubin');
INSERT INTO users (login, password, salt, firstname, lastname)
VALUES('oohorodnik', '$2a$10$DGBJukC/EkflGgUIU4uF8u6F10RyCVOhdKM6X2ywtLua1/CYHxiCa', '$2a$10$DGBJukC/EkflGgUIU4uF8u', 'Oleksandr', 'Ohorodnik');