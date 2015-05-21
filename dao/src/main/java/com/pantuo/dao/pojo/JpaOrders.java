package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.*;

import com.pantuo.dao.pojo.JpaProduct.Type;

import java.util.Date;

/**
 * @author tliu
 *
 * 订单
 */
@Entity
@Table(name="orders", indexes = @Index(name="order_index",
        columnList="city, userId, stats, type, startTime, endTime"))
public class JpaOrders extends CityEntity {
    public static enum PayType {
        online, contract,other
    }
    public static enum Status {
        unpaid, paid, scheduled, started, completed, cancelled
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userId;
    @ManyToOne
    @JoinColumn(name = "suppliesId")
    private JpaSupplies supplies;
    @ManyToOne
    @JoinColumn(name = "productId")
    private JpaProduct product;
    private int contractId;
    private String contractCode;
    private Date startTime;
    private Date endTime;
    private JpaProduct.Type type;
    private PayType payType;
    private Status stats;
    private String creator;
    private int isInvoice;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy="order", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<JpaOrderBuses> orderBuses;

    public JpaOrders() {
        //for serialization
    }


    public JpaOrders(int id, String userId, JpaSupplies supplies,
			JpaProduct product, int contractId, String contractCode,
			Date startTime, Date endTime, Type type, PayType payType,
			Status stats, String creator, int isInvoice) {
		super();
		this.id = id;
        this.userId = userId;
		this.supplies = supplies;
		this.product = product;
        this.contractId = contractId;
        this.contractCode = contractCode;
        this.startTime = startTime;
        this.endTime = endTime;
		this.type = type;
        this.payType = payType;
        this.stats = stats;
        this.creator = creator;
		this.isInvoice = isInvoice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(int isInvoice) {
		this.isInvoice = isInvoice;
	}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSuppliesId() {
        return supplies == null ? 0 : supplies.getId();
    }

    public void setSuppliesId(int suppliesId) {
        if (supplies == null)
            supplies = new JpaSupplies();
        this.supplies.setId(suppliesId);
    }

    public JpaSupplies getSupplies() {
        return supplies;
    }

    public void setSupplies(JpaSupplies supplies) {
        this.supplies = supplies;
    }

    public int getProductId() {
        return product == null ? 0 : product.getId();
    }

    public void setProductId(int productId) {
        if (product == null) {
            product = new JpaProduct();
        }
        this.product.setId(productId);
    }

    public JpaProduct getProduct() {
        return product;
    }

    public void setProduct(JpaProduct product) {
        this.product = product;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public JpaProduct.Type getType() {
        return type;
    }

    public void setType(JpaProduct.Type type) {
        this.type = type;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public Status getStats() {
        return stats;
    }

    public void setStats(Status stats) {
        this.stats = stats;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Set<JpaOrderBuses> getOrderBuses() {
        return orderBuses;
    }

    public void setOrderBuses(Set<JpaOrderBuses> orderBuses) {
        this.orderBuses = orderBuses;
    }

    public List<JpaOrderBuses> getOrderBusesList() {
        List<JpaOrderBuses> buses = null;
        if (orderBuses != null)
            buses = new ArrayList<JpaOrderBuses>(orderBuses);
        else
            buses = new ArrayList<JpaOrderBuses>();

        Collections.sort(buses, new Comparator<JpaOrderBuses>() {
            @Override
            public int compare(JpaOrderBuses o1, JpaOrderBuses o2) {
                return o1.getId() - o2.getId();
            }
        });
        return buses;
    }

    public int getSelectableBusesNumber() {
        if (product == null)
            return 0;
        int ordered = 0;
        if (orderBuses != null) {
            for (JpaOrderBuses o : orderBuses) {
                ordered += o.getBusNumber();
            }
        }
        return product.getBusNumber() - ordered;
    }

    @Override
    public String toString() {
        return "JpaOrders{" +
                "userId='" + userId + '\'' +
                ", suppliesId=" + supplies.getId() +
                ", productId=" + product.getId() +
                ", contractId=" + contractId +
                ", contractCode='" + contractCode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type=" + type +
                ", payType=" + payType +
                ", stats=" + stats +
                ", creator='" + creator + '\'' +
                '}';
    }
}