package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-26.
 */

public class msg_YT_sdegree extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_YT_SDEGREE = 18;
    public static final int MAVLINK_MSG_LENGTH = 9;

    public byte zoomValue;
    public float wnatural;
    public float hnatural;

    public msg_YT_sdegree(MAVLinkPacket packet) {
        this.sysid = packet.sysid;
        this.compid = packet.compid;
        this.msgid = MAVLINK_MSG_ID_YT_SDEGREE;
        unpack(packet.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_YT_SDEGREE;
        packet.payload.putByte(zoomValue);
        packet.payload.putFloat(wnatural);
        packet.payload.putFloat(hnatural);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.zoomValue = payload.getByte();
        this.wnatural = payload.getFloat();
        this.hnatural = payload.getFloat();
    }
}
