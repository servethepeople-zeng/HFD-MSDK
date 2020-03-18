package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_camera_auto_takepic extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_RADIO_END = 23;
    public static final int MAVLINK_MSG_LENGTH = 2;

    public byte autoNum;
    public byte towerNum;

    public msg_camera_auto_takepic(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_RADIO_END;
        unpack(mavLinkPacket.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_RADIO_END;
        packet.payload.putByte(autoNum);
        packet.payload.putByte(towerNum);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.autoNum = payload.getByte();
        this.towerNum = payload.getByte();
    }
}
