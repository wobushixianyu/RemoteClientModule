package com.oray.sunlogin.jni;

/**
 * Created by shikh on 2017/2/14.
 */

public interface IAcceptorListener {
    /**
     * 连接状态, 未知状态
     */
    public final static int STATUS_UNKNOWN = 0;

    /**
     * 连接状态, 登录成功
     */
    public final static int STATUS_LOGIN_OK = 1;

    /**
     * 连接状态, 登录失败
     */
    public final static int STATUS_LOGIN_FAIL = 2;

    /**
     * 连接状态, 已连接成功
     */
    public final static int STATUS_CONNECT = 3;

    /**
     * 连接状态, 已断开连接
     */
    public final static int STATUS_DISCONNECT = 4;

    /**
     * 连接状态, 插件连接成功
     */
    public final static int STATUS_PLUGIN_CONNECT = 5;

    /**
     * 连接状态, 插件连接断开
     */
    public final static int STATUS_PLUGIN_DISCONNECT = 6;

    /**
     * 无效的参数
     */
    public final static int ERROR_INVALID_ARG        = 21;
    /**
     * 无效的授权
     */
    public final static int ERROR_INVALID_LICENSE    = 22;
    /**
     * 无效的服务器地址
     */
    public final static int ERROR_INVALID_SERVERADDR = 23;
    /**
     * 无效的协议
     */
    public final static int ERROR_INVALID_PROTOCOL   = 24;
    /**
     * 授权已经过期
     */
    public final static int ERROR_LICENSE_EXPIRE     = 25;
    /**
     * appid/appkey验证失败
     */
    public final static int ERROR_VERIFY_FAIL        = 26;
    /**
     * 登录失败
     */
    public final static int ERROR_LOGIN_FAIL         = 27;

    /**
     * 当连接状态发生变化时回调(包括连接上).
     * @param status 连接状态码, 参考此类定义的常量
     * @param error 错误代码, 参考协议文档
     */
    public abstract void onStatusChanged(int status, int error);
}
