package com.pantuo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.NgTableAttributeArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pantuo.service.directive.ProductLocationDirective;
import com.pantuo.spring.SpringContextHolder;
import com.pantuo.util.ChangeMoney;
import com.pantuo.util.FreemarkerExceptionHandler;
import com.pantuo.util.HiddleUserNameEx;
import com.pantuo.util.SubstringEx;
import com.pantuo.web.ControllerInterceptor;
import com.pantuo.web.upload.CustomMultipartResolver;

import freemarker.core.Configurable;
import freemarker.template.TemplateMethodModelEx;

@Configuration
@ImportResource("classpath:/properties.xml")
@ComponentScan(basePackages = { "com.pantuo.web", "com.pantuo.service", "com.pantuo.aspect" })
//@EnableAspectJAutoProxy
@EnableWebMvc
public class WebAppConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	private ControllerInterceptor controllerInterceptor;

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(webExceptionResolver());
	}

	@Autowired
	ProductLocationDirective productLocationDirective;

	//    @Bean
	public HandlerExceptionResolver webExceptionResolver() {
		return new RestExceptionResolver();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		ResourceHandlerRegistration registration = registry.addResourceHandler("/*.html", "/js/**", "/style/**", "/css/**", "/images/**", "/imgs/**", "/doc/**", "/upload_temp/**");
		registration.addResourceLocations("/", "/js/", "/style/", "/css/", "/images/", "/imgs/", "/doc/", "/upload_temp/");

		ResourceHandlerRegistration rdup = registry.addResourceHandler("/homepage/**");
		rdup.addResourceLocations("/homepage/");
	}

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();

		List<HandlerInterceptor> list = new ArrayList<HandlerInterceptor>();
		list.add(new RestLoggingInterceptor());
		list.add(new OpenApiInterceptor());
		list.add(controllerInterceptor);
		HandlerInterceptor[] interceptors = list.toArray(new HandlerInterceptor[0]);

		mapping.setInterceptors(interceptors);
		return mapping;
	}

	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();

		/** json converter */
		{
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

			//set return content type
			List<MediaType> mediaTypes = new ArrayList<MediaType>();
			mediaTypes.add(new MediaType("application", "json", Charset.forName("UTF-8")));
			converter.setSupportedMediaTypes(mediaTypes);

			//do not output keys with null values
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			converter.setObjectMapper(mapper);

			converters.add(converter);
		}
		//http://stackoverflow.com/questions/12505141/only-using-jsonignore-during-serialization-but-not-deserialization
		/** xml converter */
		{
			Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();

			//set return content type
			List<MediaType> mediaTypes = new ArrayList<MediaType>();
			mediaTypes.add(new MediaType("application", "xml", Charset.forName("UTF-8")));
			//mediaTypes.add(new MediaType("text","html", Charset.forName("UTF-8")));
			converter.setSupportedMediaTypes(mediaTypes);

			converters.add(converter);
		}
		RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
		adapter.setMessageConverters(converters);

		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		argumentResolvers.add(new NgTableAttributeArgumentResolver());
		adapter.setCustomArgumentResolvers(argumentResolvers);
		////默认自动注册对@NumberFormat和@DateTimeFormat的支持  
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		initializer.setConversionService(defaultSonvers());
		adapter.setWebBindingInitializer(initializer);
		return adapter;
	}

	public DefaultFormattingConversionService defaultSonvers() {
		return new DefaultFormattingConversionService();
	}

	@Bean
	public FreeMarkerConfigurer freeMarker() {
		FreeMarkerConfigurer config = new FreeMarkerConfigurer();
		config.setTemplateLoaderPath("/WEB-INF/ftl/");

		Properties setting = new Properties();
		setting.setProperty(Configurable.DATETIME_FORMAT_KEY, "yyyy-MM-dd");
		setting.setProperty(Configurable.NUMBER_FORMAT_KEY, "0.##");
		//setting.setProperty(Configurable.TEMPLATE_EXCEPTION_HANDLER_KEY, "RETHROW");
		setting.setProperty(Configurable.TEMPLATE_EXCEPTION_HANDLER_KEY, FreemarkerExceptionHandler.class.getName());
		config.setFreemarkerSettings(setting);
		config.setDefaultEncoding("UTF-8");

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("substring", getSubStrMethod());
		variables.put("changeMoney", getChangeMoney());
		variables.put("hidname", getvHiddleUserNameEx());
		variables.put("productLocation", productLocationDirective);//商品位置 标签根据位置取相应的商品信息

		config.setFreemarkerVariables(variables);

		//        Map<String, Object> myBuiltInMethods = new HashMap<String, Object>();
		//        myBuiltInMethods.put("MyNumberftl", new Numberftl());
		//        config.setFreemarkerVariables(myBuiltInMethods);
		return config;
	}

	TemplateMethodModelEx getSubStrMethod() {
		return new SubstringEx();
	}
	TemplateMethodModelEx getChangeMoney() {
		return new ChangeMoney();
	}

	TemplateMethodModelEx getvHiddleUserNameEx() {
		return new HiddleUserNameEx();
	}

	@Bean
	public ViewResolver freeMarkerViewResolver() throws IOException {
		FreeMarkerViewResolver freeMarker = new FreeMarkerViewResolver();
		freeMarker.setContentType("text/html;charset=UTF-8");
		freeMarker.setExposeRequestAttributes(true);
		freeMarker.setRequestContextAttribute("rc");
		freeMarker.setExposeSpringMacroHelpers(true);
		freeMarker.setOrder(1);
		freeMarker.setSuffix(".ftl");

		//        Properties pageResource = new Properties();
		//        InputStream in = HttpConfig.class.getResourceAsStream("/pageresource.properties");
		//        pageResource.load(in);
		//        pageResource.setProperty("OA_VER", OA_VER);
		//        pageResource.setProperty("timeZone", timeZone);

		//        freeMarker.setAttributes(pageResource);
		return freeMarker;
	}

	/**
	 * 
	 * 文件上传
	 *
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CustomMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		resolver.setResolveLazily(true);
		//	resolver.setMaxUploadSize(20971520);
		return resolver;
	}

	@Bean
	@Lazy
	public SpringContextHolder getHolder() {
		return new SpringContextHolder();
	}
}
