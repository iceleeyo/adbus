package com.pantuo.web.view;

import java.util.List;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Supplies;

public class ContractView {

	 public Contract mainView;
	 public List<Attachment> files;

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
	 
	 

}
