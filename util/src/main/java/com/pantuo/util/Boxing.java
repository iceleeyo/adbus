package com.pantuo.util;

import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaGoods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 装箱算法
 */
public class Boxing {
    private static Logger log = LoggerFactory.getLogger(Boxing.class);

    private HashSet<JpaBox> boxes;       //箱子，用hashset生成打乱但是确定的列表
    private HashSet<JpaGoods> goods;      //货物，用hashset生成打乱但是确定的列表
    private List<JpaGoods> goodsList;
    private List<JpaBox> boxList;
    private double remainPercentage;

    public Boxing() {
        this.boxes = new HashSet<JpaBox> ();
        this.goods = new HashSet<JpaGoods> ();
        goodsList = new LinkedList<JpaGoods> ();
        boxList = new LinkedList<JpaBox> ();
    }

    public void addBox(JpaBox box) {
        this.boxes.add(box);
    }

    public void addGoods(List<JpaGoods> goods) {
        this.goods.addAll(goods);
    }

    public HashSet<JpaGoods> getGoods() {
        return goods;
    }

    public double getRemainPercentage() {
        return remainPercentage;
    }

    //返回不能装箱的货物列表
    public List<JpaGoods> boxing() {
        log.info("Start boxing with {} boxes and {} goods", boxes.size(), goods.size());

        Iterator<JpaGoods> iter = goods.iterator();
        while (iter.hasNext()) {
            goodsList.add(iter.next());
        }
        Iterator<JpaBox> iterb = boxes.iterator();
        while (iterb.hasNext()) {
            boxList.add(iterb.next());
        }

        List<JpaGoods> notBoxed = new ArrayList<JpaGoods> ();
        Collections.sort(goodsList);
        for (JpaGoods g : goodsList) {
            shuffleAndSortBoxes(g);
            for (JpaBox b : boxList) {
                if (b.put(g) >= 0) {
                    log.debug("Put good {} into box {} @ postion {}, box remain {}",
                            g.getOrderId(), b.getSlotId(), g.getInboxPosition(), b.getRemains());
                    break;
                }
            }

            if (!g.isBoxed()) {
                notBoxed.add(g);
            }
        }

        validate();
        return notBoxed;
    }

    private void shuffleAndSortBoxes(JpaGoods forThisGoods) {
        this.boxes = new HashSet<JpaBox> ();
        for (JpaBox box : boxList) {
            box.setTmpAbsoluteWight(-box.numberOfGoodsForOrder(forThisGoods.getOrderId()));
            box.increaseSeed(31*13);
            boxes.add(box);
        }
        boxList = new LinkedList<JpaBox> ();
        Iterator<JpaBox> iterb = boxes.iterator();
        while (iterb.hasNext()) {
            boxList.add(iterb.next());
        }
        Collections.sort(boxList);
    }

    private void validate() {
        log.info("Validating boxing");
        long totalSize = 0;
        long totalRemain = 0;
        for (JpaBox box : boxes) {
            totalSize += box.getSize();
            totalRemain += box.getRemain();
            long occupied = 0;
            for (JpaGoods g : box.getGoods()) {
                if (g.isFirst())
                    assert (g.getInboxPosition() == 0);
                if (g.isLast())
                    assert (g.getInboxPosition() == box.getSize() - g.getSize());
                occupied += g.getSize();
            }
            assert (occupied <= box.getSize());
        }
        remainPercentage = (totalRemain * 10000 / totalSize) / 100.0;
    }

    //返回按照一般排序的Box列表
    public List<JpaBox> toOrderedBoxList() {
        List<JpaBox> boxes = new ArrayList<JpaBox> (boxList);
        Collections.sort(boxes, new Comparator<JpaBox>() {
           // @Override
            public int compare(JpaBox o1, JpaBox o2) {
                return o1.getSlotId() - o2.getSlotId();
            }
        });
        return boxes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Boxes status:\n");
        for (JpaBox box : toOrderedBoxList()) {
            sb.append("\t").append(box).append("\n");
            for (JpaGoods g : box.getGoods()) {
                sb.append("\t\t").append(g).append("\n");
            }
        }
        sb.append("\tRemain percentage: ").append(getRemainPercentage()).append("%\n");
        return sb.toString();
    }
}
