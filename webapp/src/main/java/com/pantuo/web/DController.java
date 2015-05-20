package com.pantuo.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.web.view.AutoCompleteView;

@Controller
@RequestMapping("/dts")
public class DController {

	@RequestMapping(value = "/test", produces = "text/html;charset=utf-8")
	public String proDetail(Model model, HttpServletRequest request) {
		return "autocomplete";
	}

	@RequestMapping(value = "/autoComplete")
	@ResponseBody
	public List<AutoCompleteView> red(Model model, HttpServletRequest request) {
		Enumeration pNames = request.getParameterNames();
		while (pNames.hasMoreElements()) {
			String name = (String) pNames.nextElement();
			String value = request.getParameter(name);
			System.out.println(name + "=" + value);
		}
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		r.add(new AutoCompleteView("aduserb", "aduserb"));
		r.add(new AutoCompleteView("aduserb", "aduserb"));
		r.add(new AutoCompleteView("adusera", "adusera"));
		r.add(new AutoCompleteView("4", "fbc"));
		r.add(new AutoCompleteView("5", "中国人民工林"));
		r.add(new AutoCompleteView("1", "abc"));
		r.add(new AutoCompleteView("2", "bc"));
		r.add(new AutoCompleteView("3", "ebc木木木木木111"));
		r.add(new AutoCompleteView("4", "fbc"));
		r.add(new AutoCompleteView("5", "gbc"));
		for (int i = 0; i < 15; i++) {
			r.add(new AutoCompleteView(System.currentTimeMillis()+"_"+i, System.currentTimeMillis()+"_"+i));
		}
		 
		System.out.println(r.size());
		
		return r;
	}

	@RequestMapping(value = "/dsgtsss", method = RequestMethod.GET)
	public String getMovie4(String q, HttpSession session, HttpServletResponse p) {

		StringBuilder sb = new StringBuilder();
		sb.append("{text:'apple', value:'100'}");
		sb.append("\n");
		sb.append("{text:'pear1', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear2', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear3', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear4', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear5', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear6', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear7', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear8', value:'200'}");
		sb.append("\n");
		sb.append("{text:'pear9', value:'200'}");

		try {
			p.getWriter().println(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//return sb.toString();
	}

}