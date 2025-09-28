package com.team.db.countries;

import com.team.db.Database;
import java.net.http.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class CountryLoader {
    public static void main(String[] args) throws Exception {
        // Fetch countries from API
        List<Country> countries = fetchCountries();

        try (Connection c = Database.get()) {
            // Clear old rows
            try (PreparedStatement del = c.prepareStatement("DELETE FROM countries")) {
                del.executeUpdate();
            }
            // Insert new rows
            try (PreparedStatement ins = c.prepareStatement(
                "INSERT INTO countries(cca2, name_common, region, subregion, population) VALUES (?, ?, ?, ?, ?)"
            )) {
                for (Country k : countries) {
                    ins.setString(1, k.cca2);
                    ins.setString(2, k.nameCommon);
                    ins.setString(3, k.region);
                    ins.setString(4, k.subregion);
                    ins.setLong(5, k.population);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
        }

        // Write summary file
        java.nio.file.Files.createDirectories(java.nio.file.Path.of("data/summaries"));
        String summary = "load_time=" + java.time.OffsetDateTime.now()
                + "\nrecords=" + countries.size()
                + "\nsource=REST Countries\n";
        java.nio.file.Files.writeString(java.nio.file.Path.of("data/summaries/countries_summary.txt"), summary);

        System.out.println("Loaded countries: " + countries.size());
    }

    static List<Country> fetchCountries() throws Exception {
        // REST Countries API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder(
            URI.create("https://restcountries.com/v3.1/all?fields=cca2,name,region,subregion,population")
        ).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        JsonArray arr = JsonParser.parseString(res.body()).getAsJsonArray();
        List<Country> out = new ArrayList<>();

        for (var el : arr) {
            var obj = el.getAsJsonObject();
            String cca2 = obj.get("cca2").getAsString();
            String nameCommon = obj.getAsJsonObject("name").get("common").getAsString();
            String region = obj.has("region") && !obj.get("region").isJsonNull() ? obj.get("region").getAsString() : null;
            String sub = obj.has("subregion") && !obj.get("subregion").isJsonNull() ? obj.get("subregion").getAsString() : null;
            long pop = obj.has("population") && !obj.get("population").isJsonNull() ? obj.get("population").getAsLong() : 0L;
            out.add(new Country(cca2, nameCommon, region, sub, pop));
        }
        return out;
    }
}