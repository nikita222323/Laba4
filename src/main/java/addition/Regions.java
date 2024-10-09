package addition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Regions {
    private ArrayList<String> regions = new ArrayList<>();
    private Map<String, String> countries = new HashMap<>();

    public Regions() {
        this.regions = fillRegions();
        this.countries = fillCountries();
    }

    private ArrayList<String> fillRegions() {
        regions.add("Western Europe"); // западная
        regions.add("North Africa and the Middle East");
        regions.add("Central and Eastern Europe");
        regions.add("Sub-Saharan Africa");
        regions.add("The Russian-Eurasian region");
        regions.add("North America"); // северная
        regions.add("East Asia"); // восточная
        regions.add("Latin America");
        regions.add("South Asia"); // южная
        regions.add("Australia");
        regions.add("Southeast Asia");
        regions.add("Oceania");
        return regions;
    }

    private Map<String, String> fillCountries() {
        countries.put("Argentina", "Latin America");
        countries.put("Armenia", "North Africa and the Middle East");
        countries.put("Bangladesh", "South Asia");
        countries.put("Belarus", "Central and Eastern Europe");
        countries.put("Belgium", "Western Europe");
        countries.put("Brazil", "Latin America");
        countries.put("Bulgaria", "Western Europe");
        countries.put("Canada", "North America");
        countries.put("China", "Southeast Asia");
        countries.put("Czech Republic", "Central and Eastern Europe");
        countries.put("Egypt", "North Africa and the Middle East");
        countries.put("Finland", "Western Europe");
        countries.put("France", "Western Europe");
        countries.put("Germany", "Western Europe");
        countries.put("Hungary", "Western Europe");
        countries.put("India", "South Asia");
        countries.put("Iran, Islamic Republic", "North Africa and the Middle East");
        countries.put("Italy", "Western Europe");
        countries.put("Japan", "East Asia");
        countries.put("Kazakhstan", "The Russian-Eurasian region");
        countries.put("Korea", "Southeast Asia");
        countries.put("Lithuania", "Central and Eastern Europe");
        countries.put("Mexico", "Latin America");
        countries.put("Netherlands", "Western Europe");
        countries.put("Pakistan", "South Asia");
        countries.put("Romania", "Central and Eastern Europe");
        countries.put("Russia", "The Russian-Eurasian region");
        countries.put("Slovakia", "Central and Eastern Europe");
        countries.put("Slovenia", "Central and Eastern Europe");
        countries.put("South Africa", "Sub-Saharan Africa");
        countries.put("Spain", "Western Europe");
        countries.put("Sweden", "Western Europe");
        countries.put("Switzerland", "Western Europe");
        countries.put("Türkiye", "North Africa and the Middle East");
        countries.put("Ukraine", "Central and Eastern Europe");
        countries.put("United Arab Emirates", "North Africa and the Middle East");
        countries.put("United Kingdom", "Western Europe");
        countries.put("United States of America", "North America");
        countries.put("Taiwan", "Southeast Asia");
        return countries;
    }

    public ArrayList<String> getRegions() {
        return regions;
    }

    public Map<String, String> getCountries() {
        return countries;
    }
}