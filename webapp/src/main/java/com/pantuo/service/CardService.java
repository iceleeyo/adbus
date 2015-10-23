package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.mybatis.domain.CardboxBody;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.mybatis.domain.CardboxMedia;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.web.view.CardView;

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
	public double getBoxPrice(long seriaNum,int iscomfirm, List<Integer> meLists, List<Integer> boLists);
	public double getBoxPrice( Principal principal);

	public Pair<Double, Integer> saveCard(int proid,int needCount, Principal principal, int city, String type,int IsDesign);
	
	
	
	
	public CardView getMediaList(Principal principal, int isComfirm, String meids,String boids);

	public Pair<Boolean, String> delOneCarBox(String type, int id);

	public void confirmByids(Principal principal, String meids, String boids);
	
	public void test();
	  Page<JpaBusOrderDetailV2> searchProducts(int city, Principal principal,
	    		TableRequest req);

	public Pair<Boolean, String> payment(String paytype, String divid, long seriaNum, Principal principal, int city, String meids, String boids);

	public void updateCardboxUser(long seriaNum, Principal principal);

	public JpaBusOrderDetailV2 getJpaBusOrderDetailV2Byid(int id);

	public Pair<Boolean, String> putIncar(int proid, int needCount, int days, Principal principal, int city, String type);

	public Pair<Boolean, String> buy(int proid, int needCount, int days, Principal principal, int city, String type);

	public JpaProduct getJpaProductByid(int id);
}
