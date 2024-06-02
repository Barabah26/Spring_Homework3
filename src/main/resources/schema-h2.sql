DROP TABLE IF EXISTS customer_employer;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS employers;

CREATE TABLE customers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(250) NOT NULL,
                           email VARCHAR(250) NOT NULL,
                           age INT NOT NULL,
                           password VARCHAR(250) NOT NULL,
                           phone_number VARCHAR(250) NOT NULL,
                           creation_date      TIMESTAMP NULL,
                           last_modified_date TIMESTAMP NULL
);

CREATE TABLE employers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(250) NOT NULL,
                           address VARCHAR(250) NOT NULL,
                           creation_date      TIMESTAMP NULL,
                           last_modified_date TIMESTAMP NULL
);

CREATE TABLE accounts (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          number VARCHAR(36) NOT NULL,
                          currency VARCHAR(10) NOT NULL,
                          balance DOUBLE NOT NULL,
                          customer_id BIGINT,
                          creation_date      TIMESTAMP NULL,
                          last_modified_date TIMESTAMP NULL,
                          CONSTRAINT fk_customer
                              FOREIGN KEY (customer_id)
                                  REFERENCES customers(id)
                                  ON DELETE CASCADE
);

CREATE TABLE customer_employer (
                                   customer_id INT,
                                   employer_id INT,
                                   PRIMARY KEY (customer_id, employer_id),
                                   CONSTRAINT fk_customer_employer_customer
                                       FOREIGN KEY (customer_id)
                                           REFERENCES customers(id)
                                           ON DELETE CASCADE,
                                   CONSTRAINT fk_customer_employer_employer
                                       FOREIGN KEY (employer_id)
                                           REFERENCES employers(id)
                                           ON DELETE CASCADE
);

