package com.pantuo.dao.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pantuo.util.DateUtil;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Created by tliu on 4/10/15.
*/
//箱子
@Entity
@Table(name="box", uniqueConstraints=@UniqueConstraint(columnNames={"city", "day", "slotId"}),
    indexes = @Index(name="box_index", columnList="city, day, year, month, hour, slotId")
)
public class JpaBox extends CityEntity implements Comparable<JpaBox>, Serializable {
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
    private int year;       //back up for report
    private int month;      //back up for report
    private int hour;      //back up for report
    private long size;
    private long remain;
    private int fremain;
    private boolean isfirst=false;
    @JsonIgnore
    private String remainString;

    @Transient
    private BoxRemain remains;


    @ManyToOne
    @JoinColumn(name = "slotId")
    private JpaTimeslot timeslot;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy="box", fetch = FetchType.EAGER)
    @Where(clause="is_deleted=0")
    private List<JpaGoods> goods;

    private int tmpAbsoluteWight;       //绝对权重，越大越靠前

    @Transient
    private long putWeight;    //刚刚堆放货物，权重降低
    @Transient
    private int seed;           //用作随机的种子
    private int sort;           
    private int fsort;           

    public JpaBox() {}

    public JpaBox(int city, Date day, JpaTimeslot slot) {
        super(city);
        goods = new ArrayList<JpaGoods>();
        this.day = day;
        int[] yearMon = DateUtil.getYearAndMonthAndHour(day);
        this.year = yearMon[0];
        this.month = yearMon[1];
        this.hour = yearMon[2];
        timeslot = slot;
        remain=30;
        fremain=0;
        size=slot.getDuration();
//        this.size = slot.getDuration();
//        this.remain = size;
//        this.remainStart = 0;
//        remains = new BoxRemain(slot.getDuration(), 0, slot.getDuration());
//        updateRemainToDb();
    }

    public Date getDay() {
        return day;
    }

    public int getSlotId() {
        return timeslot.getId();
    }

    public boolean isIsfirst() {
		return isfirst;
	}

	public void setIsfirst(boolean isfirst) {
		this.isfirst = isfirst;
	}

	public JpaTimeslot getTimeslot() {
        return timeslot;
    }

    public long getSize() {
        return size;
    }

    public int getSort() {
		return sort;
	}

	public int getFremain() {
		return fremain;
	}

	public void setFremain(int fremain) {
		this.fremain = fremain;
	}

	public int getFsort() {
		return fsort;
	}

	public void setFsort(int fsort) {
		this.fsort = fsort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	
	
//	public long getRemain() {
//        return getRemains().remainSize();
//    }

//    public long getRemainStart() {
//        return remainStart;
//    }

    public long getRemain() {
		return remain;
	}

	public List<JpaGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<JpaGoods> goods) {
        this.goods = goods;
    }

    public static ThreadLocal<Long> getPUT_WEIGHT() {
		return PUT_WEIGHT;
	}

	public static void setPUT_WEIGHT(ThreadLocal<Long> pUT_WEIGHT) {
		PUT_WEIGHT = pUT_WEIGHT;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public String getRemainString() {
		return remainString;
	}

	public void setRemainString(String remainString) {
		this.remainString = remainString;
	}

	public long getPutWeight() {
		return putWeight;
	}

	public void setPutWeight(long putWeight) {
		this.putWeight = putWeight;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public int getTmpAbsoluteWight() {
		return tmpAbsoluteWight;
	}

	public void setRemain(long remain) {
		this.remain = remain;
	}

	public void setDay(Date day) {
        this.day = day;
    }

    public void setSize(long size) {
        this.size = size;
    }

//    public void setRemain(long remain) {
//        this.remain = remain;
//    }

//    public void setRemainStart(long remainStart) {
//        this.remainStart = remainStart;
//    }

    public void setTimeslot(JpaTimeslot timeslot) {
        this.timeslot = timeslot;
    }

    public void setTmpAbsoluteWight(int weight) {
        this.tmpAbsoluteWight = weight;
    }

    public void loadRemainFromDb() {
        if (remainString != null) {
            //use side effect
            remains = BoxRemain.fromJson(remainString);
        } else {
            remains = new BoxRemain(size, 0, size);
        }

    }

    public void updateRemainToDb() {
        if (remains != null) {
            remainString = remains.toString();
            remain = remains.remainSize();
            size = remains.getDuration();
        }
    }

    public BoxRemain getRemains() {
        if (remains == null) {
            loadRemainFromDb();
        }
        return remains;
    }

    public void setRemains(BoxRemain remains) {
        this.remains = remains;
        updateRemainToDb();
    }

    public int numberOfGoodsForOrder(int orderId) {
        int count = 0;
        if (goods == null)
            return count;
        for (JpaGoods g : goods) {
            if (g.getOrderId() == orderId) {
                count ++;
            }
        }
        return count;
    }

    public void increaseSeed(int seed) {
        this.seed += seed;
    }

    @Override
    //箱子按剩余空间由小到大排列
    public int compareTo(JpaBox o) {
        if (o == null)
            return 1;
        if (o.tmpAbsoluteWight != tmpAbsoluteWight) {
            return o.tmpAbsoluteWight - tmpAbsoluteWight;
        }
        //刚刚放入的权重降低
//        long s = remain - o.remain;
        double s = getRemains().getMax().getSize() * (1 + 10.0/(PUT_WEIGHT.get() - putWeight))
                - o.getRemains().getMax().getSize() * (1 + 10.0/((PUT_WEIGHT.get() - o.putWeight)));
        return (int) s;
    }

/*    private long position(JpaGoods good) {
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
    }*/

    public long put(JpaGoods good) {
/*        long pos = position(good);
        if (pos < 0)
            return false;
        good.put(this, pos);
        goods.add(good);
        remain -= good.getSize();
        if (pos == remainStart) {
            remainStart += good.getSize();
        }*/
        long putPos = -1;
        if (good.isFirst()) {
            putPos = getRemains().putHead(good.getSize(), true);
        } else if (good.isLast()) {
            putPos = getRemains().putTail(good.getSize(), true);
        } else {
            putPos = getRemains().putLeft(good.getSize(), true);
        }
        if (putPos >= 0) {
            good.put(this, putPos);
            goods.add(good);
            putWeight = PUT_WEIGHT.get();
            PUT_WEIGHT.set(putWeight + 1);
            updateRemainToDb();
        }
        return putPos;
    }

    public void put(JpaGoods good, long putPos) {
        if (putPos >= 0) {
            good.put(this, putPos);
            goods.add(good);
            putWeight = PUT_WEIGHT.get();
            PUT_WEIGHT.set(putWeight + 1);
            updateRemainToDb();
        }
    }

    private double remainPercentage () {
//        return (remain * 10000/size)/100.0;
        return getRemains().remainPercentage();
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
                ", remains=" + getRemains() +
//                ", size=" + size +
//                ", remain=" + remain +
//                ", remainStart=" + remainStart +
                ", remain%=" + remainPercentage() + '%' +
                '}';
    }
}
