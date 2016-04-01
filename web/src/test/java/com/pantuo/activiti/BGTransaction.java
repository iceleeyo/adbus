package com.pantuo.activiti;



import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.util.logging.Logger;
public class BGTransaction implements JavaDelegate {
	
	private static final Logger log = Logger.getLogger(BGTransaction.class.getName());
	static int i=0;
	//@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info(execution.getVariables()+"");
		// varInSubprocess<->varOutFromSubprocess
		String varOutFromSubprocess = (String)execution.getVariable("varOutFromSubprocess");
		log.info("in mainprocess get(varOutFromSubprocess): " + varOutFromSubprocess);
		
		log.info("variavles=" + execution.getVariables());
		execution.setVariable("reson2", "这是是北广，终审通过");
		if(i==1)
		execution.setVariable("deptLeaderPass", true);
		else
			execution.setVariable("deptLeaderPass", false);
		
		if((i++)>=2) {
			execution.setVariable("deptLeaderPass", true);
		}
		log.info("终审通过");
	}
}