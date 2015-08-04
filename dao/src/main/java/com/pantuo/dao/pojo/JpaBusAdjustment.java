package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 公交车调车记录
 */
@Entity
@Table(name="bus_adjustment")
public class JpaBusAdjustment extends CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "fromLineId")
    private JpaBusline fromLine;
    @ManyToOne
    @JoinColumn(name = "toLineId")
    private JpaBusline toLine;

    //公交车
    @ManyToOne
    @JoinColumn(name = "busId")
    private JpaBus bus;

    //调车生效的日期
    private Date day;

    public JpaBusAdjustment() {
    }

    public JpaBusAdjustment(int cityId) {
        super(cityId);
    }
}