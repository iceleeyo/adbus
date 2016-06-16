package com.pantuo.web.view;

import java.util.List;

import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.InvoiceDetail;

public class InvoiceView {
	public Invoice mainView;
	public InvoiceDetail detailView;
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
	public InvoiceDetail getDetailView() {
		return detailView;
	}
	public void setDetailView(InvoiceDetail detailView) {
		this.detailView = detailView;
	}
	
	 
}
