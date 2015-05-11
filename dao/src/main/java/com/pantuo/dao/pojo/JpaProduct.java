package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 产品套餐
 */
@Entity
@Table(name="product")
public class JpaProduct extends CityEntity {
    public static enum Type {
        video("视频"), image("图片"), info("文本"), other("其他");

        private final String name;
        private Type(String name) {
            this.name = name;
        }

        public String getTypeName() {
            return name;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Type type;
    private String name;	//套餐名称
	private long duration;	//套餐时长(S)
    private int playNumber;	//单日播放次数
    private int firstNumber;	//首播次数
    private int lastNumber;		//末播次数
    private double hotRatio;	//高峰占比
    private int days;			//播放天数
    private double price;		//套餐价格
    private boolean padding;	//是否用作垫片
    private boolean enabled = true;

    public JpaProduct() {
        //for serialization
    }

    public JpaProduct(int city, Type type, String name, long duration,
                      int playNumber, int firstNumber, int lastNumber, double hotRatio,
                      int days, double price, boolean padding) {
		super(city);
		this.type = type;
		this.name = name;
		this.duration = duration;
		this.playNumber = playNumber;
		this.firstNumber = firstNumber;
		this.lastNumber = lastNumber;
		this.hotRatio = hotRatio;
		this.days = days;
		this.price = price;
		this.padding = padding;
	}
	public int getId() {
		return id;
	}
    public void setId(int id) { this.id = id; }
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getPlayNumber() {
		return playNumber;
	}
	public void setPlayNumber(int playNumber) {
		this.playNumber = playNumber;
	}
	public int getFirstNumber() {
		return firstNumber;
	}
	public void setFirstNumber(int firstNumber) {
		this.firstNumber = firstNumber;
	}
	public int getLastNumber() {
		return lastNumber;
	}
	public void setLastNumber(int lastNumber) {
		this.lastNumber = lastNumber;
	}
	public double getHotRatio() {
		return hotRatio;
	}
	public void setHotRatio(double hotRatio) {
		this.hotRatio = hotRatio;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isPadding() {
		return padding;
	}
	public void setPadding(boolean padding) {
		this.padding = padding;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "JpaProduct{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", playNumber=" + playNumber +
                ", firstNumber=" + firstNumber +
                ", lastNumber=" + lastNumber +
                ", hotRatio=" + hotRatio +
                ", days=" + days +
                ", price=" + price +
                ", padding=" + padding +
                ", enabled=" + enabled +
                '}';
    }
}