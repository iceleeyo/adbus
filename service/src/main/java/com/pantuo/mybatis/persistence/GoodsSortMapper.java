package com.pantuo.mybatis.persistence;

import java.util.List;

import com.pantuo.vo.SortRequest;

public interface GoodsSortMapper {

	public void sortBlackGood(List<SortRequest> views);

	public void sortNormailGood(List<SortRequest> views);

}
