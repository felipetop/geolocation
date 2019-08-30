package com.felipesales.geolocation.dataTransferObject.metaWeather;

import java.util.ArrayList;

public class Weather {

    ArrayList < ConsolidateWeather > consolidated_weather = new ArrayList<ConsolidateWeather>();

    public ArrayList<ConsolidateWeather> getConsolidated_weather() {
        return consolidated_weather;
    }

    public void setConsolidated_weather(ArrayList<ConsolidateWeather> consolidated_weather) {
        this.consolidated_weather = consolidated_weather;
    }
}