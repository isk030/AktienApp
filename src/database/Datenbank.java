package database;

import database.connection.SQLiteDatenbankverbindung;

import java.sql.SQLException;

/**
 * Klasse zur Steuerung der Datenbankvorgänge. Definiert als Singelton:
 * https://de.wikibooks.org/wiki/Muster:_Java:_Singleton
 */
public class Datenbank extends SQLiteDatenbankverbindung {

    private final static String DBFILE = "src\\stocks2.sqlite";
    private static Datenbank db;

    /**
     * private Konstruktor
     */
    private Datenbank() {
        super(DBFILE);
    }

    /**
     * Klassenmethode, die die Instanz der Datenbankklasse zurückgibt (Fabrikmethode genannt)
     *
     * @return Instanz der Datenbank-Klasse
     */
    public static Datenbank getInstance() {
        if (db == null) {
            // Neue Instanz von Datenbank erzeugen
            db = new Datenbank();
            db.init();
        }

        return db;
    }

    /**
     * Diese Methode soll die erforderliche Datenstruktur der Datenbank herstellen
     */
    private void init() {
        try {
            execute("CREATE TABLE IF NOT EXISTS \"fav_stocks\" (symbol VARCHAR(10) NOT NULL UNIQUE, name VARCHAR(255) NOT NULL, currency VARCHAR(4) NOT NULL);");
            this.commit();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
