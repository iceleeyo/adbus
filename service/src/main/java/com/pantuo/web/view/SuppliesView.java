package com.pantuo.web.view;

import java.util.List;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Supplies;

public class SuppliesView {

	 public Supplies mainView;
	 public List<Attachment> files;
	 public List<Attachment> payvouchers;
	public Supplies getMainView() {
		return mainView;
	}
	public void setMainView(Supplies mainView) {
		this.mainView = mainView;
	}
	public List<Attachment> getFiles() {
		return files;
	}
	public void setFiles(List<Attachment> files) {
		this.files = files;
	}
	public List<Attachment> getPayvouchers() {
		return payvouchers;
	}
	public void setPayvouchers(List<Attachment> payvouchers) {
		this.payvouchers = payvouchers;
	}
	 
	 

}
