package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.mybatis.domain.ProductV2;

public class BodyProView {
	JpaBusOrderDetailV2 orderDetailV2;
	ProductV2 productV2;
	MediaSurvey mediaSurvey;
	public JpaBusOrderDetailV2 getOrderDetailV2() {
		return orderDetailV2;
	}
	public void setOrderDetailV2(JpaBusOrderDetailV2 orderDetailV2) {
		this.orderDetailV2 = orderDetailV2;
	}
	public ProductV2 getProductV2() {
		return productV2;
	}
	public void setProductV2(ProductV2 productV2) {
		this.productV2 = productV2;
	}
	public MediaSurvey getMediaSurvey() {
		return mediaSurvey;
	}
	public void setMediaSurvey(MediaSurvey mediaSurvey) {
		this.mediaSurvey = mediaSurvey;
	}
	
}
