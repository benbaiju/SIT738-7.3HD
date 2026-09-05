CREATE DATABASE IF NOT EXISTS finsightdb;

CREATE USER IF NOT EXISTS 'finsightadmin'@'localhost'
IDENTIFIED BY 'finsightadmin';

GRANT ALL PRIVILEGES ON finsightdb.*
TO 'finsightadmin'@'localhost';

FLUSH PRIVILEGES;