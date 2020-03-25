package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_waypoint_power_fail extends MAVLinkMessage {

    public static final int MAVLINK_MSG_POWER_FAIL = 72;
    public static final int MAVLINK_MSG_LENGTH = 0;

    public msg_waypoint_power_fail(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_POWER_FAIL;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_POWER_FAIL;
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
    }
}
