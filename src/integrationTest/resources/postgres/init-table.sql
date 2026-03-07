-- DROP TABLE IF EXISTS PRODUCTS;

-- Bestehende Daten bereinigen
-- DELETE
-- FROM products;

CREATE TABLE PRODUCTS
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255)     NOT NULL,
    description TEXT             NOT NULL,
    price       DOUBLE PRECISION NOT NULL
);

INSERT INTO products (id, name, description, price)
VALUES (1, 'Gaming Laptop Pro', 'High-End Laptop mit 32GB RAM und RTX 4080 Grafikkarte', 2499.00),
       (2, 'Kabellose Maus', 'Ergonomische Funkmaus mit optischem Präzisionssensor', 49.50),
       (3, 'Mechanische Tastatur', 'RGB-Beleuchtung, mechanische Blue Switches, USB-C', 129.99),
       (4, 'Ultrawide Monitor 34 Zoll', '34 Zoll Curved Display, 144Hz, IPS-Panel für Grafikdesign', 549.00),
       (5, 'Noise Cancelling Headset', 'Bluetooth Kopfhörer mit aktiver Geräuschunterdrückung', 199.00),
       (6, 'USB-C Docking Station', '10-in-1 Hub mit HDMI, Ethernet und Power Delivery', 89.00),
       (7, 'Ergonomischer Bürostuhl', 'Atmungsaktiver Netzrücken, verstellbare Armlehnen', 299.00),
       (8, 'LED Schreibtischlampe', 'Dimmbare Lampe mit USB-Ladeanschluss und Timer', 35.00),
       (9, 'Externe SSD 2TB', 'Schnelle NVMe SSD, stoßfestes Gehäuse, USB 3.2 Gen 2', 159.00),
       (10, 'Webcam 4K Ultra HD', 'Streaming Kamera mit Autofokus und Dual-Mikrofon', 110.00),
       (11, 'Gaming Stuhl "Throne"', 'Premium Lederoptik, Lordosenstütze, 4D Armlehnen', 349.00),
       (12, 'Bluetooth Lautsprecher', 'Wasserdicht nach IPX7, 20 Stunden Akkulaufzeit', 75.00),
       (13, 'Grafiktablett M-Size', 'Druckempfindlicher Stift, perfekt für digitales Zeichnen', 180.00),
       (14, 'Stehschreibtisch Gestell', 'Elektrisch höhenverstellbar mit Memory-Funktion', 420.00),
       (15, 'VR Headset Gen 2', 'Standalone VR-Brille mit 128GB Speicher und Controllern', 449.00),
       (16, 'Streaming Mikrofon', 'Kondensatormikrofon mit Nierencharakteristik für Podcasts', 115.00),
       (17, 'Laptop Ständer Aluminium', 'Faltbarer Ständer für ergonomisches Arbeiten', 29.00),
       (18, 'XXL Mousepad', '900x400mm, rutschfeste Unterseite, vernähte Kanten', 20.00),
       (19, 'Wi-Fi 6 Router', 'Dual-Band Gigabit Router für schnelles Heimnetzwerk', 145.00),
       (20, 'Smart Home Hub', 'Zentrale Steuerung für alle smarten Geräte im Haus', 99.00),
       (21, 'Mobiler Monitor 15 Zoll', 'Tragbares Display mit USB-C für Unterwegs', 210.00),
       (22, 'Laptop Tasche 14 Zoll', 'Gepolstertes Fach, wasserabweisendes Material', 45.00),
       (23, 'Powerbank 20.000 mAh', 'Schnellladefunktion, 3 USB-Ausgänge, LED-Anzeige', 55.00),
       (24, 'Ethernet Kabel 10m', 'Cat 7 LAN Kabel mit Goldsteckern für stabiles Internet', 15.00),
       (25, '4K Monitor 27 Zoll', 'UHD Auflösung, HDR Unterstützung, schmales Design', 320.00),
       (26, 'Smartphone Pro Max', '6.7 Zoll OLED Display, Triple-Kamera, 256GB Speicher', 1199.00),
       (27, 'USB-C Ladekabel 2m', 'Schnellladekabel mit Textilummantelung, USB 3.1', 19.90),
       (28, 'Tablet Air 10 Zoll', 'Leichtes Tablet für Office und Entertainment, WiFi 6', 380.00),
       (29, 'Laser-Drucker Schwarzweiß', 'Kompakter Drucker mit WLAN und Duplexdruck', 149.00),
       (30, 'WLAN Verstärker / Repeater', 'Erhöht die Reichweite des Heimnetzwerks, Mesh-fähig', 45.00),
       (31, 'Smarte Glühbirne RGB', 'App-Steuerung, kompatibel mit Alexa und Google Home', 25.00),
       (32, 'Drahtlose Ladestation', 'Induktives Laden mit 15W für Smartphones und Buds', 35.00),
       (33, 'Action Kamera 4K', 'Wasserdicht, Bildstabilisierung, inkl. Zubehörkit', 280.00),
       (34, 'Externe HDD 4TB', 'Viel Speicherplatz für Backups, USB 3.0 Anschluss', 95.00),
       (35, 'Gaming Controller Wireless', 'Kompatibel mit PC und Konsole, haptisches Feedback', 65.00),
       (36, 'VR Tracking Sensoren', 'Zusätzliche Sensoren für präzises Full-Body-Tracking', 120.00),
       (37, 'MicroSD Karte 256GB', 'A2 V30 Speed für schnelle 4K Videoaufnahmen', 39.00),
       (38, 'HDMI Switch 3-auf-1', 'Umschalter für mehrere Geräte an einem Monitor, 4K 60Hz', 49.00),
       (39, 'Sicherheitskamera Outdoor', 'Wetterfest, Nachtsicht, Benachrichtigung aufs Handy', 130.00),
       (40, 'Smartes Türschloss', 'Keyless Entry via Smartphone, einfache Montage', 199.00),
       (41, 'Adapter USB-C auf HDMI', 'Verbindet Laptop mit TV oder Beamer, 4K Support', 22.00),
       (42, 'Laptop Sleeve 13 Zoll', 'Weiches Innenfutter, schützt vor Kratzern', 15.00),
       (43, 'Laptop Kühler', 'Aktive Kühlung durch zwei leise Lüfter, USB-betrieben', 30.00),
       (44, 'Bildschirm-Reinigungskit', 'Alkoholfreies Spray mit Mikrofasertuch', 12.00),
       (45, 'Kabel-Organizer Box', 'Verdeckt Kabelsalat unter dem Schreibtisch', 18.00),
       (46, 'Smarte Steckdose', 'Stromverbrauch messen und zeitgesteuert schalten', 20.00),
       (47, 'Luftreiniger Smart', 'HEPA-Filter, entfernt Allergene, App-Steuerung', 180.00),
       (48, 'Bluetooth USB Dongle', 'Nachrüstung für PCs ohne integriertes Bluetooth 5.0', 14.00),
       (49, 'Thunderbolt 4 Kabel', '40Gbps Datentransfer, unterstützt zwei 4K Monitore', 59.00),
       (50, 'Stehschreibtisch Matte', 'Anti-Ermüdungsmatte für komfortables Stehen', 45.00);

CREATE TABLE ORDERS
(
    orderId    VARCHAR(36) PRIMARY KEY,
    productId  INTEGER      NOT NULL,
    receiptKey VARCHAR(255) NOT NULL
);
