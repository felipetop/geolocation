package com.felipesales.geolocation.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @SequenceGenerator(name="Client_Generator", sequenceName="client_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Client_Generator")
    @Column(name = "client_id", nullable = false)
    private Integer id;

    @NotNull(message = "{nameCanNotBeNull}")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "{ageCanNotBeNull}")
    @Column(name = "age", nullable = false)
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geolocation_id", referencedColumnName = "geolocation_id")
    private Geolocation geolocation;

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
