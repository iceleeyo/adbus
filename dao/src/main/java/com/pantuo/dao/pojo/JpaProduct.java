package com.pantuo.dao.pojo;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;


import java.util.*;

/**
 * @author tliu
 *
 * 产品套餐
 */
@Entity
@Table(name="product")
public class JpaProduct extends CityEntity {
    public static enum Type {
        video("硬广广告"), image("INFO图片"), info("INFO字幕"),team("团类广告"), body("车身"), other("其他"),inchof32("32寸屏广告");//,mixture("混合类型")

        private final String name;
        private Type(String name) {
            this.name = name;
        }

        public String getTypeName() {
            return name;
        }
    }
    public static enum FrontShow{
    	N,Y;
    }

    public static Map<JpaCity.MediaType, List<Type>> productTypesForMedia = new HashMap<JpaCity.MediaType, List<Type>>();
    static {
        productTypesForMedia.put(JpaCity.MediaType.screen, Arrays.asList(Type.video, Type.image, Type.info,Type.team,Type.inchof32));
        productTypesForMedia.put(JpaCity.MediaType.body, Arrays.asList(Type.body));
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
    private int busNumber;      //车身广告公交车数量
    private int days;			//播放天数
    private double price;		//套餐价格，车身广告的媒体费
    private double produceCost; //车身广告的制作费用
    private boolean padding;	//是否用作垫片
    private boolean enabled = true;
    private boolean exclusive = false;  //是否专用套餐，专用套餐定向对某一个用户有效
    private String exclusiveUser;   //定向用户
    private String creator;       //创建人
    private String updator;       //最后修改人
    private String remarks;
    private String tags;
    @Column(length=128) 
    private String imgurl;
    private int iscompare;
    private FrontShow frontShow;
    @Column(length=1000) 
	private String jsonString;
    //add by impanxh 首页根据 发现json responseBody到前端时json死循环
   // @OneToOne(cascade = { CascadeType.ALL },mappedBy="product", fetch = FetchType.EAGER, orphanRemoval = true)
   // private JpaCpd jpaCpd;

    public JpaProduct() {
        //for serialization
    }

    public JpaProduct(int id, Type type, String name, long duration, int playNumber, int firstNumber, int lastNumber,
			double hotRatio,  int busNumber, int days, double price, double produceCost,
			boolean padding, boolean enabled, boolean exclusive, String exclusiveUser, String remarks) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.duration = duration;
		this.playNumber = playNumber;
		this.firstNumber = firstNumber;
		this.lastNumber = lastNumber;
		this.hotRatio = hotRatio;
		this.busNumber = busNumber;
		this.days = days;
		this.price = price;
		this.produceCost = produceCost;
		this.padding = padding;
		this.enabled = enabled;
		this.exclusive = exclusive;
		this.exclusiveUser = exclusiveUser;
		this.remarks = remarks;
	}

	//车身
    public static JpaProduct newForBody(int city, String name,
                      int busNumber,
                      int days, double price, double produceCost) {
        JpaProduct p = new JpaProduct();
        p.setCity(city);
        p.setName(name);
        p.setBusNumber(busNumber);
        p.setDays(days);
        p.setPrice(price);
        p.setProduceCost(produceCost);
        return p;
    }

	public int getIscompare() {
		return iscompare;
	}

	public void setIscompare(int iscompare) {
		this.iscompare = iscompare;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
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
	
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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


    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public double getProduceCost() {
        return produceCost;
    }

    public void setProduceCost(double produceCost) {
        this.produceCost = produceCost;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public String getExclusiveUser() {
        return exclusiveUser;
    }

    public void setExclusiveUser(String user) {
        if (StringUtils.isNoneBlank(user)) {
            this.exclusive = true;
            this.exclusiveUser = user;
        } else {
            this.exclusive = false;
            this.exclusiveUser = null;
        }
    }

	@Override
	public String toString() {
		return "JpaProduct [id=" + id + ", type=" + type + ", name=" + name + ", duration=" + duration
				+ ", playNumber=" + playNumber + ", firstNumber=" + firstNumber + ", lastNumber=" + lastNumber
				+ ", hotRatio=" + hotRatio +  ", busNumber=" + busNumber + ", days=" + days
				+ ", price=" + price + ", produceCost=" + produceCost + ", padding=" + padding + ", enabled=" + enabled
				+ ", exclusive=" + exclusive + ", exclusiveUser=" + exclusiveUser + ", remarks=" + remarks + "]";
	}

	public FrontShow getFrontShow() {
		return frontShow;
	}

	public void setFrontShow(FrontShow frontShow) {
		this.frontShow = frontShow;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}