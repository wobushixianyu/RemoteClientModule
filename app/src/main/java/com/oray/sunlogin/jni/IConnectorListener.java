package com.oray.sunlogin.jni;

/**
 * 定义与远程服务器连接过程监听器
 * @author lity
 */
public interface IConnectorListener {
	/**
	 * 连接状态, p2p连接
	 */
	public final static int P2P = 0;
	
	/**
	 * 连接状态, 数据转发连接
	 */
	public final static int FORWARD = 1;
	
	/**
	 * 连接状态, 登录主机
	 */
	public final static int LOGIN = 2;
	
	/**
	 * 连接状态, 已连接, 可以发送接收数据
	 */
	public final static int CONNECTED = 3;
	
	/**
	 * 连接状态, 已断开连接, 不能发送接收数据
	 */
	public final static int DISCONNECTED = 4;

	/**
	 * 连接状态, 仅限桌面插件
	 */
	public final static int FIRSTFRAME = 5;
	
	/**
	 * 连接状态, p2p连接成功
	 */
	public final static int P2P_CONNECTED = 6;
	/**
	 * 连接状态, P2P握手中
	 */
	public final static int P2P_SHAKEHANDLE = 7;
	
	/**
	 * 连接状态, 转发连接成功
	 */
	public final static int FWD_CONNECTED = 8;
	
	/**
	 * 连接状态, 转发握手中
	 */
	public final static int FWD_SHAKEHANDLE = 9;
	
	/**
	 * 连接状态, 转发断开连接
	 */
	public final static int FWD_DISCONNECT = 10;
	
    /**
     * 当连接状态发生变化时回调(包括连接上).
     * @param status 连接状态码, 参考此类定义的常量
     * @param error 错误代码, 参考协议文档
     */
    public abstract void onStatusChanged(int status, int error);
}
