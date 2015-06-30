package com.pantuo;


import com.pantuo.dao.DaoBeanConfiguration;
import com.pantuo.service.DataInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * This class will be loaded by a servlet 3.0 container automatically.
 *
 * @see org.springframework.web.WebApplicationInitializer
 */
public class SpringWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected Class[] getRootConfigClasses() {
        return new Class[] { InitializationConfiguration.class, DaoBeanConfiguration.class, ActivitiConfiguration.class, WebAppConfiguration.class, SecurityConfiguration.class, CacheConfiguration.class, SchedulerConfiguration.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[0];
    }

/*    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(servletContext);
        context.register(DaoBeanConfiguration.class, WebAppConfiguration.class, SecurityConfiguration.class, CacheConfiguration.class, SchedulerConfiguration.class);
        context.setConfigLocation("/WEB-INF/servlet-context.xml");
        context.refresh();
        String dispatcherName = StringUtils.uncapitalize(DispatcherServlet.class.getSimpleName());
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(dispatcherName, new DispatcherServlet(context));
        dynamic.setLoadOnStartup(1);
        dynamic.addMapping("/");
    }*/

}
