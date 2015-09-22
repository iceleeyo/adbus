package com.pantuo.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusline;

/**
 * @author tliu
 */

public interface BusRepository extends JpaSpecificationExecutor<JpaBus>, JpaRepository<JpaBus, Integer>,
		QueryDslPredicateExecutor<JpaBus> {
	long countByCityAndEnabled(int city, boolean enabled);

/*	@Query(" select w from (select b from JpaBus b left JOIN  b.line t where   b.city=:city and b.company.id=:company"
			+ " and  t.level=:leval and b.category=:category ) u left join JpaBusOnline w on w.jpabus.id = u.id ")
	Page<JpaBus> queryBus(@Param("city") int city, @Param("company") int company, @Param("leval")  JpaBusline.Level leval,
			@Param("category") JpaBus.Category category, Pageable pageable);*/
}