package com.pantuo.web;

import com.blogspot.nurkiewicz.springwadl.WadlGenerator;
import net.java.dev.wadl.WadlApplication;
import net.java.dev.wadl.WadlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * WADL generator
 *
 * @author tliu
 */
@Controller
@RequestMapping(method={RequestMethod.GET}, produces = "application/xml;charset=utf-8")
public class WadlController {
    @Autowired
    private RequestMappingHandlerMapping mapping;

    @ResponseBody
    @RequestMapping(value = "/wadl")
    public WadlApplication generate(HttpServletRequest request)
    {
        WadlApplication app = new WadlGenerator(this.mapping.getHandlerMethods(), request.getRequestURL().toString())
                .generate();
        app.getDoc().clear();
        app.withDoc(new WadlDoc().withTitle("公交广告交易系统接口"));
        return app;
    }
}
