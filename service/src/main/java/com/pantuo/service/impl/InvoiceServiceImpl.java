package com.pantuo.service.impl;

import java.security.Principal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.ContractRepository;
import com.pantuo.dao.InvoiceRepository;
import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.dao.pojo.QJpaInvoice;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.InvoiceServiceData;
import com.pantuo.util.Request;

@Service
public class InvoiceServiceImpl implements InvoiceServiceData {

	@Autowired
	ContractRepository contractRepo;
	@Autowired
	InvoiceRepository invoiceRepository;

	public Page<JpaInvoice> getAllInvoice(int city, TableRequest req, Principal principal) {
		String name = req.getFilter("title");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaInvoice.jpaInvoice.city.eq(city);
		if (StringUtils.isNotBlank(name)) {
				query = query.and(QJpaInvoice.jpaInvoice.title.like("%" + name + "%"));
		}
		query = query.and(QJpaInvoice.jpaInvoice.userId.eq(Request.getUserId(principal)));
		return invoiceRepository.findAll(query, p);
	}

	public Page<JpaInvoice> getValidInvoice(int city, int page, int pageSize, Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort(Sort.Direction.DESC, "id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		Predicate query = QJpaInvoice.jpaInvoice.city.eq(city);
		return invoiceRepository.findAll(query, p);
	}

}
