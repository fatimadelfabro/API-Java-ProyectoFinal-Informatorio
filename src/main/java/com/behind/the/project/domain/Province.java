package com.behind.the.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String province;
    @JsonIgnore
    @OneToMany(mappedBy = "province")
    private List<City> cities;
    @JsonIgnore
    @ManyToOne
    private Country country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<City> getCities() {
        if(cities == null)
            this.cities = new ArrayList<>();
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
