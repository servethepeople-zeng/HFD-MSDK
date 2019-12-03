package hfd.msdk.internal;

/**
 * 观察者
 * Created by zfz on 19/11/16.
 */
public interface HFDObserver {
    public void update(byte mesType,String mesContent);
}
