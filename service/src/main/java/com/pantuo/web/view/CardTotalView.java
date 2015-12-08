package com.pantuo.web.view;
/**
 * 
 * <b><code>CardTotalView</code></b>
 * <p>
 * 购物车合计 包含用户购物车的总价 总的商品个数 总的需求商品个数 
 * </p>
 * <b>Creation Time:</b> 2015年10月25日 下午3:26:02
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class CardTotalView {
	public long seriaNum;
	public double price;
	public int cardCount;
	public int needCount;
	public long getSeriaNum() {
		return seriaNum;
	}
	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCardCount() {
		return cardCount;
	}
	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}
	public int getNeedCount() {
		return needCount;
	}
	public void setNeedCount(int needCount) {
		this.needCount = needCount;
	}
	public CardTotalView(long seriaNum, double price, int cardCount, int needCount) {
		this.seriaNum = seriaNum;
		this.price = price;
		this.cardCount = cardCount;
		this.needCount = needCount;
	}
	
	
}
