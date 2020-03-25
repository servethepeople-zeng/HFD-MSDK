package hfd.msdk.mavlink;

/**
 * Created by Administrator on 2018-03-26.
 */

public class msg_waypoint_upload extends MAVLinkMessage {

    public static final int MAVLINK_MSG_WAYPOINT_UPLOAD = 71;
    public static final int MAVLINK_MSG_LENGTH = 40;

    public int towerNum;
    public byte pointType;
    public byte variety;
    public int seqNum;
    public float latitude;
    public float longitude;
    public float altitude;
    public float toward;
    public float pitch;
    public float angle;
    public byte objType;
    public byte side;
    public int totalNum;

    public msg_waypoint_upload(MAVLinkPacket packet) {
        this.sysid = packet.sysid;
        this.compid = packet.compid;
        this.msgid = MAVLINK_MSG_WAYPOINT_UPLOAD;
        unpack(packet.payload);
    }

    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket();
        packet.len = MAVLINK_MSG_LENGTH;
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_WAYPOINT_UPLOAD;
        packet.payload.putInt(towerNum);
        packet.payload.putByte(pointType);
        packet.payload.putByte(variety);
        packet.payload.putInt(seqNum);
        packet.payload.putFloat(latitude);
        packet.payload.putFloat(longitude);
        packet.payload.putFloat(altitude);
        packet.payload.putFloat(toward);
        packet.payload.putFloat(pitch);
        packet.payload.putFloat(angle);
        packet.payload.putByte(objType);
        packet.payload.putByte(side);
        packet.payload.putInt(totalNum);
        return packet;
    }

    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        this.totalNum = payload.getInt();
        this.pointType = payload.getByte();
        this.variety = payload.getByte();
        this.seqNum = payload.getInt();
        this.latitude = payload.getFloat();
        this.longitude = payload.getFloat();
        this.altitude = payload.getFloat();
        this.toward = payload.getFloat();
        this.pitch = payload.getFloat();
        this.angle = payload.getFloat();
        this.objType = payload.getByte();
        this.side = payload.getByte();
        this.totalNum = payload.getInt();
    }
}
