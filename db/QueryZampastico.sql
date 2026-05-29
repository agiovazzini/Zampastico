-- 1.1 GESTIONE CATALOGO: INSERIMENTO (CREATE)
INSERT INTO Categoria (nome, id_supercategoria) VALUES (?, ?);
INSERT INTO Sconto (nome_promozione, percentuale_sconto) VALUES (?, ?);
INSERT INTO Prodotto (id_categoria, nome, brand, descrizione) VALUES (?, ?, ?, ?);
INSERT INTO VarianteProdotto (id_prodotto, id_sconto, formato, prezzo_listino) VALUES (?, ?, ?, ?);
INSERT INTO InventarioStock (id_varianteProdotto, quantita) VALUES (?, ?);
INSERT INTO ImmagineVariante (id_prodotto, url_immagine, testo_alt, ordine_visualizzazione, immagine_copertina) VALUES (?, ?, ?, ?, ?);

-- 1.2 GESTIONE CATALOGO: MODIFICA (UPDATE)
UPDATE Categoria SET nome = ?, id_supercategoria = ? WHERE id_categoria = ?;
UPDATE Sconto SET nome_promozione = ?, percentuale_sconto = ? WHERE id_sconto = ?;
UPDATE Prodotto SET id_categoria = ?, nome = ?, brand = ?, descrizione = ? WHERE id_prodotto = ?;
UPDATE VarianteProdotto SET id_sconto = ?, formato = ?, prezzo_listino = ? WHERE id_varianteProdotto = ?;
UPDATE InventarioStock SET quantita_disponibile = ? WHERE id_varianteProdotto = ?;
UPDATE ImmagineVariante SET url_immagine = ?, testo_alt = ?, ordine_visualizzazione = ?, immagine_copertina = ? WHERE id_immagine = ?;

-- 1.3 GESTIONE CATALOGO: ELIMINAZIONE (DELETE) 
DELETE FROM Categoria WHERE id_categoria = ?;
DELETE FROM Sconto WHERE id_sconto = ?;
DELETE FROM Prodotto WHERE id_prodotto = ?;
DELETE FROM Coupon WHERE id_coupon = ?;

-- 1.4 GESTIONE ORDINI (VISUALIZZAZIONE E MODIFICA) 
CREATE OR REPLACE VIEW VistaOrdiniAdmin AS
SELECT o.id_ordine, o.data_ordine, u.id_utente, CONCAT(u.nome, ' ', u.cognome) AS cliente, u.email, o.stato AS stato_ordine,
    o.totale, s.corriere, s.codice_tracking, s.stato AS stato_spedizione FROM Ordine o
JOIN Utente u ON o.id_utente = u.id_utente LEFT JOIN Spedizione s ON o.id_ordine = s.id_ordine
ORDER BY o.data_ordine DESC;
SELECT * FROM VistaOrdiniAdmin WHERE DATE(data_ordine) = ? ORDER BY data_ordine DESC;
SELECT * FROM VistaOrdiniAdmin WHERE email = ? ORDER BY data_ordine DESC;
SELECT * FROM VistaOrdiniAdmin WHERE DATE(data_ordine) = ? AND id_utente = ? AND stato_ordine = ?;

-- Gestione profilo utente
-- INSERT INTO Utente (nome, cognome, email, pass, data_creazione, data_anonimizzazione, ruolo, attivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?);
-- UPDATE Utente SET nome = ?, cognome = ?, pass = ?, data_anonimizzazione = ?,  attivo = ? WHERE id_utente = ?;
-- UPDATE Utente SET attivo = false, data_anonimizzazione = CURRENT_TIMESTAMP WHERE id_utente = ?;
-- SELECT id_utente, nome, cognome, email, pass, data_creazione, data_anonimizzazione, ruolo, attivo FROM Utente WHERE email = ?;
-- SELECT id_utente, nome, cognome, email, pass, data_creazione, data_anonimizzazione, ruolo, attivo FROM Utente ORDER BY data_creazione DESC;



UPDATE Indirizzo SET citta = ?, provincia = ?, via = ?, cap = ?, predefinito = ? WHERE id_indirizzo = ? AND id_utente = ?;
SELECT id_ordine, data_ordine, totale, stato, spedizione_via, spedizione_citta FROM Ordine WHERE id_utente = ? ORDER BY data_ordine DESC;

