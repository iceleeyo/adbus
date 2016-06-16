package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 基于城市的Entity
 */
@MappedSuperclass
public class CityEntity extends BaseEntity {
    public int city = -1;

    public CityEntity() {
    }

    public CityEntity(int cityId) {
        this.city = cityId;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int cityId) {
        this.city = cityId;
    }

    @Override
    public String toString() {
        return "CityEntity{" +
                "city='" + city + '\'' +
                '}';
    }
}