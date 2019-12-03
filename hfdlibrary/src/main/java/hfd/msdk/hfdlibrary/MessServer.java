package hfd.msdk.hfdlibrary;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hfd.msdk.internal.HFDObserver;
import hfd.msdk.internal.HFDObserverable;

/**
 * 被观察者实现类
 * Created by zfz on 19/11/16.
 */
public class MessServer implements HFDObserverable {
    //注意到这个List集合的泛型参数为Observer接口，设计原则：面向接口编程而不是面向实现编程
    private List<HFDObserver> list;

    public MessServer() {
        list = new ArrayList<HFDObserver>();
    }

    @Override
    public void registerObserver(HFDObserver userObserver) {

        list.add(userObserver);
    }

    @Override
    public void removeObserver(HFDObserver userObserver) {
        if(!list.isEmpty())
            list.remove(userObserver);
    }

    //遍历
    @Override
    public void notifyObserver(byte mesType,String mesContent) {
        for(int i = 0; i < list.size(); i++) {
            HFDObserver oserver = list.get(i);
            oserver.update(mesType,mesContent);
        }
    }

    public void setInfomation(byte mesType,String mesContent) {
        Log.d("tagtag",mesType+"----"+mesContent);
        //消息更新，通知所有观察者
        notifyObserver(mesType,mesContent);
    }

}
