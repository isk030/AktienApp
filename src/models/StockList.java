package models;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Die StockList Klasse stellt eine Liste dar, die verschiedene Stock Objekte beinhaltet und zur Verfügung stellt
 */
public class StockList {

    private final ArrayList<Stock> stockList;

    /**
     * Standard Konstruktor
     *
     * @param stockArrayList Liste von Stock Objekten als ArrayList
     */
    public StockList(ArrayList<Stock> stockArrayList) {
        this.stockList = stockArrayList;
    }

    /**
     * Getter, welcher die Liste mit Aktien Objekten zurück gibt.
     *
     * @return StockList als ArrayList
     */
    public ArrayList<Stock> getStockList() {
        return stockList;
    }


    /**
     * Standard equals() Methdode, um Aktien Objekt mit anderen Objekten zu vergleichen.
     *
     * @param o Generisches Objekt
     * @return Boolean Auswertung des Vergleichs
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockList stockList1 = (StockList) o;
        return Objects.equals(getStockList(), stockList1.getStockList());
    }

    /**
     * Standard hashCode() Methode
     *
     * @return Gibt HashCode des Objekts zurück
     */
    @Override
    public int hashCode() {
        return Objects.hash(getStockList());
    }

    /**
     * Standard toString() Methode um Ausgabestring erzeugen zu können
     *
     * @return Rückgabewert als String
     */
    @Override
    public String toString() {
        return "StockList{" +
                "stockList=" + stockList +
                '}';
    }
}
