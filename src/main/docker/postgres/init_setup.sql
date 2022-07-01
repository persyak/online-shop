CREATE DATABASE items OWNER postgres;

\c items;

CREATE TABLE item (
	id serial PRIMARY KEY,
    name varchar(255),
    price double precision not null,
    creationdate timestamp not null,
    description varchar
);

INSERT INTO item (name, price, creationDate, description)
VALUES ('vacuum-cleaner', 300, '2020-11-29 01:04:00', 'automated vaccum-cleaner Samsung');
INSERT INTO item (name, price, creationDate, description)
VALUES ('iphone 6', 400, '2016-10-11 01:04:00', 'metallic color');
INSERT INTO item (name, price, creationDate, description)
VALUES ('macbook', 400, '2021-09-11 01:04:00', 'silver macbook air');
INSERT INTO item (name, price, creationDate, description)
VALUES ('inverter', 1600, '2020-10-01 01:04:00', 'huawei inverter');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login varchar(255),
    password varchar(255),
    firstName varchar(50),
    lastName varchar(50)
);

INSERT INTO users (login, password, firstName, lastName)
VALUES('atrubin', '$2a$08$nbAUf70eUmrC3PTv9i3jw.8Qiy.U/Dy.oHA4pBo/aiJHFPO3JVh5m', 'Tolik', 'Trubin');
INSERT INTO users(login, password, firstName, lastName)
VALUES('oohorodnik', '$2a$08$oA7LnDR4.bZL0gTjQBc4M.KIxERJBk2MaOT0pSfWBZCOGldP5RPAW', 'Oleksandr', 'Ohorodnik');