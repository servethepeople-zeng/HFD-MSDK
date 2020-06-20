package hfd.msdk.mavlink;

/**
 * @Description 获取当前相机时间zoom值
 * @Author ma
 * @Date 2020/4/20 10:21
 */
public class msg_camera_realzoom extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_CAMERA_REALZOOM = 101;
    public static final int MAVLINK_MSG_LENGTH = 0;

    public msg_camera_realzoom(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CAMERA_REALZOOM;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_CAMERA_REALZOOM;
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
    }
}
