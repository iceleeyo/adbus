package com.pantuo.service.impl;

import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.mybatis.domain.Box;
/**
 * 
 * <b><code>ScheduleAbstract</code></b>
 * <p>
 * 
 * </p>
 * <b>Creation Time:</b> 2015年12月6日 下午4:14:46
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public abstract class ScheduleAbstract {
	
	
	
	public   JpaBox getJpaBoxFromEntity(JpaOrders order, Box box) {
		//-----------------------------------
		JpaTimeslot slot = new JpaTimeslot();
		slot.setId(box.getSlotId());
		//-------------------------------------
		JpaBox storeBox = new JpaBox();
		storeBox.setCity(order.getCity());
		storeBox.setDay(box.getDay());
		storeBox.setId(box.getId());

		storeBox.setTimeslot(slot);
		return storeBox;
	}

}
