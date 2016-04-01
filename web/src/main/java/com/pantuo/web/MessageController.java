package com.pantuo.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.MessageService;
import com.pantuo.service.security.Request;
import com.pantuo.web.view.MessageView;

/**
 * 
 * message Controller

 *
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@RequestMapping("ajax-messagelist")
	@ResponseBody
	public DataTablePage<MessageView> getAllContracts(TableRequest req, Principal principal) {
		Page<MessageView> w = messageService.getValidMessage(req.getPage(), req.getLength(), principal);
		return new DataTablePage<MessageView>(w, req.getDraw());
	}

	@RequestMapping(value = "/all")
	public String list() {
		return "message/all";
	}
	/**
	 * 
	 * 显示未读条数
	 *
	 * @param model
	 * @param principal
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@RequestMapping(value = "/unread")
	@ResponseBody
	//@PreAuthorize("hasRole('advertiser')")
	//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public long unread(Model model, Principal principal) {
		
		//Authentication authentication = SecurityContextHolder.getContext()
         //       .getAuthentication();
		//System.out.println("Hello " + authentication);
		long c = messageService.getUnReadCount(Request.getUserId(principal));
		return c;
	}

}
