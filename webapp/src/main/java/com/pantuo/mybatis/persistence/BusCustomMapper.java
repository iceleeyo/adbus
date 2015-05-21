package com.pantuo.mybatis.persistence;

import com.pantuo.mybatis.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusCustomMapper {

    List<CountableBusLine> getBuslines(@Param("city") int city,
                                  @Param("level") Integer level,
                                  @Param("category") Integer category,
                                  @Param("lineId") Integer lineId,
                                  @Param("modelId") Integer busModelId,
                                  @Param("companyId") Integer companyId);

    List<CountableBusModel> getBusModels(@Param("city") int city,
                                    @Param("level") Integer level,
                                    @Param("category") Integer category,
                                    @Param("lineId") Integer lineId,
                                    @Param("modelId") Integer busModelId,
                                    @Param("companyId") Integer companyId);

    List<CountableBusinessCompany> getBusinessCompanies(@Param("city") int city,
                                                   @Param("level") Integer level,
                                                   @Param("category") Integer category,
                                                   @Param("lineId") Integer lineId,
                                                   @Param("modelId") Integer busModelId,
                                                   @Param("companyId") Integer companyId);
}