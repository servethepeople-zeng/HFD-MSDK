package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_cal_stop extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_CAL_STOP = 76;
    public static final int MAVLINK_MSG_LENGTH = 1;

    public byte orderByte;

    public msg_cal_stop(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CAL_STOP;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_CAL_STOP;
        packet.payload.putByte(orderByte);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.orderByte = payload.getByte();
    }
}
