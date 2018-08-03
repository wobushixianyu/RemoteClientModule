package com.oray.sunlogin.jni;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 封装一个主线程的Handler, 可以方便地把任务或者消息中转到主线程里
 * @author lity
 *
 */
public abstract class JavaCxxManager extends JavaCxxObject {

	private Handler mMainHandler;
	public JavaCxxManager() {
		super();
	}

	class MsgHandler extends Handler {
		
		public MsgHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void dispatchMessage(Message msg) {
			boolean handle;
			Runnable task = msg.getCallback();
			if (null != task) {
				task.run();
				handle = true;
			} else {
				handle = onDispatchMessage(msg);
			}
			if (!handle) {
				super.dispatchMessage(msg);
			}
		}
	}
    
    /**
     * POST一个消息到主线程(这些消息一般都是cxx通过jni调过来的)
     * @param arg1 参数1 
     * @param arg2 参数2
     * @param obj 参数3
     * @return ture 投递成功, {@link #onDispatchMessage(Message)} 在未来将被主线程调用
     */
    public boolean postMainThread(int msgId, int arg1, int arg2, Object obj) {
    	Message msg = Message.obtain();
    	msg.what = msgId;
    	msg.arg1 = arg1;
    	msg.arg2 = arg2;
    	msg.obj = obj;
    	return postMainThread(msg);
    }

	/**
     * POST一个任务到主线程(这些消息一般都是cxx通过jni调过来的)
     * @param task Runnable对象, 将在主线程执行的任务
     * @return ture 投递成功, task.run() 在未来将被主线程调用
     */
    public boolean postMainThread(Runnable task) {
    	Message msg = Message.obtain(null, task);
    	return postMainThread(msg);
    }
    
    /**
     * POST一个消息到主线程(这些消息一般都是cxx通过jni调过来的)
     * @param msg 消息, 消息的handler属性将被覆盖
     * @return ture 投递成功, {@link #onDispatchMessage(Message)} 在未来将被主线程调用
     */
    public boolean postMainThread(Message msg) {
    	if (null == mMainHandler) {
			synchronized (this) {
				if (null == mMainHandler) {
					mMainHandler = new MsgHandler(Looper.getMainLooper());
				}
			}
		}
    	msg.setTarget(mMainHandler);
    	msg.sendToTarget();
    	return true;
    }
    
    /**
     * 处理消息(一般子类需要重写此方法, 来处理之前Post的消息)
     * @param msg 消息对象, 参数从中获取
     * @return true 消息已处理, 父类将不再处理; false 父类继续处理
     */
    protected boolean onDispatchMessage(Message msg) {
    	return false;
    }
}
