package com.pantuo.dao.pojo;

import javax.persistence.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 车辆信息
 */

@Entity
@Table(name="bus", uniqueConstraints=@UniqueConstraint(columnNames={"city", "plateNumber"}),
        indexes = @Index(name="bus_index",
                columnList="city, plateNumber, serialNumber, lineId, category, modelId, companyId"))
public class JpaBus extends CityEntity{

    public static enum Category {
        baoche ("包车"),
        banche ("班车"),
        jidongche ("机动车"),
        yunyingche ("运营车");

        private static Map<String, Category> nameStrMap = new HashMap<String, Category>();
        static {
            for (Category c : Category.values()) {
                nameStrMap.put(c.getNameStr(), c);
            }
        }

        private final String nameStr;
        private Category (String nameStr) {
            this.nameStr = nameStr;
        }

        public String getNameStr() {
            return nameStr;
        }

        public static Category fromNameStr(String nameStr) {
            return nameStrMap.get(nameStr);
        }
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "lineId")
    private JpaBusline line;

    private String serialNumber;        //自编号
    private String oldSerialNumber;     //旧自编号
    private String plateNumber;         //车牌号
    private Category category;              //车辆分类

    @ManyToOne
    @JoinColumn(name = "modelId")
    private JpaBusModel model;            //车型

    @ManyToOne
    @JoinColumn(name = "companyId")
    private JpaBusinessCompany company; //运营公司

    private String adStatus;            //车身广告状态
    private String description;         //车辆情况
    private String office;              //所属公司
    private String branch;              //所属分公司
    private String bushis;              //车史
  //预计上刊时间
    //validation:必须为当天之后的几天
    private Date startDay;
    //预计下刊时间
    //validation：必须大于startDay
    private Date endDay;
    private boolean enabled = true;      //是否启用

//    @OneToMany(mappedBy="bus", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
//    private Set<JpaBusSchedule> schedules;

	public JpaBus(){

	}

    public JpaBus(int city, int busId) {
        super(city);
        this.id = busId;
    }

    public JpaBus(int city, JpaBusline line, Category category,
                  String serialNumber, String oldSerialNumber,
                  String plateNumber, JpaBusModel model, JpaBusinessCompany company,
                  String office, String branch,
                  String adStatus, String description,Date startDay, Date endDay) {
		super(city);
        this.line = line;
        this.category = category;
        this.serialNumber = serialNumber;
        this.oldSerialNumber = oldSerialNumber;
        this.plateNumber = plateNumber;
        this.model = model;
        this.company = company;
        this.office = office;
        this.branch = branch;
        this.adStatus = adStatus;
        this.description = description;
        this.startDay = startDay;
		this.endDay = endDay;
	}


	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}

	public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryStr() {
        return category == null ? "" : category.getNameStr();
    }

    public void setModel(JpaBusModel model) {
        this.model = model;
    }

    public JpaBusline getLine() {
        return line;
    }

    public void setLine(JpaBusline line) {
        this.line = line;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOldSerialNumber() {
        return oldSerialNumber;
    }

    public void setOldSerialNumber(String oldSerialNumber) {
        this.oldSerialNumber = oldSerialNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public JpaBusModel getModel() {
        return model;
    }

    public void setType(JpaBusModel model) {
        this.model = model;
    }

    public JpaBusinessCompany getCompany() {
        return company;
    }

    public void setCompany(JpaBusinessCompany company) {
        this.company = company;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

//    public Set<JpaBusSchedule> getSchedules() {
//        return schedules;
//    }
//
//    public void setSchedules(Set<JpaBusSchedule> schedules) {
//        this.schedules = schedules;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaBus jpaBus = (JpaBus) o;

        if (city != jpaBus.city) return false;
        if (plateNumber != null ? !plateNumber.equals(jpaBus.plateNumber) : jpaBus.plateNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = city;
        result = 31 * result + (plateNumber != null ? plateNumber.hashCode() : 0);
        return result;
    }

	public String getBushis() {
		return bushis;
	}

	public void setBushis(String bushis) {
		this.bushis = bushis;
	}


	@Override
	public String toString() {
		return "JpaBus [id=" + id + ", line=" + line + ", serialNumber=" + serialNumber + ", oldSerialNumber="
				+ oldSerialNumber + ", plateNumber=" + plateNumber + ", category=" + category + ", model=" + model
				+ ", company=" + company + ", adStatus=" + adStatus + ", description=" + description + ", office="
				+ office + ", branch=" + branch + ", startDay=" + startDay + ", endDay=" + endDay + ", enabled="
				+ enabled + "]";
	}

}
