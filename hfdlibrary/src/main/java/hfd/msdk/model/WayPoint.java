package hfd.msdk.model;


/**
 * @author Arvin zeng
 * @Time 2020-3-23 17:41:00
 *
 */

public class WayPoint {

    private String id;//杆塔id
    private String towerNum;//杆塔号
    private float latitude;//纬度
    private float longitude;//经度
    private float altitude;//高度
    private float toward;//飞机朝向
    private float apitch;//云台pitch角度
    private int pointType;//航点类型
    private int variety;//识别类型一对一或一对多
    private int seqNumber;//航点顺序号
    private float angle;//与识别端的夹角
    private int object;//识别端类型
    private int side;//线侧

    public String getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public String getTowerNum() {
        return towerNum;
    }

    public void setTowerNum(String towerNum) {
        this.towerNum = towerNum;
    }

    public float getToward() {
        return toward;
    }

    public void setToward(float toward) {
        this.toward = toward;
    }

    public float getApitch() {
        return apitch;
    }

    public void setApitch(float apitch) {
        this.apitch = apitch;
    }

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    public int getVariety() {
        return variety;
    }

    public void setVariety(int variety) {
        this.variety = variety;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getObject() {
        return object;
    }

    public void setObject(int object) {
        this.object = object;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }
}