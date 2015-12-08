package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaBusline;

public class CarUseView {

	public JpaBusline line;
	public BodyUseView view;

	public String levelString;

	public String getLevelString() {

		if (line != null) {
			return line.getLevelStr();
		}
		return levelString;
	}

	public void setLevelString(String levelString) {

		this.levelString = levelString;
	}

	public CarUseView(JpaBusline line) {
		this.line = line;
	}

	public JpaBusline getLine() {
		return line;
	}

	public void setLine(JpaBusline line) {
		this.line = line;
	}

	public BodyUseView getView() {
		return view;
	}

	public void setView(BodyUseView view) {
		this.view = view;
	}

}
