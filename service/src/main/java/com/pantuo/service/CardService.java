package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaCardBoxBody;
import com.pantuo.dao.pojo.JpaCardBoxHelper;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.mybatis.domain.BodyOrderLog;
import com.pantuo.mybatis.domain.CardboxBody;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.mybatis.domain.CardboxMedia;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.web.view.BodyProView;
import com.pantuo.web.view.CardBoxHelperView;
import com.pantuo.web.view.CardTotalView;
import com.pantuo.web.view.CardView;
import com.pantuo.web.view.MediaSurvey;
import com.pantuo.web.view.Offlinecontract;
import com.pantuo.web.view.UserQualifiView;

public interface CardService {

	/**
	 * 
	 * 查用户订单序号 有就直接取的 没有分配一个
	 * 查的序号放后放session 内 ,表单就不用传值了
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public long getCardBingSeriaNum(Principal principal);
	
	/**
	 * 暂时不需要实现
	 * 
	 * 判断序列号是否是某个用户的 在操作购物车时判断一下，有可能被伪造
	 *
	 * @param seriaNum
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean checkSeriaNumOwner(long seriaNum,Principal principal);

	/**
	 * 
	 * 先判断用户和商品是否有记录，如果没有增加如果有就加减计数
	 * 
	 *@param isadd false时表示删除该记录
	 * @param media
	 * @param principal
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> updateMedia(CardboxMedia media,boolean isadd, Principal principal,long seriaNum);

	/**
	 * 
	 * 先判断用户和商品是否有记录，如果没有增加如果有就加减计数
	 * 
	 *
	 * @param media
	 * @param principal
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> updateBody(CardboxBody media,boolean isadd, Principal principal,long seriaNum);
	/**
	 * 
	 * 最后一步增加发标 素材待信息
	 *
	 * @param helper
	 * @param principal
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void add(CardboxHelper helper, Principal principal);
	/**
	 * 
	 * 取的购物车总价
	 *
	 * @param seriaNum
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public CardTotalView getBoxPrice(long seriaNum,int iscomfirm, List<Integer> meLists, List<Integer> boLists);
	public double getBoxPrice( Principal principal);
	
	public CardTotalView getCarSumInfo( Principal principal);

	public CardTotalView saveCard(int proid,int needCount, Principal principal, int city, String type,int IsDesign);
	
	
	
	
	public CardView getMediaList(Principal principal, int isComfirm, String meids,String boids);

	public Pair<Boolean, String> delOneCarBox(String type, int id);

	public void confirmByids(Principal principal, String meids, String boids);
	
	public void test();
	  Page<JpaBusOrderDetailV2> searchProducts(int city, Principal principal,
	    		TableRequest req);

	public Pair<Boolean, Object> payment(HttpServletRequest request,String startdate1,String paytype,int isdiv, String divid, long seriaNum, Principal principal, int city, String meids, String boids, long runningNum);

	public void updateCardboxUser(long seriaNum, Principal principal);

	public JpaBusOrderDetailV2 getJpaBusOrderDetailV2Byid(int id);

	public Pair<Boolean, String> putIncar(int proid, int needCount, int days, Principal principal, int city,String startdate1, String type, HttpServletRequest request);

	public Pair<Boolean, String> buy(int proid, int needCount, int days, Principal principal, int city, String startdate1, String type, HttpServletRequest request);
	
	
	
/**
 * 
 * Comment here.
 *
 * @param city
 * @param principal
 * @param req
 * @return
 * @since pantuo 1.0-SNAPSHOT
 */
	  Page<CardBoxHelperView> myCards(int city, Principal principal,
	    		TableRequest req);

	public JpaProduct getJpaProductByid(int id);

	public Page<JpaCardBoxBody> queryCarBoxBody(int cityId, TableRequest req, int page, int length, Sort sort);

	public Page<JpaCardBoxMedia> queryCarBoxMedia(int cityId, TableRequest req, int page, int length, Sort sort);

	public JpaCardBoxHelper queryCarHelperyByid(int id);

	public Pair<Boolean, String> editCarHelper(CardboxHelper helper, String stas, String userId, String remarks);

	public MediaSurvey getJsonfromJsonStr(String jsonString);

	public UserQualifiView getUserQualifiView(String qulifijsonstr);

	public boolean updateCardMeida(String start, int mediaId,String isChangeOrder);

	public BodyProView getBodyProViewfromJsonStr(String jString);

	public List<BodyOrderLog>  getBodyOrderLog(Principal principal, TableRequest req);

	public Offlinecontract getContractfromJsonStr(String jsonStr);
	
	
	public boolean checkPayed(long runningNum, int orderId);

	UserQualifiView getUserQualifiViewfromJsonStr(String jsonString);
		
		
		

}
