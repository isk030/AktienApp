package view;

import data.DataController;
import database.beans.StockBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Analytic;
import models.Stock;
import models.StockList;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Standard GUIController Klasse für die JAVAFx Oberfläche
 */
public class GuiController {
    private ArrayList<Stock> favList;
    private ObservableList<Stock> tablefavsData, tableSearchData;
    private ObservableList<Analytic> tableAnalyticsData;
    private Stock lastfocusedItem;

    @FXML
    private TableView<Stock> tvStock;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnTarget;

    @FXML
    private TextField tfSearch;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSearch;

    @FXML
    private ProgressIndicator pbMain;

    @FXML
    private Button btn6mo;

    @FXML
    private Button btn1mo;

    @FXML
    private Button btn1wk;

    @FXML
    private Button btn1d;

    @FXML
    private Button btnFavs;

    @FXML
    private TableView<Stock> tvStockFavs;

    @FXML
    private TableView<Analytic> tvAnalytics;


    /**
     * Standard initialize() Methode der JavaFx Applikation.
     */
    @FXML
    void initialize() {
        assert tvStock != null : "fx:id=\"tvStock\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert tvAnalytics != null : "fx:id=\"tvAnalytics\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btn6mo != null : "fx:id=\"btn6mo\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btn1mo != null : "fx:id=\"btn6mo\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btn1wk != null : "fx:id=\"btn6mo\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btn1d != null : "fx:id=\"btn6mo\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btnSearch != null : "fx:id=\"btnSearch\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btnTarget != null : "fx:id=\"btnTarget\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert tvStockFavs != null : "fx:id=\"tvStockFavs\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert tfSearch != null : "fx:id=\"tfSearch\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert pbMain != null : "fx:id=\"pbMain\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btnRefresh != null : "fx:id=\"btnRefresh\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btnFavs != null : "fx:id=\"btnFavs\" was not injected: check your FXML file 'aktienGui.fxml'.";
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'aktienGui.fxml'.";

        initMainTable();
    }