-- 2.1 RICERCA PRODOTTI CON FILTRI 
SELECT 
    p.nome, p.brand, vp.formato, vp.prezzo_listino,
    COALESCE(ROUND(vp.prezzo_listino * (1 - s.percentuale_sconto), 2), vp.prezzo_listino) AS prezzo_effettivo,
    iv.url_immagine
FROM Prodotto p
JOIN VarianteProdotto vp ON p.id_prodotto = vp.id_prodotto
JOIN Categoria c ON p.id_categoria = c.id_categoria
LEFT JOIN Sconto s ON vp.id_sconto = s.id_sconto
LEFT JOIN ImmagineVariante iv ON p.id_prodotto = iv.id_prodotto AND iv.immagine_copertina = TRUE
WHERE c.nome = ? AND p.brand = ?;

-- 2.2 FEED CONSIGLIATI (STORED PROCEDURE) 
DELIMITER //
DROP PROCEDURE IF EXISTS GeneraFeedConsigliati //
DELIMITER //
CREATE PROCEDURE GeneraFeedConsigliati(IN p_id_utente INT)
BEGIN
    DECLARE v_conteggio_ordini INT DEFAULT 0;
    IF p_id_utente IS NOT NULL THEN
        SELECT COUNT(*) INTO v_conteggio_ordini FROM Ordine WHERE id_utente = p_id_utente;
    END IF;
    
    IF v_conteggio_ordini > 0 THEN
        -- PROFILAZIONE: Utente con storico ordini
        SELECT DISTINCT p.id_prodotto, p.nome, p.brand, vp.prezzo_listino, iv.url_immagine
        FROM Prodotto p
        JOIN VarianteProdotto vp ON p.id_prodotto = vp.id_prodotto
        LEFT JOIN ImmagineVariante iv ON vp.id_varianteProdotto = iv.id_varianteProdotto AND iv.immagine_copertina = TRUE
        WHERE p.id_categoria IN (
            SELECT p2.id_categoria
            FROM VoceOrdine vo
            JOIN Ordine o ON vo.id_ordine = o.id_ordine
            JOIN VarianteProdotto vp2 ON vo.id_varianteProdotto = vp2.id_varianteProdotto
            JOIN Prodotto p2 ON vp2.id_prodotto = p2.id_prodotto
            WHERE o.id_utente = p_id_utente
        ) LIMIT 10;
    ELSE
        -- FALLBACK: Guest o nuovo utente (Bestseller assoluti)
        SELECT p.id_prodotto, p.nome, p.brand, vp.prezzo_listino, SUM(vo.quantita) as unita_vendute, iv.url_immagine
        FROM Prodotto p
        JOIN VarianteProdotto vp ON p.id_prodotto = vp.id_prodotto
        JOIN VoceOrdine vo ON vp.id_varianteProdotto = vo.id_varianteProdotto
        LEFT JOIN ImmagineVariante iv ON vp.id_varianteProdotto = iv.id_varianteProdotto AND iv.immagine_copertina = TRUE
        GROUP BY p.id_prodotto, p.nome, p.brand, vp.prezzo_listino, iv.url_immagine
        ORDER BY unita_vendute DESC LIMIT 10;
    END IF;
END //
DELIMITER ;
-- CALL GeneraFeedConsigliati(NULL); -- Per un Guest
-- CALL GeneraFeedConsigliati(15);   -- Per l'utente ID 15

-- 4.2 LA PROCEDURA DI CHECKOUT SICURA
-- Cambiamo il delimitatore standard da ; a //
DELIMITER //

-- Rimuoviamo la procedura se esiste già
DROP PROCEDURE IF EXISTS FinalizzaAcquisto //

