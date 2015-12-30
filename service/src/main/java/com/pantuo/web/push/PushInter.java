package com.pantuo.web.push;
/**
 * 
 * <b><code>PushInter</code></b>
 * <p>
 * web推送接口
 * </p>
 * <b>Creation Time:</b> 2015年12月16日 下午3:37:40
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public interface PushInter {
	public void pushMsgToClient(String msg);

	/**
	 * 
	 * 告诉客户端结束 
	 *
	 * @param result
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void endAndClosePush(Object result);

	//--------------------------------------------------------

	public static PushInter DONOTHING = new DoNothingImpl();

	static class DoNothingImpl implements PushInter {
		public void pushMsgToClient(String msg) {
		}

		public void endAndClosePush(Object result) {
		}
	}
}
