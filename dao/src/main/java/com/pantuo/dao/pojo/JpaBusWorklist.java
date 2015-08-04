package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author tliu
 *
 * 车身工单
 */
@Entity
@Table(name="bus_work_list")
public class JpaBusWorklist extends CityEntity {
    public static enum Source {
        order,  //因合同生成的工单
        adjust, //因调车生成的工单
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Source source;
    @ManyToOne
    @JoinColumn(name = "contractId")
    //工单对应的合同
    private JpaContract contract;
    @ManyToOne
    @JoinColumn(name = "suppliesId")
    //工单对应的素材
    private JpaSupplies supplies;

    //如果是因锁定（预留）形成的工单，记录锁定ID
    @ManyToOne
    @JoinColumn(name = "reservationId")
    private JpaBusReservation reservation;

    //上刊时间
    private Date startDay;
    //下刊时间
    private Date endDay;
    //续刊可能性,0到1
    private double renewPossibility;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy="worklist", fetch = FetchType.EAGER, orphanRemoval = true)
    //工单详情列表
    private Set<JpaBusWorklistDetail> details;

    public JpaBusWorklist() {
    }

    public JpaBusWorklist(int cityId) {
        super(cityId);
    }
}