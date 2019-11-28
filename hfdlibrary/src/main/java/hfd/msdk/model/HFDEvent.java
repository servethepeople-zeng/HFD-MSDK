package hfd.msdk.model;

import java.util.EventObject;

public class HFDEvent extends EventObject {

    private Object eventObj;
    private String eventDes;

    public HFDEvent(Object eventObj,String eventDes){
        super(eventObj);
        this.eventObj = eventObj;
        this.eventDes = eventDes;
    }

    public  Object getObject(){
        return eventObj;
    }

    public String getEventDes(){
        return eventDes;
    }
}
