package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Klasse um ein Analyse Objekt zu halten und zu verwalten.
 */
public class Analytic {
    private StringProperty provider;
    private StringProperty publishedDate;
    private StringProperty forcastTimeFrame;
    private StringProperty targetValue;
    private StringProperty rating;
    private StringProperty delta;

    /**
     * Standard Konstruktor
     * @param provider Name des Analysehauses
     * @param publishedDate Veröffentlichtes Datum der Analyse
     * @param forcastTimeFrame Zeitrahmen der Prognose
     * @param targeValue Zielwert der Analyse
     * @param rating Allgemeine Bewertung der Aktie
     * @param delta Abstand zum aktuellen Aktienwert in%
     */
    public Analytic (String provider, String publishedDate,
                    String forcastTimeFrame, String targeValue, String rating, String delta) {

        this.provider = new SimpleStringProperty(provider);
        this.publishedDate = new SimpleStringProperty(publishedDate);
        this.forcastTimeFrame = new SimpleStringProperty(forcastTimeFrame);
        this.targetValue = new SimpleStringProperty(targeValue);
        this.rating = new SimpleStringProperty(rating);
        this.delta = new SimpleStringProperty(delta);
    }

    public String getProvider() {
        return provider.get();
    }

    public StringProperty providerProperty() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider.set(provider);
    }

    public String getPublishedDate() {
        return publishedDate.get();
    }

    public StringProperty publishedDateProperty() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate.set(publishedDate);
    }

    public String getForcastTimeFrame() {
        return forcastTimeFrame.get();
    }

    public StringProperty forcastTimeFrameProperty() {
        return forcastTimeFrame;
    }

    public void setForcastTimeFrame(String forcastTimeFrame) {
        this.forcastTimeFrame.set(forcastTimeFrame);
    }

    public String getTargetValue() {
        return targetValue.get();
    }

    public StringProperty targetValueProperty() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue.set(targetValue);
    }

    public String getRating() {
        return rating.get();
    }

    public StringProperty ratingProperty() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating.set(rating);
    }

    public String getDelta() {
        return delta.get();
    }

    public StringProperty deltaProperty() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta.set(delta);
    }
}

