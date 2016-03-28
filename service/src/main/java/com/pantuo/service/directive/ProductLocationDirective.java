package com.pantuo.service.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.ProductLocationRepository;
import com.pantuo.dao.ProductTagRepository;
import com.pantuo.dao.pojo.JpaProductTag;
import com.pantuo.dao.pojo.QJpaProductTag;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * <b><code>ProductLocationDirective</code></b>
 * <p>
 * 商品位置 自定义标签 
 * 
 * 	<@productLocation locationTag="hot_left_1" >
  			  <#if jpaProductTag??  >
           		 <a href="">${jpaProductTag.product.name}</a><br>
   			 </#if>
	</@productLocation>
 * 
 * 
 * 
 * </p>
 * <b>Creation Time:</b> 2016年3月28日 上午11:44:36
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */

@Service
public class ProductLocationDirective implements TemplateDirectiveModel {

	private static Logger log = LoggerFactory.getLogger(ProductLocationDirective.class);

	@Autowired
	ProductLocationRepository productLocationRepository;
	@Autowired
	ProductTagRepository productTagRepository;

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		String locationTag = params.get("locationTag").toString();
		JpaProductTag jpaProductTag = getOneLocation(locationTag);
		try {
			env.setVariable("jpaProductTag", BeansWrapper.DEFAULT_WRAPPER.wrap(jpaProductTag));
			body.render(env.getOut());
		} catch (TemplateModelException e) {
			log.error("ProductLocationDirective -ex", e);
		}
	}

	public JpaProductTag getOneLocation(String locationTag) {
		BooleanExpression query = QJpaProductTag.jpaProductTag.productLocation.locationTag.eq(locationTag);
		//add else query expression
		Iterable<JpaProductTag> tag = productTagRepository.findAll(query);
		return tag.iterator().hasNext() ? tag.iterator().next() : null;
	}
}
