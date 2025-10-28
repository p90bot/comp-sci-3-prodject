# CSC240 Phase 1 – API → SQLite Pipeline

## Team Members
- Stephen
- Amara  
- Justin  
- Myles 

## Dependencies
- Java 21 (LTS)
- Maven
- SQLite3

This project now targets Java 21. If you're on macOS you can install a matching JDK with Homebrew or Adoptium.

Example (macOS, zsh):

```bash
# install latest Temurin JDK 21 via Homebrew
brew install --cask temurin21

# verify java version
java -version

# build
mvn -T 1C clean package
```

If you prefer another distribution (Adoptium, Azul, Oracle), install a Java 21 JDK and make sure `java` and `javac` point to it.

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
mvn exec:java -D"exec.mainClass"="com.team.db.RunSql" -D"exec.args"="sql/create_countries.sql"
mvn exec:java -D"exec.mainClass"="com.team.db.RunSql" -D"exec.args"="sql/create_headlines.sql"

### 4. Load data
mvn exec:java -D"exec.mainClass"="com.team.countries.CountryLoader"
mvn exec:java -D"exec.mainClass"="com.team.news.HeadlineLoader"

### 5. Verify data
mvn exec:java -D"exec.mainClass"="com.team.countries.CountryVerifier"
mvn exec:java -D"exec.mainClass"="com.team.news.HeadlineVerifier"

### 6. Inspect database manually
sqlite3 project.db
.tables
SELECT COUNT(*) FROM countries;
SELECT COUNT(*) FROM country_headlines;
.quit


After running these steps, you should see **countries** and **headlines** tables populated with API data, along with matching summary verification.

mvn exec:java -D"exec.mainClass"="com.team.aggragated_county_news.CountryNews" -D"exec.args"="news_from_max_pop_country"