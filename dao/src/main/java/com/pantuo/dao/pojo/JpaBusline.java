package com.pantuo.dao.pojo;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 车辆线路信息
 */

@Entity
@Table(name="bus_line", uniqueConstraints=@UniqueConstraint(columnNames={"name"}),
        indexes = @Index(name="bus_line_index",columnList="city, id"))
public class JpaBusline extends CityEntity{
    public static enum Level {
        S ("特级"),
        APP ("A++"),
        AP ("A+"),
        A ("A"),
        LATLONG("经纬线");

        private static Map<String, Level> nameStrMap = new HashMap<String, Level>();
        static {
            for (Level l : Level.values()) {
                nameStrMap.put(l.getNameStr(), l);
            }
        }

        private final String nameStr;
        private Level (String nameStr) {
            this.nameStr = nameStr;
        }

        public String getNameStr() {
            return nameStr;
        }

        public static Level fromNameStr(String nameStr) {
            return nameStrMap.get(nameStr);
        }
    }
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;    //线路名称
    private Level level;    //线路级别
    private int _cars;//线路对应车辆总数
    private int _persons;//人车流量
    
    private int _today;//当天在刊车数
    
    private int _month1day;//当天+30天日期的那天 在刊车数
    
    private int _month2day;
    
    private int _month3day;
    @Transient
    private int _sim;//相近的站点数量
	public JpaBusline(){

	}

	public JpaBusline(int city, String name, Level level) {
		super(city);
        this.name = name;
        this.level = level;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getLevelStr () {
        return level == null ? "" : level.getNameStr();
    }

    @Override
    public String toString() {
        return "JpaBusline{" +
                "name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

	public int get_cars() {
		return _cars;
	}

	public void set_cars(int _cars) {
		this._cars = _cars;
	}

	public int get_persons() {
		return _persons;
	}

	public void set_persons(int _persons) {
		this._persons = _persons;
	}

	public int get_today() {
		return _today;
	}

	public void set_today(int _today) {
		this._today = _today;
	}

	public int get_month1day() {
		return _month1day;
	}

	public void set_month1day(int _month1day) {
		this._month1day = _month1day;
	}

	public int get_month2day() {
		return _month2day;
	}

	public void set_month2day(int _month2day) {
		this._month2day = _month2day;
	}

	public int get_month3day() {
		return _month3day;
	}

	public void set_month3day(int _month3day) {
		this._month3day = _month3day;
	}

	public int get_sim() {
		return _sim;
	}

	public void set_sim(int _sim) {
		this._sim = _sim;
	}

 
    
}
