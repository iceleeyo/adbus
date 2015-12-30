package com.pantuo.web.view.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.pantuo.dao.pojo.JpaBox; 
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.util.DateUtil;

public   class UiBox extends Box {
	private List<JpaGoods> goods;

	public UiBox() {
		goods = new ArrayList<JpaGoods>();
	}

	public UiBox(Box box) {
		BeanUtils.copyProperties(box, this);
		if (goods == null) {
			goods = new ArrayList<JpaGoods>();
		}
	}

	public UiBox(JpaBox box) {
		BeanUtils.copyProperties(box, this);
		goods = new ArrayList<JpaGoods>();
	}

	public void addGood(JpaGoods good) {
		//cut connection from JpaGoods to Box to avoid loop in serialization
		good.setBox(null);
		goods.add(good);
	}

	public List<JpaGoods> getGoods() {
		return goods;
	}

	public void setGoods(List<JpaGoods> goods) {
		this.goods = goods;
	}

	public List<JpaGoods> fetchSortedGoods(/*add param by impanxh*/boolean queryBlackIn,
			BlackAdGrouop blackAdGrouop) {
		List<JpaGoods> list = null;
		if (goods != null)
			list = new ArrayList<JpaGoods>(goods);
		else
			list = new ArrayList<JpaGoods>();

		if (queryBlackIn) {
			list.addAll(blackAdGrouop.getBlackGoods(getSlotId(), getCity()));
		}
		Collections.sort(list, new Comparator<JpaGoods>() {
			//  @Override
			public int compare(JpaGoods o1, JpaGoods o2) {
				return (int) (o1.getSort_index() - o2.getSort_index());
				//return (int) (o1.getInboxPosition() - o2.getInboxPosition());
			}
		});
		return list;
	}

	/**
	 * 
	 * 查空的档位 impanxh
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<FreeBox> fetchFreeGoods() {
		List<FreeBox> free = new ArrayList<FreeBox>();
		List<JpaGoods> goods = fetchSortedGoods(false, null);
		if (goods.isEmpty()) {
			free.add(new FreeBox(0, getSize()));
		} else {
			int s = goods.size();
			for (int i = 0; i < s; i++) {
				JpaGoods row = goods.get(i);
				boolean isNoramlElement = false;
				if (i == 0) {
					//处理头部
					if (row.getInboxPosition() == 0) {
						if (s == 1) {//如果只有一段 比如 0-30 那么剩余就是 30-180
							free.add(new FreeBox(row.getSize(), getSize()));
						} else {
							if (goods.get(1).getInboxPosition() != row.getSize()) {
								free.add(new FreeBox(row.getSize(), goods.get(1).getInboxPosition()));
							}
						}
					} else {//如果直接开始的时候不是0 比如 第一段是30-45 那么空余是0-30
						free.add(new FreeBox(0, row.getInboxPosition()));
						isNoramlElement = true;
						/*isNoramlElement=true;
						long f = row.getSize() + row.getInboxPosition();
						long _end = (i + 1) >= s ? getSize() : goods.get(i + 1).getInboxPosition();
						if (f != _end) {
							free.add(new FreeBox(f, _end));
						}*/
					}
				} else {
					isNoramlElement = true;
				}
				if (isNoramlElement) {
					long f = row.getSize() + row.getInboxPosition();
					long _end = (i + 1) >= s ? getSize() : goods.get(i + 1).getInboxPosition();
					if (f != _end) {
						free.add(new FreeBox(f, _end));
					}
				}
			}
		}
		return free;

	}

	public String getRemainStr() {
		//	System.out.println(this.getDay() +" " +this.getSize() +" "+ getRemain() +" " +( getFremain()));

		int f = getFremain() == null ? 0 : (30 - getFremain());
		return DateUtil.toShortStr(this.getSize() - getRemain() + f);//
		//	return DateUtil.toShortStr( getRemain() );//
	}

}