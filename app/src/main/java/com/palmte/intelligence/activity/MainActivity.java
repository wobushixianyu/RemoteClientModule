package com.palmte.intelligence.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oray.screenlibrary.server.LocalServer;
import com.oray.screenlibrary.util.RootTools;
import com.oray.screenlibrary.util.ShellProcess;
import com.oray.screenlibrary.util.ThreadPoolManage;
import com.oray.sunlogin.jni.IAcceptorListener;
import com.oray.sunlogin.servicesdk.jni.ClientServiceSDK;
import com.palmte.intelligence.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Build.VERSION.SDK_INT;
import static com.oray.screenlibrary.util.CommandLineDesc.APPPRIPATH;
import static com.oray.screenlibrary.util.CommandLineDesc.INPUTAGENT;
import static com.oray.screenlibrary.util.CommandLineDesc.INPUTAGENT5;
import static com.oray.screenlibrary.util.CommandLineDesc.SCREENAGENT;

public class MainActivity extends Activity implements IAcceptorListener {

    private static final String TEMPFILE = "/dev/";
    public static final boolean USE_ROOT_PERMISSIONS = true;
    private int REQUEST_CODE = 100;
    private boolean isReadOnly = false;
    private final String TAG = "MainActivity";
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_remoteSession)
    TextView mTvRemoteSession;
    @BindView(R.id.tv_voiceSession)
    TextView mTvVoiceSession;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.btn_startHelp)
    Button mBtnStart;
    @BindView(R.id.btn_stopHelp)
    Button mBtnStop;
    private ClientServiceSDK mServiceManager;
    private String mAddress;
    private String mSession;
    private MediaProjectionManager mMediaProjectionManager;
    private Timer mTimer;
    private LocalServer mServer;
    private int mResultCode;
    private Intent mResultData;
    private CommonHandler mHandler = new CommonHandler(this);
    private static final int HANDLER_SHOW_STATUS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.btn_startHelp, R.id.btn_stopHelp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startHelp:
                remoteConnect();
                break;
            case R.id.btn_stopHelp:
                remoteDisconnect();
                break;
        }
    }

    //开启远程服务
    private void remoteConnect() {
        if (mServiceManager == null) {
            mServiceManager = new ClientServiceSDK();
            mServiceManager.start();
        }

        //更改状态提示，并禁用按钮
        mTvStatus.setText("正在请求远程服务，请稍候...");
        mBtnStart.setEnabled(false);
        mBtnStop.setEnabled(false);
        mServiceManager.addConnectorListener(this);

        String appId = "6001";
        String appkey = "2a5feb5aeb0b0e7e70cf3f97e77b2584";
        mServiceManager.loginWithOpenID(appId, appkey);
    }

    private void remoteDisconnect() {
        if (!TextUtils.isEmpty(mSession)) {
            mServiceManager.destroySession(mSession);
            mTvAddress.setText("");
            if (SDK_INT >= 21) {
                //停止录屏服务
                LocalServer server = LocalServer.getInstance();
                server.stopServer();
            }

            //杀掉所有相关so
            fistKillSO();
        }
    }

    @Override
    public void onStatusChanged(int status, int error) {
        Log.i("MainActivity", "call onStatusChanged, status: " + status + " !!!");
        switch (status) {
            case IAcceptorListener.STATUS_CONNECT:
                mTvStatus.setText("已成功连接");
                enableStartHelp(false, true);
                //创建远程桌面
                createRemoteSession();
                break;
            case IAcceptorListener.STATUS_DISCONNECT:
                mTvStatus.setText("连接已经断开");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.STATUS_LOGIN_OK:
                mTvStatus.setText("已成功登录服务器");
                enableStartHelp(false, true);
                break;
            case IAcceptorListener.STATUS_LOGIN_FAIL:
                mTvStatus.setText("登录失败，请确认网络或appid与appkey");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.STATUS_PLUGIN_CONNECT:
                mTvStatus.setText("插件连接成功");
                break;
            case IAcceptorListener.STATUS_PLUGIN_DISCONNECT:
                mTvStatus.setText("插件连接断开");
                break;
            case IAcceptorListener.ERROR_INVALID_ARG:
                mTvStatus.setText("无效的参数");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.ERROR_INVALID_LICENSE:
                mTvStatus.setText("无效的授权");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.ERROR_INVALID_PROTOCOL:
                mTvStatus.setText("无效的协议");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.ERROR_INVALID_SERVERADDR:
                mTvStatus.setText("无效的地址");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.ERROR_LICENSE_EXPIRE:
                mTvStatus.setText("授权已经过期");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.ERROR_LOGIN_FAIL:
                mTvStatus.setText("登录失败");
                enableStartHelp(true, false);
                break;
            case IAcceptorListener.ERROR_VERIFY_FAIL:
                mTvStatus.setText("appid/appkey验证失败");
                enableStartHelp(true, false);
                break;
            default:
                mTvStatus.setText("未知错误，请确认网络或appid与appkey");
                enableStartHelp(true, false);
                break;
        }
    }

    private void createRemoteSession() {
        mAddress = mServiceManager.getAddress();
        mSession = mServiceManager.createSession("desktop");

        mTvAddress.setText(mAddress);
        mTvRemoteSession.setText(mSession);

        if (SDK_INT >= 21) {
            //开启屏幕录屏弹框
            startMediaProjection();
        }

        requestRootPermission();
    }

    private void enableStartHelp(boolean start, boolean stop) {
        mBtnStart.setEnabled(start);
        mBtnStop.setEnabled(stop);
    }


    /**
     * 开启获取录像弹窗
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startMediaProjection() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //可以每次都重新获取录屏权限也可以读上一次的缓存信息
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            mResultCode = RESULT_OK;
            mResultData = data;
            if (null == mServer) {
                mServer = LocalServer.getInstance();
            }

            mServer.setLocalSocketStatusListener(new LocalServer.LocalSocketStatusListener() {
                @Override
                public void onStatusChanged(String s) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = HANDLER_SHOW_STATUS;
                    msg.obj = s;
                    mHandler.sendMessage(msg);
                }
            });
            //用户确认，开启录屏服务
            mServer.startServer(MainActivity.this, resultCode, data, mMediaProjectionManager);
        }
    }

    /**
     * 请求root权限
     */
    private void requestRootPermission() {
        if (SDK_INT >= 21) {//高于5.0, 21

            //请求root权限
            ThreadPoolManage.getShortPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (RootTools.isRootAvailable()) { // 判断手机是否root
                        if (RootTools.isRootPermission()) { // 判断应用是否获得root权限
                            //先查杀
                            fistKillSO();
                            String res = ShellProcess.executeCommand("chmod 777 " + APPPRIPATH + INPUTAGENT5, true);
                            if (!TextUtils.isEmpty(res) && res.contains("Read-only")) {
                                //只读
                                isReadOnly = true;

                                //复制
                                copySO(INPUTAGENT5);

                                //修改权限
                                ShellProcess.execCommand("chmod 777 " + TEMPFILE + INPUTAGENT5, true);

                                //执行
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(TEMPFILE + INPUTAGENT5, true);
                                    }
                                });


                            } else {
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(APPPRIPATH + INPUTAGENT5, true);
                                    }
                                });

                            }

                            checkControlService();

                        } else {
                            // TODO 获取root权限失败
                        }
                    } else {
                        // TODO 手机未root
                    }
                    //root状态获得结束
                    if (null != mRootStatusListener) {
                        mRootStatusListener.getRootStatusFinish();
                    }
                }
            });


        } else if (SDK_INT >= 19 && SDK_INT < 21) {//
            ThreadPoolManage.getShortPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 判断手机是否root并获取root权限
                    if (RootTools.isRootAvailable()) { // 判断手机是否root
                        if (RootTools.isRootPermission()) {//判断应用是否获得root权限
                            //先查杀
                            fistKillSO();
                            String res = ShellProcess.executeCommand("chmod 777 " + APPPRIPATH + INPUTAGENT, true);
                            if (!TextUtils.isEmpty(res) && res.contains("Read-only")) {
                                //只读
                                isReadOnly = true;
                                //复制
                                copySO(SCREENAGENT);
                                copySO(INPUTAGENT);

                                //修改权限
                                ShellProcess.executeCommand("chmod 777 " + TEMPFILE + SCREENAGENT, true);
                                ShellProcess.executeCommand("chmod 777 " + TEMPFILE + INPUTAGENT, true);

                                //执行
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.executeCommandNoResult(TEMPFILE + SCREENAGENT, true);
                                    }
                                });

                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(TEMPFILE + INPUTAGENT, true);
                                    }
                                });

                            } else {
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.executeCommandNoResult(APPPRIPATH + SCREENAGENT, true);
                                    }
                                });
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(APPPRIPATH + INPUTAGENT, true);
                                    }
                                });
                            }

                            checkControlService();

                        } else {
                            // TODO 获取root权限失败
                        }
                    } else {
                        // TODO 手机未root
                    }
                    //root状态获得结束
                    if (null != mRootStatusListener) {
                        mRootStatusListener.getRootStatusFinish();
                    }
                }
            });
        }
    }

    private static RootStatusListener mRootStatusListener;

    public interface RootStatusListener {
        void getRootStatusFinish();
    }

    public static void setRootStatusListener(RootStatusListener listener) {
        mRootStatusListener = listener;
    }

    public static void removeRootStatusListener() {
        mRootStatusListener = null;
    }

    /**
     * 开启线程定时检查控制线程的存在
     */
    private void checkControlService() {
        if (mTimer == null) {
            mTimer = new Timer();
            TimerTask timerTask = null;
            if (SDK_INT >= 21) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        String cmdline = "ps | grep oray";
                        HashMap<String, String> processMap = ShellProcess.execCommand(cmdline, USE_ROOT_PERMISSIONS);
                        boolean isExist = false;
                        for (String key : processMap.keySet()) {
                            String value = processMap.get(key);
                            if ((APPPRIPATH + INPUTAGENT5).equals(value)) {
                                isExist = true;
                            }
                        }
                        if (!isExist && null != mTimer) {
                            Log.i(TAG, INPUTAGENT5 + " is not exist, start again...");
                            if (isReadOnly) {
                                String res = ShellProcess.executeCommand("chmod 777 " + TEMPFILE + INPUTAGENT5, true);
                                if (!TextUtils.isEmpty(res) && res.contains("No such file")) {//缓存目录中没找到INPUTAGENT5文件
                                    copySO(INPUTAGENT5);
                                }
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(TEMPFILE + INPUTAGENT5, true);
                                    }
                                });

                            } else {
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(APPPRIPATH + INPUTAGENT5, true);
                                    }
                                });

                            }
                        }
                    }
                };
            } else {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        String cmdline = "ps | grep oray";
                        HashMap<String, String> processMap = ShellProcess.execCommand(cmdline, USE_ROOT_PERMISSIONS);
                        boolean isScreenAgentExist = false;
                        boolean isInputAgentExist = false;
                        for (String key : processMap.keySet()) {
                            String value = processMap.get(key);
                            if ((APPPRIPATH + SCREENAGENT).equals(value)) {
                                isScreenAgentExist = true;
                            }
                            if ((TEMPFILE + SCREENAGENT).equals(value)) {
                                isScreenAgentExist = true;
                            }
                            if ((APPPRIPATH + INPUTAGENT).equals(value)) {
                                isInputAgentExist = true;
                            }
                            if ((TEMPFILE + INPUTAGENT).equals(value)) {
                                isInputAgentExist = true;
                            }
                        }
                        if (!isScreenAgentExist && null != mTimer) {
                            Log.i(TAG, SCREENAGENT + "is not exist, start again...");
                            if (isReadOnly) {

                                String res = ShellProcess.executeCommand("chmod 777 " + TEMPFILE + SCREENAGENT, true);
                                if (!TextUtils.isEmpty(res) && res.contains("No such file")) {//缓存中没找到SCREENAGENT文件

                                    copySO(SCREENAGENT);

                                }
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.executeCommandNoResult(TEMPFILE + SCREENAGENT, true);
                                    }
                                });

                            } else {
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.executeCommandNoResult(APPPRIPATH + SCREENAGENT, true);
                                    }
                                });
                            }
                        }
                        if (!isInputAgentExist && null != mTimer) {
                            Log.i(TAG, INPUTAGENT + "is not exist, start again...");

                            if (isReadOnly) {

                                String res = ShellProcess.executeCommand("chmod 777 " + TEMPFILE + INPUTAGENT, true);
                                if (!TextUtils.isEmpty(res) && res.contains("No such file")) {//缓存中没找到INPUTAGENT文件
                                    copySO(INPUTAGENT);
                                }
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(TEMPFILE + INPUTAGENT, true);
                                    }
                                });


                            } else {
                                ThreadPoolManage.getShortPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShellProcess.execCommand(APPPRIPATH + INPUTAGENT, true);
                                    }
                                });
                            }
                        }
                    }
                };
            }

            mTimer.schedule(timerTask, 0, 1000);
        }
    }

    /**
     * 杀掉remote service
     */
    public void killRemoteService(String serviceCmd) {
        try {
            Log.i("MainActivity", "enter kill remote command.." + serviceCmd);
            String cmdline = "ps | grep oray";
            HashMap<String, String> processMap = ShellProcess.execCommand(cmdline, USE_ROOT_PERMISSIONS);
            Iterator<HashMap.Entry<String, String>> iterator = processMap.entrySet().iterator();
            while (iterator.hasNext()) {
                HashMap.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();

                if (true == entry.getValue().equals(serviceCmd)) {

                    String command = "kill -9 " + key;
                    ShellProcess.executeCommand(command, USE_ROOT_PERMISSIONS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fistKillSO() {
        String cmdline = "ps | grep oray";
        HashMap<String, String> processMap = ShellProcess.execCommand(cmdline, USE_ROOT_PERMISSIONS);
        for (String key : processMap.keySet()) {
            String value = processMap.get(key);
            if ((APPPRIPATH + INPUTAGENT5).equals(value)) {
                killRemoteService(APPPRIPATH + INPUTAGENT5);
            }
            if ((APPPRIPATH + SCREENAGENT).equals(value)) {
                killRemoteService(APPPRIPATH + SCREENAGENT);
            }
            if ((APPPRIPATH + INPUTAGENT).equals(value)) {
                killRemoteService(APPPRIPATH + INPUTAGENT);
            }
            if ((TEMPFILE + INPUTAGENT5).equals(value)) {
                killRemoteService(TEMPFILE + INPUTAGENT5);
            }
            if ((TEMPFILE + SCREENAGENT).equals(value)) {
                killRemoteService(TEMPFILE + SCREENAGENT);
            }
            if ((TEMPFILE + INPUTAGENT).equals(value)) {
                killRemoteService(TEMPFILE + INPUTAGENT);
            }
        }
    }

    private void copySO(String soName) {
        ShellProcess.execCommand("cp " + APPPRIPATH + soName + " " + TEMPFILE + soName, true);
    }

    public static class CommonHandler extends Handler {
        private WeakReference<Activity> mActivityReference;

        CommonHandler(Activity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (null != activity) {
                switch (msg.what) {
                    case HANDLER_SHOW_STATUS:
                        MainActivity ac = (MainActivity) activity;
                        ac.mTvStatus.setText((CharSequence) msg.obj);
                        break;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, ".....start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, ".....onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, ".....onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, ".....onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, ".....onDestroy");
        if (null != mServer)
            mServer.stopServer();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {

        return super.onRetainNonConfigurationInstance();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏

        } else {//横屏

        }
    }

}
