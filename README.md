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
├── README-phase2.md
├── docker-compose.yml
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
├── phase2/
│ ├── data-api/
│ │ ├── pom.xml
│ │ └── src/main/java/com/team/api/
│ │ └── DataApi.java
│ ├── class-api/
│ ├── ui-api/
│ └── conf/
│ ├── config.yaml
│ └── apisix.yaml
└── project.db (SQLite database file - created by Phase 1)

## Quickstart

### 1. Clone the repo
```bash
git clone https://github.com/<team-repo>/comp-sci-3-project.git
cd comp-sci-3-project
```

### 2. Build the project
```bash
mvn compile
```

### 3. Create the database
```bash
mvn exec:java -D"exec.mainClass"="com.team.db.RunSql" -D"exec.args"="sql/create_countries.sql"
mvn exec:java -D"exec.mainClass"="com.team.db.RunSql" -D"exec.args"="sql/create_headlines.sql"
```

### 4. Load data
```bash
mvn exec:java -D"exec.mainClass"="com.team.countries.CountryLoader"
mvn exec:java -D"exec.mainClass"="com.team.news.HeadlineLoader"
```

### 4A. Copy database to data-api directory
```bash
cp project.db phase2/data-api/
cd phase2/data-api
mvn clean compile
mvn exec:java
```
Important Note: The Data API currently coded (`DB_PATH = "./project.db"`) will only work if you copy the database to the `phase2/data-api/` directory before running step 4A.

### 5. Verify data
```bash
mvn exec:java -D"exec.mainClass"="com.team.countries.CountryVerifier"
mvn exec:java -D"exec.mainClass"="com.team.news.HeadlineVerifier"
```

### 6. Inspect database manually
```bash
sqlite3 project.db
.tables
SELECT COUNT(*) FROM countries;
SELECT COUNT(*) FROM country_headlines;
.quit
```
## Phase 2 - Data API

### 7. Run the Data API 
```bash
cd phase2/data-api
mvn clean compile
mvn exec:java
```
The Data API will run on `http://localhost:7001` with these endpoints:
`GET /health` - Health check endpoint
`GET /countries` - Returns first 25 countries as JSON

### 8. Test the Data API
```bash
curl http://localhost:7001/health
curl http://localhost:7001/countries
```

### 9. Start Apache APISIX gateway
```bash
docker-compose up -d
```
APISIX will run on `http://localhost:9080`

### 10. Run all three APIs
```bash
# In separate terminals:
cd phase2/data-api && mvn exec:java      # Port 7001
cd phase2/class-api && mvn exec:java     # Port 7002  
cd phase2/ui-api && mvn exec:java        # Port 7003
```

### 11. Configure APISIX routes
Edit `phase2/conf/apisix.yaml` to set up routing between your APIs.

After running these steps, you should see **countries** and **headlines** tables populated with API data, along with matching summary verification.

For Phase 2 aggregation features:
```bash
mvn exec:java -D"exec.mainClass"="com.team.aggragated_county_news.CountryNews" -D"exec.args"="news_from_max_pop_country"
```
