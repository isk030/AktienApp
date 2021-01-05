package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Die Stock Klasse bildet eine einzelne Aktie ab mit essentiellen Details zu dieser Aktie.
 */
public class Stock {

    private StringProperty symbol;
    private StringProperty stockName;
    private StringProperty stockCurrency;
    private DoubleProperty actualValue;
    private DoubleProperty changePercent;
    private ArrayList<Integer> timpestampSet;
    private ArrayList<Double> quoteList;
    private StringProperty timeZone;
    private ArrayList<Analytic> analytics;


    /**
     * Standard Konstruktor indem wesentliche Details des Objekts gesetzt werden
     *
     * @param symbol        Einzigartiger Kürzel der Aktie nach amerikanischem Vorbild
     * @param aktienName    Name der Firma der Aktie
     * @param actualValue   Aktueller Aktienkurs
     * @param stockCurrency Währungseinheit des aktuellen Aktienkurses
     * @param changePercent Veränderung des Aktienkurses im Vergleich zu gestern als Double
     */
    public Stock(String symbol, String aktienName, Double actualValue, String stockCurrency, Double changePercent) {
        this.symbol = new SimpleStringProperty();
        this.stockName = new SimpleStringProperty();
        this.actualValue = new SimpleDoubleProperty();
        this.stockCurrency = new SimpleStringProperty();
        this.changePercent = new SimpleDoubleProperty();
        this.timpestampSet = new ArrayList<>();
        this.quoteList = new ArrayList<>();
        this.timeZone = new SimpleStringProperty();
        this.analytics = new ArrayList<>();

        this.setSymbol(symbol);
        this.setActualValue(actualValue);
        this.setStockName(aktienName);
        this.setStockCurrency(stockCurrency);
        this.setChangePercent(changePercent);
    }


    /**
     * Parameterfreier Kontruktor um ein leeres Stock Objekt zu generieren. Wir gebraucht z.B um aus der Datenbank heraus
     * ein Stock Objekt zu erzeugen und zu definieren
     */
    public Stock() {
        this.stockCurrency = new SimpleStringProperty();
        this.symbol = new SimpleStringProperty();
        this.stockName = new SimpleStringProperty();


        this.setStockCurrency("");
        this.setSymbol("");
        this.setStockName("");
    }

    public ArrayList<Analytic> getAnalytics() {
        return analytics;
    }

    public void setAnalytics(ArrayList<Analytic> analytics) {
        this.analytics = analytics;
    }

    /**
     * Getter, welcher die Zeitzone der Aktie zurück gibt.
     *
     * @return Zeitzone als String
     */
    public String getTimeZone() {
        return timeZone.get();
    }


    /**
     * Setter, welcher die Zeitzone einer Aktie setzt
     *
     * @param timeZone Zeitzone als String
     */
    public void setTimeZone(String timeZone) {
        this.timeZone.set(timeZone);
    }


    /**
     * Getter, welches die Liste mit Zeitstempeln der Aktie (Epoch Time Millisekunden) zurück gibt.
     *
     * @return List von Zeitstempeln (Epoch Time Millisekunden) als ArrayList
     */
    public ArrayList<Integer> getTimpestampSet() {
        return timpestampSet;
    }

    /**
     * Setter, welches die Liste mit Zeitstempeln (Epoch Time Millisekunden) einer Aktie setzt.
     *
     * @param timpestampSet Liste von Zeitstempeln (Epoch Time Millisekunden) als ArrayList
     */
    public void setTimpestampSet(ArrayList<Integer> timpestampSet) {
        this.timpestampSet = timpestampSet;
    }

    /**
     * Getter, welches die Liste mit Aktienkurswerten der Aktie zurück gibt.
     *
     * @return List von Aktienkurswerten als ArrayList
     */
    public ArrayList<Double> getQuoteList() {
        return quoteList;
    }

    /**
     * Setter, welches die Liste mit Aktienkurswerten der Aktie setzt.
     *
     * @param quoteList List der Aktienkurswerte als ArrayList
     */
    public void setQuoteList(ArrayList<Double> quoteList) {
        this.quoteList = quoteList;
    }

    /**
     * Getter, welcher die prozentuale Veränderung des Aktienwertes zum gestrigen Tag zurück gibt.
     *
     * @return Prozentuale Veränderung des Aktienwertes zum Vortag als Double
     */
    public Double getChangePercent() {
        return changePercent.get();
    }

    /**
     * Setter, welcher die prozentuale Veränderung des Aktienwertes zum gestrigen Tag setzt.
     *
     * @param changePercent prozentuale Veränderung zum Vortag als Double
     */
    public void setChangePercent(Double changePercent) {
        this.changePercent.set(changePercent);
    }

    /**
     * Getter, welcher die Währungseinheit der Aktie zurück gibt.
     *
     * @return Währungseinheit der Aktie als String
     */
    public String getStockCurrency() {
        return stockCurrency.get();
    }

    /**
     * Setter, welcher die Währungseinheit der Aktie setzt
     *
     * @param stockCurrency Währungseinheit der Aktie als String
     */
    public void setStockCurrency(String stockCurrency) {
        this.stockCurrency.set(stockCurrency);
    }

    /**
     * Getter, welcher den einzigartigen Aktienkürzel der Aktie zurück gibt.
     *
     * @return Aktienkürzel der Aktie als String
     */
    public String getSymbol() {
        return symbol.getValue();
    }

    /**
     * Setter, welcher den einzigartigen Aktienkürzel der Aktie setzt.
     *
     * @param symbol Aktienkürzel der Aktie als String
     */
    public void setSymbol(String symbol) {
        this.symbol.setValue(symbol);
    }

    /**
     * Getter, welcher den Namen der Firma der Aktie zurück gibt.
     *
     * @return Name der Firma der Aktie als String
     */
    public String getStockName() {
        return stockName.getValue();
    }

    /**
     * Setter, welcher den Namen der Firma der Aktie setzt.
     *
     * @param stockName Name der Firma der Aktie als String
     */
    public void setStockName(String stockName) {
        this.stockName.setValue(stockName);
    }

    /**
     * Getter, welcher den aktuellen Aktienkurswert der Aktie zurück gibt.
     *
     * @return Aktienkurswert der Aktie als Double
     */
    public Double getActualValue() {
        return actualValue.getValue();
    }

    /**
     * Setter, welcher den Aktienkurswert der Aktie setzt.
     *
     * @param actualValue Aktienkurs der Aktie als Double
     */
    public void setActualValue(Double actualValue) {
        this.actualValue.setValue(actualValue);
    }

    /**
     * Standard equals() Methdode, um Aktien Objekt mit anderen Objekten zu vergleichen.
     *
     * @param o Generisches Objekt
     * @return Boolean Auswertung des Vergleichs
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Stock stock = (Stock) o;
        return Objects.equals(getSymbol(), stock.getSymbol()) &&
                Objects.equals(getStockName(), stock.getStockName()) &&
                Objects.equals(getActualValue(), stock.getActualValue());
    }

    /**
     * Standard hashCode() Methode
     *
     * @return Gibt HashCode des Objekts zurück
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSymbol(), getStockName(), getActualValue());
    }

    /**
     * Standard toString() Methode um Ausgabestring erzeugen zu können
     *
     * @return Rückgabewert als String
     */
    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", aktienName='" + stockName + '\'' +
                ", actualValue=" + actualValue +
                '}';
    }
}
