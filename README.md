# ChemQuest - A Futuristic Virtual Chemistry Lab (Phase 2: runnable foundation)

## What works now
- Animated futuristic lab welcome screen (Java2D vector drawing, no image files)
- Sign-up (validation, duplicate check, PBKDF2 salted password hashing), Login, "Enter the Lab ->"
- Dashboard shell with sidebar, page switching, protected pages and working Logout (with confirmation)
- Apparatus browser with rotatable JavaFX 3D previews and detail pages
- Full periodic-table layout with searchable elements and element details
- Virtual lab with a scrollable chemical library, automatic matching for unambiguous reactant sets, a rotatable 3D beaker, balanced reaction models and mass-conservation notes
- Filterable experiment cards with structured experiment write-ups
- Basic, Intermediate and Advanced quizzes with instant feedback, explanations and XP scoring

## What is NOT built yet
Persistent progress tracking, Spring Boot + JDBC + MySQL. Virtual Lab reactions are visual educational models only;
they do not simulate measured reaction kinetics or perform real chemical experiments.

## Authentication is LOCAL DEMO only
Accounts are saved in `~/.chemquest/users.properties` (password hashes only).
Nothing is stored in SQL.

## Requirements
- JDK 21 or newer
- JavaFX 21 (resolved automatically by Maven for supported macOS, Windows, and Linux architectures)
- The Maven Wrapper downloads Maven 3.9.9 on first use; a separate Maven install is not required

## Run (macOS / Linux)
```powershell
java -version
./mvnw clean package
./mvnw exec:java
```

## Run (Windows PowerShell)
```powershell
java -version
.\mvnw.cmd clean package
.\mvnw.cmd exec:java
```

## Package a jar
```powershell
java -jar target/chemquest-1.0.0.jar
```
