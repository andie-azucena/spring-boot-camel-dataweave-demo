CREATE TABLE IF NOT EXISTS employees (
  employee_id INT AUTO_INCREMENT,
  name VARCHAR(255),
  position VARCHAR(255),
  date_hired DATE,
  PRIMARY KEY(employee_id)
);

INSERT INTO employees (name, position, date_hired) VALUES ('Andie', 'Developer', '2001-01-01');
INSERT INTO employees (name, position, date_hired) VALUES ('Aiden', 'Developer', '2001-02-02');