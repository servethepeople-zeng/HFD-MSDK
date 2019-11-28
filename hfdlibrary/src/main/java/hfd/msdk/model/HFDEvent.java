package hfd.msdk.model;

import hfd.msdk.internal.HFDEventListener;

public class HFDEvent {

    private Object eventObj;
    private String eventDes;

    private HFDEventListener hfdEventListener;

//    public HFDEvent(Object eventObj,String eventDes){
//        super(eventObj);
//        this.eventObj = eventObj;
//        this.eventDes = eventDes;
//    }

    public Object getEventObj(){
        return  eventObj;
    }
    public String getEventDes(){
        return  eventDes;
    }
    public void setEventObj(Object eventObj){
        this.eventObj = eventObj;
    }
    public void setEventDes(String eventDes){
        this.eventDes = eventDes;
    }

    public void addListener(HFDEventListener hfdEventListener){
        this.hfdEventListener = hfdEventListener;
    }

    public void send(){
        hfdEventListener.hfdEvent(eventObj,eventDes);
    }
}
