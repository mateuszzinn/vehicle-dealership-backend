CREATE TABLE dealer (
    id BIGSERIAL PRIMARY KEY,
    corporate_name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(255) NOT NULL,
    cep VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX uk_dealer_cnpj ON dealer (cnpj);

CREATE TABLE vehicle (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    fuel_type VARCHAR(20) NOT NULL,
    color VARCHAR(255) NOT NULL,
    year INTEGER,
    chassis VARCHAR(255),
    price INTEGER,
    dealer_id BIGINT,
    CONSTRAINT fk_vehicle_dealer FOREIGN KEY (dealer_id) REFERENCES dealer (id),
    CONSTRAINT ck_vehicle_fuel_type CHECK (fuel_type IN ('GASOLINE', 'ETHANOL', 'FLEX', 'DIESEL', 'ELECTRIC', 'HYBRID'))
);

CREATE INDEX idx_vehicle_dealer_id ON vehicle (dealer_id);

