package com.pantuo.web.view;

/**
 * 
 * <b><code>AutoCompleteView</code></b>
 * <p>
 * 自动补全功能value为显示在列表的内容 label为选中后要显示的内容 
 * </p>
 * <b>Creation Time:</b> 2015年5月20日 下午9:15:38
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class AutoCompleteView {

	public String value;
	public String label;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public AutoCompleteView(String value, String label) {
		this.value = value;
		this.label = label;
	}

}
