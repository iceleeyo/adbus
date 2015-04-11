package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Created by tliu on 4/10/15.
*/
//箱子
@Entity
@Table(name="box", uniqueConstraints=@UniqueConstraint(columnNames={"day", "slotId"}))
public class JpaBox implements Comparable<JpaBox>, Serializable {
    private static ThreadLocal<Long> PUT_WEIGHT = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return 0l;
        }
    };
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Date day;
    private long size;
    private long remain;
    private long remainStart;
    @ManyToOne
    @JoinColumn(name = "slotId")
    private JpaTimeslot timeslot;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy="box", fetch = FetchType.EAGER)
    private List<JpaGoods> goods;

    @Transient
    private long putWeight;    //刚刚堆放货物，权重降低
    @Transient
    private int seed;           //用作随机的种子

    public JpaBox() {}

    public JpaBox(Date day, int slotId, long size) {
        goods = new ArrayList<JpaGoods>();
        this.day = day;
        timeslot = new JpaTimeslot();
        timeslot.setId(slotId);
        this.size = size;
        this.remain = size;
        this.remainStart = 0;
    }

    public Date getDay() {
        return day;
    }

    public int getSlotId() {
        return timeslot.getId();
    }

    public JpaTimeslot getTimeslot() {
        return timeslot;
    }

    public long getSize() {
        return size;
    }

    public long getRemain() {
        return remain;
    }

    public long getRemainStart() {
        return remainStart;
    }

    public List<JpaGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<JpaGoods> goods) {
        this.goods = goods;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setRemain(long remain) {
        this.remain = remain;
    }

    public void setRemainStart(long remainStart) {
        this.remainStart = remainStart;
    }

    public void setTimeslot(JpaTimeslot timeslot) {
        this.timeslot = timeslot;
    }

    public void increaseSeed(int seed) {
        this.seed += seed;
    }

    @Override
    //箱子按剩余空间由小到大排列
    public int compareTo(JpaBox o) {
        if (o == null)
            return 1;
        //刚刚放入的权重降低
//        long s = remain - o.remain;
        double s = remain * (1 + 10.0/(PUT_WEIGHT.get() - putWeight))
                - o.remain * (1 + 10.0/((PUT_WEIGHT.get() - o.putWeight)));
        return (int) s;
    }

    private long position(JpaGoods good) {
        if (remain < good.getSize())
            return -1;
        if (good.isFirst()) {
            if (remainStart > 0)
                return -1;
            return remainStart;
        }
        if (good.isLast()) {
            if (remainStart + remain < size)
                return -1;
            return size - good.getSize();
        }
        return remainStart;
    }

    public boolean put(JpaGoods good) {
        long pos = position(good);
        if (pos < 0)
            return false;
        good.put(this, pos);
        goods.add(good);
        remain -= good.getSize();
        if (pos == remainStart) {
            remainStart += good.getSize();
        }
        putWeight = PUT_WEIGHT.get();
        PUT_WEIGHT.set(putWeight + 1);
        return true;
    }

    private double remainPercentage () {
        return (remain * 10000/size)/100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaBox box = (JpaBox) o;

        if (getSlotId() != box.getSlotId()) return false;
        if (seed != box.seed) return false;
        if (day != null ? !day.equals(box.day) : box.day != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getSlotId();
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + seed;
        return result;
    }

    @Override
    public String toString() {
        return "JpaBox{" +
                "slotId=" + getSlotId() +
                ", day=" + day +
                ", size=" + size +
                ", remain=" + remain +
                ", remainStart=" + remainStart +
                ", remain%=" + remainPercentage() + '%' +
                '}';
    }
}
