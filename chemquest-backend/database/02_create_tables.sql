
USE chemquest;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS apparatus (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    category VARCHAR(100),
    description TEXT,
    uses TEXT,
    safety TEXT
);

CREATE TABLE IF NOT EXISTS experiments (
    experiment_id VARCHAR(30) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    short_desc TEXT,
    difficulty VARCHAR(50),
    duration_minutes INT,
    category VARCHAR(50),
    aim TEXT,
    theory LONGTEXT,
    result TEXT,
    hazard TEXT,
    lab TEXT,
    summary TEXT
);

CREATE TABLE IF NOT EXISTS experiment_apparatus (
    experiment_id VARCHAR(30) NOT NULL,
    apparatus_id BIGINT NOT NULL,
    PRIMARY KEY (experiment_id, apparatus_id),
    FOREIGN KEY (experiment_id) REFERENCES experiments(experiment_id)
        ON DELETE CASCADE,
    FOREIGN KEY (apparatus_id) REFERENCES apparatus(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    prompt TEXT NOT NULL,
    explanation TEXT
);

CREATE TABLE IF NOT EXISTS quiz_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    option_order INT NOT NULL,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (question_id) REFERENCES quiz_questions(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category VARCHAR(50),
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    earned_xp INT DEFAULT 0,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    attempted INT DEFAULT 0,
    correct INT DEFAULT 0,
    completed_quizzes INT DEFAULT 0,
    earned_xp INT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (user_id, category),
    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);
