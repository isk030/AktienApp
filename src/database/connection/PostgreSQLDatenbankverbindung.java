package database.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLDatenbankverbindung extends AllgemeineDatenbankverbindung {

    private final String SERVER;
    private final int PORT;
    private final String DATABASE;
    private final String USER;
    private final String PASSWD;

    /**
     * Konstruktor, der die übergebenen Verbindungswerte auf gültige Werte überprüft.
     * Sollte dies nicht der Fall sein, wird eine IllegalArgumentException geworfen
     *
     * @param server   PostgreSQL-Server zu dem eine Datenbankverbindung aufgebaut werden soll
     * @param port     Port unter dem der MySQL-Server erreichbar ist
     * @param database Datenbank zu dem eine Verbincung aufgebaut werden soll
     * @param user     Datenbankbenutzer, der für die Verbindung erforderlich ist
     * @param passwd   Passwort für den Datenbankbenutzer
     * @throws IllegalArgumentException wird geworfen, wenn einer der übergebene Parameter (server, port, database) ungültig ist.
     */
    public PostgreSQLDatenbankverbindung(String server, int port, String database, String user, String passwd) {
        if (server == null || server.isEmpty()) {
            throw new IllegalArgumentException("Kein gültiger Server angegeben!");
        }

        if (port < 0) {
            throw new IllegalArgumentException("Kein gültiger Port angegeben!");
        }

        if (database == null || database.isEmpty()) {
            throw new IllegalArgumentException("Keine gültige Datenbank angegeben!");
        }

        this.SERVER = server;
        this.PORT = port;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWD = passwd;

        connect();
    }

    @Override
    public boolean connect() {
        if (con == null) {

            try {
                // Treiber laden
                Class.forName("org.postgresql.Driver");

                // Datenbankverbindung aufbauen
                con = DriverManager.getConnection("jdbc:postgresql://" + SERVER + ":" + PORT + "/" + DATABASE, USER, PASSWD);

                // AutoCommit auf false setzen
                con.setAutoCommit(false);

            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Der JDBC-Treiber für PostgreSQL konnte nicht gefunden werden!");
            } catch (SQLException e) {
                throw new IllegalArgumentException("Problem beim Zugriff auf die Datenbank '" + DATABASE + "' auf dem Server '" + SERVER + "' mit dem Port '" + PORT + "' und dem User '" + USER + "'. Bitte Zugriff prüfen!");
            }
        }

        return true;
    }

}
