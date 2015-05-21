package com.pantuo.dao.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 车身订单的车辆信息
 */

@Entity
@Table(name="order_buses")
public class JpaOrderBuses extends CityEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "orderId")
    @JsonIgnore
    private JpaOrders order;

    private JpaBusline.Level level;         //线路级别，对应套餐的线路级别

    @ManyToOne
    @JoinColumn(name = "lineId")
    private JpaBusline line;                //线路
    private JpaBus.Category category;       //车辆分类

    @ManyToOne
    @JoinColumn(name = "modelId")
    private JpaBusModel model;              //车型

    @ManyToOne
    @JoinColumn(name = "companyId")
    private JpaBusinessCompany company;     //运营公司

    private int busNumber;                  //车辆数量

	public JpaOrderBuses(){
        this.order = new JpaOrders();
	}

	public JpaOrderBuses(int orderId, int city, JpaBusline.Level level,
                         JpaBusline line, JpaBus.Category category,
                         JpaBusModel model, JpaBusinessCompany company) {
		super(city);
        this.order = new JpaOrders();
        this.order.setId(orderId);
        this.level = level;
        this.line = line;
        this.category = category;
        this.model = model;
        this.company = company;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JpaOrders getOrder() {
        return order;
    }

    public void setOrder(JpaOrders order) {
        this.order = order;
    }

    public JpaBusline.Level getLevel() {
        return level;
    }

    public void setLevel(JpaBusline.Level level) {
        this.level = level;
    }

    public JpaBusline getLine() {
        return line;
    }

    public void setLine(JpaBusline line) {
        this.line = line;
    }

    public JpaBus.Category getCategory() {
        return category;
    }

    public String getCategoryStr() {
        return category == null ? "" : category.getNameStr();
    }

    public void setCategory(JpaBus.Category category) {
        this.category = category;
    }

    public JpaBusModel getModel() {
        return model;
    }

    public void setModel(JpaBusModel model) {
        this.model = model;
    }

    public JpaBusinessCompany getCompany() {
        return company;
    }

    public void setCompany(JpaBusinessCompany company) {
        this.company = company;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    @Override
    public String toString() {
        return "JpaOrderBuses{" +
                "busNumber=" + busNumber +
                ", company=" + company +
                ", model=" + model +
                ", category=" + category +
                ", line=" + line +
                ", level=" + level +
                ", order=" + order +
                '}';
    }
}