CREATE PROCEDURE FinalizzaAcquisto(
    IN p_id_utente INT, 
    IN p_id_indirizzo INT,          -- NULL se compilato a mano
    IN p_citta VARCHAR(255),        -- NULL se usa id_indirizzo
    IN p_provincia VARCHAR(255),
    IN p_via VARCHAR(255),
    IN p_cap VARCHAR(20),
    IN p_salva_nuovo_indirizzo BOOLEAN,
    IN p_id_coupon INT,
    IN p_metodo_pagamento VARCHAR(50)
)
BEGIN
    DECLARE v_id_ordine INT;
    DECLARE v_totale DECIMAL(10,2) DEFAULT 0;
    DECLARE v_sconto_coupon DECIMAL(3,2) DEFAULT 0.00;
    DECLARE v_id_carrello INT DEFAULT NULL;
    DECLARE v_qta_carrello INT DEFAULT 0;
    
    DECLARE v_citta_finale VARCHAR(255);
    DECLARE v_provincia_finale VARCHAR(255);
    DECLARE v_via_finale VARCHAR(255);
    DECLARE v_cap_finale VARCHAR(20);
    
    START TRANSACTION;
    
    -- 1. CONTROLLO CARRELLO
    SELECT id_carrello INTO v_id_carrello FROM Carrello WHERE id_utente = p_id_utente;
    IF v_id_carrello IS NOT NULL THEN
        SELECT SUM(quantita) INTO v_qta_carrello FROM VoceCarrello WHERE id_carrello = v_id_carrello;
    END IF;
    
    IF v_qta_carrello = 0 OR v_qta_carrello IS NULL THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Il carrello è vuoto o non esiste.';
    END IF;
    
    -- 2. SCELTA E SALVATAGGIO INDIRIZZO
    IF p_id_indirizzo IS NOT NULL THEN
        SELECT citta, provincia, via, cap INTO v_citta_finale, v_provincia_finale, v_via_finale, v_cap_finale
        FROM Indirizzo WHERE id_indirizzo = p_id_indirizzo AND id_utente = p_id_utente;
        
        IF v_citta_finale IS NULL THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Indirizzo non valido.';
        END IF;
    ELSE
        SET v_citta_finale = p_citta; SET v_provincia_finale = p_provincia; SET v_via_finale = p_via; SET v_cap_finale = p_cap;
        IF p_salva_nuovo_indirizzo = TRUE THEN
            INSERT INTO Indirizzo (id_utente, citta, provincia, via, cap)
            VALUES (p_id_utente, v_citta_finale, v_provincia_finale, v_via_finale, v_cap_finale);
        END IF;
    END IF;
    
    -- 3. RECUPERO COUPON E CREAZIONE ORDINE
    IF p_id_coupon IS NOT NULL THEN
        SELECT percentuale_sconto INTO v_sconto_coupon FROM Coupon WHERE id_coupon = p_id_coupon;
    END IF;
    
    INSERT INTO Ordine (id_utente, id_coupon, totale, spedizione_citta, spedizione_provincia, spedizione_via, spedizione_cap)
    VALUES (p_id_utente, p_id_coupon, 0, v_citta_finale, v_provincia_finale, v_via_finale, v_cap_finale);
    SET v_id_ordine = LAST_INSERT_ID();
    
    -- 4. CONGELAMENTO PREZZI STORICI
    INSERT INTO VoceOrdine (id_ordine, id_varianteProdotto, quantita, prezzo_acquisto)
    SELECT v_id_ordine, vc.id_varianteProdotto, vc.quantita,
           COALESCE(ROUND(vp.prezzo_listino * (1 - s.percentuale_sconto), 2), vp.prezzo_listino)
    FROM VoceCarrello vc
    JOIN VarianteProdotto vp ON vc.id_varianteProdotto = vp.id_varianteProdotto
    LEFT JOIN Sconto s ON vp.id_sconto = s.id_sconto
    WHERE vc.id_carrello = v_id_carrello;
    
    -- 5. BLOCCO OVERSELLING E GESTIONE STOCK PRENOTATO
    UPDATE InventarioStock is_stock
    JOIN VoceCarrello vc ON is_stock.id_varianteProdotto = vc.id_varianteProdotto
    SET is_stock.quantita_disponibile = is_stock.quantita_disponibile - vc.quantita,
        is_stock.quantita_prenotata = is_stock.quantita_prenotata + vc.quantita
    WHERE vc.id_carrello = v_id_carrello;
    
    -- 6. TOTALE, PULIZIA E ATTESA PAGAMENTO
    SELECT SUM(quantita * prezzo_acquisto) INTO v_totale FROM VoceOrdine WHERE id_ordine = v_id_ordine;
    SET v_totale = ROUND(v_totale * (1 - v_sconto_coupon), 2);
    UPDATE Ordine SET totale = v_totale WHERE id_ordine = v_id_ordine;
    
    DELETE FROM VoceCarrello WHERE id_carrello = v_id_carrello;
    
    INSERT INTO Pagamento (id_ordine, totale, metodo, stato, fattura)
    VALUES (v_id_ordine, v_totale, p_metodo_pagamento, 'in_elaborazione', CONCAT('FATT-', v_id_ordine));
    
    COMMIT;
