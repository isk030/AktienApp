import database.Datenbank;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Standard Main Klasse für eine gewöhnliche JAVAFx App.
 * AktienApp ist eine Applikation die aktuelle Aktienkurse für beliebe Finanzprodukte aus der Yahoo Finance Api abfragt
 * und dem User zur Verfügung stellt. Ferner können die Aktienkursverläufe in Diagrammen dargestellt werden für verschiedene Zeiträume.
 * Eine Speicherung von favorisierten Finanzprodukten ist auch möglich
 * @author Iskender Dilaver
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Standard Main Methode der JavaFx App
     *
     * @param args Standard Übergabeparameter als String[]
     */
    public static void main(String[] args) {
        Datenbank.getInstance();
        launch(args);
    }

    /**
     * Standard start() Methode, um die initialisierung der JAVAFx App vorzubereiten
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/aktienGui.fxml"));
        primaryStage.setTitle("Aktien App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}
