package hfd.msdk.model;

import java.util.EventObject;

import hfd.msdk.internal.HFDEventListener;

public class HFDEvent extends EventObject {

    private Object eventObj;
    private String eventDes;
    private HFDEventListener hfdEventListener;

    public HFDEvent(Object eventObj,String eventDes){
        super(eventObj);
        this.eventObj = eventObj;
        this.eventDes = eventDes;
    }

    public void addListener(HFDEventListener hfdEventListener){
        this.hfdEventListener = hfdEventListener;
    }
    public  Object getObject(){
        return eventObj;
    }

    public String getEventDes(){
        return eventDes;
    }

    public void send(){
        hfdEventListener.hfdEvent(eventObj,eventDes);
    }
}
