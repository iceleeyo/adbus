package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.pantuo.dao.pojo.JpaSupplies;

/**
 * 
 * 
 * <b><code>SuppliesRepository</code></b>
 * <p>
 * 默认不选择时绑定的特定素材
 * </p>
 * <b>Creation Time:</b> 2015年5月11日 下午11:17:21
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */

public interface SuppliesRepository extends JpaRepository<JpaSupplies, Integer>, QueryDslPredicateExecutor<JpaSupplies> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "insert into supplies (id,created,updated,city,supplies_type,duration) values(1,now(),now(),0,0,0)")
	public int insertDefaultSupplies();
}