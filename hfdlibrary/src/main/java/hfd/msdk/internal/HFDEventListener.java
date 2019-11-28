package hfd.msdk.internal;

import java.util.EventListener;

public interface HFDEventListener extends EventListener {

    void hfdEvent(Object eventObj,String eventDes);
}
