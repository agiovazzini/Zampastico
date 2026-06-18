CREATE SCHEMA IF NOT EXISTS Zampastico;
USE Zampastico;

-- ==========================================
-- DOMINIO 1: UTENTI E INDIRIZZI
-- ==========================================
CREATE TABLE IF NOT EXISTS Utente(
    id_utente INT  AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cognome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    pass VARCHAR(255) NOT NULL,
    data_creazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ruolo ENUM('amministratore', 'cliente') DEFAULT 'cliente' NOT NULL,
    attivo BOOLEAN DEFAULT TRUE,
    data_anonimizzazione TIMESTAMP NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS Indirizzo(
    id_indirizzo INT  AUTO_INCREMENT PRIMARY KEY,
    id_utente INT  NOT NULL,
    citta VARCHAR(255) NOT NULL,
    provincia VARCHAR(255) NOT NULL,
    via VARCHAR(255) NOT NULL,
    cap VARCHAR(20) NOT NULL,
    predefinito BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_utente) REFERENCES Utente(id_utente) ON DELETE CASCADE
);

-- ==========================================
-- DOMINIO 2: CATALOGO E MAGAZZINO
-- ==========================================
CREATE TABLE IF NOT EXISTS Categoria(
    id_categoria INT  AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    id_supercategoria INT  DEFAULT NULL,
    FOREIGN KEY (id_supercategoria) REFERENCES Categoria(id_categoria) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Sconto(
    id_sconto INT  AUTO_INCREMENT PRIMARY KEY,
    nome_promozione VARCHAR(255) NOT NULL,
    percentuale_sconto DECIMAL(3,2) NOT NULL CHECK (percentuale_sconto BETWEEN 0 AND 1)
);

CREATE TABLE IF NOT EXISTS Prodotto(
    id_prodotto INT  AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT ,
    nome VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    descrizione TEXT,
    data_creazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    eliminato BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS VarianteProdotto(
    id_varianteProdotto INT  AUTO_INCREMENT PRIMARY KEY,
    id_prodotto INT  NOT NULL,
    id_sconto INT  DEFAULT NULL,
    formato VARCHAR(255),
    prezzo_listino DECIMAL(10,2) NOT NULL,
    disponibile BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_prodotto) REFERENCES Prodotto(id_prodotto) ON DELETE CASCADE,
    FOREIGN KEY (id_sconto) REFERENCES Sconto(id_sconto) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ImmagineVariante(
    id_immagine INT  AUTO_INCREMENT PRIMARY KEY,
    id_varianteProdotto INT  NOT NULL, 
    url_immagine VARCHAR(500) NOT NULL,
    testo_alt VARCHAR(255), 
    ordine_visualizzazione INT DEFAULT 0, 
    immagine_copertina BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_varianteProdotto) REFERENCES VarianteProdotto(id_varianteProdotto) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS InventarioStock(
    id_varianteProdotto INT  PRIMARY KEY,
    quantita_disponibile INT NOT NULL DEFAULT 0,
    quantita_prenotata INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_varianteProdotto) REFERENCES VarianteProdotto(id_varianteProdotto) ON DELETE CASCADE,
    CONSTRAINT prodotto_disponibile CHECK (quantita_disponibile >= 0),
    CONSTRAINT prodotto_prenotato CHECK (quantita_prenotata >= 0)
);

-- ==========================================
-- DOMINIO 3: INTERAZIONI E PREFERENZE
-- ==========================================
CREATE TABLE IF NOT EXISTS Recensione(
    id_utente INT  NOT NULL,
    id_varianteProdotto INT  NOT NULL,
    votazione INT CHECK (votazione BETWEEN 1 AND 5),
    commento TEXT,
    data_creazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_utente, id_varianteProdotto),
    FOREIGN KEY (id_utente) REFERENCES Utente(id_utente) ON DELETE CASCADE,
    FOREIGN KEY (id_varianteProdotto) REFERENCES VarianteProdotto(id_varianteProdotto) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Carrello(
    id_carrello INT  AUTO_INCREMENT PRIMARY KEY,
    id_sessione VARCHAR(255) UNIQUE,
    id_utente INT  UNIQUE,
    data_aggiornamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_utente) REFERENCES Utente(id_utente) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS VoceCarrello(
    id_carrello INT  NOT NULL,
    id_varianteProdotto INT  NOT NULL,
    quantita INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id_carrello, id_varianteProdotto),
    FOREIGN KEY (id_carrello) REFERENCES Carrello(id_carrello) ON DELETE CASCADE,
    FOREIGN KEY (id_varianteProdotto) REFERENCES VarianteProdotto(id_varianteProdotto) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Wishlist(
    id_wishlist INT  AUTO_INCREMENT PRIMARY KEY,
    id_utente INT  NOT NULL,
    nome VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_utente) REFERENCES Utente(id_utente) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS VoceWishlist(
    id_wishlist INT  NOT NULL,
    id_varianteProdotto INT  NOT NULL,
    PRIMARY KEY (id_wishlist, id_varianteProdotto),
    FOREIGN KEY (id_wishlist) REFERENCES Wishlist(id_wishlist) ON DELETE CASCADE,
    FOREIGN KEY (id_varianteProdotto) REFERENCES VarianteProdotto(id_varianteProdotto) ON DELETE CASCADE
);

-- ==========================================
-- DOMINIO 4: TRANSAZIONI (ORDINI E SPEDIZIONI)
-- ==========================================
CREATE TABLE IF NOT EXISTS Coupon(
    id_coupon INT  AUTO_INCREMENT PRIMARY KEY,
    codice VARCHAR(255) UNIQUE NOT NULL,
    percentuale_sconto DECIMAL(3,2) NOT NULL CHECK (percentuale_sconto BETWEEN 0 AND 1),
    data_scadenza TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 24 HOUR)
);

