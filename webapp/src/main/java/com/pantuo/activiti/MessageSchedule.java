package com.pantuo.activiti;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.activiti.engine.impl.el.Expression;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.ActivitiService;
import com.pantuo.util.DateConverter;
import com.pantuo.util.DateUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * <b><code>MessageSchedule</code></b>
 * <p>
 * 消息通知box
 * </p>
 * <b>Creation Time:</b> 2015年5月9日 下午1:27:35
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
@Service("messageBox")
public class MessageSchedule extends MailActivityBehavior {
	/**
	 * Comment here.
	 *
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	private static final long serialVersionUID = -1730171436107510776L;

	private static final Logger log = LoggerFactory.getLogger(MessageSchedule.class);

	@Autowired
	FreeMarkerConfigurer freeMarker;
	public Expression actionType;
	public Expression sendMail;
	public Expression sendSiteMsg;
	public Expression sendPhoneMsg;

	public enum ActionType {
		payFail, pass, deny,jianbo,shangbo,effective;
	}

	//@Override
	public void execute(ActivityExecution execution) {
		UserDetail owner = (UserDetail) execution.getVariable(ActivitiService.OWNER);
		int orderId = (Integer) execution.getVariable(ActivitiService.ORDER_ID);
		String mtype = (String) actionType.getValue(execution);

		/**
		 * 发送站内消息
		 */
		if (org.apache.commons.lang3.BooleanUtils.toBoolean((String) sendSiteMsg.getValue(execution))) {

		}

		/**
		 * 发送短信通知
		 */
		if (org.apache.commons.lang3.BooleanUtils.toBoolean((String) sendPhoneMsg.getValue(execution))) {

		}

		/**
		 * 发送邮件
		 */
		if (org.apache.commons.lang3.BooleanUtils.toBoolean((String) sendMail.getValue(execution))) {
			super.from = new FixedValue(execution.getVariable("_theEmail"));
			super.to = new FixedValue(owner.getUser().getEmail());
			super.charset = new FixedValue("utf-8");
			Map<String, String> context = new HashMap<String, String>();
			if (ActionType.payFail.name().equals(mtype)) {
				super.subject = new FixedValue("您的订单支付失败");
			} else if (ActionType.pass.name().equals(mtype)) {
				super.subject = new FixedValue("您的订单已通过审核");
			} else if (ActionType.deny.name().equals(mtype)) {
				super.subject = new FixedValue("您的订单需要修改");
			}else if (ActionType.jianbo.name().equals(mtype)) {
				super.subject = new FixedValue("您的广告已经播出完成");
			}else if (ActionType.shangbo.name().equals(mtype)) {
				super.subject = new FixedValue("您的广告订单已经上播");
			}else if (ActionType.effective.name().equals(mtype)) {
				super.subject = new FixedValue("您的广告投放订单已经生效");
			} 


		
			context.put("firstName", owner.getUser().getFirstName());
			context.put("lastName", owner.getUser().getLastName());
			context.put("_now", DateConverter.doConvertToString(System.currentTimeMillis(),
					DateConverter.DATETIME_PATTERN_NO_SECOND));
			context.put("_theCompany", (String) execution.getVariable("_theCompany"));
			context.put("approve1Comments", (String) execution.getVariable("approve1Comments"));
			String mailContext = getMailContext(context, mtype);
			super.html = new FixedValue(mailContext);
			//send mail
			if (StringUtils.isNoneBlank(mailContext)) {
				super.execute(execution);
			}
		}

	}

	/**
	 * 
	 * 读取邮件内容 
	 *
	 * @param context
	 * @param ftlName
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public String getMailContext(Map<String, String> context, String ftlName) {
		Configuration cfg = freeMarker.getConfiguration();
		StringWriter swriter = new StringWriter();
		try {
			Template t = cfg.getTemplate("mail_templete/" + ftlName + ".ftl");
			t.process(context, swriter);
		} catch (Exception e) {
			log.error("getMailInfo-error", e);
		}
		return swriter.toString();
	}
}