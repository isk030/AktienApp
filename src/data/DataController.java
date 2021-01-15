package data;

import models.Analytic;
import models.Stock;
import models.StockList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.SeleniumService;
import services.YahooApiService;
import sun.awt.windows.WPrinterJob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * Die DataController Klasse kümmert sich um die Verknüpftung der Daten mit den jeweiligen Datenmodellen. Sie ruft den
 * YahooClient auf, um Daten abzufragen und mappt diese in die benötigten Objekte.
 */
public class DataController {

    private static DataController singletonDataController = null;
    private StockList stockList;
    private ArrayList<String> queryResults;
    private SeleniumService seleniumService;

    /**
     * Privater Konstruktor des DataController Objekts
     */
    private DataController() {
        this.queryResults = new ArrayList<>();

    }

    /**
     * Statische Methode, um das Singleton Pattern auf diese Klasse anzuwenden. Synchronisiert um threadsicher zu sein.
     *
     * @return Gibt ein DataConroller Objekt als Singleton zurück
     */
    public synchronized static DataController getInstance() {
        if (singletonDataController == null) {
            singletonDataController = new DataController();
        }
        return singletonDataController;
    }

    /**
     * Getter für das StockList Attribut
     *
     * @return StockList Objekt
     */
    public StockList getStockList() {
        return stockList;
    }

    /**
     * Setter für das StockList Attribut
     *
     * @param stockList als StockList Objekt
     */
    public void setStockList(StockList stockList) {
        this.stockList = stockList;
    }

    /**
     * Getter für das QueryResults Objekt
     *
     * @return Liste von symbol String als ArrayList
     */
    public ArrayList<String> getQueryResults() {
        return queryResults;
    }

    /**
     * Methode zur Konvertierung von Aktien Json Objekten in Java Objekte.
     *
     * @param jsonStockList Liste von Json Objekten als ArrayList
     * @return Gibt ein StockList Objekt zurück
     */
    private StockList mapToStockList(ArrayList<JSONObject> jsonStockList) {

        ArrayList<Stock> stockList = new ArrayList<>();
        for (JSONObject el : jsonStockList) {
            try {
                stockList.add(new Stock(el.getString("symbol"), el.getString("shortName"), el.getDouble("regularMarketPrice"), el.getString("currency"), el.getDouble("regularMarketChangePercent")));
            } catch (JSONException e) {
                System.out.println(e.getClass() + el.getString("symbol"));
            }


        }


        return new StockList(stockList);
    }


    /**
     * Methode um den YahooApiService für allgemeine Aktieninformationen zu starten und die Ergebnisse für
     * Java Objekte zur Verfügung zu stellen.
     *
     * @param symbols Symbol der Aktien, deren Infos abgefragt werden sollen als String
     */
    public void getStocks(ArrayList<String> symbols) throws IOException {
        YahooApiService yahooApiService = new YahooApiService();
        yahooApiService.fetchStocks(symbols);

        this.setStockList(mapToStockList(yahooApiService.getJsonStockList()));
    }

