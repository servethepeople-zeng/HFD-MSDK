package hfd.msdk.model;

public class Point {

	public double latitude;
	public double longitude;
    public float altitude;//高度
	public float haltitude;//海拔高度
    public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public float getAltitude() {
		return altitude;
	}
	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}
	public float getHaltitude() {
		return haltitude;
	}
	public void setHaltitude(float haltitude) {
		this.haltitude = haltitude;
	}
    
}
