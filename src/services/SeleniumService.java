package services;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Diese Klasse benutzt das Selenium Framwork um einen Browser zu simulieren und zu bedienen, sodass Inernetseiten
 * angesteuert werden können, um Kursziele zu extrahieren und nutzbar zu machen.
 */
public class SeleniumService {

    private final ArrayList<String> fetchedLines;
    private final ChromeDriver chrome1;
    private final ChromeOptions options;
    private final WebDriverWait wait;

    /**
     * Standard Konstruktor, um das Selenium Framework  zu iniitieren.
     */
    public SeleniumService() {
        this.fetchedLines = new ArrayList<>();
        this.options = new ChromeOptions();
        this.options.addArguments("--allow-insecure-content");
        this.options.addArguments("--start-maximized");
        this.options.addArguments("--ignore-certificate-errors");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        this.chrome1 = new ChromeDriver(options)
        ;

        this.wait = new WebDriverWait(chrome1, 30);
    }

    /**
     * Methode, um die Seite mit den Kurszielen anzusteuern mithilfe des Selenium Frameworks.
     *
     * @param symbol Aktiensymbol als String
     * @return Gibt einen Link zurück mit den gesuchten Kurszielen
     * @throws InterruptedException Falls es Unterbrechungen beim Crawlen gibt
     */
    private String getAnalytisSite(String symbol) throws InterruptedException {

        chrome1.navigate().to("https://www.finanztreff.de/suche/");

        chrome1.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        final WebElement shadowHost = chrome1.findElement(By.cssSelector("#usercentrics-root"));
        final JavascriptExecutor javascriptExecutor = chrome1;
        final WebElement shadowRoot = (WebElement) javascriptExecutor.executeScript("return arguments[0].shadowRoot", shadowHost);

        WebElement footer = shadowRoot.findElement(By.cssSelector("footer"));
        WebElement button = footer.findElement(By.xpath("div/div/button[2]"));


        WebElement inputfield = chrome1.findElement(By.xpath("//*[@id=\"USFsearchDropDownHead\"]"));
        WebElement searchButton = chrome1.findElement(By.xpath("//*[@id=\"finanztreff\"]/div[6]/div/header/div[1]/form/input[3]"));

        button.click();
        chrome1.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        try {
            chrome1.switchTo().frame(
                    chrome1.findElement(By.cssSelector("#ur-render-target")));
            new WebDriverWait(chrome1, 20).until(
                    ExpectedConditions.elementToBeClickable(By
                            .xpath("//*[@id=\"btn-reject\"]"))).click();
        } catch (Exception e) {
        }

        try {
            chrome1.switchTo().parentFrame();
            inputfield.sendKeys(symbol);
            searchButton.click();
        } catch (Exception e) {
        }

        String xpathBranche;
        String xpathName;

        WebElement resultTable = chrome1.findElement(By.tagName("table"));
        List<WebElement> bodyelements = resultTable.findElements(By.xpath("//tbody/tr"));
        WebElement analyticLink = null;

        for (int i = 1; i < bodyelements.size() + 1; i++) {
            xpathBranche = "//tbody/tr[" + i + "]/td[4]";
            xpathName = "//tbody/tr[" + i + "]/td[3]/a";
            WebElement checkBranche = resultTable.findElement(By.xpath(xpathBranche));
            String brancheText = checkBranche.getText();
            String camelCasePattern = "[A-Z][A-Z0-9\\s]+"; // 3rd edit, getting better
            if (!brancheText.isEmpty()) {
                analyticLink = resultTable.findElement(By.xpath(xpathName));
                break;
            } else {
                analyticLink = resultTable.findElement(By.tagName("a"));
            }
        }


        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("a")));

        analyticLink.click();

        WebElement StockTable = chrome1.findElement(By.tagName("table"));
        WebElement analyticDetails = StockTable.findElement(By.xpath("//*[text()='Analysen']"));

        analyticDetails.click();
        return chrome1.getCurrentUrl();
    }

    /**
     * Methode, um mit der ermittelten Internetseite die Kursziele zu extrahieren.
     *
     * @param symbol Aktiensymbol als String
     * @return Liste von Aktienzielinformationen als ArrayList
     * @throws InterruptedException Falls Unterbrechungen passieren, während des Crawlen.
     */
    public ArrayList<HashMap<String, String>> fetchAnalytics(String symbol) throws InterruptedException {
        ArrayList<HashMap<String, String>> analytics = new ArrayList<>();

        try {
            chrome1.navigate().to(this.getAnalytisSite(symbol));
        } catch (Exception e) {
            chrome1.close();
        }
        String languagesParagraphXpath = "//*[@id=\"finanztreff\"]/div[6]/div/div[11]/div/table";
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(languagesParagraphXpath)));

        WebElement table = chrome1.findElement(By.xpath(languagesParagraphXpath));

        List<WebElement> row = table.findElements(By.tagName("tr"));
        row.remove(0);


        for (WebElement el : row) {
            WebElement analystWeb = el.findElement(By.tagName("a"));
            WebElement analystDate = el.findElement(By.xpath("td[1]"));
            WebElement analystRating = el.findElement(By.xpath("td[3]"));
            WebElement analystTarget = el.findElement(By.xpath("td[4]"));
            WebElement analysttargetdate = el.findElement(By.xpath("td[5]"));
            WebElement analystdelta = el.findElement(By.xpath("td[6]"));

            String name = analystWeb.getAttribute("innerText");
            String date = analystDate.getAttribute("innerText");
            String rating = analystRating.getAttribute("innerText");
            String target = analystTarget.getAttribute("innerText");
            String targetTimeFrame = analysttargetdate.getAttribute("innerText");
            String delta = analystdelta.getAttribute("innerText");

            HashMap<String, String> analytic = new HashMap<>();

            analytic.put("name", name);
            analytic.put("date", date);
            analytic.put("rating", rating);
            analytic.put("target", target);
            analytic.put("targetTimeFrame", targetTimeFrame);
            analytic.put("delta", delta);

            analytics.add(analytic);

        }
        chrome1.close();
        return analytics;
    }
}
