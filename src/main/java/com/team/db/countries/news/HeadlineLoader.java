package com.team.news;

import com.team.db.Database;
import java.net.http.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class HeadlineLoader {
    // IMPORTANT: You need a NewsAPI key → put it in an environment variable NEWSAPI_KEY
    private static final String API_KEY = System.getenv("NEWSAPI_KEY");

    public static void main(String[] args) throws Exception {
        // Example: get headlines for US (you can extend this to more countries)
        List<Headline> headlines = fetchHeadlines("us");

        try (Connection c = Database.get()) {
            // Clear old rows
            try (PreparedStatement del = c.prepareStatement("DELETE FROM country_headlines")) {
                del.executeUpdate();
            }
            // Insert new rows
            try (PreparedStatement ins = c.prepareStatement(
                "INSERT INTO country_headlines(country, title, source, published_at) VALUES (?, ?, ?, ?)"
            )) {
                for (Headline h : headlines) {
                    ins.setString(1, h.country);
                    ins.setString(2, h.title);
                    ins.setString(3, h.source);
                    ins.setString(4, h.publishedAt);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
        }

        // Write summary file
        java.nio.file.Files.createDirectories(java.nio.file.Path.of("data/summaries"));
        String summary = "load_time=" + java.time.OffsetDateTime.now()
                + "\nrecords=" + headlines.size()
                + "\nsource=NewsAPI\n";
        java.nio.file.Files.writeString(java.nio.file.Path.of("data/summaries/headlines_summary.txt"), summary);

        System.out.println("Loaded headlines: " + headlines.size());
    }

    static List<Headline> fetchHeadlines(String countryCode) throws Exception {
        if (API_KEY == null) {
            throw new RuntimeException("Missing NEWSAPI_KEY environment variable.");
        }
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://newsapi.org/v2/top-headlines?country=" + countryCode + "&apiKey=" + API_KEY;
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        JsonObject obj = JsonParser.parseString(res.body()).getAsJsonObject();
        JsonArray arr = obj.getAsJsonArray("articles");
        List<Headline> out = new ArrayList<>();

        for (var el : arr) {
            var article = el.getAsJsonObject();
            String title = article.get("title").getAsString();
            String source = article.getAsJsonObject("source").get("name").getAsString();
            String publishedAt = article.has("publishedAt") ? article.get("publishedAt").getAsString() : null;
            out.add(new Headline(countryCode, title, source, publishedAt));
        }
        return out;
    }
}