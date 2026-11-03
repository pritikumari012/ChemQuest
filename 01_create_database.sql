-- ChemQuest Stage 2: create the empty database and a dedicated user.
-- Run as the MySQL admin (root). Change CHANGE_ME_PASSWORD first!

CREATE DATABASE IF NOT EXISTS chemquest
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'chemquest'@'localhost' IDENTIFIED BY 'CHANGE_ME_PASSWORD';

-- The app user may only work inside the chemquest database.
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, DROP, REFERENCES
    ON chemquest.* TO 'chemquest'@'localhost';

FLUSH PRIVILEGES;
