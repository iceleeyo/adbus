package com.pantuo.web.view;

import java.util.List;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Supplies;

public class ContractView {

	 public Contract mainView;
	 public JpaContract jpaContract;
	 public List<Attachment> files;
	 public String industryname;

	 public Contract getMainView() {
		return mainView;
	}
	public void setMainView(Contract mainView) {
		this.mainView = mainView;
	}
	public List<Attachment> getFiles() {
		return files;
	}
	public void setFiles(List<Attachment> files) {
		this.files = files;
	}
	public String getIndustryname() {
		return industryname;
	}
	public void setIndustryname(String industryname) {
		this.industryname = industryname;
	}
	public JpaContract getJpaContract() {
		return jpaContract;
	}
	public void setJpaContract(JpaContract jpaContract) {
		this.jpaContract = jpaContract;
	}
	 
	 

}
