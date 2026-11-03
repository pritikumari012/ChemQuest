# 🧪 ChemQuest – Virtual Chemistry Lab

### A Futuristic Java-Based Virtual Chemistry Learning Platform

ChemQuest is a Java-based Virtual Chemistry Lab designed to make chemistry learning interactive, engaging, and accessible through a digital environment.

The platform provides students with an opportunity to explore chemical elements, laboratory apparatus, virtual experiments, chemical reactions, and interactive quizzes through a modern desktop application.

Built primarily using Java, ChemQuest combines an interactive graphical interface with a Spring Boot backend and MySQL database.

---

## 🚀 Key Features

### 🖥️ 1. Futuristic Welcome Interface
- Animated chemistry-themed welcome screen.
- Interactive navigation to the application.
- Modern visual design using Java graphics.

### 🔐 2. User Authentication
- Sign-up and login interface.
- Input validation and duplicate account checking.
- Password hashing using PBKDF2 with salted hashes.
- Local demo authentication using file-based storage.

### 📊 3. Interactive Dashboard
- Centralized dashboard for chemistry learning modules.
- Sidebar navigation.
- Multiple learning sections.
- Page switching and logout functionality.

### ⚛️ 4. Interactive Periodic Table
- Full periodic table layout containing 118 elements.
- Searchable chemical elements.
- Detailed information about individual elements.

### 🧪 5. Virtual Chemistry Lab
- Interactive chemical library.
- Chemical reactant matching for supported reactions.
- Balanced reaction models.
- Rotatable 3D beaker visualization.
- Mass-conservation information.

### 🔬 6. Laboratory Apparatus
- Collection of chemistry laboratory instruments.
- 3D apparatus previews.
- Detailed descriptions and uses of instruments.

### 📚 7. Experiments Module
- Filterable experiment cards.
- Structured experiment information.
- Aim, theory, required materials, procedure, observations, result, and precautions.

### 📝 8. Quiz Module
- Basic, Intermediate, and Advanced quiz levels.
- Instant feedback.
- Explanations for answers.
- XP-based scoring system.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Desktop graphical user interface |
| JavaFX | 3D visualizations |
| Java2D | Animated graphical elements |
| Spring Boot | Backend application and REST APIs |
| JDBC | Java database connectivity |
| MySQL | Relational database |
| Maven | Build automation and dependency management |
| SQL | Database creation and management |

---

## 🏗️ System Architecture

ChemQuest is organized into three major components:

### 1. Frontend – Java Desktop Application

The frontend is developed using Java Swing and JavaFX.

It handles:
- User interface and navigation.
- Dashboard and learning modules.
- Periodic table and apparatus displays.
- Virtual lab visualizations.
- Experiments and quizzes.
- Local demo authentication.

### 2. Backend – Spring Boot Application

The backend is developed using Spring Boot.

It provides:
- REST API endpoints.
- Backend authentication functionality.
- Application-level request handling.
- Database connectivity through JDBC.
- MySQL database interaction.

### 3. Database – MySQL

MySQL is used as the relational database for backend data storage.

JDBC provides connectivity between the Java backend application and MySQL.

**Integration Status:** The backend authentication APIs are developed separately from the desktop application's local authentication. The desktop sign-up and login currently use local file-based storage. Complete frontend-to-backend authentication integration is a future enhancement.

---

## 📂 Project Structure

```text
ChemQuest/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── chemquest/
│                   ├── model/
│                   ├── ui/
│                   ├── service/
│                   └── util/
│
├── chemquest-backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
├── 01_create_database.sql
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
└── .gitignore
```

---

## ⚙️ Requirements

Before running ChemQuest, make sure you have:

- JDK 21 or newer.
- Git.
- Internet connection for initial Maven dependency downloads.
- MySQL Server for backend database functionality.

JavaFX dependencies are resolved through Maven for supported operating systems and architectures.

---

## ▶️ How to Run the Project

### Step 1: Clone the Repository

```bash
git clone https://github.com/pritikumari012/ChemQuest.git
```

### Step 2: Open the Project Directory

```bash
cd ChemQuest
```

### Step 3: Build the Application

**macOS / Linux:**

```bash
./mvnw clean package
```

**Windows:**

```bash
.\mvnw.cmd clean package
```

### Step 4: Run the Desktop Application

**macOS / Linux:**

```bash
./mvnw exec:java
```

**Windows:**

```bash
.\mvnw.cmd exec:java
```

---

## 🗄️ Backend and Database

The backend component uses Spring Boot, JDBC, and MySQL.

- Spring Boot provides the backend application and REST APIs.
- JDBC establishes communication between Java and MySQL.
- MySQL manages relational database storage.
- SQL scripts are provided for database setup.

The backend can be run and tested separately from the desktop application.

**Note:** Desktop application authentication currently uses local file storage, while backend authentication is handled through separately developed APIs.

---

## 🎯 Project Objectives

The main objectives of ChemQuest are:

- To make chemistry learning more interactive and engaging.
- To provide a virtual environment for exploring laboratory apparatus.
- To help students understand chemical elements and reactions.
- To provide structured experiment information.
- To combine programming concepts with educational technology.
- To demonstrate Java desktop development and backend technologies.

---

## 📌 Current Development Status

ChemQuest is a runnable Java desktop application with a separately developed Spring Boot backend.

**Completed:**
- Java Swing desktop application.
- Animated welcome screen and interactive dashboard.
- Local demo sign-up and login.
- Periodic table with 118 elements.
- Laboratory apparatus browser with 3D previews.
- Virtual chemistry lab with educational reaction models.
- Experiment and quiz modules.
- Spring Boot backend foundation and REST APIs.
- JDBC and MySQL database connectivity.

**Future Enhancements:**
- Complete desktop frontend and backend authentication integration.
- Persistent user progress tracking.
- Database-backed user data management.
- Additional experiments and interactive chemistry simulations.

**Educational Disclaimer:** Virtual chemical reactions are visual educational models. They do not perform real chemical experiments or simulate measured reaction kinetics.

---

## 👩‍💻 Developer

**Priti Kumari**

B.Tech – Computer Science and Engineering (Data Science)

GitHub: [@pritikumari012](https://github.com/pritikumari012)

**Project Repository:**

[ChemQuest – GitHub Repository](https://github.com/pritikumari012/ChemQuest)

---

## ⭐ Support

If you find ChemQuest interesting or useful, consider giving this repository a ⭐ star!

Thank you for visiting ChemQuest! 🧪
