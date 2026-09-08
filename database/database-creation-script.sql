USE finsightdb;

ALTER TABLE users
    MODIFY COLUMN password VARCHAR(255) NOT NULL;

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
