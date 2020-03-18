package hfd.msdk.mavlink;

import java.io.Serializable;

/**
* Common interface for all MAVLink Messages
* Packet Anatomy
* This is the anatomy of one packet. It is inspired by the CAN and SAE AS-4 standards.

* Byte Index  Content              Value       Explanation
* 0            Packet start sign  v1.0: 0xFE   Indicates the start of a new packet.  (v0.9: 0x55)
* 1            Payload length      0 - 255     Indicates length of the following payload.
* 2            Packet sequence     0 - 255     Each component counts up his send sequence. Allows to detect packet loss
* 3            System ID           1 - 255     ID of the SENDING system. Allows to differentiate different MAVs on the same network.
* 4            Component ID        0 - 255     ID of the SENDING component. Allows to differentiate different components of the same system, e.g. the IMU and the autopilot.
* 5            Message ID          0 - 255     ID of the message - the id defines what the payload means and how it should be correctly decoded.
* 6 to (n+6)   Payload             0 - 255     Data of the message, depends on the message id.
* (n+7)to(n+8) Checksum (low byte, high byte)  ITU X.25/SAE AS-4 hash, excluding packet start sign, so bytes 1..(n+6) Note: The checksum also includes MAVLINK_CRC_EXTRA (Number computed from message fields. Protects the packet from decoding a different version of the same packet but with different variables).

* The checksum is the same as used in ITU X.25 and SAE AS-4 standards (CRC-16-CCITT), documented in SAE AS5669A. Please see the MAVLink source code for a documented C-implementation of it. LINK TO CHECKSUM
* The minimum packet length is 8 bytes for acknowledgement packets without payload
* The maximum packet length is 263 bytes for full payload
*
*/
public class MAVLinkPacket implements Serializable {
private static final long serialVersionUID = 2095947771227815314L;

public static final int MAVLINK_STX = 253;//FD

/**
* Message length. NOT counting STX, LENGTH, SEQ, SYSID, COMPID, MSGID, CRC1 and CRC2
*/
public int len;             //长度
/**
* Message sequence
*/
public int seq = 20; //14
/**
* ID of the SENDING system. Allows to differentiate different MAVs on the
* same network.
*/
public int sysid = 255; //FF
/**
* ID of the SENDING component. Allows to differentiate different components
* of the same system, e.g. the IMU and the autopilot.
*/
public int compid = 190; //BE
/**
* ID of the message - the id defines what the payload means and how it
* should be correctly decoded.
*/
public int msgid;           //根据id执行什么动作
/**
* Data of the message, depends on the message id.
*/
public MAVLinkPayload payload;     //动作的具体内容
/**
* ITU X.25/SAE AS-4 hash, excluding packet start sign, so bytes 1..(n+6)
* Note: The checksum also includes MAVLINK_CRC_EXTRA (Number computed from
* message fields. Protects the packet from decoding a different version of
* the same packet but with different variables).
*/
public CRC crc;
    public int capture;

    public MAVLinkPacket(){
payload = new MAVLinkPayload();
}

/**
* Check if the size of the Payload is equal to the "len" byte
*/
public boolean payloadIsFilled() {
if (payload.size() >= MAVLinkPayload.MAX_PAYLOAD_SIZE-1) {
return true;
}
return (payload.size() == len);
}

/**
* Update CRC for this packet.
*/
public void generateCRC(){
crc = new CRC();
crc.update_checksum(len);
crc.update_checksum(seq);
crc.update_checksum(sysid);
crc.update_checksum(compid);
crc.update_checksum(msgid);
payload.resetIndex();
for (int i = 0; i < payload.size(); i++) {
crc.update_checksum(payload.getByte());
}
crc.finish_checksum(msgid);
}

/**
* Encode this packet for transmission.
*
* @return Array with bytes to be transmitted
*/
public byte[] encodePacket() {
byte[] buffer = new byte[6 + len + 2];
int i = 0;
buffer[i++] = (byte) MAVLINK_STX;
buffer[i++] = (byte) len;
buffer[i++] = (byte) seq;
buffer[i++] = (byte) sysid;
buffer[i++] = (byte) compid;
buffer[i++] = (byte) msgid;
for (int j = 0; j < payload.size(); j++) {
buffer[i++] = payload.payload.get(j);
}
generateCRC();
buffer[i++] = (byte) (crc.getLSB());
buffer[i++] = (byte) (crc.getMSB());
return buffer;
}

/**
* Unpack the data in this packet and return a MAVLink message
*
* @return MAVLink message decoded from this packet
*/
//public MAVLinkMessage unpack() {
//    switch (msgid) {
//        case msg_camera_changeMode.MAVLINK_MSG_ID_CAMERA_CHANGEMODE:
//            return new msg_camera_changeMode(this);
//        case msg_radio_start.MAVLINK_MSG_ID_RADIO_START:
//            return new msg_radio_start(this);
//        case msg_radio_end.MAVLINK_MSG_ID_RADIO_END:
//            return new msg_radio_end(this);
//        case msg_radio_zoom.MAVLINK_MSG_ID_RADIO_ZOOM:
//            return new msg_radio_zoom(this);
//        case msg_picture_zoom.MAVLINK_MSG_ID_PICTURE_ZOOM:
//            return new msg_picture_zoom(this);
//        case msg_picture_press.MAVLINK_MSG_ID_PICTURE_PRESS:
//            return new msg_picture_press(this);
//        case msg_picture_focus.MAVLINK_MSG_ID_PICTURE_FOCUS:
//            return new msg_picture_focus(this);
//        case msg_YT_channel_ctrl.MAVLINK_MSG_ID_YT_CHANNEL_CTRL:
//            return new msg_YT_channel_ctrl(this);
//        case msg_YT_reset.MAVLINK_MSG_ID_YT_reset:
//            return new msg_YT_reset(this);
//        case msg_YT_focus.MAVLINK_MSG_ID_YT_FOCUS:
//            return new msg_YT_focus(this);
//        case msg_GC_mode.MAVLINK_MSG_ID_GC_MODE:
//            return new msg_GC_mode(this);
//        case msg_drag_zoom_focus.MAVLINK_MSG_ID_DRAG_ZOOM_FOCUS:
//            return new msg_drag_zoom_focus(this);
//        case msg_AirComputer_memory.MAVLINK_MSG_ID_AIRCOMPUTER_MEMORY:
//            return new msg_AirComputer_memory(this);
//        case msg_YT_currentPosition.MAVLINK_MSG_ID_YT_CURRENTPOSITION:
//            return new msg_YT_currentPosition(this);
//        case msg_gain_camera_zoom.MAVLINK_MSG_ID_GAIN_CAMERA_ZOOM:
//            return new msg_gain_camera_zoom(this);
//        default:
//            return null;
//        }
//    }
}

        
        