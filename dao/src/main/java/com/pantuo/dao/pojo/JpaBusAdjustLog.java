package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bus_adjustlog")
public class JpaBusAdjustLog extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@ManyToOne
	@JoinColumn(name = "busid")
	private JpaBus jpabus;
	@ManyToOne
	@JoinColumn(name = "nowlineid")
	private JpaBusline nowline;
	@ManyToOne
	@JoinColumn(name = "oldlineid")
	private JpaBusline oldline;
	private String updator;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public JpaBus getJpabus() {
		return jpabus;
	}
	public void setJpabus(JpaBus jpabus) {
		this.jpabus = jpabus;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public JpaBusline getNowline() {
		return nowline;
	}
	public void setNowline(JpaBusline nowline) {
		this.nowline = nowline;
	}
	public JpaBusline getOldline() {
		return oldline;
	}
	public void setOldline(JpaBusline oldline) {
		this.oldline = oldline;
	}
	
}
