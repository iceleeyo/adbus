package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * 运营公司
 */

@Entity
@Table(name="business_company", uniqueConstraints=@UniqueConstraint(columnNames={"name"}),
        indexes = @Index(name="business_company_index",columnList="city, id"))
public class JpaBusinessCompany extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;                //公司名

    private String contact;         //联系人
    private String phone;           //联系电话
    private String address;         //联系地址
    private String office;          //办公地址

	public JpaBusinessCompany(){

	}

	public JpaBusinessCompany(int city, String name, String contact, String phone,
                              String address, String office) {
		super(city);
        this.name = name;
        this.contact = contact;
        this.phone = phone;
        this.address = address;
        this.office = office;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Override
    public String toString() {
        return "JpaBusinessCompany{" +
                "office='" + office + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", contact='" + contact + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