    /**
     * Methode um den YahooApiService für detaillierte Informationen zu einer einzelen Aktie abzurufen und die Ergebnisse
     * Java Objekten zur Verfügung zustellen. Zum Parsen wird org.Json genutzt.
     *
     * @param symbol    Symbol der Aktie als String
     * @param timeframe gewünschter Zeitabschnitt der Aktiendetails als String
     * @param interval  gewünschtes Interval der Details als String
     */
    public void getStockDetails(String symbol, String timeframe, String interval) {
        YahooApiService yahooApiService = new YahooApiService();
        try {
            yahooApiService.fetchDetails(symbol, timeframe, interval);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapStockDetails(yahooApiService.getJsonDetails(), interval);

    }

    public void getAnalytics(Stock stock) throws InterruptedException {
            String cleanName=null;
            this.seleniumService = new SeleniumService();
            try{
                cleanName = stock.getStockName().substring(0,stock.getStockName().indexOf(" "));
            }catch (Exception e){
                e.printStackTrace();
            }
            ArrayList<HashMap<String, String>> analyticsList = this.seleniumService.fetchAnalytics(cleanName);
            this.mapAnalyticsIntoModel(analyticsList, stock);
    }

    public void mapAnalyticsIntoModel(ArrayList<HashMap<String, String>> analyticsList, Stock stock) {

        ArrayList<Analytic> modelAnalytics = new ArrayList<>();
        for (HashMap<String, String> el : analyticsList) {
            modelAnalytics.add(new Analytic(el.get("name"), el.get("date"), el.get("targetTimeFrame"),
                    el.get("target"), el.get("rating"), el.get("delta")));
        }
        stock.setAnalytics(modelAnalytics);
    }

    /**
     * Methode zur Konvertierung von Aktiendetails eines Json Objekts in ein Java Objekt.
     *
     * @param jsonObject Json Objekt als JsonObject
     * @param interval   Interval der Details als String
     */
    private void mapStockDetails(JSONObject jsonObject, String interval) {
        String checkSymbol = jsonObject.getJSONObject("meta").getString("symbol");
        ArrayList<Integer> timestampSet = new ArrayList<>();
        ArrayList<Double> quoteList = new ArrayList<>();

        for (Stock el : this.getStockList().getStockList()) {
            el.setTimeZone(jsonObject.getJSONObject("meta").getString("exchangeTimezoneName"));
            if (el.getSymbol().equals(checkSymbol)) {
                for (int i = 0; i < jsonObject.getJSONArray("timestamp").length(); i++) {
                    timestampSet.add(jsonObject.getJSONArray("timestamp").getInt(i));
                }
                // Intervall wird explizit überprüft, da in einigen Fällen die JSON Datei anders aussieht
                if (interval == "15m" || interval == "5m" || interval == "60m") {
                    for (int i = 0; i < jsonObject.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("close").length(); i++) {
                        try {
                            quoteList.add(jsonObject.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("close").getDouble(i));
                        } catch (JSONException e) {
                            // Falls keine Preisdetails zu bestimmten Zeitpunkten nicht existieren, wird ein Durchschnitt der gegeben Werte ermittelt und stattdessen eingesetzt
                            quoteList.add(quoteList.stream().mapToDouble(d -> el.getActualValue()).average().orElse(0d));
                        }
                    }
                } else {
                    for (int i = 0; i < jsonObject.getJSONObject("indicators").getJSONArray("adjclose").getJSONObject(0).getJSONArray("adjclose").length(); i++) {
                        try {
                            // Falls keine Preisdetails zu bestimmten Zeitpunkten nicht existieren, wird ein Durchschnitt der gegeben Werte ermittelt und stattdessen eingesetzt
                            quoteList.add(jsonObject.getJSONObject("indicators").getJSONArray("adjclose").getJSONObject(0).getJSONArray("adjclose").getDouble(i));
                        } catch (JSONException e) {
                            quoteList.add(quoteList.stream().mapToDouble(d -> el.getActualValue()).average().orElse(0d));
                        }

                    }
                }
                el.setTimpestampSet(timestampSet);
                el.setQuoteList(quoteList);
            }
        }
    }

    /**
     * Methode um anhand eines Suchbegriffs Suchergebnisse zur Verfügung zu stellen
     *
     * @param query Suchparameter als String
     */
    public void getQuerySymbols(String query) throws IOException {
        YahooApiService yahooApiService = new YahooApiService();
        yahooApiService.searchStocks(query);
        this.queryResults.clear();

        JSONArray jsonSearchResult = yahooApiService.getJsonSearchResult();

        for (int i = 0; i < jsonSearchResult.length(); i++) {
            this.queryResults.add(jsonSearchResult.getJSONObject(i).getString("symbol"));
        }
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
        DataController that = (DataController) o;
        return Objects.equals(getStockList(), that.getStockList()) &&
                Objects.equals(getQueryResults(), that.getQueryResults());
    }

    /**
     * Standard hashCode() Methode
     *
     * @return Gibt HashCode des Objekts zurück
     */
    @Override
    public int hashCode() {
        return Objects.hash(getStockList(), getQueryResults());
    }

    /**
     * Standard toString() Methode um Ausgabestring erzeugen zu können
     *
     * @return Rückgabewert als String
     */
    @Override
    public String toString() {
        return "DataController{" +
                "stockList=" + stockList +
                ", queryResults=" + queryResults +
                '}';
    }
}
