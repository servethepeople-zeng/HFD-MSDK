package hfd.msdk.internal;

/**
 * 被观察者
 * Created by zfz on 19/11/16.
 */
public interface HFDObserverable {
    public void registerObserver(HFDObserver myOberver);
    public void removeObserver(HFDObserver myOberver);
    public void notifyObserver(byte mesType,String mesText);
}
