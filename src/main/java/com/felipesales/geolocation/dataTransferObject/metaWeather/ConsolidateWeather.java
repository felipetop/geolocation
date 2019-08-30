package com.felipesales.geolocation.dataTransferObject.metaWeather;

public class ConsolidateWeather {

    Float min_temp;
    Float max_temp;
    String applicable_date;

    public Float getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(Float min_temp) {
        this.min_temp = min_temp;
    }

    public Float getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(Float max_temp) {
        this.max_temp = max_temp;
    }

    public String getApplicable_date() {
        return applicable_date;
    }

    public void setApplicable_date(String applicable_date) {
        this.applicable_date = applicable_date;
    }
}