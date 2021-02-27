package hfd.msdk.model;

public enum HFDErrorCode {

    FLIGHT_NOT_FOUND("E001","flight not found"),
    GIMBAL_NOT_FOUND("E002","gimbal not found"),
    FLIGHT_MOP_DISCONNECT("E003","the downlond channel is disconnect please restart the gimbal"),
    GIMBAL_SYS_DOWN("E004","the gimbal system not start"),
    FLIGHT_MOP_SENDFAIL("E005","send commond to payload fail"),
    FLIGHT_MOP_DOWNING("E006","Downloading, please try again later"),
    FLIGHT_MOP_DOWNING_FAIL("E007","downloading mediaList fail"),
    GIMBAL_TAK_PIC_FAIL("E008","take photo fail"),
    GIMBAL_DOWN_FAIL("E009","wrong filepath"),
    GIMBAL_DOWNFILE_LENGTH("E010","wrong file length"),
    GIMBAL_DOWNFILE_Fail("E011","File download failed"),
    INIT_SUCCESS("0000","sdk init success");

    private String value;
    private String desc;

    HFDErrorCode(String value, String desc) {
        this.setValue(value);
        this.setDesc(desc);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "[" + this.value + "]" + this.desc;
    }
}
