package com.pantuo.mybatis.persistence;

import com.pantuo.mybatis.domain.TimeslotReport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ReportMapper {

    List<TimeslotReport> getRemainTimeslots( @Param("city") int city,
                                             @Param("from") Date from, @Param("to") Date to,
                                             @Param("peak") Boolean peak);

    List<TimeslotReport> getMonthlyRemainTimeslots( @Param("city") int city,
                                                    @Param("year") int year,
                                                    @Param("peak") Boolean peak);

    List<TimeslotReport> getOrderTimeslots( @Param("city") int city,
                                            @Param("from") Date from, @Param("to") Date to,
                                             @Param("peak") Boolean peak);

    List<TimeslotReport> getOrderTimeslotsByIndustries( @Param("city") int city,
                                                        @Param("from") Date from, @Param("to") Date to,
                                            @Param("industries") List<Integer> industries, @Param("peak") Boolean peak);
}