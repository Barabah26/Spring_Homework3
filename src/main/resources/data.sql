INSERT INTO customers (name, email, age, password, phone_number) VALUES ('John Doe', 'john.doe@example.com', 30, '1', '38099763264');
INSERT INTO customers (name, email, age, password, phone_number) VALUES ('Jane Smith', 'jane.smith@example.com', 25, '2', '380345466213');

INSERT INTO employers (name, address) VALUES ('Tech Corp', '123 Tech Road');
INSERT INTO employers (name, address) VALUES ('Business Inc', '456 Business Avenue');

INSERT INTO accounts (number, currency, balance, customer_id) VALUES ('619bc356-6f93-42da-a422-f7df335529a8', 'USD', 1000.0, 1);
INSERT INTO accounts (number, currency, balance, customer_id) VALUES ('500bc356-6f93-42da-a422-f7df335529a8', 'EUR', 2000.0, 2);

INSERT INTO EMPLOYERS_CUSTOMERS (customer_id, employer_id) VALUES (1, 1);
INSERT INTO EMPLOYERS_CUSTOMERS (customer_id, employer_id) VALUES (2, 2);