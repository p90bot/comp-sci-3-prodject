package com.team.db.countries;

public class Country {
    public final String cca2;       // country code
    public final String nameCommon; // common name
    public final String region;
    public final String subregion;
    public final long population;

    public Country(String cca2, String nameCommon, String region, String subregion, long population) {
        this.cca2 = cca2;
        this.nameCommon = nameCommon;
        this.region = region;
        this.subregion = subregion;
        this.population = population;
    }
}