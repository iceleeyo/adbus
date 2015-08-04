package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 公交车身库存
 * 需要考虑：
 * a，只保留一份最新库存，每天更新库存，
 * b，每天计算一份库存
 */
@Entity
@Table(name="bus_inventory")
public class JpaBusInventory extends CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //日期（如果使用方案b）
    private Date day;

    @ManyToOne
    @JoinColumn(name = "lineId")
    private JpaBusline line;

    //库存数量
    private int remain;

    public JpaBusInventory() {
    }

    public JpaBusInventory(int cityId) {
        super(cityId);
    }
}