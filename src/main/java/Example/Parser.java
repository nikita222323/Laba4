package Example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    Document doc;
    String id;
    String url;
    HashMap<String, String> countries = new HashMap<>();
    HashMap<String , String> reactorsInCountries = new HashMap<>();
    List<String> rowData = new ArrayList<>();
    List<List<String>> dataList = new ArrayList<>();
    HashMap<String, HashMap<Integer, Double>> mapForLoadMap = new HashMap<>();
    HashMap<Integer, Double> loadMap = new HashMap<>();
    Map<String, String> cookies = new HashMap<>();
    private String status;
    private String firstGridConnection;
    private String suspendedData;
    private String permanentData;


    public Parser() {
        readCountriesFromPage();
        readReactorsFromCountries();
        parseReactors();
    }

    public void readCountriesFromPage() {
        url = "https://pris.iaea.org/PRIS/CountryStatistics/CountryStatisticsLandingPage.aspx";
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String country = "";
        int counter = 0;
        while(true) {
            id = "MainContent_rptSideNavigation_hypNavigation_" + String.valueOf(counter);
            if (doc.getElementById(id) != null) {
                Element element = doc.getElementById(id);
                country = element.text();
                String[] href = element.attr("href").split("");
                countries.put(country, href[href.length - 2] + href[href.length - 1]);
                countries.put("Taiwan", "TW");
                System.out.println(counter + " " +country + " " + href[href.length-2]+href[href.length-1]);
            }
            else {break;}
            counter += 1;
        }
    }

    public void readReactorsFromCountries()  {
        int valueOfReactors = 0;

        for(HashMap.Entry<String, String> country: countries.entrySet()) {
            String url = "https://pris.iaea.org/PRIS/CountryStatistics/CountryDetails.aspx?current=" + country.getValue();
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int counter = 0;

            while(true) {
                if (doc.getElementById("MainContent_MainContent_rptCountryReactors_hypReactorName_" + String.valueOf(counter)) != null) {

                    Element element = doc.getElementById("MainContent_MainContent_rptCountryReactors_hypReactorName_" + String.valueOf(counter));
                    Element elem = element.parent();
                    System.out.println(country.getKey() + " " + elem.text());
                    counter += 1;
                    valueOfReactors += 1;
                    reactorsInCountries.put(elem.text(), country.getKey());

                } else {break;}
            }
        }
        System.out.println("ВСЕГО РЕАКТОРОВ" + ": " + valueOfReactors);
    }

    public void parseReactors() {
        int valueOfReactors = 0;


        for (int i = 1; i < 1100; i++) {
            try {
                url = "https://pris.iaea.org/PRIS/CountryStatistics/ReactorDetails.aspx?current=" + i;
                System.out.println("Processing URL: " + url);
                Connection.Response response = getResponse(url);

                if (response != null && response.statusCode() != 403) {
                    cookies = response.cookies();
                    doc = response.parse();

                    status = getStatus();
                    firstGridConnection = getFirstGridConnection();
                    suspendedData = getSuspendedData();
                    permanentData = getPermanentData();

                    if (isValidReactor()) {
                        List<String> rowData = extractReactorData();
                        if (!rowData.isEmpty()) {
                            dataList.add(rowData);
                            System.out.println("Data List Size: " + dataList.size() + " Reactor Name: " + rowData.get(0));
                            System.out.println(loadMap);
                        }
                        valueOfReactors++;
                    }
                } else {
                    System.out.println("Доступ запрщен URL: " + url);
                }
            } catch (IOException e) {
                System.out.println("Ошибка подключения to URL: " + url);
            }
        }
        System.out.println("Конечный размер дата листа: " + dataList.size());
        System.out.println(mapForLoadMap);
    }

    private Connection.Response getResponse(String url) throws IOException {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .cookies(cookies)
                    .execute();
        } catch (IOException e) {
            System.out.println("Ошибка подключения к URL: " + url);
            return null;
        }
    }

    private boolean isValidReactor() {
        return status != null && (status.equals("Operational") || status.equals("Permanent Shutdown") || status.equals("Suspended Operation"));
    }

    private String getStatus() {
        String status = getText("MainContent_MainContent_lblReactorStatus");
        System.out.println("Status: " + status);
        return status;
    }

    private String getFirstGridConnection() {
        String firstGridConnection = getText("MainContent_MainContent_lblGridConnectionDate", 1);
        System.out.println("First Grid Connection: " + firstGridConnection);
        return firstGridConnection;
    }

    private String getSuspendedData() {
        if ("Suspended Operation".equals(status)) {
            String suspendedData = getText("MainContent_MainContent_lblLongTermShutdownDate", 1);
            System.out.println("Suspended Data: " + suspendedData);
            return suspendedData;
        }
        return null;
    }

    private String getPermanentData() {
        if ("Permanent Shutdown".equals(status)) {
            String permanentData = getText("MainContent_MainContent_lblPermanentShutdownDate", 1);
            System.out.println("Permanent Data: " + permanentData);
            return permanentData;
        }
        return null;
    }

    private List<String> extractReactorData() {
        List<String> rowData = new ArrayList<>();

        String name = getText("MainContent_MainContent_lblReactorName");
        String country = name != null ? reactorsInCountries.get(name) : null;
        String type = getText("MainContent_MainContent_lblType");
        String owner = getText("MainContent_MainContent_hypOwnerUrl");
        String operator = getText("MainContent_MainContent_hypOperatorUrl");
        String thermalCapacity = getText("MainContent_MainContent_lblThermalCapacity");
        String loadFactor = getLoadFactor("MainContent_MainContent_lblLoadFactor");

        String h5owner = getH5Text(1, 2);
        String h5operator = getH5Text(1, 3);

        if (owner == null) {
            owner = h5owner;
        }
        if (operator == null) {
            operator = h5operator;
        }

        boolean flag = shouldIncludeReactor();

        if (flag) {
            initializeLoadMap();
            processLoadData(name);
            mapForLoadMap.put(name, new HashMap<>(loadMap)); // Fix here
        }

        rowData.add(name != null ? name : "N/A");
        rowData.add(country != null ? country : "N/A");
        rowData.add(status != null ? status : "N/A");
        rowData.add(type != null ? type : "N/A");
        rowData.add(owner != null ? owner : "N/A");
        rowData.add(operator != null ? operator : "N/A");
        rowData.add(thermalCapacity != null ? thermalCapacity : "N/A");
        rowData.add(firstGridConnection != null ? firstGridConnection : "N/A");
        rowData.add(loadFactor != null ? loadFactor : "N/A");
        rowData.add(suspendedData != null ? suspendedData : "N/A");
        rowData.add(permanentData != null ? permanentData : "N/A");

        return flag ? rowData : new ArrayList<>();
    }

    private String getText(String elementId) {
        Element element = doc.getElementById(elementId);
        return element != null ? element.text() : null;
    }

    private String getText(String elementId, int splitIndex) {
        Element element = doc.getElementById(elementId);
        if (element != null && element.text().contains(",")) {
            return element.text().split(",")[splitIndex].trim();
        }
        return null;
    }

    private String getH5Text(int rowIndex, int cellIndex) {
        Elements rows = doc.select("tr");
        if (rows.size() > rowIndex) {
            Elements cells = rows.get(rowIndex).select("td");
            if (cells.size() > cellIndex) {
                Element h5Element = cells.get(cellIndex).selectFirst("h5");
                return h5Element != null ? h5Element.text() : "";
            }
        }
        return "";
    }

    private boolean shouldIncludeReactor() {
        if ("Suspended Operation".equals(status)) {
            int susdata = Integer.parseInt(suspendedData);
            return susdata >= 2014;
        } else if ("Permanent Shutdown".equals(status)) {
            int permdata = Integer.parseInt(permanentData);
            return permdata >= 2014;
        }
        return true;
    }

    private void initializeLoadMap() {
        loadMap.clear();
        for (int j = 2014; j <= 2024; j++) {
            loadMap.put(j, 0.0);
        }
    }

    private void processLoadData(String name) {
        Elements tables = doc.select("table");
        if (tables.size() >= 3) {
            Element table = tables.get(2);
            Elements rowsload = table.select("tr");
            List<Integer> years = new ArrayList<>();
            for (int z = 2014; z <= 2024; z++) {
                years.add(z);
            }
            for (Element row : rowsload) {
                Elements tds = row.select("td");
                if (tds.size() >= 2) {
                    Element firstTd = tds.first();
                    Element penultimateTd = tds.get(tds.size() - 2);
                    Integer year = Integer.parseInt(firstTd.text());
                    if (year >= 2014) {
                        String loadtext = penultimateTd.text();
                        Double load = loadtext.length() > 0 ? Double.parseDouble(penultimateTd.text()) : 85.0;

                        years.remove(Integer.valueOf(year));
                        if ("Permanent Shutdown".equals(status)) {
                            if (Integer.parseInt(permanentData) >= year) {
                                loadMap.put(year, load);
                            }
                        } else if ("Suspended Operation".equals(status)) {
                            if (Integer.parseInt(suspendedData) >= year) {
                                loadMap.put(year, load);
                            }
                        } else {
                            if (year > Integer.parseInt(firstGridConnection)) {
                                loadMap.put(year, load);
                            }
                        }
                    }
                }
            }
            if ((!years.isEmpty()) && "Operational".equals(status)) {
                for (Integer yr : years) {
                    if (Integer.parseInt(firstGridConnection) < yr) {
                        loadMap.put(yr, 85.0);
                    }
                }
            }
        }
    }
    private String getLoadFactor(String elementId) {
        Element element = doc.getElementById(elementId);
        if (element != null) {
            String text = element.text().replace("%", "").trim();
            try {
                return String.valueOf(Double.parseDouble(text));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing load factor: " + text);
            }
        }
        return "N/A";
    }

    public List<List<String>> getDataList() {
        return dataList;
    }

    public HashMap<String, HashMap<Integer, Double>> getMapForLoadMap() {
        return mapForLoadMap;
    }
}