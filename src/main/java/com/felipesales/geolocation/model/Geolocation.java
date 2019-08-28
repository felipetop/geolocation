package com.felipesales.geolocation.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "geolocation")
public class Geolocation {

    @Id
    @SequenceGenerator(name="Geolocation_Generator", sequenceName="geolocation_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Geolocation_Generator")
    @Column(name = "geolocation_id", nullable = false)
    private int id;

    @NotNull(message = "{maxTemperatureCanNotBeNull}")
    @Column(name = "max_temperature", nullable = false)
    private Float maxTemperature;

    @NotNull(message = "{minTemperatureCanNotBeNull}")
    @Column(name = "min_temperature", nullable = false)
    private Float minTemperature;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Float minTemperature) {
        this.minTemperature = minTemperature;
    }
}