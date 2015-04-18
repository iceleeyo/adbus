package com.pantuo.mybatis.persistence;

import com.pantuo.mybatis.domain.Box;
import com.pantuo.mybatis.domain.TimeslotReport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ReportMapper {

    List<TimeslotReport> getRemainTimeslots( @Param("from") Date from, @Param("to") Date to,
                                             @Param("peak") Boolean peak);

    List<TimeslotReport> getMonthlyRemainTimeslots( @Param("year") int year,
                                                    @Param("peak") Boolean peak);

    List<TimeslotReport> getOrderTimeslots( @Param("from") Date from, @Param("to") Date to,
                                             @Param("peak") Boolean peak);

    List<TimeslotReport> getOrderTimeslotsByIndustries( @Param("from") Date from, @Param("to") Date to,
                                            @Param("industries") List<Integer> industries, @Param("peak") Boolean peak);
}