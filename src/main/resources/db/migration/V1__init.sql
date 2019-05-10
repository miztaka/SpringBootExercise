--
-- Table structure for table `User`
--

CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL UNIQUE,
  `sub` VARCHAR(255) NOT NULL UNIQUE,
  `created_at` DATETIME NOT NULL,
  `updated_at` TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `Talk`
--

CREATE TABLE `talks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `status` ENUM('draft', 'submitted') DEFAULT 'submitted',
  `prized` TINYINT(1) DEFAULT false,
  `topic` VARCHAR(80) NOT NULL,
  `description` TEXT NOT NULL,
  `session_date` DATE NOT NULL,
  `submitted_at` DATETIME NOT NULL,
  `updated_at` TIMESTAMP NOT NULL,
  `ipaddr` VARCHAR(255),
  `hostname` VARCHAR(255),
  `os` VARCHAR(255),
  `browser` VARCHAR(255),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE talks
  ADD FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON UPDATE RESTRICT
  ON DELETE RESTRICT
;
