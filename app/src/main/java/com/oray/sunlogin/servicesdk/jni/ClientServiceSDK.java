package com.oray.sunlogin.servicesdk.jni;

import android.os.Message;
import android.util.Log;

import com.oray.sunlogin.jni.IAcceptorListener;
import com.oray.sunlogin.jni.JavaCxxManager;
import com.oray.sunlogin.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by shikh on 2017/2/13.
 */

public class ClientServiceSDK extends JavaCxxManager {

    private static final String LOG_TAG = "AndroidSunlogin";
    /**
     * 消息ID, 连接事件
     */
    private static final int MSG_ID_CONNECTEVENT = 1000;


    private ArrayList<IAcceptorListener> mConnectorListeners;

    /**
     * 添加连接过程监听器
     * @param listener　监听器
     * @return　添加成功　true, 否则 null == listener
     */
    public boolean addConnectorListener(IAcceptorListener listener) {
        if (null == listener) {
            return false;
        }
        // 初始化监听list
        if (null == mConnectorListeners) {
            mConnectorListeners = new ArrayList<IAcceptorListener>();
        }
        if (!mConnectorListeners.contains(listener)) {
            return mConnectorListeners.add(listener);
        }
        return false;
    }

    /**
     * 移除连接过程监听器
     * @param listener　监听器
     * @return　移除成功　true, 否则　null == listener 或者不在监听器列表中
     */
    public boolean removeConnectorListener(IAcceptorListener listener) {
        if (null == listener) {
            return false;
        }
        if (null == mConnectorListeners || !mConnectorListeners.contains(listener)) {
            return false;
        } else {
            return mConnectorListeners.remove(listener);
        }
    }

    /**
     * jni通过反射调用的方法
     * @param state
     * @param error
     */
    protected void jniCallOnConnectorOnEvent(int state, int error) {
        Log.i(LOG_TAG, "jniCallOnConnectorOnEvent, state:" +state + ", error:"+error);
        Message msg = Message.obtain(null, MSG_ID_CONNECTEVENT, state, error);
        postMainThread(msg);
    }

    /**
     * 处理postMainThread过来的消息
     * @param msg 消息对象, 参数从中获取
     * @return
     */
    @Override
    protected boolean onDispatchMessage(Message msg) {
        if (MSG_ID_CONNECTEVENT == msg.what) {
            onAcceptorEvent(msg.arg1, msg.arg2);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当连接有事件反馈时回调
     * @param state 状态码, 参考 {@link IAcceptorListener} 定义的常量
     * @param error 错误码, 参考文档
     * @return 未定义
     */
    protected int onAcceptorEvent(int state, int error) {
        int count = null == mConnectorListeners ? 0 : mConnectorListeners.size();
        IAcceptorListener l;
        for (int i = count - 1; i >= 0; i--) {
            l = mConnectorListeners.get(i);
            l.onStatusChanged(state, error);
        }
        return 0;
    }


    @Override
    protected long onCreateCxxObject() {
        return nativeCreateCxxObject();
    }

    /**
     * 开启插件服务
     * @return 0, 开启成功; 1服务已经开启
     */
    public int start() {
        return nativeStart();
    }

    /**
     * 关闭插件服务
     * @return 0, 关闭成功; 1服务尚未开启
     */
    public int stop() {
        return nativeStop();
    }

    /**
     * 登录服务器
     * @param appId 应用程序ID
     * @param appKey 应用程序Key
     * @return 0 登录请求发送成功; 1 服务未开启或者已停止
     */
    public int loginWithOpenID(String appId, String appKey) {
        return nativeLoginWithOpenID(appId, appKey);
    }

    /**
     * 登录服务器
     * @param addr 登录地址
     * @param license 登录授权
     * @return 0 登录请求发送成功; 1 服务未开启或者已停止
     */
    public int login(String addr, String license) {
        return nativeLogin(addr, license);
    }

    /**
     * 获取插件连接地址, 应该在登录完成之后调用
     * @return 连接地址 登录成功后调用返回登录地址，其他情况下返回null或者空字符串
     */
    public String getAddress() {
        return nativeGetAddress();
    }

    /**
     * 创建一次性Session, 应该在登录完成之后调用
     * @param plugin 插件类型(桌面/摄像头/文件远程), 现只实现桌面, 只能传"desktop"
     * @return 一次性Session
     */
    public String createSession(String plugin) {
        return nativeCreateSession(plugin);
    }

    /**
     * 销毁之前创建的一次性Session<br/>
     * 存在意义: 这里的Session都是一次性的, 创建后连接一次将会失效; 若不连接将一直存在, 所以提供此方法,
     * 在创建Session后不再连接调用此方法销毁, 防止内存泄漏
     * @param session 之前创建的Session
     * @return 0 销毁成功; 1 Session已经被使用过或者参数错误
     */
    public int destroySession(String session) {
        return nativeDestroySession(session);
    }

    /**
     * 标识是否已登录成功
     * @return true, 已成功登录
     */
    public boolean isLogged() {
        return nativeIsLogged();
    }

    /**
     * 注销当前的登录状态
     * @return 0 注销成功; 1未成功登录
     */
    public int logout() {
        return nativeLogout();
    }

    /**
     * 标识服务是否正在运行
     * @return true, 正在运行; false 未开始或者已中止
     */
    public boolean isRunning() {
        return nativeIsRunning();
    }


    /**
     * 获取当前屏幕旋转角度
     *
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    private static int jniCallGetDisplayRotation() {
        return UIUtils.getDisplayRotation(UIUtils.getContext());
    }


    /**
     * 开启插件服务
     * @return 0, 开启成功; 1服务已经开启
     */
    private native int nativeStart();

    /**
     * 关闭插件服务
     * @return 0, 关闭成功; 1服务尚未开启
     */
    private native int nativeStop();

    /**
     * 使用appid/appke登录服务器
     * @param appid appid
     * @param appkey appkey
     * @return
     */
    private native int nativeLoginWithOpenID(String appid, String appkey);

    /**
     * 使用地址与授权登录服务器
     * @param address 登录地址
     * @param license 登录授权
     * @return
     */
    private native int nativeLogin(String address, String license);

    /**
     * 注销登录
     * @return
     */
    private native int nativeLogout();

    /**
     * 获取登录地址，仅当登录成功后调用才有用
     * @return
     */
    private native String nativeGetAddress();

    /**
     * 生成session
     * @param plugin 插件名称
     * @return
     */
    private native String nativeCreateSession(String plugin);

    /**
     * 销毁session
     * @param plugin
     * @return
     */
    public native int nativeDestroySession(String plugin);

    /**
     * 是否已经登录成功
     * @return
     */
    private native boolean nativeIsLogged();

    /**
     * 是否已经运行
     * @return
     */
    private native boolean nativeIsRunning();

    /**
     * 生成cxx对象
     * @return
     */
    private native long nativeCreateCxxObject();

    static {
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("oraycommon");
        System.loadLibrary("orayservice_sdk");
    }
}
