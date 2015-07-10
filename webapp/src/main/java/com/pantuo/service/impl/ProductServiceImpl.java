package com.pantuo.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.dao.pojo.QJpaProduct;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.ProductExample;
import com.pantuo.mybatis.persistence.ProductMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ProductService;
import com.pantuo.simulate.ProductProcessCount;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.ProductOrderCount;
import com.pantuo.util.Request;
import com.pantuo.web.view.ProductView;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductMapper productMapper;

	@Autowired
	ProductRepository productRepo;

	public Page<JpaProduct> getAllProducts(int city, boolean includeExclusive, String exclusiveUser, TableRequest req) {
		String name = req.getFilter("name");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		if (!includeExclusive) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false));
		} else if (exclusiveUser != null) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false).or(
					QJpaProduct.jpaProduct.exclusiveUser.eq(exclusiveUser)));
		}
		if (name != null && !StringUtils.isEmpty(name)) {
			query = query.and(QJpaProduct.jpaProduct.name.like("%" + name + "%"));
		}
		query = query.and(QJpaProduct.jpaProduct.iscompare.eq(0));
		return productRepo.findAll(query, p);
	}

	BooleanExpression getQueryFromPage(String name, String sh) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		BooleanExpression query = null;
		String[] shSplit = StringUtils.split(sh, ",");
		if (shSplit != null) {
			for (String string : shSplit) {//p1
				String[] one = StringUtils.split(string, "_");
				String field = one[0];
				String v = one[1];
				if (!map.containsKey(field)) {
					map.put(field, new ArrayList<String>());
				}
				if (!StringUtils.equals("all", v)) {
					map.get(field).add(v);
				}
			}
		}
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			List<String> vIntegers = entry.getValue();
			if (StringUtils.equals(entry.getKey(), "t") && vIntegers.size() > 0) {
				List<JpaProduct.Type> right = new ArrayList<JpaProduct.Type>();
				for (String type : vIntegers) {
					right.add(JpaProduct.Type.valueOf(type));
				}
				query = query == null ? QJpaProduct.jpaProduct.type.in(right) : query.and(QJpaProduct.jpaProduct.type
						.in(right));
			} else if (StringUtils.equals(entry.getKey(), "p") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String playNumber : vIntegers) {
					if (StringUtils.equals("2", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.iscompare.eq(1) : subQuery
								.or(QJpaProduct.jpaProduct.iscompare.eq(1));
					} else if (StringUtils.equals("3", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.iscompare.eq(0) : subQuery
								.or(QJpaProduct.jpaProduct.iscompare.eq(0));
					}
				}
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "s") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String playNumber : vIntegers) {
					if (StringUtils.equals("2", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.playNumber.between(0, 6) : subQuery
								.or(QJpaProduct.jpaProduct.playNumber.between(0, 6));
					} else if (StringUtils.equals("3", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.playNumber.between(7, 11) : subQuery
								.or(QJpaProduct.jpaProduct.playNumber.between(7, 11));
					} else if (StringUtils.equals("4", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.playNumber.gt(11) : subQuery
								.or(QJpaProduct.jpaProduct.playNumber.gt(11));
					}
				}
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "d") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String playNumber : vIntegers) {

					if (StringUtils.equals("2", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.days.eq(1) : subQuery
								.or(QJpaProduct.jpaProduct.days.eq(1));
					} else if (StringUtils.equals("3", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.days.between(2, 6) : subQuery
								.or(QJpaProduct.jpaProduct.days.between(2, 6));
					} else if (StringUtils.equals("4", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.days.eq(7) : subQuery
								.or(QJpaProduct.jpaProduct.days.eq(7));

					} else if (StringUtils.equals("5", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.days.gt(7) : subQuery
								.or(QJpaProduct.jpaProduct.days.gt(7));
					}
				}
				query = query == null ? subQuery : query.and(subQuery);
			}

		}

		return query;

	}

	public Page<JpaProduct> searchProducts(int city, Principal principal, TableRequest req) {
		String name = req.getFilter("name");
		String sh = req.getFilter("sh");
		BooleanExpression commonEx = getQueryFromPage(name, sh);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		if (principal == null || Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false));
		}
		if (commonEx != null) {
			query = query.and(commonEx);
		}
		if (name != null && !StringUtils.isEmpty(name)) {
			query = query.and(QJpaProduct.jpaProduct.name.like("%" + name + "%"));
		}

		return productRepo.findAll(query, p);
	}

	//  @Override
	public Page<JpaProduct> getValidProducts(int city, JpaProduct.Type type, boolean includeExclusive,
			String exclusiveUser, TableRequest req, FrontShow... fs) {
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort(Sort.Direction.DESC, "id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		if (!includeExclusive) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false));
		} else if (exclusiveUser != null) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false).or(
					QJpaProduct.jpaProduct.exclusiveUser.eq(exclusiveUser)));
		}
		if (type != null) {
			query = query.and(QJpaProduct.jpaProduct.type.eq(type));
		}
		if (fs != null && fs.length > 0) {
			query = query.and(QJpaProduct.jpaProduct.frontShow.eq(fs[0]));
		}
		query = query.and(QJpaProduct.jpaProduct.iscompare.eq(0));
		query = query.and(QJpaProduct.jpaProduct.enabled.isTrue());
		return productRepo.findAll(query, p);
	}

	//  @Override
	public JpaProduct findById(int productId) {
		return productRepo.findOne(productId);
	}

	//  @Override
	public void saveProduct(int city, JpaProduct product) {
		product.setCity(city);
		com.pantuo.util.BeanUtils.filterXss(product);
		product.setExclusiveUser(product.getExclusiveUser());
		productRepo.save(product);
	}

	public int countMyList(int city, String name, String code, HttpServletRequest request) {
		return productMapper.countByExample(getExample(city, name, code));
	}

	public ProductExample getExample(int city, String name, String code) {
		ProductExample example = new ProductExample();
		ProductExample.Criteria ca = example.createCriteria();
		ca.andCityEqualTo(city);
		if (StringUtils.isNoneBlank(name)) {
			//	ca.andContractNameLike("%" + name + "%");
		}
		if (StringUtils.isNoneBlank(code) && Long.parseLong(code) > 0) {
			//ca.andContractNumEqualTo(Long.parseLong(code));
		}
		return example;
	}

	public List<Product> queryContractList(int city, NumberPageUtil page, String name, String code,
			HttpServletRequest request) {
		ProductExample ex = getExample(city, name, code);
		ex.setOrderByClause("id desc");
		ex.setLimitStart(page.getLimitStart());
		ex.setLimitEnd(page.getPagesize());
		return productMapper.selectByExample(ex);
	}

	public Product selectProById(Integer productId) {
		return productMapper.selectByPrimaryKey(productId);
	}

	@Override
	public Page<ProductView> getProductView(Page<JpaProduct> list) {

		List<ProductView> plist = new ArrayList<ProductView>();
		if (list != null) {
			for (JpaProduct jpaProduct : list) {
				ProductView w = new ProductView();
				BeanUtils.copyProperties(jpaProduct, w);
				plist.add(w);
				ProductOrderCount c = ProductProcessCount.map.get(jpaProduct.getId());
				if (c != null) {
					w.setRunningCount(c.getRunningCount());
					w.setFinishedCount(c.getFinishedCount());
				}
			}
		}
		Pageable p = new PageRequest(list.getNumber(), list.getSize(), list.getSort());
		org.springframework.data.domain.PageImpl<ProductView> r = new org.springframework.data.domain.PageImpl<ProductView>(
				plist, p, list.getTotalElements());
		return r;
	}
}
