package hfd.msdk.mavlink;


/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_radio_start extends MAVLinkMessage {
    public static final int MAVLINK_MSG_ID_RADIO_START = 2;
    public static final int MAVLINK_MSG_LENGTH = 0;

    public msg_radio_start(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_RADIO_START;
        unpack(mavLinkPacket.payload);
    }


    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_RADIO_START;
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
    }
}
