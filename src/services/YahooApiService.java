package services;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Die YahooApiService Klasse nutzt die OkHttpClient Library um die Yahoo Finance API anzusprechen und die Ergebnisse
 * als rohe JSON Objekte mit org.Json weiterzuleiten.
 */
public class YahooApiService {

    private final ArrayList<JSONObject> jsonStockList;
    private JSONObject jsonDetails;
    private JSONArray jsonSearchResult;

    /**
     * Standard Konstruktor
     */
    public YahooApiService() {
        jsonStockList = new ArrayList<>();
    }

    /**
     * Getter, welcher die Aktiendetails aus der API Anfrage als JSON Objekt zurück gibt.
     *
     * @return JSON Objekt als JSONObject
     */
    public JSONObject getJsonDetails() {
        return jsonDetails;
    }

    /**
     * Setter, welcher die Aktiendetails im JSON Format setzt
     *
     * @param jsonDetails Json Objekt mit Aktiendetails als JSONObject
     */
    public void setJsonDetails(JSONObject jsonDetails) {
        this.jsonDetails = jsonDetails;
    }

    /**
     * Getter, welcher die Suchergebnisse der API Anfrage als JSON Liste zurück gibt
     *
     * @return JSON Objektliste als JSONArray
     */
    public JSONArray getJsonSearchResult() {
        return jsonSearchResult;
    }

    /**
     * Setter, welcher die Suchergebnisse der API als JSON Liste setzt.
     *
     * @param jsonSearchResult List von JSON Objekten als JSONArray
     */
    public void setJsonSearchResult(JSONArray jsonSearchResult) {
        this.jsonSearchResult = jsonSearchResult;
    }

    /**
     * Getter, welcher Aktieninformationen als Liste zurück gibt.
     *
     * @return Liste von JSON Objekten als ArrayList
     */
    public ArrayList<JSONObject> getJsonStockList() {
        return jsonStockList;
    }

    /**
     * Methode um mit dem OKHTTP Client die Yahoo Finance API anzusprechen, um gewünschte Aktiendaten in einer Aktienliste
     * als JsonObject zu erhalten.
     *
     * @param symbols Aktienamenkürzel nach amerikanischem Standard als ArrayList
     * @throws IOException Fehlerhafte Parameter geben eine IOException aus
     */
    public void fetchStocks(ArrayList<String> symbols) throws IOException {
        StringBuilder urlStocks = new StringBuilder("https://rapidapi.p.rapidapi.com/market/v2/get-quotes?symbols=");

        for (String s : symbols) {
            urlStocks.append(s).append("%2C");
        }
        urlStocks.append("&region=US");

        String jsonData = "";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(String.valueOf(urlStocks))
                .get()
                .addHeader("x-rapidapi-key", "594cc31351mshee5d86395344b5ap11605fjsnb289c6caa8b5")
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();

        jsonData = response.body().string();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONObject("quoteResponse").getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonEl = jsonArray.getJSONObject(i);
                this.jsonStockList.add(jsonEl);
            }
        } catch (JSONException ignored) {
        }


    }

    /**
     * Methode um mit dem OKHTTP Client die Yahoo Finance API anzusprechen, um gewünschte Aktiendetails in einer Aktienliste
     * als JsonObject zu erstellen.
     *
     * @param symbol Aktienamenkürzel nach amerikanischem Standard als String
     * @param timeframe Zeitabschnitt als String
     * @param interval Zeitinterval als String
     * @throws IOException Fehlerhafte Parameter geben eine IOException aus
     */
    public void fetchDetails(String symbol, String timeframe, String interval) throws IOException {
        StringBuilder urlDetails = new StringBuilder("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-charts?symbol=");

        urlDetails.append(symbol).append("&interval=").append(interval).append("&range=").append(timeframe).append("&region=US");

        String jsonData = "";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.valueOf(urlDetails))
                .get()
                .addHeader("x-rapidapi-key", "594cc31351mshee5d86395344b5ap11605fjsnb289c6caa8b5")
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();

        jsonData = response.body().string();

        JSONObject jsonObject = new JSONObject(jsonData);

        JSONObject jsonResult = jsonObject.getJSONObject("chart").getJSONArray("result").getJSONObject(0);

        setJsonDetails(jsonResult);

    }

    /**
     * Methode um mit dem OkHttpClient die API für eine Suche anzufragen um spezifische Aktien zu finden.
     *
     * @param query Suchstring als String
     * @throws IOException Fehlerhafte Parameter geben eine IOException aus
     */
    public void searchStocks(String query) throws IOException {
        StringBuilder urlDetails = new StringBuilder("https://apidojo-yahoo-finance-v1.p.rapidapi.com/auto-complete?q=");

        urlDetails.append(query).append("&region=US");

        String jsonData = "";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.valueOf(urlDetails))
                .get()
                .addHeader("x-rapidapi-key", "594cc31351mshee5d86395344b5ap11605fjsnb289c6caa8b5")
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();

        jsonData = response.body().string();

        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray jsonResult = jsonObject.getJSONArray("quotes");

        setJsonSearchResult(jsonResult);
    }


}
