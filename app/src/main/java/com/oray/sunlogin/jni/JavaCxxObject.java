package com.oray.sunlogin.jni;

/**
 * 抽象类, 定义一个JavaCxxObject对象规范<br/>
 * 所谓JavaCxxObject对象, 就是功能实现由Java层(java代码)、Cxx层(c++ 代码)来完成, 中间使用jni连接。
 * Java层与Cxx层是双引用的强组合关系:
 * <ul>
 *  <li>Java类对象(此类或子类实例)中有一个{@link#mJniObject}字段，用于存儲Cxx对象<li/>
 *  <li>Cxx类对象(CCxxPlugin类或子类实例)中有一个mJavaAdd字段， 用于存儲Java对象<li/>
 *  <li>Java层与Cxx层的强组合关系是在jni层关联、拆除的。
 * </ul>
 * 
 * 但为了减弱Cxx对Java的联系, 所以不再向Cxx传递监听器接口, 监听器机制是在Java层实现.<br/>
 * 子类化插件类及native方法实现约定：
 * <ul>
 *  <li>Java层为完成某一功能调用的方法是非阻塞的, 即立即return, 而不是等待此功能完成后才return.</li>
 *  <li>插件类中声明的以jni为前綴的方法均为Cxx的回调用, 所以一般不要轻易修改其签名.</li>
 * <ul>
 * @author lity
 *
 */
public abstract class JavaCxxObject extends Object {
	
	/**
	 * 存储JNI中的CCxxJavaObject对象地址<br/>
	 * 不能修改此成员字段的名字（否则cxx引用不到此字段)
	 */
    private long mJniObject;

	public JavaCxxObject() {
		super();
		onCreateCxxObject();
	}
	
	/**
	 * 创建Cxx对象, 一般在构造方法中调用.</br>
	 * 此方法必须完成如下功能：</br>
	 * <ul><li>创建对应的cxx对象必须是CCxxJavaObject的子类实例</li>
	 * <li>把当前Java对象(this)由本地引用转成全局强引用</li>
	 * <li>用创建的cxx对象调用{@link #jniAttachCxxObject(long)}方法</li>
	 * <li>cxx对象记录this对象的全局引用(native thread 使用)</li> 
	 * @return 未定义
	 */
	protected abstract long onCreateCxxObject();
	
	/**
	 * 当弱化cxx对象对当前对象(this)的引用时调用(从全局强引用转换成全局弱引用)</br>
	 * @return 未定义
	 */
	protected long onWeakCxxRef() {
		return nativeWeakCxxRef();
	}
	
	/**
	 * 当释放cxx对象时调用</br>
	 * 此方法必须完成如下功能:</br>
	 * <ul><li>拆除java对象对cxx对象的引用, 即调用{@link #jniDetachCxxObject(long)}</li>
	 * <li>拆除cxx对象对java对象的引用</li>
	 * <li>释放cxx对象</li></ul>
	 * @return 未定义
	 */
	protected long onReleaseCxxObject() {
		return nativeReleaseCxxObject();
	}
	
	/**
	 * 附加cxx对象到当前java对象
	 * @param cPointer 对象首地址(void*)
	 * @return 附加上的cxx对象
	 */
	protected long jniAttachCxxObject(long cPointer) {
		return mJniObject = cPointer;
	}
	
	/**
	 * 获取附加到当前java对象上的cxx对象
	 * @return 附加到java对象的cxx对象
	 */
	protected long jniGetCxxObject() {
		return mJniObject;
	}
	
	/**
	 * 移除附加到当前java对象上的cxx对象
	 * @param cPointer 之前附加的cxx对象(void*)
	 * @return 之前附加到java对象的cxx对象
	 */
	protected long jniDetachCxxObject(long cPointer) {
		if (mJniObject != cPointer) {
			throw new RuntimeException();
		}
		mJniObject = 0;
		return cPointer;
	}
	
	/**
	 * 弱化cxx对象对当前对象(this)的引用, 保证GC在未来某时刻可以释放当前对象
	 * @return 未定义
	 */
	public long weakCxxRef() {
		return onWeakCxxRef();
	}
	
	@Override
	protected void finalize() {
		try {
			super.finalize();
			onReleaseCxxObject();
		} catch (Throwable e) {
			e.printStackTrace();
		}		
	}
	
	private native long nativeWeakCxxRef();
	private native long nativeReleaseCxxObject();
	
// 不需要在此处加载so
//	static {
//		System.loadLibrary("gnustl_shared");
//        System.loadLibrary("oraycommon");
//	}
}
