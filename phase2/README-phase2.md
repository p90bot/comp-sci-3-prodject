# CSC240 Phase 2 – Data, Class, and UI APIs

## Overview
This project implements a three-tier API system consisting of:
1. Data API – Handles country data and database queries using SQLite.
2. Class API  – Provides API access to student/class-related data (Phase 1 integration).
3. UI API  – Acts as a front-end service routing through Apache APISIX.

Each API runs independently but communicates through APISIX for centralized routing and management.


## Requirements
- Java 21  
- Maven 3.9+  
- SQLite database (`project.db`)  
- Apache APISIX configured to route requests  


## How to Run Each API
From the project root:
```bash
# Run Data API (port 7001)
cd phase2/data-api
mvn clean compile
mvn exec:java

# Run Class API (port 7002)
cd phase2/class-api
mvn clean compile
mvn exec:java

# Run UI API (port 7003)
cd phase2/ui-api
mvn clean compile
mvn exec:java

After running, each API will display:
Data API server running on http://localhost:7001
Class API server running on http://localhost:7002
UI API server running on http://localhost:7003