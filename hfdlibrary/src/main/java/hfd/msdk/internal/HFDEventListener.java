package hfd.msdk.internal;

import java.util.EventListener;

import hfd.msdk.model.HFDEvent;

public interface HFDEventListener extends EventListener {

    void hfdEvent(HFDEvent fhdEvent);
}
