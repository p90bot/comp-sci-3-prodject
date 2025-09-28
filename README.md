# CSC240 Phase 1 – API → SQLite Pipeline

## Team Members
- Stephen
- Amara  
- Justin  
- Myles 

## Dependencies
- Java 17+  
- Maven  
- SQLite3  

Make sure these are installed before running the project.

## Project Structure
comp-sci-3-project/
├── pom.xml
├── README.md
├── sql/
│   ├── create_countries.sql
│   └── create_headlines.sql
├── src/
│   └── main/java/com/team/
│       ├── db/
│       │   ├── Database.java
│       │   └── RunSql.java
│       ├── countries/
│       │   ├── Country.java
│       │   ├── CountryLoader.java
│       │   └── CountryVerifier.java
│       └── news/
│           ├── Headline.java
│           ├── HeadlineLoader.java
│           └── HeadlineVerifier.java
├── data/
│   └── summaries/   (auto-generated summary files)
└── screenshots/     (proof for Phase 1 submission)

## Quickstart

### 1. Clone the repo
git clone https://github.com/<team-repo>/comp-sci-3-project.git
cd comp-sci-3-project

### 2. Build the project
mvn compile

### 3. Create the database
mvn exec:java -Dexec.mainClass="com.team.db.RunSql" -Dexec.args="sql/create_countries.sql"
mvn exec:java -Dexec.mainClass="com.team.db.RunSql" -Dexec.args="sql/create_headlines.sql"

### 4. Load data
mvn exec:java -Dexec.mainClass="com.team.countries.CountryLoader"
mvn exec:java -Dexec.mainClass="com.team.news.HeadlineLoader"

### 5. Verify data
mvn exec:java -Dexec.mainClass="com.team.countries.CountryVerifier"
mvn exec:java -Dexec.mainClass="com.team.news.HeadlineVerifier"

### 6. Inspect database manually
sqlite3 project.db
.tables
SELECT COUNT(*) FROM countries;
SELECT COUNT(*) FROM country_headlines;
.quit


After running these steps, you should see **countries** and **headlines** tables populated with API data, along with matching summary verification.