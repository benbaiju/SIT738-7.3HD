USE finsightdb;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS uploaded_files;
DROP TABLE IF EXISTS bank_transactions;
DROP TABLE IF EXISTS financial_goals;
DROP TABLE IF EXISTS investments;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    fullName VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE expenses (
    id INT NOT NULL AUTO_INCREMENT,
    userId INT,
    description VARCHAR(255),
    category VARCHAR(255),
    amount DOUBLE,
    expenseDate DATE,
    PRIMARY KEY (id)
);

CREATE TABLE investments (
    id INT NOT NULL AUTO_INCREMENT,
    userId INT,
    assetName VARCHAR(255),
    assetType VARCHAR(255),
    country VARCHAR(255),
    quantity DOUBLE,
    purchasePrice DOUBLE,
    currentValue DOUBLE,
    PRIMARY KEY (id)
);

CREATE TABLE loans (
    id INT NOT NULL AUTO_INCREMENT,
    userId INT,
    loanType VARCHAR(255),
    lender VARCHAR(255),
    principalAmount DOUBLE,
    outstandingBalance DOUBLE,
    interestRate DOUBLE,
    monthlyRepayment DOUBLE,
    PRIMARY KEY (id)
);

CREATE TABLE financial_goals (
    id INT NOT NULL AUTO_INCREMENT,
    userId INT,
    goalName VARCHAR(255),
    description VARCHAR(255),
    targetAmount DOUBLE,
    currentAmount DOUBLE,
    targetDate DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE bank_transactions (
    id INT NOT NULL AUTO_INCREMENT,
    userId INT,
    transactionReference VARCHAR(255),
    description VARCHAR(255),
    category VARCHAR(255),
    amount DOUBLE,
    transactionType VARCHAR(255),
    transactionDate DATE,
    PRIMARY KEY (id)
);

CREATE TABLE uploaded_files (
    id INT NOT NULL AUTO_INCREMENT,
    userId INT,
    originalFileName VARCHAR(255),
    storedFileName VARCHAR(255),
    filePath VARCHAR(255),
    fileType VARCHAR(255),
    uploadedAt DATETIME,
    PRIMARY KEY (id)
);
