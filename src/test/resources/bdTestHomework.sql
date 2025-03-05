CREATE TABLE Files (
    name VARCHAR(10),
    consist VARCHAR(10),
    type VARCHAR(5),
    owner VARCHAR(10),
    tag VARCHAR(10)
);

CREATE TABLE Buckets (
    name VARCHAR(10),
    consistNameOfFile VARCHAR(20),
    description VARCHAR(50),
    owner VARCHAR(10),
    tag VARCHAR(10)
);