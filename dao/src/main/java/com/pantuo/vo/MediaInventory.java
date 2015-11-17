package com.pantuo.vo;

public class MediaInventory {
    private String bname;
    private int bsize;
    private int normalremain;
    private int fremain;
    
	public MediaInventory() {
	}
	public MediaInventory(String bname, int bsize, int normalremain, int fremain) {
		super();
		this.bname = bname;
		this.bsize = bsize;
		this.normalremain = normalremain;
		this.fremain = fremain;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public int getBsize() {
		return bsize;
	}
	public void setBsize(int bsize) {
		this.bsize = bsize;
	}
	public int getNormalremain() {
		return normalremain;
	}
	public void setNormalremain(int normalremain) {
		this.normalremain = normalremain;
	}
	public int getFremain() {
		return fremain;
	}
	public void setFremain(int fremain) {
		this.fremain = fremain;
	}
    
} 
