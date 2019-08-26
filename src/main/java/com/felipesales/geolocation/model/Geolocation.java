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

    @NotNull(message = "{maxAndMinTemperatureCanNotBeNull}")
    @Column(name = "max_and_min_temperature", nullable = false)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}