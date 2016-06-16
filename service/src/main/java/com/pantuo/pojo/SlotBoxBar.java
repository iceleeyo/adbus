package com.pantuo.pojo;

import com.pantuo.dao.pojo.BoxRemain;
import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于slot的行级box
 *
 * Created by tliu on 7/4/15.
 */
public class SlotBoxBar extends JpaBox {
    private List<JpaBox> boxes;
    public SlotBoxBar(List<JpaBox> boxes) {
        super(boxes.get(0).getCity(), boxes.get(0).getDay(), boxes.get(0).getTimeslot());
        this.setTimeslot(boxes.get(0).getTimeslot());

        this.boxes = boxes;

        List<BoxRemain> remains = new ArrayList<BoxRemain>();
        for (JpaBox b : boxes) {
            remains.add(b.getRemains());
        }
        this.setRemains(BoxRemain.intersect(remains));
    }

    @Override
    public long put(JpaGoods good) {
        long putPos = super.put(good);
        if (putPos >= 0) {
            for (JpaBox b : boxes) {
                b.put(good.clone(), putPos);
            }
        }
        return putPos;
    }
}