END //
DELIMITER ;
-- CALL FinalizzaAcquisto(UtenteID, IndirizzoID, CouponID_oppure_NULL, 'carta_credito');

-- 2.5 LISTA ORDINI EFFETTUATI 
SELECT id_ordine, data_ordine, totale, stato FROM Ordine WHERE id_utente = ? ORDER BY data_ordine DESC;

-- 3.1 CARRELLO LATO GUEST NON LOGGATO (Basato su id_sessione)
INSERT IGNORE INTO Carrello (id_sessione) VALUES (?);
-- Aggiunge o incrementa
INSERT INTO VoceCarrello (id_carrello, id_varianteProdotto, quantita)
SELECT id_carrello, ?, ? FROM Carrello WHERE id_sessione = ?
ON DUPLICATE KEY UPDATE quantita = quantita + VALUES(quantita);
-- Rimuove elemento specifico
DELETE vc FROM VoceCarrello vc
JOIN Carrello c ON vc.id_carrello = c.id_carrello
WHERE c.id_sessione = ? AND vc.id_varianteProdotto = ?;

-- 3.2 CARRELLO LATO UTENTE LOGGATO (Basato su id_utente)
INSERT IGNORE INTO Carrello (id_utente) VALUES (?);
-- Aggiunge o incrementa
INSERT INTO VoceCarrello (id_carrello, id_varianteProdotto, quantita)
SELECT id_carrello, ?, ? FROM Carrello WHERE id_utente = ?
ON DUPLICATE KEY UPDATE quantita = quantita + VALUES(quantita);
-- Aggiorna la quantità esatta (es. selezione da un menu a tendina)
UPDATE VoceCarrello vc
JOIN Carrello c ON vc.id_carrello = c.id_carrello
SET vc.quantita = ?
WHERE c.id_utente = ? AND vc.id_varianteProdotto = ?;
-- Rimuove elemento specifico
DELETE vc FROM VoceCarrello vc
JOIN Carrello c ON vc.id_carrello = c.id_carrello
WHERE c.id_utente = ? AND vc.id_varianteProdotto = ?;
-- Svuota intero carrello
DELETE vc FROM VoceCarrello vc
JOIN Carrello c ON vc.id_carrello = c.id_carrello
WHERE c.id_utente = ?;

-- 3.3 REGISTRAZIONE / LOGIN (MERGE DEL CARRELLO)
-- A) Registrazione nuovo utente (La conversione è diretta)
UPDATE Carrello SET id_sessione = NULL, id_utente = LAST_INSERT_ID() WHERE id_sessione = ?;
-- B) Login utente esistente (Merge dei prodotti dalla sessione guest al carrello utente)
INSERT IGNORE INTO Carrello (id_utente) VALUES (?); 
INSERT INTO VoceCarrello (id_carrello, id_varianteProdotto, quantita)
SELECT 
    (SELECT id_carrello FROM Carrello WHERE id_utente = ?), 
    vc_guest.id_varianteProdotto, 
    vc_guest.quantita
FROM VoceCarrello vc_guest
JOIN Carrello c_guest ON vc_guest.id_carrello = c_guest.id_carrello
WHERE c_guest.id_sessione = ?
ON DUPLICATE KEY UPDATE quantita = VoceCarrello.quantita + VALUES(quantita);

-- Pulizia della vecchia sessione guest
DELETE FROM Carrello WHERE id_sessione = ?;
