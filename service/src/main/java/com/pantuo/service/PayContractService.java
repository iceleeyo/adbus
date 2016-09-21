package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaPayContract;
import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.mybatis.domain.PaycontractWithBLOBs;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.OrderView;

public interface PayContractService {
	/**
	 * 
	 * 查我的未支付订单
	 *
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<String> queryMyUnPayOrders(Principal principal, String customName);
	/**
	 * 
	 * 根据订单号查
	 *
	 * @param orderId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<JpaOrders> queryOrders(List<String> orderId);
	
	
	
	/**
	 * 
	 * 查是不是都属于同一个
	 *
	 * @param orderId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean checkSameCustom(List<String> orderId);
	public List<AutoCompleteView> OrderIdComplete(Principal principal, String name, HttpServletRequest request);
	public List<OrderView> showOrderDetail(String orderIds);
	Pair<Boolean, String> savePayContract(Principal principal, PaycontractWithBLOBs contract, HttpServletRequest request);
	public Page<JpaPayContract> getAllContracts(TableRequest req, Principal principal);
	public JpaPayContract getPayContractById(int id);
	public Pair<Boolean, String> delPayContract(Principal principal, int contractId);
	
	
	
	
	/**
	 * 
	 * 根据分期查合同信息
	 *
	 * @param planId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public JpaPayContract queryContractByPlanId(int planId);
	
	
	/**
	 * 
	 * 根据id查plan
	 *
	 * @param planId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public JpaPayPlan queryByPlanId(int planId);
	public Page<JpaPayContract> getAllNotPayContracts(TableRequest req, Principal principal);
	public String toRestPayContract(int contarctId, Model model, Principal principal);

}
