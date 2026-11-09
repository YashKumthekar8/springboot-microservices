create table if not exists `cards` (
    `card_id` int AUTO_INCREMENT PRIMARY KEY,
    `mobile_no` varchar(15) NOT NULL,
    `card_no` varchar(100) NOT NULL,
    `card_type` varchar(100) NOT NULL,
    `total_limit` int NOT NULL,
    `amount_used` int NOT NULL,
    `available_amount` int NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);