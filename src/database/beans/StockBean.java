package database.beans;

import database.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Stock;
import models.StockList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Standard Bean Klasse, um CRUD Operation mit der Datenbank auszuführen.
 */
public class StockBean {

    // PreparedStatements
    private final static PreparedStatement pstmtSelect;
    private final static PreparedStatement pstmtSelectAll;
    private final static PreparedStatement pstmtInsert;
    private final static PreparedStatement pstmtUpdate;
    private final static PreparedStatement pstmtDelete;

    /*
      Initialisierung von der PreparedStatements
     */
    static {
        pstmtSelect = Datenbank.getInstance().prepareStatement("SELECT symbol, name, currency FROM fav_stocks WHERE symbol = ?;");
        pstmtSelectAll = Datenbank.getInstance().prepareStatement("SELECT symbol, name, currency FROM fav_stocks;");
        pstmtInsert = Datenbank.getInstance().prepareStatement("INSERT INTO fav_stocks (symbol, name, currency) VALUES (?, ?, ?);");
        pstmtUpdate = Datenbank.getInstance().prepareStatement("UPDATE fav_stocks SET symbol = ?, name = ?, currency = ? WHERE symbol = ?;");
        pstmtDelete = Datenbank.getInstance().prepareStatement("DELETE FROM fav_stocks WHERE symbol = ?;");
    }

    /**
     * Statische Methode, um Ergebnisse der Suchtabelle für potenzielle Speicherung vorzubereiten. Aktuell
     * für die Umwandlung einer StockList in eine ObservableList für die Tableview genutzt.
     *
     * @param stockList Liste von Aktien als StockList
     * @return Gibt eine Liste von Aktien zurück als ObservableList
     */
    public static ObservableList<Stock> getStockList(StockList stockList) {
        ObservableList<Stock> result = FXCollections.observableArrayList();

        result.addAll(stockList.getStockList());
        return result;
    }

    /**
     * Statische Methode, um eine Liste von Stock Objekten für die Tabellenansicht zu einer ObservableList
     * umzuwandeln und Aktien in die Datenbank zu speichern oder upzudaten.
     *
     * @param stockList List von Aktien als ArrayList
     * @return List von Aktien als ObservableList
     */
    public static ObservableList<Stock> saveFavs(ArrayList<Stock> stockList) {
        ObservableList<Stock> favList = FXCollections.observableArrayList();

        favList.addAll(stockList);
        for (Stock el : stockList) {
            upsert(el);
        }
        return favList;
    }

    /**
     * Methode um die Datenbank anhand eines Symbol Attributs nach einer Aktie abzufragen.
     *
     * @param symbol Symbol einer Aktie als String
     * @return Gibt eine Aktie als Stock Objekt zurück
     */
    public static Stock get(String symbol) {
        Stock result = null;

        try {
            pstmtSelect.setString(1, symbol);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                result = new Stock();
                result.setSymbol(rs.getString(1));
                result.setStockName(rs.getString(2));
                result.setStockCurrency(rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Methode, um alle Einträge aus der Datenbank abzufragen
     *
     * @return Liste mit allen Objekten als ArrayList aus der Datenbank oder null
     */
    public static ArrayList<Stock> getAll() {
        ArrayList<Stock> result = null;

        try {
            ResultSet rs = pstmtSelectAll.executeQuery();
            result = new ArrayList<>();

            while (rs.next()) {
                Stock eintrag = new Stock();
                eintrag.setSymbol(rs.getString(1));
                eintrag.setStockName(rs.getString(2));
                eintrag.setStockCurrency(rs.getString(3));


                result.add(eintrag);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Methode um eine Aktie in die Datenbank zu speichern oder zu aktualisieren.
     *
     * @param zuSpeichern Aktie als Stock
     */
    public static void upsert(Stock zuSpeichern) {

        try {
            Stock vorhanden = get(zuSpeichern.getSymbol());

            PreparedStatement pstmt;
            if (vorhanden == null) {
                // INSERT
                pstmtInsert.setString(1, zuSpeichern.getSymbol());
                pstmtInsert.setString(2, zuSpeichern.getStockName());
                pstmtInsert.setString(3, zuSpeichern.getStockCurrency());

                pstmt = pstmtInsert;
            } else {
                // UPDATE
                pstmtUpdate.setString(1, zuSpeichern.getSymbol());
                pstmtUpdate.setString(2, zuSpeichern.getStockName());
                pstmtUpdate.setString(3, zuSpeichern.getStockCurrency());

                pstmt = pstmtUpdate;
            }

            // INSERT oder UPDATE ausführen und kontrollieren ob es ok war
            int rows = pstmt.executeUpdate();

            // Prüfen ob der Eintrag erfolgreich war. Wenn ja, dann werden die Informationen in die Datenbank
            // übertragen (commit). Wenn nicht, werden sie verworfen (rollback)
            if (rows == 1) {
                Datenbank.getInstance().commit();
            } else {
                Datenbank.getInstance().rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Methode, um eine bestimmte Aktie aus der Datenbank zu löschen.
     *
     * @param zuLoeschen Aktie als Stock
     */
    public static void delete(Stock zuLoeschen) {

        try {
            pstmtDelete.setString(1, zuLoeschen.getSymbol());

            int rows = pstmtDelete.executeUpdate();

            // Prüfen ob der Eintrag erfolgreich war. Wenn ja, dann werden die Informationen in die Datenbank
            // übertragen (commit). Wenn nicht, werden sie verworfen (rollback)
            if (rows == 1) {
                Datenbank.getInstance().commit();
            } else {
                Datenbank.getInstance().rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

