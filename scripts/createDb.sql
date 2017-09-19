-- -- -- -- -- -- -- -- --
DROP DATABASE IF EXISTS hotel;
DROP USER IF EXISTS read_write;
-- -- -- -- -- -- -- -- --
CREATE USER read_write WITH PASSWORD '123';
CREATE DATABASE hotel OWNER read_write;
-- -- -- -- -- -- -- -- --
\c hotel;
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- Tables and indexes
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
CREATE TABLE users (
  id           SERIAL PRIMARY KEY UNIQUE,
  name         VARCHAR(64) NOT NULL,
  age          INTEGER,
  phone_number VARCHAR(64) UNIQUE,
  email        VARCHAR(64) NOT NULL UNIQUE
);

CREATE UNIQUE INDEX users_idx
  ON users (id);

CREATE TYPE ACCOUNT_TYPE AS ENUM ('CLIENT', 'MANAGER');

CREATE TABLE accounts (
  id          SERIAL PRIMARY KEY UNIQUE,
  userid      INTEGER REFERENCES users (id) NOT NULL,
  login       VARCHAR(64)                   NOT NULL UNIQUE,
  password    VARCHAR(64)                   NOT NULL,
  "type"      ACCOUNT_TYPE DEFAULT 'CLIENT',
  create_date TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX accounts_idx
  ON accounts (id);

CREATE TYPE APARTMENTS_STATUS AS ENUM ('FREE', 'BOOKED', 'BUSY', 'NOT_AVAILABLE');
CREATE TYPE APARTMENTS_TYPE AS ENUM ('ECONOMY', 'BUSINESS', 'PREMIUM');

CREATE TABLE apartments (
  id           SERIAL PRIMARY KEY UNIQUE,
  number       INTEGER UNIQUE                                         NOT NULL,
  rooms        INTEGER                                                NOT NULL,
  person_count INTEGER CHECK (person_count > 0)                       NOT NULL,
  price        FLOAT CHECK (price > 0)                                NOT NULL,
  --('FREE', 'BOOKED', 'BUSY', 'NOT_AVAILABLE')
  status       APARTMENTS_STATUS DEFAULT 'FREE',
  --('ECONOMY', 'BUSINESS', 'PREMIUM')
  "type"       APARTMENTS_TYPE   DEFAULT 'BUSINESS',
  photo_path   VARCHAR(512)
);

CREATE UNIQUE INDEX apartments_idx
  ON apartments (id);

CREATE TYPE ORDER_STATUS AS ENUM ('UNCHECKED', 'WAITED', 'PAID', 'IN_PROCESS', 'CANCELED', 'DONE');

CREATE TABLE orders (
  id             SERIAL PRIMARY KEY UNIQUE,
  accountid      INTEGER REFERENCES accounts (id) ON DELETE CASCADE                                            NOT NULL,
  apartmentid    INTEGER REFERENCES apartments (id) ON DELETE CASCADE,
  --('UNCHECKED', 'WAITED', 'PAID', 'IN_PROCESS', 'CANCELED', 'DONE')
  status         ORDER_STATUS DEFAULT 'UNCHECKED',
  person_count   INTEGER                                                                                       NOT NULL,
  apartment_type APARTMENTS_TYPE                                                                               NOT NULL,
  "from"         TIMESTAMP                                                                                     NOT NULL,
  "to"           TIMESTAMP CHECK (orders.to > orders.from)                                                     NOT NULL,
  create_date    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX orders_idx
  ON orders (id);
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- Triggers
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
CREATE OR REPLACE FUNCTION update_order()
  RETURNS TRIGGER AS $update_order$
BEGIN
  IF (SELECT status
      FROM apartments
      WHERE id = new.apartmentid) = 'NOT_AVAILABLE'
  THEN
    RETURN new;
  END IF;

  IF new.status = 'WAITED' OR new.status = 'PAID'
  THEN
    UPDATE apartments
    SET status = 'BOOKED'
    WHERE id = new.apartmentid;
  ELSEIF new.status = 'IN_PROCESS'
    THEN
      UPDATE apartments
      SET status = 'BUSY'
      WHERE id = new.apartmentid;
  ELSEIF new.status = 'CANCELED' OR new.status = 'DONE'
    THEN
      UPDATE apartments
      SET status = 'FREE'
      WHERE id = new.apartmentid;
  END IF;
  RETURN new;
END;
$update_order$ LANGUAGE plpgsql;

CREATE TRIGGER update_order
BEFORE INSERT OR UPDATE ON orders
FOR EACH ROW EXECUTE PROCEDURE update_order();
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- Views
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
CREATE VIEW free_apartments AS
  SELECT
    id,
    number,
    rooms,
    person_count,
    price,
    status,
    "type",
    photo_path
  FROM apartments
  WHERE status = 'FREE';
-- -- -- -- -- -- -- -- --
GRANT ALL
ON ALL TABLES IN SCHEMA public
TO read_write;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO read_write;
-- -- -- -- -- -- -- -- --