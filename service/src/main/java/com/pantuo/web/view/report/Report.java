package com.pantuo.web.view.report;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.util.DateUtil;

public class Report implements Serializable {
	private Map<String/*date*/, UiBox> boxes;
	private JpaTimeslot slot;

	public Report(JpaTimeslot slot) {
		this.slot = slot;
		this.boxes = new HashMap<String, UiBox>();
	}

	public String addBox(Box box) {
		String key = DateUtil.longDf.get().format(box.getDay());
		if (!(box instanceof UiBox))
			box = new UiBox(box);
		boxes.put(key, (UiBox) box);
		return key;
	}

	public String addBox(JpaBox box) {
		List<JpaGoods> goods = box.getGoods();

		String key = DateUtil.longDf.get().format(box.getDay());
		UiBox b = boxes.get(key);
		if (b == null) {
			b = new UiBox(box);
			boxes.put(key, b);
		}
		if (goods != null) {
			for (JpaGoods g : goods) {
				b.addGood(g);
			}
		}
		return key;
	}

	public String addBox(JpaBox box, JpaGoods good) {
		String key = DateUtil.longDf.get().format(box.getDay());
		UiBox b = boxes.get(key);
		if (b != null) {
			b.addGood(good);
		} else {
			b = new UiBox(box);
			b.addGood(good);
			boxes.put(key, b);
		}

		return key;
	}

	public Map<String/*date*/, UiBox> getBoxes() {
		return boxes;
	}

	public void setBoxes(Map<String, UiBox> boxes) {
		this.boxes = boxes;
	}

	public UiBox getBox(String dayStr) {
		return boxes.get(dayStr);
	}

	public JpaTimeslot getSlot() {
		return slot;
	}
}
