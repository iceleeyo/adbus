package com.pantuo.util;

import com.pantuo.dao.pojo.Box;
import com.pantuo.dao.pojo.Goods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 装箱算法
 */
public class Boxing {
    private static Logger log = LoggerFactory.getLogger(Boxing.class);

    private HashSet<Box> boxes;       //箱子，用hashset生成打乱但是确定的列表
    private HashSet<Goods> goods;      //货物，用hashset生成打乱但是确定的列表
    private List<Goods> goodsList;
    private List<Box> boxList;
    private double remainPercentage;

    public Boxing() {
        this.boxes = new HashSet<Box> ();
        this.goods = new HashSet<Goods> ();
        goodsList = new LinkedList<Goods> ();
        boxList = new LinkedList<Box> ();
    }

    public void addBox(Box box) {
        this.boxes.add(box);
    }

    public void addGoods(List<Goods> goods) {
        this.goods.addAll(goods);
    }

    public HashSet<Goods> getGoods() {
        return goods;
    }

    public double getRemainPercentage() {
        return remainPercentage;
    }

    //返回不能装箱的货物列表
    public List<Goods> boxing() {
        log.info("Start boxing with {} boxes and {} goods", boxes.size(), goods.size());

        Iterator<Goods> iter = goods.iterator();
        while (iter.hasNext()) {
            goodsList.add(iter.next());
        }
        Iterator<Box> iterb = boxes.iterator();
        while (iterb.hasNext()) {
            boxList.add(iterb.next());
        }

        List<Goods> notBoxed = new ArrayList<Goods> ();
        Collections.sort(goodsList);
        for (Goods g : goodsList) {
            shuffleAndSortBoxes();
            for (Box b : boxList) {
                if (b.put(g)) {
                    log.debug("Put good {} into box {} @ postion {}, box remain {}", g.getOrderId(), b.getSlotId(), g.getInboxPosition(), b.getRemain());
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

    private void shuffleAndSortBoxes() {
        this.boxes = new HashSet<Box> ();
        for (Box box : boxList) {
            box.increaseSeed(31*13);
            boxes.add(box);
        }
        boxList = new LinkedList<Box> ();
        Iterator<Box> iterb = boxes.iterator();
        while (iterb.hasNext()) {
            boxList.add(iterb.next());
        }
        Collections.sort(boxList);
    }

    private void validate() {
        log.info("Validating boxing");
        long totalSize = 0;
        long totalRemain = 0;
        for (Box box : boxes) {
            totalSize += box.getSize();
            totalRemain += box.getRemain();
            long occupied = 0;
            for (Goods g : box.getGoods()) {
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
    public List<Box> toOrderedBoxList() {
        List<Box> boxes = new ArrayList<Box> (boxList);
        Collections.sort(boxes, new Comparator<Box>() {
            @Override
            public int compare(Box o1, Box o2) {
                return o1.getSlotId() - o2.getSlotId();
            }
        });
        return boxes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Boxes status:\n");
        for (Box box : toOrderedBoxList()) {
            sb.append("\t").append(box).append("\n");
            for (Goods g : box.getGoods()) {
                sb.append("\t\t").append(g).append("\n");
            }
        }
        sb.append("\tRemain percentage: ").append(getRemainPercentage()).append("%\n");
        return sb.toString();
    }
}
