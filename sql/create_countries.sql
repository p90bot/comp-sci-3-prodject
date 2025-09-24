DROP TABLE IF EXISTS countries;

CREATE TABLE countries (
    cca2 TEXT PRIMARY KEY,
    name_common TEXT NOT NULL,
    region TEXT,
    subregion TEXT,
    population INTEGER
);