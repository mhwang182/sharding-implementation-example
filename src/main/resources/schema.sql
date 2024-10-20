
--CREATE SCHEMA IF NOT EXISTS SHARD1;
--CREATE SCHEMA IF NOT EXISTS SHARD2;
--
--DROP TABLE IF EXISTS SHARD1.orders;
--DROP TABLE IF EXISTS SHARD1.customers;
--
--DROP TABLE IF EXISTS SHARD2.orders;
--DROP TABLE IF EXISTS SHARD2.customers;

DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id varchar(50) NOT NULL PRIMARY KEY,
    firstname varchar(50) NOT NULL,
    lastname varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    created_at datetime DEFAULT NULL,
    CONSTRAINT uk_email UNIQUE (email)
);

CREATE TABLE orders (
    Id varchar(50) NOT NULL PRIMARY KEY,
    customer_id varchar(50) DEFAULT NULL,
    product_sku int NOT NULL,
    created_at datetime DEFAULT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

--CREATE TABLE SHARD2.customers (
--    id varchar(50) NOT NULL PRIMARY KEY,
--    firstname varchar(50) NOT NULL,
--    lastname varchar(50) NOT NULL,
--    email varchar(50) NOT NULL,
--    created_at datetime DEFAULT NULL,
--    CONSTRAINT uk_email UNIQUE (email)
--);
--
--CREATE TABLE SHARD2.orders (
--    Id varchar(50) NOT NULL PRIMARY KEY,
--    customer_id varchar(50) DEFAULT NULL,
--    product_sku int NOT NULL,
--    created_at datetime DEFAULT NULL,
--    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
--);








