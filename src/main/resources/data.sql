CREATE DATABASE mydatabase;

USE mydatabase;

CREATE TABLE clients (
                         clientId VARCHAR(36) PRIMARY KEY,
                         lastName VARCHAR(255),
                         firstName VARCHAR(255),
                         secondName VARCHAR(255)
);

CREATE TABLE flights (
                         flightId VARCHAR(36) PRIMARY KEY,
                         origin VARCHAR(255),
                         destination VARCHAR(255),
                         flightDate DATETIME
);

CREATE TABLE flightSeats (
                             flightId VARCHAR(36) NOT NULL,
                             seatNumber INT NOT NULL,
                             isTaken BOOLEAN,
                             PRIMARY KEY (flightId, seatNumber)
);

CREATE TABLE orders (
                        orderId VARCHAR(36) PRIMARY KEY,
                        creditCardId VARCHAR(36),
                        deliveryAddress VARCHAR(255),
                        orderDate DATETIME,
                        deliveryDate DATETIME
);

CREATE TABLE tickets (
                         ticketId VARCHAR(36) PRIMARY KEY,
                         clientId VARCHAR(36),
                         flightId VARCHAR(36),
                         orderId VARCHAR(36),
                         seatNumber INT,
                         FOREIGN KEY (clientId) REFERENCES clients(clientId),
                         FOREIGN KEY (flightId) REFERENCES flights(flightId),
                         FOREIGN KEY (orderId) REFERENCES orders(orderId)
);