package com.pantuo.pojo;

import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.util.DateUtil;
import com.pantuo.web.ScheduleController;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class FlatScheduleListItem {
    private String slotDesc;
    private String slot;
    private int slotSize;
    private String materialName = "";
    private Integer materialSize;
    private String monthDay;

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

    public FlatScheduleListItem(String monthDay, ScheduleController.Report r) {
        this.monthDay = monthDay;
        slotDesc = StringUtils.defaultString(r.getSlot().getName(), "");
        slot = StringUtils.defaultString(r.getSlot().getStartTimeStr());
        slotSize = (int) r.getSlot().getDuration();
        JpaGoods good = null;
        if (!r.getBoxes().isEmpty()) {
            Iterator<Map.Entry<String, ScheduleController.UiBox>> iter = r.getBoxes().entrySet().iterator();
            ScheduleController.UiBox box = iter.next().getValue();
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
        materialName = (supplySeqNum == null ? "" : supplySeqNum) + (supplyName == null ? "" : " " + supplyName);

        Long size = null;
        if (good != null && good.getOrder() != null && good.getOrder().getProduct() != null)
            size = good.getOrder().getProduct().getDuration();
        materialSize = size == null ? null : size.intValue();
    }
}
