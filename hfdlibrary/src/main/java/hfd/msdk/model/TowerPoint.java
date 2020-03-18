package hfd.msdk.model;


/**
 * @author Arvin zeng
 * @Time 2019-4-10 17:41:00
 *
 */

public class TowerPoint {

    private int id;//航点号
    private String towerNum;//塔号(自定义编排的序号)
    private String towerTypeName;//塔类型
    private String kiloName;//塔牌上的千伏数
    private String lineName;//塔牌上的线名
    private String lineNumber;//塔牌上的线号
    private String towerNumber;//塔牌上的号
    private double latitude;//纬度
    private double longitude;//经度
    private float altitude;//高度
    private float toward;//机头朝向
    private int pointType;//航点类型 识别点 升降点 平飞点

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    public int getId() {
        return id;
    }

    public String getTowerNum() {
        return towerNum;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTowerNum(String towerNum){
        this.towerNum = towerNum;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public String getTowerTypeName() {
        return towerTypeName;
    }

    public void setTowerTypeName(String towerTypeName) {
        this.towerTypeName = towerTypeName;
    }

    public String getKiloName() {
        return kiloName;
    }

    public void setKiloName(String kiloName) {
        this.kiloName = kiloName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getTowerNumber() {
        return towerNumber;
    }

    public void setTowerNumber(String towerNumber) {
        this.towerNumber = towerNumber;
    }

    public float getToward() {
        return toward;
    }

    public void setToward(float toward) {
        this.toward = toward;
    }
}