    /**
     * Methode zur Initialisierung der Tabellen und Laden der Favoriten aus der Datenbank
     */
    private void initMainTable() {
        favList = new ArrayList<>();
        getSelectedItem();
        tablefavsData = FXCollections.observableArrayList();


        // Spalten erstellen
        TableColumn<Stock, String> tc1 = new TableColumn<>("Name");
        TableColumn<Stock, String> tc2 = new TableColumn<>("Kürzel");
        TableColumn<Stock, Double> tc3 = new TableColumn<>("Aktienkurs");
        TableColumn<Stock, String> tc4 = new TableColumn<>("Währung");
        TableColumn<Stock, Double> tc5 = new TableColumn<>("% zum Vortag");

        // Zuordnung Werte <-> Model
        tc1.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        tc2.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        tc3.setCellValueFactory(new PropertyValueFactory<>("actualValue"));
        tc4.setCellValueFactory(new PropertyValueFactory<>("stockCurrency"));
        tc5.setCellValueFactory(new PropertyValueFactory<>("changePercent"));

        // Spalten erstellen
        TableColumn<Stock, String> tFc1 = new TableColumn<>("Name");
        TableColumn<Stock, String> tFc2 = new TableColumn<>("Kürzel");
        TableColumn<Stock, Double> tFc3 = new TableColumn<>("Aktienkurs");
        TableColumn<Stock, String> tFc4 = new TableColumn<>("Währung");
        TableColumn<Stock, Double> tFc5 = new TableColumn<>("% zum Vortag");

        // Zuordnung Werte <-> Model
        tFc1.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        tFc2.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        tFc3.setCellValueFactory(new PropertyValueFactory<>("actualValue"));
        tFc4.setCellValueFactory(new PropertyValueFactory<>("stockCurrency"));
        tFc5.setCellValueFactory(new PropertyValueFactory<>("changePercent"));

        // Spalten erstellen
        TableColumn<Analytic, String> tAc1 = new TableColumn<>("Datum");
        TableColumn<Analytic, String> tAc2 = new TableColumn<>("Analyst");
        TableColumn<Analytic, String> tAc3 = new TableColumn<>("Bewertung");
        TableColumn<Analytic, String> tAc4 = new TableColumn<>("Kursziel");
        TableColumn<Analytic, String> tAc5 = new TableColumn<>("Zeitraum");
        TableColumn<Analytic, String> tAc6 = new TableColumn<>("Delta");

        // Zuordnung Werte <-> Model
        tAc1.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        tAc2.setCellValueFactory(new PropertyValueFactory<>("provider"));
        tAc3.setCellValueFactory(new PropertyValueFactory<>("rating"));
        tAc4.setCellValueFactory(new PropertyValueFactory<>("targetValue"));
        tAc5.setCellValueFactory(new PropertyValueFactory<>("forcastTimeFrame"));
        tAc6.setCellValueFactory(new PropertyValueFactory<>("delta"));

        // Spalten hinzufügen
        tvAnalytics.getColumns().add(tAc1);
        tvAnalytics.getColumns().add(tAc2);
        tvAnalytics.getColumns().add(tAc3);
        tvAnalytics.getColumns().add(tAc4);
        tvAnalytics.getColumns().add(tAc5);
        tvAnalytics.getColumns().add(tAc6);

        // Spalten hinzufügen
        tvStockFavs.getColumns().add(tFc1);
        tvStockFavs.getColumns().add(tFc2);
        tvStockFavs.getColumns().add(tFc3);
        tvStockFavs.getColumns().add(tFc4);
        tvStockFavs.getColumns().add(tFc5);

        // Spalten hinzufügen
        tvStock.getColumns().add(tc1);
        tvStock.getColumns().add(tc2);
        tvStock.getColumns().add(tc3);
        tvStock.getColumns().add(tc4);
        tvStock.getColumns().add(tc5);

        tc3.setStyle("-fx-alignment:CENTER-RIGHT");

        tc5.setCellFactory(param -> new TableCell<Stock, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    NumberFormat formatter = NumberFormat.getInstance(Locale.US);
                    formatter.setMaximumFractionDigits(2);
                    setText(formatter.format(item));
                    if (item < 0d) {
                        setStyle("-fx-background-color: #f89696; -fx-alignment:CENTER-RIGHT;");
                    } else if (item > 0d) {
                        setStyle("-fx-background-color: #94ef9d; -fx-alignment:CENTER-RIGHT");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        tFc3.setStyle("-fx-alignment:CENTER-RIGHT");

        /*
        Methode updateItem wird überschrieben um Zellenformatierung zu implementieren
         */
        tFc5.setCellFactory(param -> new TableCell<Stock, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    NumberFormat formatter = NumberFormat.getInstance(Locale.US);
                    formatter.setMaximumFractionDigits(2);
                    setText(formatter.format(item));
                    if (item < 0d) {
                        setStyle("-fx-background-color: #f89696; -fx-alignment:CENTER-RIGHT;");
                    } else if (item > 0d) {
                        setStyle("-fx-background-color: #94ef9d; -fx-alignment:CENTER-RIGHT");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // Suche mit Enter ausführen
        tfSearch.setOnAction(e -> {
            search();
        });

        favList = StockBean.getAll();
        ArrayList<String> favSymbolList = new ArrayList<>();

        for (Stock el : favList) {
            favSymbolList.add(el.getSymbol());
        }

        pbMain.setVisible(true);
        tvStockFavs.setDisable(true);
        Task<Void> getFavListActualData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DataController.getInstance().getStocks(favSymbolList);

                tablefavsData = StockBean.getStockList(DataController.getInstance().getStockList());
                tvStockFavs.setItems(tablefavsData);
                return null;
            }
        };

        getFavListActualData.setOnSucceeded((WorkerStateEvent event) -> {
            pbMain.setVisible(false);
            tvStockFavs.setDisable(false);
            favList = new ArrayList<Stock>(tablefavsData);
            StockBean.saveFavs(favList);
        });
        new Thread(getFavListActualData).start();

    }

    /**
     * Methode um mit dem Suchbutton die Suche auszulösen
     */
    @FXML
    void search() {

        tableSearchData = null;
        pbMain.setVisible(true);
        tvStock.setDisable(true);

        String queryString = tfSearch.getText();
        if (queryString == null || queryString.isEmpty()) {
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setHeight(400);
            alert.setTitle("Keine gültige Suchanfrage");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie einen gültigen Suchbegriff ein");
            alert.showAndWait();
            pbMain.setVisible(false);
            tvStock.setDisable(false);
            return;
        }

        Task<Void> getSearchResults = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                DataController.getInstance().getQuerySymbols(queryString);


                DataController.getInstance().getStocks(DataController.getInstance().getQueryResults());
                tableSearchData = StockBean.getStockList(DataController.getInstance().getStockList());
                tvStock.setItems(tableSearchData);
                return null;
            }
        };

        getSearchResults.setOnSucceeded((WorkerStateEvent event) -> {
            pbMain.setVisible(false);
            tvStock.setDisable(false);
        });
        new Thread(getSearchResults).start();

    }

