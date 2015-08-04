package com.pantuo.dao.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 车身工单的线路详情
 */
@Entity
@Table(name="bus_work_list_detail")
public class JpaBusWorklistDetail extends CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "worklistId")
    @JsonIgnore
    //对应的工单
    private JpaBusWorklist worklist;

    @ManyToOne
    @JoinColumn(name = "lineId")
    //线路
    private JpaBusline line;

    //车辆数量
    private int busNumber;

    public JpaBusWorklistDetail() {
    }

    public JpaBusWorklistDetail(int cityId) {
        super(cityId);
    }
}