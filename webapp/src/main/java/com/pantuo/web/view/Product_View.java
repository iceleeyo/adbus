package com.pantuo.web.view;

public class Product_View {
	
		String productName = "这是测试商品";
		String type = "video";
		int price = 111;
		public String getA() {
			return productName;
		}
		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

}
