package hfd.msdk.mavlink;


/**
 * Created by Administrator on 2018-06-22.
 * 变到固定焦距
 */

public class msg_camera_zoom extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_CAMERA_ZOOM = 12;
    public static final int MAVLINK_MSG_LENGTH = 1;

    public byte type;

    public msg_camera_zoom(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CAMERA_ZOOM;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_CAMERA_ZOOM;
        packet.payload.putByte(type);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.type = payload.getByte();
    }
}
