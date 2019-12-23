package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-26.
 */

public class msg_YT_degree extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_YT_DEGREE = 17;
    public static final int MAVLINK_MSG_LENGTH = 8;

    public float wnatural;
    public float hnatural;

    public msg_YT_degree(MAVLinkPacket packet) {
        this.sysid = packet.sysid;
        this.compid = packet.compid;
        this.msgid = MAVLINK_MSG_ID_YT_DEGREE;
        unpack(packet.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_YT_DEGREE;
        packet.payload.putFloat(wnatural);
        packet.payload.putFloat(hnatural);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.wnatural = payload.getFloat();
        this.hnatural = payload.getFloat();
    }
}
