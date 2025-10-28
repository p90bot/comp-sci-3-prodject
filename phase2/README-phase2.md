# Phase 2 – Data API

## Overview
This project implements a lightweight **Java HTTP server** that serves data from a **SQLite database (`project.db`)**.  
The API exposes endpoints for health checking and country data retrieval.

---

## Features
- Uses **Java HttpServer** (no external frameworks)
- Connects to **SQLite** through the `sqlite-jdbc` driver
- Returns data as **JSON**
- Includes two working endpoints:
  - `/health` → confirms server is running
  - `/countries` → returns an array of country data (cca2, name, region, subregion, population)

---

## Requirements
- **Java 21**
- **Maven 3.x**
- SQLite database file: `project.db` (included)

---

## Project Structure