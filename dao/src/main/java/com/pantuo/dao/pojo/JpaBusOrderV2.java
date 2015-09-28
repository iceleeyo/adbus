package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "busOrder_V2")
public class JpaBusOrderV2 extends CityEntity {

	public JpaBusOrderV2() {
	}

	public JpaBusOrderV2(int cityId) {
		super(cityId);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private long seriaNum;//表单序列号

	@ManyToOne
	@JoinColumn(name = "productId")
	private JpaProductV2 JpaProductV2;

	private double productPrice; //套餐价格或是下单后的价格
	private double orderPrice;//订单实际管理员促销后的价格
	private boolean ispay=false;

	public static enum BusOrderStatus {
		begin, auth, reduct, over,
		/*订单开始,订单收到,订单处理,订单完成*/
	}

	public BusOrderStatus orderStatus;

	private String creater;//下单人

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaProductV2 getJpaProductV2() {
		return JpaProductV2;
	}

	public void setJpaProductV2(JpaProductV2 jpaProductV2) {
		JpaProductV2 = jpaProductV2;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}

	public boolean isIspay() {
		return ispay;
	}

	public void setIspay(boolean ispay) {
		this.ispay = ispay;
	}

	public BusOrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(BusOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

}
