package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author tliu
 *
 * 车身上刊单的回单
 */
@Entity
@Table(name="bus_publish_list")
public class JpaBusPublishlist extends CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "worklistId")
    //回单对应的工单
    private JpaBusWorklist worklist;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    //已经上刊的车辆列表
    private Set<JpaBus> buses;

    public JpaBusPublishlist() {
    }

    public JpaBusPublishlist(int cityId) {
        super(cityId);
    }
}