CREATE TABLE IF NOT EXISTS Ordine(
    id_ordine INT  AUTO_INCREMENT PRIMARY KEY,
    id_utente INT  NOT NULL,
    id_coupon INT  DEFAULT NULL,
    totale DECIMAL(10,2) NOT NULL,
    spedizione_citta VARCHAR(255) NOT NULL,
    spedizione_provincia VARCHAR(255) NOT NULL,
    spedizione_via VARCHAR(255) NOT NULL,
    spedizione_cap VARCHAR(20) NOT NULL,
    stato ENUM('in_attesa', 'pagato', 'in_lavorazione', 'spedito', 'consegnato', 'annullato') DEFAULT 'in_attesa' NOT NULL,
    data_ordine TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_utente) REFERENCES Utente(id_utente) ON DELETE RESTRICT,
    FOREIGN KEY (id_coupon) REFERENCES Coupon(id_coupon) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS VoceOrdine(
    id_ordine INT  NOT NULL,
    id_varianteProdotto INT  NOT NULL,
    quantita INT NOT NULL,
    prezzo_acquisto DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_ordine, id_varianteProdotto),
    FOREIGN KEY (id_ordine) REFERENCES Ordine(id_ordine) ON DELETE CASCADE,
    FOREIGN KEY (id_varianteProdotto) REFERENCES VarianteProdotto(id_varianteProdotto) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Pagamento(
    id_pagamento INT  AUTO_INCREMENT PRIMARY KEY,
    id_ordine INT  NOT NULL,
    totale DECIMAL(10,2) NOT NULL,
    metodo ENUM('carta_credito', 'paypal', 'bonifico') NOT NULL,
    stato ENUM('completato', 'fallito', 'rimborsato', 'in_elaborazione') DEFAULT 'in_elaborazione' NOT NULL,
    fattura VARCHAR(255) NOT NULL,
    data_pagamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (id_ordine) REFERENCES Ordine(id_ordine) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Spedizione(
    id_spedizione INT  AUTO_INCREMENT PRIMARY KEY,
    id_ordine INT  NOT NULL, 
    corriere VARCHAR(255),
    stato ENUM('preparazione', 'in_transito', 'consegnato') DEFAULT 'preparazione' NOT NULL,
    FOREIGN KEY (id_ordine) REFERENCES Ordine(id_ordine) ON DELETE CASCADE
);