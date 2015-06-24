package com.pantuo.web;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.SuppliesServiceData;
import com.pantuo.util.FileHelper;
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.Pair;
import com.pantuo.web.view.SuppliesView;

@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/supplies")
public class SuppliesController {

	@Autowired
	IndustryRepository industryRepo;

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String r(Model model) {
		model.addAttribute("industries", industryRepo.findAll());
		return "supplies_add";
	}
	@RequestMapping(value = "getIndustry")
	@ResponseBody
	public List<Industry> getIndustry(Supplies obj, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city, HttpServletRequest request)
			throws IllegalStateException, IOException {
		return suppliesService.getIndustry();
	}
	@Autowired
	SuppliesService suppliesService;
	@Autowired
	SuppliesServiceData suppliesDataService;

	@RequestMapping(value = "/u2", produces = "text/html;charset=utf-8")
	public String u2(Model model, HttpServletRequest request) {
		return "u2";
	}

	@RequestMapping(value = "u2_save", method = RequestMethod.POST)
	@ResponseBody
	public void u2_save(HttpServletRequest request) throws IllegalStateException, IOException, ParseException {
		try {
			System.out.println(request.getParameter("dos_authorize_token"));
			//request.("Connection", "close");
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
					.getServletContext());
			if (multipartResolver.isMultipart(request)) {
				String path = request.getSession().getServletContext()
						.getRealPath(com.pantuo.util.Constants.FILE_UPLOAD_DIR).replaceAll("WEB-INF", "");
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null && !file.isEmpty()) {
						String oriFileName = file.getOriginalFilename();
						if (StringUtils.isNoneBlank(oriFileName)) {
							String storeName = GlobalMethods.md5Encrypted((System.currentTimeMillis() + oriFileName)
									.getBytes());
							Pair<String, String> p = FileHelper.getUploadFileName(path,
									storeName += FileHelper.getFileExtension(oriFileName, true));
							File localFile = new File(p.getLeft());

							// 不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉  
							FileUtils.copyInputStreamToFile(file.getInputStream(), localFile);

							/*long t=0;
							InputStream is = file.getInputStream();
							byte[] bs = new byte[1024];
							int len;
							OutputStream os = new FileOutputStream(p.getLeft());
							while ((len = is.read(bs)) != -1) {
								t+=len;
								os.write(bs, 0, len);
							}
							System.out.println(t);
							os.close();
							is.close();*/
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "put", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> put(Supplies obj, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city, HttpServletRequest request)
			throws IllegalStateException, IOException {
		return suppliesService.addSupplies(city, obj, principal, request);
	}
	@RequestMapping(value = "getMysupplies")
	@ResponseBody
	public List<Supplies> getMysupplies(Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city, HttpServletRequest request)
					throws IllegalStateException, IOException {
		return suppliesService.querySuppliesByUser(city, principal);
	}

	@RequestMapping(value = "/list")
	public String list(Model model) {
		model.addAttribute("industries", industryRepo.findAll());
		return "supplies_list";
	}

	@RequestMapping(value = "/suppliesDetail/{supplies_id}", produces = "text/html;charset=utf-8")
	public String suppliesDetail(Model model, @PathVariable("supplies_id") int supplies_id, Principal principal,
			HttpServletRequest request) {
		//    	int supplies_id=Integer.parseInt(request.getParameter("supplies_id"));
		SuppliesView view = suppliesService.getSuppliesDetail(supplies_id, principal);
		model.addAttribute("view", view);
		return "suppliesDetail";
	}

	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<JpaSupplies> getAllContracts(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		return new DataTablePage(suppliesDataService.getAllSupplies(city, principal, req), req.getDraw());
	}
	@RequestMapping(value = "/delSupp/{Suppid}")
	@ResponseBody
	public Pair<Boolean, String> delSupp(Model model,
			@PathVariable("Suppid") int Suppid, Principal principal,
				HttpServletRequest request) {
		return suppliesService.delSupp(Suppid,principal);
	}
}
