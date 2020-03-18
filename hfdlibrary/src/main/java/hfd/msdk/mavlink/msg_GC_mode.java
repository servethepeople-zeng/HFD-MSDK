package hfd.msdk.mavlink;


/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_GC_mode extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_GC_MODE= 11;
    public static final int MAVLINK_MSG_LENGTH = 2;

    public byte type;
    public byte fieldView;

    public msg_GC_mode(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GC_MODE;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_GC_MODE;
        packet.payload.putByte(type);
        packet.payload.putByte(fieldView);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.type = payload.getByte();
        this.fieldView = payload.getByte();
    }
}
