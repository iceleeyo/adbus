package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 公交车身锁定（预留）
 *
 * 车身锁定时，只减库存，不确定上刊/下刊具体车辆，不出上刊单
 * 所以打印上刊单时，车辆列表的车辆数量应该参考车身库存
 */
@Entity
@Table(name="bus_reservation")
public class JpaBusReservation extends CityEntity {
    public static enum Status {
        enabled,    //有效
        disabled,   //禁用
        expired,    //过期
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "lineId")
    private JpaBusline line;

    //预留数量
    private int reservedNumber;
    //状态
    private Status status = Status.enabled;

    //预计上刊时间
    //validation:必须为当天之后的几天
    private Date startDay;
    //预计下刊时间
    //validation：必须大于startDay
    private Date endDay;
    //过期天数，当预留超过几天（比较当前时间和created)，改预留失效
    private int expireDays;

    public JpaBusReservation() {
    }

    public JpaBusReservation(int cityId) {
        super(cityId);
    }
}