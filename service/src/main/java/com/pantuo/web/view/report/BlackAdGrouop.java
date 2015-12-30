package com.pantuo.web.view.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaGoodsBlack;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.mybatis.domain.Supplies;

public class BlackAdGrouop {

	Map<Integer, List<JpaGoodsBlack>> timeslotMap;
	Map<Integer, Supplies> blackSuppliesMap;

	public BlackAdGrouop(Map<Integer, List<JpaGoodsBlack>> timeslotMap, Map<Integer, Supplies> blackSuppliesMap) {
		this.timeslotMap = timeslotMap;
		this.blackSuppliesMap = blackSuppliesMap;
	}

	public List<JpaGoods> getBlackGoods(int timeslotId, int city) {  
		List<JpaGoods> list = new ArrayList<JpaGoods>();
		if (this.timeslotMap.containsKey(timeslotId)) {
			List<JpaGoodsBlack> blacks = this.timeslotMap.get(timeslotId);

			for (JpaGoodsBlack jpaGoodsBlack : blacks) {
				Supplies s = this.blackSuppliesMap.get(jpaGoodsBlack.getSuppliesId());
				if (s != null) {
					JpaSupplies js = new JpaSupplies(city, s.getName(),
							JpaProduct.Type.values()[s.getSuppliesType()], s.getIndustryId(), s.getUserId(),
							s.getDuration(), s.getFilePath(), s.getInfoContext(),
							/*JpaSupplies.Status.values()[s.getStats()],*/null, s.getOperFristuser(),
							s.getOperFristcomment(), s.getOperFinaluser(), s.getOperFinalcomment(),
							s.getSeqNumber(), s.getCarNumber(), s.getResponseCid());
					JpaProduct p = new JpaProduct(Integer.MAX_VALUE, JpaProduct.Type.video, "filler",
							s.getDuration(), 0, 0, 0, 0, 0, 0, 0, 0, true, true, false, null,
							null);
					JpaOrders o = new JpaOrders(Integer.MAX_VALUE, "", js, p, null, 0, null, null, null,
							JpaProduct.Type.video, JpaOrders.PayType.remit, JpaOrders.Status.completed, null, null,
							0, null, null, null,  null, null);
					JpaGoods g = new JpaGoods(city, 0, s.getDuration(), false, false, 0);
					g.setInboxPosition(jpaGoodsBlack.getInboxPosition());
					g.setSort_index(jpaGoodsBlack.getSort_index());
					g.setOrder(o);
					list.add(g);
				}
			}
		}
		return list;
	}


}
