USE finsightdb;

ALTER TABLE users
    MODIFY COLUMN password VARCHAR(255) NOT NULL;

-- Run once. Ignore errors if columns already exist.
ALTER TABLE users
    ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

ALTER TABLE users
    ADD COLUMN advisorId INT NULL;

UPDATE users
SET role = 'USER'
WHERE role IS NULL OR TRIM(role) = '';

DROP PROCEDURE IF EXISTS search_expenses_by_description_for_user;

DELIMITER //

CREATE PROCEDURE search_expenses_by_description_for_user(
    IN p_user_id INT,
    IN p_description VARCHAR(255)
)
BEGIN
    SELECT id,
           userId,
           description,
           category,
           amount,
           expenseDate
    FROM expenses
    WHERE userId = p_user_id
      AND description = p_description;
END //

DELIMITER ;

-- Seed accounts. Password for all three: Password1!
-- BCrypt hash generated with Spring BCryptPasswordEncoder.
INSERT INTO users (fullName, email, password, role, advisorId)
SELECT 'FinSight Admin', 'admin@finsight.local',
       '$2a$10$6vL2.ZZtH6Q3MZ91bL8Ej.xx3ehgv9YccmopE9xhDla6iWcEWhoby',
       'ADMIN', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@finsight.local'
);

INSERT INTO users (fullName, email, password, role, advisorId)
SELECT 'FinSight Advisor', 'advisor@finsight.local',
       '$2a$10$6vL2.ZZtH6Q3MZ91bL8Ej.xx3ehgv9YccmopE9xhDla6iWcEWhoby',
       'ADVISOR', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'advisor@finsight.local'
);

INSERT INTO users (fullName, email, password, role, advisorId)
SELECT 'FinSight Client', 'user@finsight.local',
       '$2a$10$6vL2.ZZtH6Q3MZ91bL8Ej.xx3ehgv9YccmopE9xhDla6iWcEWhoby',
       'USER', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'user@finsight.local'
);
