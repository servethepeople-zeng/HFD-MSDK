package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_picture_zoom extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_PICTURE_ZOOM = 5;
    public static final int MAVLINK_MSG_LENGTH = 1;

    public byte type;

    public msg_picture_zoom(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_PICTURE_ZOOM;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_PICTURE_ZOOM;
        packet.payload.putByte(type);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.type = payload.getByte();
    }
}
