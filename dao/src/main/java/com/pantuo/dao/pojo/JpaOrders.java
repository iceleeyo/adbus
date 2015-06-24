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
        online, contract,check,remit,cash
    }
    public static enum Status {
        unpaid ("未支付"),
        paid ("已支付"),
        scheduled ("已排期"),
        started ("已上播"),
        completed ("已完成"),
        cancelled ("已取消");

        private final String nameStr;
        private Status(String nameStr) {
            this.nameStr = nameStr;
        }

        public String getNameStr () {
            return nameStr;
        }
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
    @ManyToOne
    @JoinColumn(name = "invoiceId")
    private JpaInvoiceDetail invoiceDetail;
    private int contractId;
    private String contractCode;
    private Date startTime;
    private Date endTime;
    private JpaProduct.Type type;
    private PayType payType;
    private Status stats;
    private String ordRemark;
    private String creator;
    private int isInvoice;
    private Date scheduleDay;
    private Date shangboDay;
    private Date jianboDay;
    private Date financialCheckDay;
    private Date cancelDay;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy="order", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<JpaOrderBuses> orderBuses;

    public JpaOrders() {
        //for serialization
    }

    public JpaOrders(int id, String userId, JpaSupplies supplies, JpaProduct product, JpaInvoiceDetail invoiceDetail,
			int contractId, String contractCode, Date startTime, Date endTime, Type type, PayType payType,
			Status stats, String ordRemark, String creator, int isInvoice, Date scheduleDay, Date shangboDay,
			Date jianboDay, Date financialCheckDay, Date cancelDay, Set<JpaOrderBuses> orderBuses) {
		super();
		this.id = id;
		this.userId = userId;
		this.supplies = supplies;
		this.product = product;
		this.invoiceDetail = invoiceDetail;
		this.contractId = contractId;
		this.contractCode = contractCode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.payType = payType;
		this.stats = stats;
		this.ordRemark = ordRemark;
		this.creator = creator;
		this.isInvoice = isInvoice;
		this.scheduleDay = scheduleDay;
		this.shangboDay = shangboDay;
		this.jianboDay = jianboDay;
		this.financialCheckDay = financialCheckDay;
		this.cancelDay = cancelDay;
		this.orderBuses = orderBuses;
	}

	public JpaOrders(int city, int orderId) {
        super(city);
        this.id = orderId;
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
    public int getInvoiceDetailId() {
    	return invoiceDetail == null ? 0 : invoiceDetail.getId();
    }
    
    public void setinvoiceDetailId(int invoiceDetailid) {
    	if (invoiceDetail == null) {
    		invoiceDetail = new JpaInvoiceDetail();
    	}
    	this.invoiceDetail.setId(invoiceDetailid);
    }

    public JpaProduct getProduct() {
        return product;
    }

    public void setProduct(JpaProduct product) {
        this.product = product;
    }

    public JpaInvoiceDetail getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(JpaInvoiceDetail invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
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
    public List<JpaOrderBuses> getOrderBusesListByPriority() {
        List<JpaOrderBuses> buses = null;
        if (orderBuses != null)
            buses = new ArrayList<JpaOrderBuses>(orderBuses);
        else
            buses = new ArrayList<JpaOrderBuses>();

        Collections.sort(buses, new Comparator<JpaOrderBuses>() {
            @Override
            public int compare(JpaOrderBuses o1, JpaOrderBuses o2) {
                return o2.getPriority() - o1.getPriority();
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
    

    public Date getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(Date scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public Date getShangboDay() {
        return shangboDay;
    }

    public void setShangboDay(Date shangboDay) {
        this.shangboDay = shangboDay;
    }

    public Date getJianboDay() {
        return jianboDay;
    }

    public void setJianboDay(Date jianboDay) {
        this.jianboDay = jianboDay;
    }

    public Date getFinancialCheckDay() {
        return financialCheckDay;
    }

    public void setFinancialCheckDay(Date financialCheckDay) {
        this.financialCheckDay = financialCheckDay;
    }

    public Date getCancelDay() {
        return cancelDay;
    }

    public void setCancelDay(Date cancelDay) {
        this.cancelDay = cancelDay;
    }



	public String getOrdRemark() {
		return ordRemark;
	}

	public void setOrdRemark(String ordRemark) {
		this.ordRemark = ordRemark;
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
                ", ordRemark="+ordRemark+'\''+
                '}';
    }
}