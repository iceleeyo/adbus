package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
* Created by tliu on 4/10/15.
*/
//货物
@Entity
@Table(name="goods", uniqueConstraints=@UniqueConstraint(columnNames={"orderId", "day", "seed"}))
public class Goods implements Comparable<Goods>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private JpaOrders order;
    private int seed;       //用于生成确定但是随即的hashcode
    private long size;
    private boolean first;  //放在箱子首位
    private boolean last;   //放在箱子末尾

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="boxSlotId", referencedColumnName="slotId"),
        @JoinColumn(name="day", referencedColumnName="day")
    })
    private Box box = null;             //箱子
    private long inboxPosition = -1;    //箱子中的位置

    public Goods() {}

    public Goods(int orderId, long size, boolean first, boolean last, int seed) {
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

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public long getInboxPosition() {
        return inboxPosition;
    }

    public void setInboxPosition(long inboxPosition) {
        this.inboxPosition = inboxPosition;
    }

    public boolean isBoxed () {
        return box != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goods goods = (Goods) o;

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
    public int compareTo(Goods o) {
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

    public void put(Box box, long inboxPosition) {
        this.box = box;
        this.inboxPosition = inboxPosition;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "orderId=" + getOrderId() +
                ", inboxPosition=" + inboxPosition +
                ", size=" + size +
                (first ? ", first": "") +
                (last ? ", last": "") +
                '}';
    }
}
