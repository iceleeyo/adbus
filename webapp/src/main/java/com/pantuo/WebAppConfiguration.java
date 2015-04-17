package com.pantuo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.pantuo.service.DataInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
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
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import freemarker.core.Configurable;

@Configuration
@ImportResource("classpath:/properties.xml")
@ComponentScan(basePackages = { "com.pantuo.web", "com.pantuo.service", "com.pantuo.aspect" })
@EnableAspectJAutoProxy
@EnableWebMvc
public class WebAppConfiguration extends WebMvcConfigurerAdapter {
	@Bean
	public HandlerExceptionResolver webExceptionResolver() {
		return new RestExceptionResolver();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		ResourceHandlerRegistration registration = registry.addResourceHandler("/*.html", "/js/**", "/style/**",
				"/css/**", "/images/**", "/imgs/**", "/doc/**","/upload_temp/**");
		registration.addResourceLocations("/", "/js/", "/style/", "/css/", "/images/", "/imgs/", "/doc/","/upload_temp/");


		
		
		ResourceHandlerRegistration rdup = registry.addResourceHandler("/*.html", "**/js/**", "**/style/**",
				"**/css/**", "**/images/**", "**/imgs/**", "/doc/**","**/upload_temp/**");
		rdup.addResourceLocations("/", "/js/", "/style/", "/css/", "/images/", "/imgs/", "/doc/","/upload_temp/");
	}

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();

		HandlerInterceptor[] interceptors = new HandlerInterceptor[1];
		interceptors[0] = new RestLoggingInterceptor();

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
		return adapter;
	}

	@Bean
	public FreeMarkerConfigurer freeMarker() {
		FreeMarkerConfigurer config = new FreeMarkerConfigurer();
		config.setTemplateLoaderPath("/WEB-INF/ftl/");

		Properties setting = new Properties();
		setting.setProperty(Configurable.DATETIME_FORMAT_KEY, "yyyy-MM-dd");
		setting.setProperty(Configurable.NUMBER_FORMAT_KEY, "0.##");
		config.setFreemarkerSettings(setting);
		config.setDefaultEncoding("UTF-8");

		//        Map<String, Object> myBuiltInMethods = new HashMap<String, Object>();
		//        myBuiltInMethods.put("MyNumberftl", new Numberftl());
		//        config.setFreemarkerVariables(myBuiltInMethods);
		return config;
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
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}
}
