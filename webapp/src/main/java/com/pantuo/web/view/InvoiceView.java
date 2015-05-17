package com.pantuo.web.view;

import java.util.List;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Invoice;

public class InvoiceView {
	public Invoice mainView;
	 public List<Attachment> files;
	public Invoice getMainView() {
		return mainView;
	}
	public void setMainView(Invoice mainView) {
		this.mainView = mainView;
	}
	public List<Attachment> getFiles() {
		return files;
	}
	public void setFiles(List<Attachment> files) {
		this.files = files;
	}
	 
}
