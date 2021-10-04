CREATE DATABASE IF NOT EXISTS HybrisTechTask;
CREATE SCHEMA IF NOT EXISTS HybrisTechTask;
USE HybrisTechTask;
USE HybrisTechTask;

CREATE TABLE IF NOT EXISTS `orders` (
                                               `id` INT NOT NULL AUTO_INCREMENT,
                                               `user_id` INT NOT NULL,
                                               `order_status` ENUM('created', 'in_progress', 'delivered') NOT NULL,
                                               `created_at` VARCHAR(45) NOT NULL,
                                               PRIMARY KEY (`id`),
                                               UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `products` (
                                                 `id` INT NOT NULL AUTO_INCREMENT,
                                                 `name` VARCHAR(255) NOT NULL,
                                                 `price` INT NOT NULL,
                                                 `created_at` DATETIME NOT NULL,
                                                 `product_status` ENUM('out_of_stock', 'in_stock', 'running_low') NOT NULL,
                                                 PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `orders_has_products` (
                                                            `orders_id` INT NOT NULL,
                                                            `products_id` INT NOT NULL,
                                                            `quantity` INT NOT NULL,
                                                            PRIMARY KEY (`orders_id`, `products_id`),
                                                            INDEX `fk_orders_has_products_products1_idx` (`products_id` ASC) VISIBLE,
                                                            INDEX `fk_orders_has_products_orders1_idx` (`orders_id` ASC) VISIBLE,
                                                            CONSTRAINT `fk_orders_has_products_orders1`
                                                                FOREIGN KEY (`orders_id`)
                                                                    REFERENCES `orders` (`id`)
                                                                    ON DELETE CASCADE
                                                                    ON UPDATE CASCADE,
                                                            CONSTRAINT `fk_orders_has_products_products1`
                                                                FOREIGN KEY (`products_id`)
                                                                    REFERENCES `products` (`id`)
                                                                    ON DELETE CASCADE
                                                                    ON UPDATE CASCADE);

INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Mouse', 44, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Monitor', 2000, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Keyboard', 254, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Mousepad', 100, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Speaker', 500, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Cooler', 678, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Processor', 3000, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Videocard', 2354, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'Cables', 40, now(), 'in_stock');
INSERT INTO products (id, name, price, created_at, product_status) VALUES(DEFAULT, 'HDD', 690, now(), 'in_stock');



