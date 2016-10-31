package com.pantuo.dao.pojo;

import javax.persistence.*;

import com.pantuo.dao.pojo.JpaProduct.Type;

import java.util.Date;

/**
 * @author tliu
 *
 * 排期
 */
@Entity
@Table(name="infoimgschedule")
public class JpaInfoImgSchedule extends CityEntity {
	
	 public static enum Type {
	       image("INFO图片"), info("INFO字幕");
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
    private Date date;      //播出日期
    @ManyToOne
    @JoinColumn(name = "orderId")
    private JpaOrders order;
    @ManyToOne
    @JoinColumn(name = "attamentId")
    private JpaAttachment attachment;
    private Type type;
    private String proper;
    private long duration;
    
    private boolean isDeleted;//0 false

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getProper() {
		return proper;
	}

	public void setProper(String proper) {
		this.proper = proper;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public JpaAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(JpaAttachment attachment) {
		this.attachment = attachment;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}