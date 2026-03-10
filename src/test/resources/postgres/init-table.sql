DROP TABLE IF EXISTS PRODUCTS;

CREATE TABLE PRODUCTS (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE PRECISION NOT NULL
);

INSERT INTO PRODUCTS (id, name, description, price) VALUES
(1, 'Gaming Laptop Pro', 'High-End Laptop mit 32GB RAM und RTX 4080 Grafikkarte', 2499.00),
(2, 'Kabellose Maus', 'Ergonomische Funkmaus mit optischem Präzisionssensor', 49.50),
(3, 'Mechanische Tastatur', 'RGB-Beleuchtung, mechanische Blue Switches, USB-C', 129.99),
(4, 'Ultrawide Monitor 34 Zoll', '34 Zoll Curved Display, 144Hz, IPS-Panel für Grafikdesign', 549.00),
(5, 'Noise Cancelling Headset', 'Bluetooth Kopfhörer mit aktiver Geräuschunterdrückung', 199.00);

CREATE TABLE ORDERS (
    orderId VARCHAR(36) PRIMARY KEY,
    productId INTEGER NOT NULL,
    receiptKey VARCHAR(255) NOT NULL
);
