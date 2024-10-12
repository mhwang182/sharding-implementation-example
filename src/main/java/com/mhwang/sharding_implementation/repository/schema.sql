CREATE TABLE customers (
    Id INT PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    created_at DATE
);

CREATE TABLE orders (
    Id INT PRIMARY KEY,
    customer_id INT NOT NULL,
    product_sku INT NOT NULL,
    created_at DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(Id)
)


