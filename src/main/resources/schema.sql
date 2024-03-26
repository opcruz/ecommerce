DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY,
    `nif`          VARCHAR(20) NOT NULL,
    `name`         VARCHAR(30) NOT NULL,
    `surnames`     VARCHAR(30) NOT NULL,
    `direction`    VARCHAR(50),
    `province`     VARCHAR(20),
    `cp`           VARCHAR(10),
    `phone`        VARCHAR(10),
    `email`        VARCHAR(30) NOT NULL,
    `passwordhash` VARCHAR(45) NOT NULL
);

-- Table structure for table `employees`
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY,
    `nif`          VARCHAR(20) NOT NULL,
    `name`         VARCHAR(30) NOT NULL,
    `surnames`     VARCHAR(30) NOT NULL,
    `direction`    VARCHAR(50),
    `province`     VARCHAR(20),
    `cp`           VARCHAR(10),
    `phone`        VARCHAR(10),
    `email`        VARCHAR(30) NOT NULL,
    `passwordhash` VARCHAR(45) NOT NULL
);

-- Table structure for table `order`
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`
(
    `id`             INT AUTO_INCREMENT PRIMARY KEY,
    `client_id`      INT         NOT NULL,
    `status`         VARCHAR(10) NOT NULL,
    `payment_method` VARCHAR(15) NOT NULL,
    `total`          FLOAT       NOT NULL,
    `created_at`     TIMESTAMP   NOT NULL,
    CONSTRAINT `order_client_id_fk` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)
);

-- Table structure for table `stock`
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`
(
    `code`        INT AUTO_INCREMENT PRIMARY KEY,
    `description` VARCHAR(200) NOT NULL,
    `image`       BLOB,
    `color`       VARCHAR(20),
    `category`    VARCHAR(20)  NOT NULL,
    `quantity`    INT          NOT NULL,
    `price`       FLOAT        NOT NULL,
    `status`      VARCHAR(20)  NOT NULL,
    `created_by`  INT          NOT NULL,
    `updated_by`  INT          NOT NULL,
    CONSTRAINT `created_by_employees_fk` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`),
    CONSTRAINT `updated_by_employees_fk` FOREIGN KEY (`updated_by`) REFERENCES `employees` (`id`)
);

-- Table structure for table `order_details`
DROP TABLE IF EXISTS `order_details`;
CREATE TABLE `order_details`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY,
    `order_id`     INT    NOT NULL,
    `product_code` INT    NOT NULL,
    `quantity`     INT    NOT NULL DEFAULT '1',
    `price`        DOUBLE NOT NULL,
    CONSTRAINT `order_details_order_id_fk` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `order_details_product_code_fk` FOREIGN KEY (`product_code`) REFERENCES `stock` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Table structure for table `shopping_cart`
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`
(
    `id`           INT AUTO_INCREMENT PRIMARY KEY,
    `product_code` INT NOT NULL,
    `quantity`     INT NOT NULL DEFAULT '1',
    `client_id`    INT NOT NULL,
    UNIQUE (`client_id`, `product_code`),
    CONSTRAINT `client_id_fk` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `product_code_fk` FOREIGN KEY (`product_code`) REFERENCES `stock` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO employees (nif, name, surnames, direction, province, cp, phone, email, passwordhash)
VALUES ('12345678A', 'John', 'Doe', '123 Main St', 'Example Province', '12345', '1234567890', 'john.doe@example.com', '202CB962AC59075B964B07152D234B70');
