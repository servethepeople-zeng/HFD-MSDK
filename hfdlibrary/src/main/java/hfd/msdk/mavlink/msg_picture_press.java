package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_picture_press extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_PICTURE_PRESS = 6;
    public static final int MAVLINK_MSG_LENGTH = 9;

    //public byte capture;
    public float jingdu;
    public float weidu;
    public byte towNum;

    public msg_picture_press(MAVLinkPacket mavLinkPacket) {
        this.compid = mavLinkPacket.compid;
        this.sysid = mavLinkPacket.sysid;
        this.msgid = MAVLINK_MSG_ID_PICTURE_PRESS;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_PICTURE_PRESS;
        //packet.payload.putByte(capture);
        packet.payload.putFloat(jingdu);
        packet.payload.putFloat(weidu);
        packet.payload.putByte(towNum);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        //this.capture = payload.getByte();
        this.jingdu = payload.getFloat();
        this.weidu = payload.getFloat();
        this.towNum = payload.getByte();
    }
}
