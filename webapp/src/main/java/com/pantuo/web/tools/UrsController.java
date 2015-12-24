package com.pantuo.web.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import nl.justobjects.pushlet.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.pantuo.RestLoggingInterceptor;

/**
 * 
 * <b><code>UrsController</code></b>
 * <p>
 * 能显示工程中所有url地址小功能 但是无法知道一个url对应的页面 
 * 需要动态运行时才能知道返回到哪个页面
 * </p>
 * <b>Creation Time:</b> 2015年12月15日 上午10:32:11
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Controller
public class UrsController {
	private static final Logger log = LoggerFactory.getLogger(UrsController.class);
	@RequestMapping(value = "/urs", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model) {
		ServletContext servletContext = request.getSession().getServletContext();
		if (servletContext == null) {
			return null;
		}
		WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

		//请求url和处理方法的映射
		List<UrsView> requestToMethodItemList = new ArrayList<UrsView>();
		//获取所有的RequestMapping
		Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext,

		HandlerMapping.class, true, false);

		for (HandlerMapping handlerMapping : allRequestMappings.values()) {
			//本项目只需要RequestMappingHandlerMapping中的URL映射
			if (handlerMapping instanceof RequestMappingHandlerMapping) {
				RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
				Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping
						.getHandlerMethods();
				for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods
						.entrySet()) {
					RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
					HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

					RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();
					Set<RequestMethod> s = methodCondition.getMethods();
					String requestType = null;//SetUtils.first().name();

					PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
					Set<String> t = patternsCondition.getPatterns();

					//String requestUrl = null;// SetUtils.first(patternsCondition.getPatterns());
					String controllerName = mappingInfoValue.getBeanType().getSimpleName();
					String requestMethodName = mappingInfoValue.getMethod().getName();
					Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();

					String key = controllerName + requestMethodName;
					UrsView item = new UrsView(t.toString(), requestType, controllerName, requestMethodName,
							RestLoggingInterceptor.URL_VIEWMAP.get(key), methodParamTypes);

					requestToMethodItemList.add(item);
				}
				break;
			}
		}
		log.info("requestToMethodItemList.size:{}",requestToMethodItemList.size());
		model.addAttribute("methodList", requestToMethodItemList);
		return "/urs/list";
	}
}
