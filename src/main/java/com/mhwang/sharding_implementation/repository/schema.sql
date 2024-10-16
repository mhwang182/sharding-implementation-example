CREATE TABLE `customers` (
    `id` varchar(50) NOT NULL,
    `firstname` varchar(50) NOT NULL,
    `lastname` varchar(50) NOT NULL,
    `email` varchar(50) NOT NULL,
    `created_at` date DEFAULT NULL,
    PRIMARY KEY (`id`), UNIQUE KEY `uk_email` (`email`)
)

CREATE TABLE `orders` (
    `Id` varchar(50) NOT NULL,
    `customer_id` varchar(50) DEFAULT NULL,
    `product_sku` int NOT NULL,
    `created_at` datetime DEFAULT NULL,
    PRIMARY KEY (`Id`), KEY `customer_id` (`customer_id`)
)




