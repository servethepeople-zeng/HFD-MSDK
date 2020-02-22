package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-22.
 */

public class msg_time_calibration extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_TIME_CALIBRATION = 19;
    public static final int MAVLINK_MSG_LENGTH = 9;

    public int c_year;
    public byte c_month;
    public byte c_day;
    public byte c_hour;
    public byte c_minute;
    public byte c_second;

    public msg_time_calibration(MAVLinkPacket mavLinkPacket) {
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_TIME_CALIBRATION;
        unpack(mavLinkPacket.payload);
    }


    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_TIME_CALIBRATION;
        packet.payload.putInt(c_year);
        packet.payload.putByte(c_month);
        packet.payload.putByte(c_day);
        packet.payload.putByte(c_hour);
        packet.payload.putByte(c_minute);
        packet.payload.putByte(c_second);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.c_year = payload.getInt();
        this.c_month = payload.getByte();
        this.c_day = payload.getByte();
        this.c_hour = payload.getByte();
        this.c_minute = payload.getByte();
        this.c_second = payload.getByte();
    }
}
