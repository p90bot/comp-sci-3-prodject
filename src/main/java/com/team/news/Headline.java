package com.team.news;

public class Headline {
    public final String country;   // country code
    public final String title;     // news title
    public final String source;    // source name
    public final String publishedAt; // publish date

    public Headline(String country, String title, String source, String publishedAt) {
        this.country = country;
        this.title = title;
        this.source = source;
        this.publishedAt = publishedAt;
    }
}