
-- 1. Création de la base de données en UTF-8 si elle n'existe pas
SELECT 'CREATE DATABASE ecommerce WITH ENCODING = ''UTF8'''
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ecommerce')\gexec

-- 2. Connexion à la nouvelle base de données
\c ecommerce;

-- ==========================================
-- 3. CRÉATION DES TABLES (SCHEMA)
-- ==========================================

-- Table des entreprises (B2B)
CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    siret VARCHAR(14) NOT NULL UNIQUE,
    vat_number VARCHAR(50),
    credit_limit NUMERIC(12, 2),
    payment_term_days INTEGER DEFAULT 0
);

-- Table des rôles (AJOUT)
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

-- Insertion des rôles disponibles
INSERT INTO roles (code, name) VALUES
('ADMIN', 'Administrateur'),
('USER', 'Particulier'),
('PRO', 'Professionnel')
ON CONFLICT (code) DO NOTHING;

-- Table des utilisateurs
-- role_id référence la table roles
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    company_id BIGINT, -- NULL = Client B2C / Rempli = Salarié/Acheteur B2B
    role_id BIGINT NOT NULL,

    CONSTRAINT fk_user_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
);

-- Table des produits du catalogue
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_price_ht NUMERIC(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0
);

-- Table des prix (Hybride B2C / B2B) avec contrainte d'unicité
CREATE TABLE product_prices (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    company_id BIGINT, -- NULL = Prix public B2C / Rempli = Prix négocié B2B
    price_ht NUMERIC(10, 2) NOT NULL,

    -- Contraintes de clés étrangères
    CONSTRAINT fk_product_price_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_product_price_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON DELETE CASCADE,

    -- Unicité pour éviter d'avoir plusieurs prix pour le même produit et la même entreprise
    CONSTRAINT uk_product_company UNIQUE (product_id, company_id)
);

-- ==========================================
-- 4. REMPLISSAGE DES DONNÉES (SEEDING)
-- ==========================================

-- Insertion des entreprises B2B
INSERT INTO companies (id, name, siret, vat_number, credit_limit, payment_term_days) VALUES
(1, 'Acme Corp', '12345678901234', 'FR12345678901', 50000.00, 30),
(2, 'Globex Corporation', '98765432109876', 'FR98765432109', 100000.00, 60)
ON CONFLICT (id) DO NOTHING;

-- Réinitialisation de la séquence companies
SELECT setval('companies_id_seq', (SELECT MAX(id) FROM companies));

-- Insertion des utilisateurs (B2C et B2B)
-- Les hash sont conservés exactement comme dans le script original.
INSERT INTO users (
    id,
    email,
    password_hash,
    first_name,
    last_name,
    company_id,
    role_id
) VALUES
(
    1,
    'b2c.user@example.com',
    '$2a$10$qclH2vmin47Qp7gCjvWf3u7QMwYHr93RUeANLzTUSM4Kkh0nokW.O',
    'Jean',
    'Dupont',
    NULL,
    (SELECT id FROM roles WHERE code = 'USER')
), -- Client B2C pur
(
    2,
    'buyer@acme.com',
    '$2a$10$qclH2vmin47Qp7gCjvWf3u7QMwYHr93RUeANLzTUSM4Kkh0nokW.O',
    'Alice',
    'Martin',
    1,
    (SELECT id FROM roles WHERE code = 'PRO')
), -- Acheteur B2B Acme Corp
(
    3,
    'buyer@globex.com',
    '$2a$10$qclH2vmin47Qp7gCjvWf3u7QMwYHr93RUeANLzTUSM4Kkh0nokW.O',
    'Bob',
    'Lemoine',
    2,
    (SELECT id FROM roles WHERE code = 'PRO')
) -- Acheteur B2B Globex Corporation
ON CONFLICT (id) DO NOTHING;

-- Réinitialisation de la séquence users
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- Insertion des produits (avec SKU, prix de base et stock)
INSERT INTO products (id, sku, name, description, base_price_ht, stock_quantity) VALUES
(1, 'PROD-MBP16', 'MacBook Pro 16"', 'Ordinateur portable haute performance pour professionnels', 2400.00, 15),
(2, 'PROD-SCR274K', 'Écran 27 pouces 4K', 'Écran UHD idéal pour le développement et le graphisme', 450.00, 30)
ON CONFLICT (id) DO NOTHING;

-- Réinitialisation de la séquence products
SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));

-- Insertion des prix (Publics et Négociés B2B)
INSERT INTO product_prices (product_id, company_id, price_ht) VALUES
-- 1. Prix Publics (B2C : company_id est NULL)
(1, NULL, 2400.00),
(2, NULL, 450.00),

-- 2. Prix Négociés pour Acme Corp (company_id = 1)
(1, 1, 2150.00),
(2, 1, 400.00),

-- 3. Prix Négocié pour Globex Corporation (company_id = 2)
(1, 2, 2200.00)
ON CONFLICT DO NOTHING;

-- Réinitialisation de la séquence product_prices
SELECT setval('product_prices_id_seq', (SELECT MAX(id) FROM product_prices));
