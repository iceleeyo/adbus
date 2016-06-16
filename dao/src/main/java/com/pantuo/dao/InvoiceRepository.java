package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.dao.pojo.JpaSupplies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface InvoiceRepository extends JpaRepository<JpaInvoice, Integer>, QueryDslPredicateExecutor<JpaInvoice> {
}