package hfd.msdk.internal;

import org.json.JSONObject;

/**
 * 观察者
 * Created by zfz on 19/11/16.
 */
public interface HFDObserver {
    public void update(byte mesType, JSONObject mesContent);
}
