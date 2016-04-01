package com.pantuo.pojo;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.util.DateUtil;
import com.pantuo.web.view.report.Report;
import com.pantuo.web.view.report.UiBox;

public class FlatScheduleListItem {
    private String slotDesc;
    private String slot;
    private int slotSize;
    private String materialName = "";
    private Integer materialSize;
    private String monthDay;

    public FlatScheduleListItem() {
		super();
	}

	public FlatScheduleListItem(String slotDesc, String slot, int slotSize, String materialName, Integer materialSize,
			String monthDay) {
		super();
		this.slotDesc = slotDesc;
		this.slot = slot;
		this.slotSize = slotSize;
		this.materialName = materialName;
		this.materialSize = materialSize;
		this.monthDay = monthDay;
	}

	public String getMonthDay() {
		return monthDay;
	}

	public void setMonthDay(String monthDay) {
		this.monthDay = monthDay;
	}

	public void setSlotDesc(String slotDesc) {
        this.slotDesc = slotDesc;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public void setSlotSize(int slotSize) {
        this.slotSize = slotSize;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setMaterialSize(Integer materialSize) {
        this.materialSize = materialSize;
    }

    public String getSlotDesc() {
        return slotDesc;
    }

    public String getSlot() {
        return slot;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public String getMaterialName() {
        return materialName;
    }

    public Integer getMaterialSize() {
        return materialSize;
    }

    public String getMaterialSizeStr() {
        return materialSize == null ? "" : materialSize + "";
    }

    public String getSlotSizeStr() {
        return DateUtil.toShortStr2(slotSize);
    }

    public String getDaySlotDesc() {
        try {
            return "广告" + monthDay + slotDesc.substring(2);
        } catch (Exception e) {
            return slotDesc;
        }
    }

    public FlatScheduleListItem(String monthDay, Report r) {
        this.monthDay = monthDay;
        slotDesc = StringUtils.defaultString(r.getSlot().getName(), "");
        slot = StringUtils.defaultString(r.getSlot().getStartTimeStr());
        slotSize = (int) r.getSlot().getDuration();
        JpaGoods good = null;
        if (!r.getBoxes().isEmpty()) {
            Iterator<Map.Entry<String, UiBox>> iter = r.getBoxes().entrySet().iterator();
            UiBox box = iter.next().getValue();
            if (box.getGoods() != null && !box.getGoods().isEmpty()) {
                good = box.getGoods().get(0);
            }
        }
        String supplyName = null;
        String supplySeqNum = null;
        if (good != null && good.getOrder() != null && good.getOrder().getSupplies() != null) {
            supplyName = good.getOrder().getSupplies().getName();
            supplySeqNum = good.getOrder().getSupplies().getSeqNumber();
        }
        materialName = (supplySeqNum == null ? "" : supplySeqNum) +"-"+ (supplyName == null ? "" : " " + supplyName);

        Long size = null;
        if (good != null && good.getOrder() != null && good.getOrder().getProduct() != null)
            size = good.getOrder().getProduct().getDuration();
        materialSize = size == null ? null : size.intValue();
    }

	@Override
	public String toString() {
		return "FlatScheduleListItem [slotDesc=" + slotDesc + ", slot=" + slot + ", slotSize=" + slotSize
				+ ", materialName=" + materialName + ", materialSize=" + materialSize + ", monthDay=" + monthDay + "]";
	}
    
}
