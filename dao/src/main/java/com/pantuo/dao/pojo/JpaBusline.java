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
}