    @FXML
    void getAnalytics() {
        tableAnalyticsData = FXCollections.observableArrayList();
        Stock stock = this.lastfocusedItem;

        DataController.getInstance().getAnalytics(stock);

        tableAnalyticsData.addAll(stock.getAnalytics());
        tvAnalytics.setItems(tableAnalyticsData);
    }

    /**
     * Methode um einen 6 Monats Kursverlauf zu zeichnen
     *
     * @throws InterruptedException Löst eine Exception aus wenn Thread unterbrochen wird
     */
    @FXML
    void drawChart6mo() throws InterruptedException {
        generalChartDraw("6mo", "1wk");

    }

    /**
     * Methode um einen 1 Monats Verlauf des Aktienkurses zu zeichnen
     *
     * @throws InterruptedException Löst eine Exception aus wenn Thread unterbrochen wird
     */
    @FXML
    void drawChart1mo() throws InterruptedException {
        generalChartDraw("1mo", "1d");
    }

    /**
     * Methode um ausgewähltes Objekt in den Tabellen in Echtzeit zu indentifizieren.
     */
    private void getSelectedItem() {
        tvStockFavs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tvStock.getSelectionModel().clearSelection();
                this.lastfocusedItem = newVal;
            }
        });

        tvStock.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tvStockFavs.getSelectionModel().clearSelection();
                this.lastfocusedItem = newVal;
            }
        });

    }

    /**
     * Allgemeine Methode um den LineChart anhand des übermittelten Zeitfensters und Intervals zu zeichnen
     *
     * @param timeframe Zeitrahmen des Kursverlaufs als String
     * @param interval  Zeitinterval der Kursverlaufs als String
     * @throws InterruptedException Wirft eine Exception, wenn ein Thread abbricht.
     */
    private void generalChartDraw(String timeframe, String interval) throws InterruptedException {
        pbMain.setVisible(true);

        Stock stock = this.lastfocusedItem;

        if (stock == null) {
            invokeAlert();
            return;
        }
        String symbol = stock.getSymbol();

        Stage stage = new Stage();
        stage.setTitle("Aktienkurs Graph für: " + stock.getStockName());
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Zeitraum");
        yAxis.setLabel("Preis in " + stock.getStockCurrency());

        LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis, yAxis);

        lineChart.setTitle("Aktienkursverlauf für " + "(" + timeframe + "): " + stock.getStockName());

        XYChart.Series series = new XYChart.Series();
        series.setName(stock.getStockName());


        Task<Void> getAndDrawDetailsChart = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {

                /*
                 * Logikblock, welches ausgewähltes Objekt interpretiert, um die richtige StockList des Datacontrollers auszuwählen
                 */
                if (tablefavsData.contains(lastfocusedItem)) {
                    if (tableSearchData == null) {
                        ArrayList<Stock> stockListPrep = new ArrayList<>(tablefavsData);
                        DataController.getInstance().setStockList(new StockList(stockListPrep));
                    } else {
                        if (!tableSearchData.contains(lastfocusedItem)) {
                            ArrayList<Stock> stockListPrep = new ArrayList<>(tablefavsData);
                            DataController.getInstance().setStockList(new StockList(stockListPrep));
                        } else {
                            ArrayList<Stock> stockListPrep = new ArrayList<>(tableSearchData);
                            DataController.getInstance().setStockList(new StockList(stockListPrep));
                        }
                    }
                } else {
                    ArrayList<Stock> stockListPrep = new ArrayList<>(tableSearchData);
                    DataController.getInstance().setStockList(new StockList(stockListPrep));
                }

                DataController.getInstance().getStockDetails(symbol, timeframe, interval);

                for (int i = 0; i < stock.getTimpestampSet().size(); i++) {
                    ArrayList<Integer> sortedList = stock.getTimpestampSet();
                    Collections.sort(sortedList);

                    SimpleDateFormat sdf;
                    if (timeframe.equals("1d") || timeframe.equals("5d")) {
                        sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm ");
                    } else {
                        sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
                    }
                    sdf.setTimeZone(TimeZone.getTimeZone(stock.getTimeZone()));
                    sdf.setTimeZone(TimeZone.getDefault());
                    String date = sdf.format(new java.util.Date(sortedList.get(i) * 1000L));

                    series.getData().add(new XYChart.Data(date, stock.getQuoteList().get(i)));
                }
                return null;
            }
        };

        getAndDrawDetailsChart.setOnSucceeded((WorkerStateEvent event) -> {
            Double recentTarget;
            String targetTimeFrame;
            yAxis.setAutoRanging(true);
            xAxis.setAutoRanging(true);
            yAxis.setForceZeroInRange(false);
            lineChart.autosize();
            lineChart.setAnimated(false);



            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().add(series);

            if (!stock.getAnalytics().isEmpty()) {
                String[] targetValueholder = tableAnalyticsData.get(0).getTargetValue().split(" ");


                try {
                    targetValueholder[0].replace(",", ".");
                    targetValueholder[0].replace(".", ",");
                    recentTarget = Double.parseDouble(targetValueholder[0]);
                    targetTimeFrame = tableAnalyticsData.get(0).getForcastTimeFrame();
                } catch (NumberFormatException e) {
                    String[] targetValueholder2 = tableAnalyticsData.get(1).getTargetValue().split(" ");
                    recentTarget = Double.parseDouble(targetValueholder2[0].replace(",", ".").replace(".", ","));
                    targetTimeFrame = tableAnalyticsData.get(1).getForcastTimeFrame();
                }

                XYChart.Series series1 = new XYChart.Series();
                series1.getData().add(new XYChart.Data("in "+targetTimeFrame , recentTarget));
                series1.setName("aktuellstes Kursziel");
                lineChart.getData().add(series1);
            }

            stage.setScene(scene);

            stage.show();
            pbMain.setVisible(false);

        });
        Thread chartThread = new Thread(getAndDrawDetailsChart);
        chartThread.start();

    }

    /**
     * Methode um einen Hinweis bei falschen oder sinnlosen Auswahlen oder Eingaben zu erzeugen.
     */
    private void invokeAlert() {
        Alert alert = new Alert((Alert.AlertType.INFORMATION));
        alert.setHeight(400);
        alert.setTitle("Kein Finanzprodukt ausgewählt");
        alert.setHeaderText(null);
        alert.setContentText("Bitte wählen Sie ein Finanzprodukt aus " +
                "bevor Sie sich einen Kursverlauf anzeigen lassen wollen");
        alert.showAndWait();
        pbMain.setVisible(false);
    }

    /**
     * Methode um einen 1 Wochen Kursverlauf zu zeichnen
     *
     * @throws InterruptedException Wirft eine Exception bei Threadabbruch
     */
    @FXML
    void drawChart1wk() throws InterruptedException {
        generalChartDraw("5d", "60m");
    }

    /**
     * Methode um einen 1 Tages Kursverlauf zu zeichnen
     *
     * @throws InterruptedException Wirft eine Exception bei Threadabbruch
     */
    @FXML
    void drawChart1d() throws InterruptedException {
        generalChartDraw("1d", "15m");
    }

    /**
     * Methode um ausgewählte Aktie in der der Suchtabelle in die Favoritentabelle hinzuzufügen und
     * die Elemente der Favoritentabelle in die Datenbank einzutragen.
     */
    @FXML
    void addToFavs() {
        Stock stock = tvStock.getSelectionModel().getSelectedItem();

        if (tvStockFavs.getSelectionModel().getSelectedItem() == null) {
            ArrayList<String> symbolList = new ArrayList<>();
            for (Stock el : favList) {
                symbolList.add(el.getSymbol());
            }
            if (!symbolList.contains(stock.getSymbol())) {
                favList.add(stock);
            }
            tvStockFavs.setItems(StockBean.saveFavs(favList));
        }
    }

    /**
     * Methode um ausgewählte Elemente aus der FavoritenListe und aus der Datenbank zu entfernen
     */
    @FXML
    void deleteFav() {
        favList.remove(this.lastfocusedItem);
        tvStockFavs.setItems(StockBean.saveFavs(favList));
        tvStockFavs.getSelectionModel().clearSelection();
        tvStock.getSelectionModel().clearSelection();
        StockBean.delete(this.lastfocusedItem);
    }
}