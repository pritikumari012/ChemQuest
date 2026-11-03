
-- Create ChemQuest database
CREATE DATABASE IF NOT EXISTS chemquest
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

-- Create dedicated user
CREATE USER IF NOT EXISTS 'chemquest'@'localhost'
IDENTIFIED BY 'srmjavaproject@123';

-- Give access only to ChemQuest database
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, DROP, REFERENCES
ON chemquest.* TO 'chemquest'@'localhost';

FLUSH PRIVILEGES;