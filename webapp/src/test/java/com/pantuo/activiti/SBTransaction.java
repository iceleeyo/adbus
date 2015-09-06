package com.pantuo.activiti;



import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.util.logging.Logger;
public class SBTransaction implements JavaDelegate {
	
	private static final Logger log = Logger.getLogger(SBTransaction.class.getName());
	static int i=0;
	//@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info(execution.getVariables()+"");
		// varInSubprocess<->varOutFromSubprocess
		String varOutFromSubprocess = (String)execution.getVariable("varOutFromSubprocess");
		log.info("in mainprocess get(varOutFromSubprocess): " + varOutFromSubprocess);
		log.info("variavles=" + execution.getVariables());
		execution.setVariable("reson", "这里是世巴,审核通过");
		if(i==0)
		execution.setVariable("fristpass", true);
		else
			execution.setVariable("fristpass", false);
		if((i++)>=2) {
			execution.setVariable("fristpass", true);
		}
		log.info("初审通过");
	}
}