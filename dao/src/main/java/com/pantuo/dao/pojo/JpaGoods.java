package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
* Created by tliu on 4/10/15.
*/
//货物
@Entity
@Table(name="goods")
public class JpaGoods extends CityEntity implements Comparable<JpaGoods>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private JpaOrders order;
    private int seed;       //用于生成确定但是随即的hashcode
    private long size;
    
    private int sort_index;
    private boolean first;  //放在箱子首位
    private boolean last;   //放在箱子末尾
    
    private boolean isDeleted;//0 false

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="boxSlotId", referencedColumnName="slotId"),
        @JoinColumn(name="day", referencedColumnName="day")
    })
    private JpaBox box = null;             //箱子
    private long inboxPosition = -1;    //箱子中的位置

    public JpaGoods() {}

    public JpaGoods(int city, int orderId, long size, boolean first, boolean last, int seed) {
        super(city);
        order = new JpaOrders();
        order.setId(orderId);
        this.size = size;
        this.first = first;
        this.last = last;
        this.seed = seed;
    }

    public int getOrderId() {
        return order.getId();
    }

    public int getId() {
        return id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public JpaBox getBox() {
        return box;
    }

    public void setBox(JpaBox box) {
        this.box = box;
    }

    public JpaOrders getOrder() {
        return order;
    }

    public void setOrder(JpaOrders order) {
        this.order = order;
    }

    public long getInboxPosition() {
        return inboxPosition;
    }

    public void setInboxPosition(long inboxPosition) {
        this.inboxPosition = inboxPosition;
        sort_index = (int)inboxPosition;
    }

    public boolean isBoxed () {
        return box != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaGoods goods = (JpaGoods) o;

        if (first != goods.first) return false;
        if (getOrderId() != goods.getOrderId()) return false;
        if (last != goods.last) return false;
        if (seed != goods.seed) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getOrderId();
        result = 31 * result + seed;
        result = 31 * result + (first ? 1 : 0);
        result = 31 * result + (last ? 1 : 0);
        return result;
    }

    @Override
    //货物先按首末播情况，再按size由大到小排列
    public int compareTo(JpaGoods o) {
        if (o == null)
            return -1;
        boolean firstOrLast = first || last;
        boolean oFirstOrLast = o.first || o.last;
        if (firstOrLast && !oFirstOrLast)
            return -1;
        if (oFirstOrLast && !firstOrLast)
            return 1;
        long s = o.size - size;
        return (int)s;
    }

    public void put(JpaBox box, long inboxPosition) {
        this.box = box;
        this.inboxPosition = inboxPosition;
    }

    public JpaGoods clone() {
        JpaGoods g = new JpaGoods(city, order.getId(), size, first, last, seed);
        return g;
    }

    @Override
    public String toString() {
        return "JpaGoods{" +
                "orderId=" + getOrderId() +
                ", inboxPosition=" + inboxPosition +
                ", size=" + size +
                (first ? ", first": "") +
                (last ? ", last": "") +
                '}';
    }

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
    
    
}
