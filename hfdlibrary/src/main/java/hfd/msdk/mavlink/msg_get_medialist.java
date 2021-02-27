package hfd.msdk.mavlink;


/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_get_medialist extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_GET_STORAGE = 81;
    public static final int MAVLINK_MSG_LENGTH = 1;

    public byte cmdType;

    public msg_get_medialist(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GET_STORAGE;
        unpack(mavLinkPacket.payload);
    }


    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_GET_STORAGE;
        packet.payload.putByte(cmdType);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.cmdType = payload.getByte();
    }
}
