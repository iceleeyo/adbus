package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 车辆型号信息
 */

@Entity
@Table(name="bus_model", uniqueConstraints=@UniqueConstraint(columnNames={"name"}),
        indexes = @Index(name="bus_model_index",columnList="city, id"))
public class JpaBusModel extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;                //车型
    @ManyToOne
    @JoinColumn(name = "modeldescId")
    private JpaModeldesc modeldesc;         //车型描述
    private String manufacturer;        //生产商
    private boolean doubleDecker = false;   //是否双层
    private String adSlot;                 //广告位尺寸
    private String description;                 //备注

	public JpaBusModel(){

	}

	public JpaBusModel(int city, String name, boolean doubleDecker,
                       String manufacturer,
                       String adSlot) {
		super(city);
        this.name = name;
        this.doubleDecker = doubleDecker;
        this.manufacturer = manufacturer;
        this.adSlot = adSlot;
	}

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


    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JpaModeldesc getModeldesc() {
		return modeldesc;
	}

	public void setModeldesc(JpaModeldesc modeldesc) {
		this.modeldesc = modeldesc;
	}

	public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isDoubleDecker() {
        return doubleDecker;
    }

    public void setDoubleDecker(boolean doubleDecker) {
        this.doubleDecker = doubleDecker;
    }

    public String getAdSlot() {
        return adSlot;
    }

    public void setAdSlot(String adSlot) {
        this.adSlot = adSlot;
    }

}
