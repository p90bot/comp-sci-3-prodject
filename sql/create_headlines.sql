DROP TABLE IF EXISTS country_headlines;

CREATE TABLE country_headlines (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    country TEXT NOT NULL,
    title TEXT NOT NULL,
    source TEXT,
    published_at TEXT
);