package hfd.msdk.hfdlibrary;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dji.common.battery.BatteryState;
import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.FlightWindWarning;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.PayloadKey;
import dji.keysdk.callback.ActionCallback;
import dji.keysdk.callback.KeyListener;
import dji.midware.data.manager.P3.DJIPayloadUsbDataManager;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import hfd.msdk.mavlink.MAVLinkPacket;
import hfd.msdk.mavlink.msg_GC_mode;
import hfd.msdk.mavlink.msg_YT_degree;
import hfd.msdk.mavlink.msg_YT_reset;
import hfd.msdk.mavlink.msg_YT_sdegree;
import hfd.msdk.mavlink.msg_cal_stop;
import hfd.msdk.mavlink.msg_camera_auto_takepic;
import hfd.msdk.mavlink.msg_camera_realzoom;
import hfd.msdk.mavlink.msg_camera_zoom;
import hfd.msdk.mavlink.msg_get_storage;
import hfd.msdk.mavlink.msg_gimbal_version;
import hfd.msdk.mavlink.msg_picture_press;
import hfd.msdk.mavlink.msg_picture_zoom;
import hfd.msdk.mavlink.msg_radio_end;
import hfd.msdk.mavlink.msg_radio_start;
import hfd.msdk.mavlink.msg_time_calibration;
import hfd.msdk.mavlink.msg_waypoint_power_fail;
import hfd.msdk.mavlink.msg_waypoint_upload;
import hfd.msdk.model.NewWayPoint;
import hfd.msdk.model.TowerPoint;
import hfd.msdk.model.WayPoint;
import hfd.msdk.utils.FileUtils;
import hfd.msdk.utils.Helper;
import hfd.msdk.utils.ToastUtils;

import static hfd.msdk.model.IConstants.Qx30U_Zoom_H10;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H11;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H12;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H13;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H14;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H15;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H16;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H17;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H18;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H19;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H20;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H21;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H22;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H23;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H24;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H25;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H26;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H27;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H28;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H29;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H30;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H31;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H6;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H7;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H8;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_H9;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V10;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V11;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V12;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V13;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V14;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V15;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V16;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V17;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V18;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V19;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V20;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V21;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V22;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V23;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V24;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V25;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V26;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V27;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V28;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V29;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V30;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V31;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V6;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V7;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V8;
import static hfd.msdk.model.IConstants.Qx30U_Zoom_V9;
import static hfd.msdk.model.IConstants.TowHor;
import static hfd.msdk.model.IConstants.TowVer;
import static hfd.msdk.model.IConstants.UVC_Horizontal_angle;
import static hfd.msdk.model.IConstants.UVC_Vertical_angle;
import static hfd.msdk.model.IConstants.calDay;
import static hfd.msdk.model.IConstants.calHour;
import static hfd.msdk.model.IConstants.calMinute;
import static hfd.msdk.model.IConstants.calMonth;
import static hfd.msdk.model.IConstants.calSecond;
import static hfd.msdk.model.IConstants.calYear;
import static hfd.msdk.model.IConstants.latLng;
import static hfd.msdk.utils.Helper.BytesToHexString;
import static hfd.msdk.utils.PointUtils.mulTGeneratePoints;
import static hfd.msdk.utils.PointUtils.oneTGeneratePoints;
import static hfd.msdk.utils.PointUtils.twoTGeneratePoints;

public class HFDManager {

    private static final String TAG = HFDManager.class.getSimpleName() + "1";
    public static FlightController flightController;
    public static MessServer messServer;
    public static int logLevelType = 0;
    public static List<TowerPoint> backPointList = new ArrayList<TowerPoint>();
    public static List<TowerPoint> tempPointList = new ArrayList<TowerPoint>();
    public static FlightControllerState mFControlState;
    private static DJIKey getDataKey, sendDataKey;
    private static JSONObject object = new JSONObject();
    public List<NewWayPoint> hfdWayPointList = new ArrayList<NewWayPoint>();
    //获取飞机
    private Aircraft aircraft = null;
    private CommonCallbacks.CompletionCallbackWith mDJICompletionCallbackc;
    private int getHomeFlag = 0, pointNum = 0;
    private LatLng liveLatLng = new LatLng(latLng.latitude, latLng.longitude);
    private int gcMode = 2, qZoom = 6, realSeq = 0;
    private TowerPoint realPoint = new TowerPoint();
    private WaypointMission mission;
    private WaypointMissionOperator waypointMissionOperator;
    private float currentAltidude = 0, compassData = 0;
    private int battery1 = 0, battery2 = 0;
    private String missonName = "";
    private Timer timer, zoomTimer = null;
    private ZoomTimerTask zoomTimerTask = null;
    private int mtowerNum = 0, mseqNum = 0, mlatitude = 0, mlongtidude = 0, maltitude = 0, mtoward = 0, mpitch = 0, mangle = 0, atotal = 0;
    public List<String> picNameList;
    private KeyListener getDataListener = new KeyListener() {
        @Override
        public void onValueChange(@Nullable Object oldValue, @Nullable final Object newValue) {
            if (newValue instanceof byte[]) {
                byte[] data = (byte[]) newValue;
                Log.d("receivedata", "HFD receive data success! " + BytesToHexString(data, data.length));
                FileUtils.writeLogFile(1, "receive data success1:" + Helper.byte2hex(data));
                if (data.length > 7) {
                    if ("fd".equals(Integer.toHexString(data[0] & 0x0FF))) {
                        if ("14".equals(Integer.toHexString(data[2] & 0x0FF))) {
                            if ("ff".equals(Integer.toHexString(data[3] & 0x0FF))) {
                                if ("be".equals(Integer.toHexString(data[4] & 0x0FF))) {
                                    if ("47".equals(Integer.toHexString(data[5] & 0x0FF))) {
                                        if ("1".equals(Integer.toHexString(data[6] & 0x0FF))) {
                                            if (data.length >= 47) {
                                                //对比塔号
                                                mtowerNum = 0;
                                                mtowerNum |= (data[7] & 0xFF);
                                                mtowerNum |= (data[8] & 0xFF) << 8;
                                                mtowerNum |= (data[9] & 0xFF) << 16;
                                                mtowerNum |= (data[10] & 0xFF) << 24;
                                                //Log.d("uploadfile", "hfdWayPointList 大小=" + hfdWayPointList.size() + ",pointNum=" + pointNum);
                                                FileUtils.writeLogFile(1, "hfdWayPointList 大小=" + hfdWayPointList.size() + ",pointNum=" + pointNum);
                                                //Log.d("uploadfile", "塔号=" + mtowerNum);
                                                if (mtowerNum == Integer.parseInt(hfdWayPointList.get(pointNum).getTowerNum().substring(1))) {
                                                    //对比点类型
                                                    //Log.d("uploadfile", "点类型=" + (data[11] & 0x0FF));
                                                    FileUtils.writeLogFile(1, "点类型=" + (data[11] & 0x0FF));
                                                    if ((data[11] & 0x0FF) == hfdWayPointList.get(pointNum).getPointType()) {
                                                        //对比识别点属性
                                                        //Log.d("uploadfile", "识别点属性=" + (data[12] & 0x0FF));
                                                        FileUtils.writeLogFile(1, "识别点属性=" + (data[12] & 0x0FF));
                                                        if ((data[12] & 0x0FF) == hfdWayPointList.get(pointNum).getVariety()) {
                                                            //对比飞行顺序号
                                                            mseqNum = 0;
                                                            mseqNum |= (data[13] & 0xFF);
                                                            mseqNum |= (data[14] & 0xFF) << 8;
                                                            mseqNum |= (data[15] & 0xFF) << 16;
                                                            mseqNum |= (data[16] & 0xFF) << 24;
                                                            //Log.d("uploadfile", "飞行顺序号=" + mseqNum);
                                                            FileUtils.writeLogFile(1, "飞行顺序号=" + mseqNum);
                                                            if (mseqNum == pointNum) {
                                                                //对比纬度
                                                                mlatitude = 0;
                                                                mlatitude |= (data[17] & 0xFF);
                                                                mlatitude |= (data[18] & 0xFF) << 8;
                                                                mlatitude |= (data[19] & 0xFF) << 16;
                                                                mlatitude |= (data[20] & 0xFF) << 24;
                                                                //Log.d("uploadfile", "纬度=" + Float.intBitsToFloat(mlatitude));
                                                                FileUtils.writeLogFile(1, "纬度=" + Float.intBitsToFloat(mlatitude));
                                                                if (Float.intBitsToFloat(mlatitude) - hfdWayPointList.get(pointNum).getLatitude() < 0.00001) {
                                                                    //对比经度
                                                                    mlongtidude = 0;
                                                                    mlongtidude |= (data[21] & 0xFF);
                                                                    mlongtidude |= (data[22] & 0xFF) << 8;
                                                                    mlongtidude |= (data[23] & 0xFF) << 16;
                                                                    mlongtidude |= (data[24] & 0xFF) << 24;
                                                                    //Log.d("uploadfile", "经度=" + Float.intBitsToFloat(mlongtidude));
                                                                    FileUtils.writeLogFile(1, "经度=" + Float.intBitsToFloat(mlongtidude));
                                                                    if (Float.intBitsToFloat(mlongtidude) - hfdWayPointList.get(pointNum).getLongitude() < 0.00001) {
                                                                        //对比高度
                                                                        maltitude = 0;
                                                                        maltitude |= (data[25] & 0xFF);
                                                                        maltitude |= (data[26] & 0xFF) << 8;
                                                                        maltitude |= (data[27] & 0xFF) << 16;
                                                                        maltitude |= (data[28] & 0xFF) << 24;
                                                                        //Log.d("uploadfile", "高度=" + Float.intBitsToFloat(maltitude));
                                                                        FileUtils.writeLogFile(1, "高度=" + Float.intBitsToFloat(maltitude));
//                                                                        if (Float.intBitsToFloat(maltitude) - hfdWayPointList.get(pointNum).getAltitude() < 0.1) {
//                                                                            //对比机头朝向
//                                                                            mtoward = 0;
//                                                                            mtoward |= (data[29] & 0xFF);
//                                                                            mtoward |= (data[30] & 0xFF) << 8;
//                                                                            mtoward |= (data[31] & 0xFF) << 16;
//                                                                            mtoward |= (data[32] & 0xFF) << 24;
//                                                                            //Log.d("uploadfile", "机头朝向=" + Float.intBitsToFloat(mtoward));
//                                                                            FileUtils.writeLogFile(1, "机头朝向=" + Float.intBitsToFloat(mtoward));
//                                                                            if (Float.intBitsToFloat(mtoward) - hfdWayPointList.get(pointNum).getToward() < 0.1) {
//                                                                                //对比俯仰角
//                                                                                mpitch = 0;
//                                                                                mpitch |= (data[33] & 0xFF);
//                                                                                mpitch |= (data[34] & 0xFF) << 8;
//                                                                                mpitch |= (data[35] & 0xFF) << 16;
//                                                                                mpitch |= (data[36] & 0xFF) << 24;
//                                                                                //Log.d("uploadfile", "俯仰角=" + Float.intBitsToFloat(mpitch));
//                                                                                FileUtils.writeLogFile(1, "俯仰角=" + Float.intBitsToFloat(mpitch));
//                                                                                if (Float.intBitsToFloat(mpitch) - hfdWayPointList.get(pointNum).getApitch() < 0.1) {
//                                                                                    //对比识别夹角
//                                                                                    mangle = 0;
//                                                                                    mangle |= (data[37] & 0xFF);
//                                                                                    mangle |= (data[38] & 0xFF) << 8;
//                                                                                    mangle |= (data[39] & 0xFF) << 16;
//                                                                                    mangle |= (data[40] & 0xFF) << 24;
//                                                                                    //Log.d("uploadfile", "识别夹角=" + Float.intBitsToFloat(mangle));
//                                                                                    FileUtils.writeLogFile(1, "识别夹角=" + Float.intBitsToFloat(mangle));
//                                                                                    if (Float.intBitsToFloat(mangle) - hfdWayPointList.get(pointNum).getAngle() < 0.1) {
//                                                                                        //对比识别端类型
//                                                                                        //Log.d("uploadfile", "识别端类型=" + (data[41] & 0x0FF));
//                                                                                        FileUtils.writeLogFile(1, "识别端类型=" + (data[41] & 0x0FF));
//                                                                                        if ((data[41] & 0x0FF) == hfdWayPointList.get(pointNum).getObject()) {
//                                                                                            //对比线侧
//                                                                                            //Log.d("uploadfile", "线侧=" + (data[42] & 0x0FF));
//                                                                                            FileUtils.writeLogFile(1, "线侧=" + (data[42] & 0x0FF));
                                                                        if ((data[42] & 0x0FF) == hfdWayPointList.get(pointNum).getSide()) {
                                                                            //对比识别点总数量
                                                                            atotal = 0;
                                                                            atotal |= (data[47] & 0xFF);
                                                                            atotal |= (data[48] & 0xFF) << 8;
                                                                            atotal |= (data[49] & 0xFF) << 16;
                                                                            atotal |= (data[50] & 0xFF) << 24;
                                                                            //Log.d("uploadfile", "识别点总数量=" + atotal);
                                                                            FileUtils.writeLogFile(1, "识别点总数量=" + atotal);
                                                                            if (atotal == hfdWayPointList.size())
                                                                                postWaypoint(1, 0);
                                                                        }
/*                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }*/
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            } else {
                                                rebackMsg(7, "fail", "接收航点验证数据错误 " + BytesToHexString(data, data.length));
                                            }
                                        } else if ("2".equals(Integer.toHexString(data[6] & 0x0FF))) {
                                            postWaypoint(2, 0);
                                        } else if ("3".equals(Integer.toHexString(data[6] & 0x0FF))) {
                                            //Log.d("uploadfile","接收到返回数据 到达航点信息");
                                            int dnum = 0;
                                            dnum |= (data[7] & 0xFF);
                                            dnum |= (data[8] & 0xFF) << 8;
                                            dnum |= (data[9] & 0xFF) << 16;
                                            dnum |= (data[10] & 0xFF) << 24;
                                            postWaypoint(3, dnum);
                                        }
                                    } else if ("66".equals(Integer.toHexString(data[5] & 0x0FF))) {
                                        stopTimer();
                                        createMAVLink(13, 0);
                                    } else if ("42".equals(Integer.toHexString(data[5] & 0x0FF))) {
                                        int stroage = 0;
                                        stroage |= (data[7] & 0xFF) << 24;
                                        stroage |= (data[8] & 0xFF) << 16;
                                        stroage |= (data[9] & 0xFF) << 8;
                                        stroage |= (data[10] & 0xFF);
                                        try {
                                            object = new JSONObject();
                                            object.put("totalStorage", (data[6] & 0x0FF) + "G");
                                            object.put("remainStorage", stroage + "M");
                                        } catch (Exception e) {
                                            object = new JSONObject();
                                        }
                                        messServer.setInfomation((byte) 2, object);
                                        FileUtils.writeLogFile(0, object.toString());
                                    }else if("4a".equals(Integer.toHexString(data[5] & 0x0FF))){
                                        createMAVLink(14, 0);
                                    }else if("17".equals(Integer.toHexString(data[5] & 0x0FF))){
                                        if ("4".equals(Integer.toHexString(data[6] & 0x0FF))) {   //即将拍摄某个部件的照片
                                            String resultName = "ID";
                                            int idx = 0;
                                            idx |= (data[7] & 0xFF);
                                            idx |= (data[8] & 0xFF) << 8;
                                            idx |= (data[9] & 0xFF) << 16;
                                            idx |= (data[10] & 0xFF) << 24;
                                            resultName +=  String.format("%04d",idx) + "_";

                                            int tn = 0;
                                            tn |= (data[11] & 0xFF);
                                            tn |= (data[12] & 0xFF) << 8;
                                            tn |= (data[13] & 0xFF) << 16;
                                            tn |= (data[14] & 0xFF) << 24;
                                            resultName +=  "TN" + String.format("%04d",tn) + "_";

                                            int side = data[15] & 0xFF;
                                            resultName +=  "SIDE" + String.format("%02d",side) + "_";

                                            int tg = data[16] & 0xFF;
                                            resultName +=  "TG" + String.format("%02d",tg) + "_";

                                            int attr = data[17] & 0xFF;
                                            resultName +=  "ATTR" + String.format("%04d",attr)+".jpg";

                                            picNameList.add(resultName);

                                            FileUtils.writeLogFile(0, "picName = "+resultName);
                                            //FileUtils.writeLogFile(0, picNameList.toString());
                                        }
                                    }else if("4c".equals(Integer.toHexString(data[5] & 0x0FF))){
                                        rebackMsg(19, "applyGoHome", "申请返航");
                                    }
                                } else if ("bf".equals(Integer.toHexString(data[4] & 0x0FF))) {
                                    //反馈命令处理 以下表示拍照完成
                                    if ("6".equals(Integer.toHexString(data[5] & 0x0FF))) {
                                        rebackMsg(1, "success", "call stopGoHome() method success");
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    };

    public HFDManager(MessServer messServer) {
        //dji回调函数们
        settingCallback();
        getFlightThread();
        initDJIkey();
        this.messServer = messServer;

        FileUtils.initLogFile();
        FileUtils.writeLogFile(0, "HFDManager init success.");
        picNameList = new ArrayList<String>();
        timer = new Timer();
        WarningTimerTask warningTimerTask = new WarningTimerTask();
        timer.schedule(warningTimerTask, 60000, 60000);

        zoomTimer = new Timer();
        zoomTimerTask = new ZoomTimerTask();
        zoomTimer.schedule(zoomTimerTask, 1000, 1000);
    }

    public static void main(String args[]) {
        /*List<WayPoint> mwaypoints = new ArrayList<WayPoint>();

        List<TowerPoint> towerLists = new ArrayList<TowerPoint>();
        TowerPoint tower1 = new TowerPoint();
        tower1 = new TowerPoint();
        tower1.setAltitude(50f);
        tower1.setId("afafdasgdsagdasg");
        tower1.setTowerNum("#11");
        tower1.setTowerTypeName("zx");
        tower1.setTowerNumber("#8");
        tower1.setLatitude(39.3810467);
        tower1.setLongitude(111.192265);
        towerLists.add(tower1);

        tower1 = new TowerPoint();
        tower1.setAltitude(50f);
        tower1.setId("ghghghghghghghf");
        tower1.setTowerNum("#9");
        tower1.setTowerTypeName("zx");
        tower1.setTowerNumber("#9");
        tower1.setLatitude(39.3796983);
        tower1.setLongitude(111.197075);
        towerLists.add(tower1);

        tower1 = new TowerPoint();
        tower1.setId("vbvxcbvxcbvxcbvxbv");
        tower1.setAltitude(50f);
        tower1.setTowerNum("#10");
        tower1.setTowerTypeName("zx");
        tower1.setTowerNumber("#10");
        tower1.setLatitude(39.3788);
        tower1.setLongitude(111.199075);
        towerLists.add(tower1);

        for (int i = towerLists.size() - 1; i >= 0; i--) {
            System.out.println(towerLists.get(i).getTowerNumber());
        }*/
//        mwaypoints = FileUtils.loadXml(towerLists);
//        System.out.println(mwaypoints.size());
//        for (int i = 0; i < mwaypoints.size(); i++) {
//            System.out.println(mwaypoints.get(i).getId() + "," + mwaypoints.get(i).getTowerNum() + "," + mwaypoints.get(i).getSeqNumber());
//        }
//        String missonName = "abce1";
//
//        byte[] missionNameByte = new byte[missonName.length()+8];
//        missionNameByte[0] = (byte)253;
//        missionNameByte[1] = (byte)missonName.length();
//        missionNameByte[2] = (byte)20;
//        missionNameByte[3] = (byte)255;
//        missionNameByte[4] = (byte)190;
//        missionNameByte[5] = (byte)70;
//        for(int i=6;i<6+missonName.length();i++) {
//            missionNameByte[i] = (byte) missonName.charAt(i-6);
//        }
//        missionNameByte[missonName.length()+6] = (byte)69;
//        missionNameByte[missonName.length()+7] = (byte)84;
//        System.out.println(Helper.byte2hex(missionNameByte));

/*        int seqNum = 20;
        System.out.println((byte) seqNum);
        System.out.println((byte) (seqNum >> 0));
        System.out.println((byte) (seqNum >> 8));
        System.out.println((byte) (seqNum >> 16));
        System.out.println((byte) (seqNum >> 24));
        System.out.println((byte) (seqNum >> 0) ^ (byte) (seqNum >> 8) ^ (byte) (seqNum >> 16) ^ (byte) (seqNum >> 24));

        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);//这个1的意识是保存结果到小数点后几位，但是特别声明：这个结果已经先＊100了。
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println(nf.format((float) 1 / 50));//自动四舍五入。

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.println(dateFormat.format(calendar.getTime()));
        String resultName = "IDX";
        int idx = 0;
        resultName +=  String.format("%04d",idx);

        System.out.println(resultName);
        List<String> picNameList1 = null;
        picNameList1 = new ArrayList<>();
        picNameList1.add("a");
        picNameList1.add("b");

        picNameList1.toString();*/
    }

    public static List<TowerPoint> loadTower1(List<TowerPoint> towerList) {
        //FileUtils.writeLogFile(0, "call loadTower() method.");
        if (towerList.size() == 0) {
            //sendErrorMessage("杆塔数据为空");
            return null;
        } else if (towerList.size() == 1) {
            //sendErrorMessage("只上传了一个直线塔，生成的航点会在塔的北向和南向，请注意飞行安全！");
            return oneTGeneratePoints(towerList);
            //有两个塔
        } else if (towerList.size() == 2) {
            return twoTGeneratePoints(towerList);
        } else {
            System.out.println("航点个数大于2");
            return mulTGeneratePoints(towerList);
        }
    }

    public static void sendErrorMessage(String mesContent) {
        object = new JSONObject();
        try {
            object.put("errorMsg", mesContent);
        } catch (Exception e) {
            object = new JSONObject();
        }
        messServer.setInfomation((byte) 0, object);

        FileUtils.writeLogFile(2, mesContent);
    }

    public static void sendUserData(byte[] data) {
        final byte[] showData = data;
        KeyManager.getInstance().performAction(sendDataKey, new ActionCallback() {
            @Override
            public void onSuccess() {
                Log.d("senddata", "" + Helper.byte2hex(showData));
                FileUtils.writeLogFile(1, "senddata success:" + Helper.byte2hex(showData));
            }

            @Override
            public void onFailure(@NonNull DJIError error) {
                //ToastUtils.setResultToToast("Not found payload device,please restart the app！");
                //Log.d("senddata",""+Helper.byte2hex(showData));
                sendErrorMessage("没有发现云台");
                FileUtils.writeLogFile(1, "senddata fail:" + Helper.byte2hex(showData));
            }
        }, data);
    }

    public static void sendUserData1(byte[] data) {
        final byte[] showData = data;
        KeyManager.getInstance().performAction(sendDataKey, new ActionCallback() {
            @Override
            public void onSuccess() {
                Log.d("senddata", "success----" + Helper.byte2hex(showData));
                //FileUtils.writeLogFile(1, "senddata success:" + Helper.byte2hex(showData));
            }

            @Override
            public void onFailure(@NonNull DJIError error) {
                //ToastUtils.setResultToToast("Not found payload device,please restart the app！");
                Log.d("senddata","fail----"+Helper.byte2hex(showData));
                //sendErrorMessage("没有发现云台");
                //FileUtils.writeLogFile(1, "senddata fail:" + Helper.byte2hex(showData));
            }
        }, data);
    }

    public void takePhoto() {
        createMAVLink(1, 0);
        FileUtils.writeLogFile(0, "call takePhoto() method.");
    }

    public void takeRecord(int type) {
        if (type == 0) {
            createMAVLink(2, 0);
        } else if (type == 1) {
            createMAVLink(3, 0);
        }
        FileUtils.writeLogFile(0, "call takeRecord() method.");
    }

    public void zooming(int zoomNum) {
        if (zoomNum < 32) {
            createMAVLink(4, zoomNum);
        } else if (zoomNum == 32) {
            createMAVLink(7, zoomNum);
        } else if (zoomNum == 33) {
            createMAVLink(8, zoomNum);
        } else if (zoomNum == 34) {
            createMAVLink(5, zoomNum);
        } else if (zoomNum == 35) {
            createMAVLink(6, zoomNum);
        }
        FileUtils.writeLogFile(0, "call zooming() method.");
    }

    public void changeView(int viewType) {
        createMAVLink(9, viewType);
        gcMode = viewType;
        FileUtils.writeLogFile(0, "call changeView() method.");
    }

    public void getSDStorage() {
        createMAVLink(10, 0);
        FileUtils.writeLogFile(0, "call getSDStorage() method.");
    }

    public void setMissionName(String missionName) {
        this.missonName = missionName;
        byte[] missionNameByte = new byte[missonName.length() + 8];
        missionNameByte[0] = (byte) 253;
        missionNameByte[1] = (byte) missonName.length();
        missionNameByte[2] = (byte) 20;
        missionNameByte[3] = (byte) 255;
        missionNameByte[4] = (byte) 190;
        missionNameByte[5] = (byte) 70;
        for (int i = 6; i < 6 + missonName.length(); i++) {
            missionNameByte[i] = (byte) missonName.charAt(i - 6);
        }
        missionNameByte[missonName.length() + 6] = (byte) 69;
        missionNameByte[missonName.length() + 7] = (byte) 84;
        sendUserData(missionNameByte);
    }

    public void returnCenter() {
        createMAVLink(11, 0);
        FileUtils.writeLogFile(0, "call returnCenter() method.");
    }

    public void pointDirection(int xAxis, int yAxis) {
        int mx = 0;
        int my = 0;
        if (xAxis < 1920 / 2)
            mx = xAxis - 1920 / 2;
        else
            mx = xAxis - 1920 / 2;
        my = 1080 / 2 - yAxis;
        MAVLinkPacket packet = new MAVLinkPacket();
        msg_YT_degree degree = new msg_YT_degree(packet);
        degree.wnatural = getDegree(1, (float) mx / (float) 1920);
        degree.hnatural = getDegree(2, (float) my / (float) 1080);
        packet = degree.pack();
        packet.generateCRC();
        byte[] bytes = packet.encodePacket();
        sendUserData(bytes);
        FileUtils.writeLogFile(0, "call pointDirection() method.");
    }

    public void frameDirection(int xAxis, int yAxis, int frameLength) {
        MAVLinkPacket packet = new MAVLinkPacket();
        msg_YT_sdegree rect = new msg_YT_sdegree(packet);
        rect.zoomValue = (byte) getVirtualZoom(frameLength);
        //以将屏幕中心为坐标系零点，计算框的中心坐标
        int coordinateX = (2 * xAxis + frameLength - 1920) / 2;
        int coordinateY = (1080 - 2 * yAxis - 9 * frameLength / 16) / 2;
        //Log.e("画框命令传输", "wnatural："+getDegree(1,(float)coordinateX/(float)viewWidth)+",hnatural:"+getDegree(2,(float)coordinateY/(float)viewHeight));
        rect.wnatural = getDegree(1, (float) coordinateX / (float) 1920);
        rect.hnatural = getDegree(2, (float) coordinateY / (float) 1080);
        packet = rect.pack();
        packet.generateCRC();
        byte[] bytes = packet.encodePacket();
        sendUserData(bytes);
        FileUtils.writeLogFile(0, "call frameDirection() method.");
    }

    public void moveDirection(float xDegree, float yDegree) {
        MAVLinkPacket packet = new MAVLinkPacket();
        msg_YT_degree degree = new msg_YT_degree(packet);
        degree.wnatural = xDegree;
        degree.hnatural = yDegree;
        packet = degree.pack();
        packet.generateCRC();
        byte[] bytes = packet.encodePacket();
        sendUserData(bytes);
        FileUtils.writeLogFile(0, "call moveDirection() method.");
    }

    public void startTakeOff() {
        if (flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        } else {
            flightController.startTakeoff(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(3, "success", "call startTakeOff() method success");
                    } else {
                        rebackMsg(3, "自动起飞失败，请查看飞机状态", "call startTakeOff() method error message is " + djiError.getDescription());
                    }
                }
            });
        }
    }

    public void cancelTakeOff() {
        if (flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        } else {
            flightController.cancelTakeoff(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(4, "success", "call cancelTakeOff() method success");
                    } else {
                        rebackMsg(4, "停止自动起飞失败，请查看飞机状态", "call cancelTakeOff() method error message is " + djiError.getDescription());
                    }
                }
            });
        }
    }

    public void startLanding() {
        if (flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        } else {
            flightController.startLanding(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(5, "success", "call startLanding() method success");
                    } else {
                        rebackMsg(5, "自动降落失败，请查看飞机状态", "call startLanding() method error message is " + djiError.getDescription());
                    }
                }
            });
        }
    }

    public void cancelLanding() {
        if (flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        } else {
            flightController.cancelLanding(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(6, "success", "call cancelLanding() method success");
                    } else {
                        rebackMsg(6, "停止自动降落失败，请查看飞机状态", "call cancelLanding() method error message is " + djiError.getDescription());
                    }
                }
            });
        }
    }

    public List<WayPoint> pastLoadTower(List<TowerPoint> towerList) {
//        hfdWayPointList.clear();
//        List<WayPoint> prePointList = new ArrayList<WayPoint>();
//        List<WayPoint> postPointList = new ArrayList<WayPoint>();
//        if(towerList.size() == 0){
//            sendErrorMessage("杆塔数据为空");
//            return null;
//        } else if(towerList.size() == 1){
//            //sendErrorMessage("只上传了一个直线塔，生成的航点会在塔的北向和南向，请注意飞行安全！");
//            return oneTGeneratePoints(towerList);
//            //有两个塔
//        }else if(towerList.size()==2){
//            return twoTGeneratePoints(towerList);
//        } else {
//            System.out.println("航点个数大于2");
//            return mulTGeneratePoints(towerList);
//        }
//        if (towerList.size() == 0) {
//            sendErrorMessage("杆塔数据为空");
//        } else {
//            hfdWayPointList = FileUtils.pastLoadXml(towerList);
//        }
//        return hfdWayPointList;
        return null;
    }

    public List<WayPoint> loadTower(List<TowerPoint> towerList) {
        List<WayPoint> returnListMarkPoint = new ArrayList<WayPoint>();
        hfdWayPointList.clear();
//        List<WayPoint> prePointList = new ArrayList<WayPoint>();
//        List<WayPoint> postPointList = new ArrayList<WayPoint>();
//        if(towerList.size() == 0){
//            sendErrorMessage("杆塔数据为空");
//            return null;
//        } else if(towerList.size() == 1){
//            //sendErrorMessage("只上传了一个直线塔，生成的航点会在塔的北向和南向，请注意飞行安全！");
//            return oneTGeneratePoints(towerList);
//            //有两个塔
//        }else if(towerList.size()==2){
//            return twoTGeneratePoints(towerList);
//        } else {
//            System.out.println("航点个数大于2");
//            return mulTGeneratePoints(towerList);
//        }
        if (towerList.size() == 0) {
            sendErrorMessage("杆塔数据为空");
        } else {
            hfdWayPointList = FileUtils.loadTower(towerList);
            for (int i = 0; i < hfdWayPointList.size(); i++) {
                WayPoint wayPoint = new WayPoint();
                wayPoint.setId(hfdWayPointList.get(i).getId());
                wayPoint.setTowerNum(hfdWayPointList.get(i).getTowerNum());
                wayPoint.setSeqNumber(hfdWayPointList.get(i).getSeqNumber());
                wayPoint.setLatitude(hfdWayPointList.get(i).getLatitude());
                wayPoint.setLongitude(hfdWayPointList.get(i).getLongitude());
                wayPoint.setAltitude(hfdWayPointList.get(i).getAltitude());
                wayPoint.setToward(hfdWayPointList.get(i).getToward());
                wayPoint.setSide(hfdWayPointList.get(i).getSide());
                returnListMarkPoint.add(wayPoint);
            }
        }

        return returnListMarkPoint;
    }

    public List<TowerPoint> loadMarkPoint(List<TowerPoint> towerList) {
        //flightController.startTakeoff();
        FileUtils.writeLogFile(0, "call loadMarkPoint() method.");
        return null;
    }

    public void oneButtonStart() {
        FileUtils.writeLogFile(0, "call oneButtonStart() method.");

//        if (flightController.getRTK() != null){
//            flightController.getRTK().setStateCallback(new RTKState.Callback() {
//                @Override
//                public void onUpdate(RTKState rtkState) {
//
//                    flightController.setHomeLocation(new LocationCoordinate2D(rtkState.getFusionMobileStationLocation().getLatitude(), rtkState.getFusionMobileStationLocation().getLongitude()), new CommonCallbacks.CompletionCallback() {
//                        @Override
//                        public void onResult(DJIError djiError) {
//                            if(djiError == null)
//                                FileUtils.writeLogFile(0, "设置home点成功.");
//                            else
//                                FileUtils.writeLogFile(0, "设置rtk home点失败，注意飞行安全.");
//                            MAVLinkPacket packet = new MAVLinkPacket();
//                            msg_camera_auto_takepic autoTakepic = new msg_camera_auto_takepic(packet);
//                            autoTakepic.autoNum = (byte) 1;
//                            packet = autoTakepic.pack();
//                            packet.generateCRC();
//                            sendUserData(packet.encodePacket());
//                        }
//                    });
//                }
//            });
//        }else{
            MAVLinkPacket packet = new MAVLinkPacket();
            msg_camera_auto_takepic autoTakepic = new msg_camera_auto_takepic(packet);
            autoTakepic.autoNum = (byte) 1;
            packet = autoTakepic.pack();
            packet.generateCRC();
            sendUserData(packet.encodePacket());
//            FileUtils.writeLogFile(1, "无法获取RTK数据，请注意飞行安全.");
//        }

        picNameList.clear();
    }

    public void powerFailure() {
        MAVLinkPacket packet = new MAVLinkPacket();
        msg_waypoint_power_fail powerFail = new msg_waypoint_power_fail(packet);
        packet = powerFail.pack();
        packet.generateCRC();
        sendUserData(packet.encodePacket());
    }

    public void uploadPoint(List<WayPoint> upWayPointList) {
//        List<WayPoint> receivePointList = new ArrayList<>(upWayPointList);
//        wayPointList = new ArrayList<WayPoint>();
//        wayPointList.addAll(receivePointList);
//        if (receivePointList.size() == 0) {
//            sendErrorMessage("航点数据为空");
//        } else {
//            pointNum = 0;
//            MAVLinkPacket packet = new MAVLinkPacket();
//            msg_waypoint_upload pointUpload = new msg_waypoint_upload(packet);
//            pointUpload.towerNum = Integer.parseInt(wayPointList.get(pointNum).getTowerNum().substring(1));
//            pointUpload.pointType = (byte) wayPointList.get(pointNum).getPointType();
//            pointUpload.variety = (byte) wayPointList.get(pointNum).getVariety();
//            pointUpload.seqNum = pointNum;
//            pointUpload.latitude = wayPointList.get(pointNum).getLatitude();
//            pointUpload.longitude = wayPointList.get(pointNum).getLongitude();
//            pointUpload.altitude = wayPointList.get(pointNum).getAltitude();
//            pointUpload.toward = wayPointList.get(pointNum).getToward();
//            pointUpload.pitch = wayPointList.get(pointNum).getApitch();
//            pointUpload.angle = wayPointList.get(pointNum).getAngle();
//            pointUpload.objType = (byte) wayPointList.get(pointNum).getObject();
//            pointUpload.side = (byte) wayPointList.get(pointNum).getSide();
//            pointUpload.totalNum = wayPointList.size();
//            packet = pointUpload.pack();
//            packet.generateCRC();
//            sendUserData(packet.encodePacket());
//        }
//        if (upWayPointList.containsAll(hfdWayPointList) && hfdWayPointList.containsAll(upWayPointList)) {
//            Log.d("uploadfile", "相等");
//        }
        if (hfdWayPointList.size() == 0) {
            sendErrorMessage("航点数据为空");
        } else {
            pointNum = 0;
            myUploadPoint();
        }
    }

    public void uploadPoint1(List<TowerPoint> towerList) {
        backPointList.clear();
        backPointList = towerList;
        realPoint = backPointList.get(realSeq);
        realSeq++;
        mission = createWaypointMission();
        DJIError djiError = waypointMissionOperator.loadMission(mission);
        if (djiError == null) {
            FileUtils.writeLogFile(0, "start uploadPoint().");
            if (WaypointMissionState.READY_TO_RETRY_UPLOAD.equals(waypointMissionOperator.getCurrentState())
                    || WaypointMissionState.READY_TO_UPLOAD.equals(waypointMissionOperator.getCurrentState())) {
                waypointMissionOperator.uploadMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError == null) {
                            object = new JSONObject();
                            try {
                                object.put("result", "success");
                            } catch (Exception e) {
                                object = new JSONObject();
                            }
                            messServer.setInfomation((byte) 7, object);
                            FileUtils.writeLogFile(0, "end uploadPoint()");
                        } else {
                            rebackMsg(7, "上传航点到飞机失败", "call uploadPoint() errorMsg is 上传航点到飞机失败");
                        }
                    }
                });
            } else {
                rebackMsg(7, "飞机尚未准备就绪", "call uploadPoint() errorMsg is 飞机尚未准备就绪");
            }
        } else {
            rebackMsg(7, "航点出错，请检查航点信息", "call uploadPoint() errorMsg is 航点出错，请检查航点信息");
        }
    }

    public void startMission() {
        //flightController.startTakeoff();
        FileUtils.writeLogFile(0, "call startMission() method.");
        if (mission != null) {
            if (WaypointMissionState.READY_TO_EXECUTE.equals(waypointMissionOperator.getCurrentState())) {
                waypointMissionOperator.startMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError == null) {
                            object = new JSONObject();
                            try {
                                object.put("result", "start");
                                object.put("tower", realPoint.getTowerNum());
                                object.put("point", realPoint.getId());
                            } catch (Exception e) {
                                object = new JSONObject();
                            }
                            messServer.setInfomation((byte) 15, object);
                            FileUtils.writeLogFile(0, "");
                        } else {
                            rebackMsg(8, "开始自动飞行失败", "call startMission() errormsg is " + djiError.getDescription());
                        }
                    }
                });
            } else {
                rebackMsg(8, "飞行航线飞行状态不对，请重新规划上传航线", "call startMission() 飞行航线飞行状态不对，请重新规划上传航线");
            }
        } else {
            rebackMsg(8, "航线任务为不能空，请上传航点", "call startMission() 航线任务为不能空，请上传航点");
        }
    }

    public void pauseMission() {
        FileUtils.writeLogFile(0, "call pauseMission() method.");
/*        if (waypointMissionOperator == null) {
            waypointMissionOperator = MissionControl.getInstance().getWaypointMissionOperator();
        }
        if (WaypointMissionState.EXECUTING.equals(waypointMissionOperator.getCurrentState())) {
            waypointMissionOperator.pauseMission(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(9, "success", "call pauseMission() success now flight state is " + waypointMissionOperator.getCurrentState());
                    } else {
                        rebackMsg(9, "暂停航点飞行发送错误", "call pauseMission() error message is " + djiError.getDescription());
                    }
                }
            });
        } else {
            rebackMsg(9, "飞机状态错误，不能执行暂停飞行方法", "call pauseMission() 无法执行暂停方法，飞机当前状态为" + waypointMissionOperator.getCurrentState());
        }*/
        createMAVLink(12, 1);
    }

    public void resumeMission() {
        FileUtils.writeLogFile(0, "call resumeMission() method.");
/*        if (WaypointMissionState.EXECUTION_PAUSED.equals(waypointMissionOperator.getCurrentState())) {
            waypointMissionOperator.resumeMission(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(10, "success", "call resumeMission() success now flight state is " + waypointMissionOperator.getCurrentState());
                    } else {
                        rebackMsg(10, "继续航点飞行发送错误", "call resumeMission() error message is " + djiError.getDescription());
                    }
                }
            });
        } else {
            rebackMsg(10, "飞机状态错误，不能继续航点飞行", "call resumeMission() 无法执行继续飞行方法，飞机当前状态为" + waypointMissionOperator.getCurrentState());
        }*/
        createMAVLink(12, 2);
    }

    public void breakpointMission() {
        FileUtils.writeLogFile(0, "call breakpointMission() method.");
        if (realSeq <= backPointList.size()) {
            if (WaypointMissionState.EXECUTING.equals(waypointMissionOperator.getCurrentState())) {
                rebackMsg(11, "success", "call breakpointMission() success now flight state is " + waypointMissionOperator.getCurrentState());
            }
        } else {
            rebackMsg(11, "跳过杆塔失败，航线飞行已完成", "call breakpointMission() success now flight state is " + waypointMissionOperator.getCurrentState());
        }
    }

    public void stopMission() {
        FileUtils.writeLogFile(0, "call stopMission() method.");
//        if (WaypointMissionState.EXECUTING.equals(waypointMissionOperator.getCurrentState())
//                || WaypointMissionState.EXECUTION_PAUSED.equals(waypointMissionOperator.getCurrentState())) {
//            waypointMissionOperator.stopMission(new CommonCallbacks.CompletionCallback() {
//                @Override
//                public void onResult(DJIError djiError) {
//                    if(djiError==null){
//                        rebackMsg(12,"success","call stopMission() success now flight state is "+waypointMissionOperator.getCurrentState());
//                    }else{
//                        rebackMsg(12,"停止航点飞行发送错误","call stopMission() error message is "+djiError.getDescription());
//                    }
//                }
//            });
//        }else{
//            rebackMsg(12,"飞机状态错误，无法执行停止航线飞行操作","call resumeMission() 无法执行继续飞行方法，飞机当前状态为"+waypointMissionOperator.getCurrentState());
//        }
        createMAVLink(12, 3);
    }

    public void startGoHome() {
        if (flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        } else {
            flightController.startGoHome(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(13, "success", "call startGoHome() method success");
                    } else {
                        rebackMsg(13, "返航，请查看飞机状态", "call startGoHome() method error message is " + djiError.getDescription());
                    }
                }
            });
        }
    }

    public void stopGoHome() {
        if (flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        } else {
            flightController.cancelGoHome(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(14, "success", "call stopGoHome() method success");
                    } else {
                        rebackMsg(14, "停止返航失败，请查看飞机状态", "call stopGoHome() method error message is " + djiError.getDescription());
                    }
                }
            });
        }
    }

    public JSONObject getFlightData() {
        FileUtils.writeLogFile(0, "call getFlightData() method.");
        JSONObject dataObject = new JSONObject();
        try {
            if (flightController == null) {
                sendErrorMessage("无法获取飞机");
                dataObject.put("speed", "0");
                dataObject.put("altitude", "0");
                dataObject.put("longitude", "0");
                dataObject.put("latitude", "0");
                dataObject.put("battery1", "0");
                dataObject.put("battery2", "0");
                dataObject.put("compass", "0");
            } else {
                aircraft.getBatteries().get(0).setStateCallback(new BatteryState.Callback() {
                    @Override
                    public void onUpdate(BatteryState batteryState) {
                        battery1 = batteryState.getChargeRemainingInPercent();
                    }
                });
                aircraft.getBatteries().get(1).setStateCallback(new BatteryState.Callback() {
                    @Override
                    public void onUpdate(BatteryState batteryState) {
                        battery2 = batteryState.getChargeRemainingInPercent();
                    }
                });

                float speedx = flightController.getState().getVelocityX();
                float speedy = flightController.getState().getVelocityY();
                float speedz = flightController.getState().getVelocityZ();
                float speed = (float) Math.sqrt(Math.pow(speedx, 2) + Math.pow(speedx, 2) + Math.pow(speedz, 2));
                dataObject.put("speed", speed + "");
                dataObject.put("altitude", "" + currentAltidude);
                dataObject.put("longitude", "0" + liveLatLng.longitude);
                dataObject.put("latitude", "0" + liveLatLng.latitude);
                dataObject.put("battery1", battery1);
                dataObject.put("battery2", battery2);
                dataObject.put("compass", compassData);
            }
        } catch (JSONException e) {
            sendErrorMessage("程序异常");
            FileUtils.writeLogFile(2, "call getFlightData() error is " + e.getMessage());
        }
        return dataObject;
    }

    public void setInspectionDir(int h, int v) {
        TowHor = h;
        TowVer = v;
    }

    public void setLogLevel(int mLogLevel) {
        logLevelType = mLogLevel;
    }

    public void allowGoHome(){
        createMAVLink(12, 5);
    }

    private void getFlightThread() {
        new Thread(new Runnable() {
            public void run() {
                Log.d(TAG, "getFlightThread");
                while (true) {
                    try {
                        if (aircraft != null && flightController != null) {
                            //Log.d("aircraft is", "aircraft"+aircraft);
                            //获取home点
                            //Log.d("aircraft home is", "getHomeFlag"+getHomeFlag);
                            if (getHomeFlag == 0) {
                                flightController.getHomeLocation(mDJICompletionCallbackc);
                            } else {
                                //获取飞机gps信息模块代码
                                flightController.setStateCallback(new FlightControllerState.Callback() {
                                    @Override
                                    public void onUpdate(FlightControllerState flightControllerState) {
                                        double latitude = flightControllerState.getAircraftLocation().getLatitude();
                                        double longitude = flightControllerState.getAircraftLocation().getLongitude();
                                        liveLatLng = new LatLng(latitude, longitude);
                                        currentAltidude = flightControllerState.getAircraftLocation().getAltitude();
                                    }
                                });
                            }
                            compassData = flightController.getCompass().getHeading();
                            Thread.sleep(1000);
                        } else {
                            aircraft = (Aircraft) DJISDKManager.getInstance().getProduct();
                            flightController = aircraft.getFlightController();
                            //Log.d("aircraft is", "aircraft"+aircraft);
                            Thread.sleep(1000);
                        }
//						createMAVLink(19);
//						Thread.sleep(1000);
                    } catch (Exception e) {
                        //Log.d("aircraft", "get aircraft error");
                    }
                }
            }
        }).start();
    }

    private void initDJIkey() {
        Log.d(TAG, "initDJIkey");
        getDataKey = PayloadKey.create(PayloadKey.GET_DATA_FROM_PAYLOAD, 0);
        sendDataKey = PayloadKey.create(PayloadKey.SEND_DATA_TO_PAYLOAD, 0);

        if (KeyManager.getInstance() != null) {
            KeyManager.getInstance().addListener(getDataKey, getDataListener);
        }

    }

    private void postWaypoint(int actionType, int seqNum) {
        if (actionType == 1) {
            pointNum++;
            if (pointNum < hfdWayPointList.size()) {
                myUploadPoint();
            } else {
                rebackMsg(7, "success", "上传航点成功");
            }
        } else if (actionType == 2) {
            rebackMsg(7, "fail", "天空端返回收到航点信息有误 ");
        } else {
            object = new JSONObject();
            try {
                object.put("result", "start");
                object.put("tower", hfdWayPointList.get(seqNum).getId());
                object.put("point", hfdWayPointList.get(seqNum).getSeqNumber());
            } catch (Exception e) {
                object = new JSONObject();
            }
            messServer.setInfomation((byte) 15, object);
            //FileUtils.writeLogFile(0, "");
            if (seqNum == hfdWayPointList.size() - 1) {
                rebackMsg(15, "success", "巡检结束");
                rebackMsg(15, "picNameList", picNameList);
            }
            if(seqNum == 2){
                stopTimer();
            }
        }
    }

    //回调函数们
    private void settingCallback() {
        Log.d(TAG, "settingCallback");
        //获取home点坐标的回调函数
        mDJICompletionCallbackc = new CommonCallbacks.CompletionCallbackWith<LocationCoordinate2D>() {
            @Override
            public void onSuccess(LocationCoordinate2D var1) {
                //Log.d("aircraft","");
                getHomeFlag = 1;
            }

            @Override
            public void onFailure(DJIError var1) {
                ToastUtils.setResultToToast(var1.getDescription());
            }
        };
    }

    private void createMAVLink(int type, int content) {
        MAVLinkPacket packet = new MAVLinkPacket();
        switch (type) {
            case 1:
                msg_picture_press pressPicture = new msg_picture_press(packet);
                pressPicture.jingdu = (float) liveLatLng.longitude;
                pressPicture.weidu = (float) liveLatLng.latitude;
                //pressPicture.capture = 1;
                packet = pressPicture.pack();
                packet.generateCRC();
                byte[] bytepress = packet.encodePacket();
                sendUserData(bytepress);
                break;
            case 2:
                msg_radio_start radioStart = new msg_radio_start(packet);
                packet = radioStart.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            case 3:
                msg_radio_end radioEnd = new msg_radio_end(packet);
                packet = radioEnd.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            case 4:
                msg_camera_zoom camera_zoom = new msg_camera_zoom(packet);
                if (content < 6)
                    content = 6;
                camera_zoom.type = (byte) content;
                packet = camera_zoom.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            //相机变焦W开始
            case 5:
                msg_picture_zoom zoom = new msg_picture_zoom(packet);
                zoom.type = (byte) 177;
                packet = zoom.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            //相机变焦W结束
            case 6:
                msg_picture_zoom zoom1 = new msg_picture_zoom(packet);
                zoom1.type = (byte) 178;
                packet = zoom1.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            //相机变焦T开始
            case 7:
                msg_picture_zoom zoom2 = new msg_picture_zoom(packet);
                zoom2.type = (byte) 161;
                packet = zoom2.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            //相机变焦T结束
            case 8:
                msg_picture_zoom zoom3 = new msg_picture_zoom(packet);
                zoom3.type = (byte) 162;
                packet = zoom3.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            case 9:
                msg_GC_mode gcMode = new msg_GC_mode(packet);
                if (content == 0) {
                    gcMode.type = (byte) 01;
                    gcMode.fieldView = (byte) 02;
                } else if (content == 1) {
                    gcMode.type = (byte) 01;
                    gcMode.fieldView = (byte) 01;
                } else if (content == 2) {
                    gcMode.type = (byte) 02;
                    gcMode.fieldView = (byte) 02;
                } else if (content == 3) {
                    gcMode.type = (byte) 02;
                    gcMode.fieldView = (byte) 01;
                }
                packet = gcMode.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            case 10:
                msg_get_storage getStorage = new msg_get_storage(packet);
                packet = getStorage.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            case 11:
                msg_YT_reset reset = new msg_YT_reset(packet);
                packet = reset.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            case 12:
                msg_cal_stop calStop = new msg_cal_stop(packet);
                calStop.orderByte = (byte) content;
                packet = calStop.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
            //发送时间校准信息
            case 13:
                msg_time_calibration calibration = new msg_time_calibration(packet);
                Calendar cal = Calendar.getInstance();
                calYear = cal.get(Calendar.YEAR);
                calMonth = cal.get(Calendar.MONTH);
                calDay = cal.get(Calendar.DATE);
                calHour = cal.get(Calendar.HOUR_OF_DAY);
                calMinute = cal.get(Calendar.MINUTE);
                calSecond = cal.get(Calendar.SECOND);
                calibration.c_year = calYear;
                calibration.c_month = (byte) calMonth;
                calibration.c_day = (byte) calDay;
                calibration.c_hour = (byte) calHour;
                calibration.c_minute = (byte) calMinute;
                calibration.c_second = (byte) calSecond;
                packet = calibration.pack();
                packet.generateCRC();
                byte[] calibrationByte = packet.encodePacket();
                sendUserData(calibrationByte);
                break;
            case 14:
                msg_gimbal_version gimbalVersion = new msg_gimbal_version(packet);
                gimbalVersion.type = (byte) 1;
                packet = gimbalVersion.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
        }
    }

    public float getDegree(int type, float perWHeight) {
        //Log.d("命令msg_YT_degree", "type="+type+",百分比perWHeight="+perWHeight);
        //Log.d("命令msg_YT_degree", "gcM="+gcMode+",qZoom="+qZoom);
        float moveDegree = 0.00f;
        //水平角
        if (type == 1) {
            //广角
            if (gcMode == 0 || gcMode == 2) {
                //moveDegree = (float)(65.76*perWHeight);
                //自己测量37.85305 厂商提供35.8
                //moveDegree = (float)(37.85305*perWHeight);
                moveDegree = UVC_Horizontal_angle * perWHeight;
            } else {
                if (qZoom <= 6) {
                    moveDegree = Qx30U_Zoom_H6 * perWHeight;
                } else if (qZoom == 7) {
                    moveDegree = Qx30U_Zoom_H7 * perWHeight;
                } else if (qZoom == 8) {
                    moveDegree = Qx30U_Zoom_H8 * perWHeight;
                } else if (qZoom == 9) {
                    moveDegree = Qx30U_Zoom_H9 * perWHeight;
                } else if (qZoom == 10) {
                    moveDegree = Qx30U_Zoom_H10 * perWHeight;
                } else if (qZoom == 11) {
                    moveDegree = Qx30U_Zoom_H11 * perWHeight;
                } else if (qZoom == 12) {
                    moveDegree = Qx30U_Zoom_H12 * perWHeight;
                } else if (qZoom == 13) {
                    moveDegree = Qx30U_Zoom_H13 * perWHeight;
                } else if (qZoom == 14) {
                    moveDegree = Qx30U_Zoom_H14 * perWHeight;
                } else if (qZoom == 15) {
                    moveDegree = Qx30U_Zoom_H15 * perWHeight;
                } else if (qZoom == 16) {
                    moveDegree = Qx30U_Zoom_H16 * perWHeight;
                } else if (qZoom == 17) {
                    moveDegree = Qx30U_Zoom_H17 * perWHeight;
                } else if (qZoom == 18) {
                    moveDegree = Qx30U_Zoom_H18 * perWHeight;
                } else if (qZoom == 19) {
                    moveDegree = Qx30U_Zoom_H19 * perWHeight;
                } else if (qZoom == 20) {
                    moveDegree = Qx30U_Zoom_H20 * perWHeight;
                } else if (qZoom == 21) {
                    moveDegree = Qx30U_Zoom_H21 * perWHeight;
                } else if (qZoom == 22) {
                    moveDegree = Qx30U_Zoom_H22 * perWHeight;
                } else if (qZoom == 23) {
                    moveDegree = Qx30U_Zoom_H23 * perWHeight;
                } else if (qZoom == 24) {
                    moveDegree = Qx30U_Zoom_H24 * perWHeight;
                } else if (qZoom == 25) {
                    moveDegree = Qx30U_Zoom_H25 * perWHeight;
                } else if (qZoom == 26) {
                    moveDegree = Qx30U_Zoom_H26 * perWHeight;
                } else if (qZoom == 27) {
                    moveDegree = Qx30U_Zoom_H27 * perWHeight;
                } else if (qZoom == 28) {
                    moveDegree = Qx30U_Zoom_H28 * perWHeight;
                } else if (qZoom == 29) {
                    moveDegree = Qx30U_Zoom_H29 * perWHeight;
                } else if (qZoom == 30) {
                    moveDegree = Qx30U_Zoom_H30 * perWHeight;
                } else if (qZoom == 31) {
                    moveDegree = Qx30U_Zoom_H31 * perWHeight;
                }
            }
            //俯仰角
        } else {
            //广角
            if (gcMode == 0 || gcMode == 2) {
                //moveDegree = (float)49.32*perWHeight;
                //自己测量21.34598 厂商提供21.6
                //moveDegree = (float)21.34598*perWHeight;
                moveDegree = UVC_Vertical_angle * perWHeight;
            } else {
                if (qZoom <= 6) {
                    moveDegree = Qx30U_Zoom_V6 * perWHeight;
                } else if (qZoom == 7) {
                    moveDegree = Qx30U_Zoom_V7 * perWHeight;
                } else if (qZoom == 8) {
                    moveDegree = Qx30U_Zoom_V8 * perWHeight;
                } else if (qZoom == 9) {
                    moveDegree = Qx30U_Zoom_V9 * perWHeight;
                } else if (qZoom == 10) {
                    moveDegree = Qx30U_Zoom_V10 * perWHeight;
                } else if (qZoom == 11) {
                    moveDegree = Qx30U_Zoom_V11 * perWHeight;
                } else if (qZoom == 12) {
                    moveDegree = Qx30U_Zoom_V12 * perWHeight;
                } else if (qZoom == 13) {
                    moveDegree = Qx30U_Zoom_V13 * perWHeight;
                } else if (qZoom == 14) {
                    moveDegree = Qx30U_Zoom_V14 * perWHeight;
                } else if (qZoom == 15) {
                    moveDegree = Qx30U_Zoom_V15 * perWHeight;
                } else if (qZoom == 16) {
                    moveDegree = Qx30U_Zoom_V16 * perWHeight;
                } else if (qZoom == 17) {
                    moveDegree = Qx30U_Zoom_V17 * perWHeight;
                } else if (qZoom == 18) {
                    moveDegree = Qx30U_Zoom_V18 * perWHeight;
                } else if (qZoom == 19) {
                    moveDegree = Qx30U_Zoom_V19 * perWHeight;
                } else if (qZoom == 20) {
                    moveDegree = Qx30U_Zoom_V20 * perWHeight;
                } else if (qZoom == 21) {
                    moveDegree = Qx30U_Zoom_V21 * perWHeight;
                } else if (qZoom == 22) {
                    moveDegree = Qx30U_Zoom_V22 * perWHeight;
                } else if (qZoom == 23) {
                    moveDegree = Qx30U_Zoom_V23 * perWHeight;
                } else if (qZoom == 24) {
                    moveDegree = Qx30U_Zoom_V24 * perWHeight;
                } else if (qZoom == 25) {
                    moveDegree = Qx30U_Zoom_V25 * perWHeight;
                } else if (qZoom == 26) {
                    moveDegree = Qx30U_Zoom_V26 * perWHeight;
                } else if (qZoom == 27) {
                    moveDegree = Qx30U_Zoom_V27 * perWHeight;
                } else if (qZoom == 28) {
                    moveDegree = Qx30U_Zoom_V28 * perWHeight;
                } else if (qZoom == 29) {
                    moveDegree = Qx30U_Zoom_V29 * perWHeight;
                } else if (qZoom == 30) {
                    moveDegree = Qx30U_Zoom_V30 * perWHeight;
                } else if (qZoom == 31) {
                    moveDegree = Qx30U_Zoom_V31 * perWHeight;
                }
            }
        }
        return (float) (Math.round(moveDegree * 100)) / 100;
    }

    //根据画的框的大小返回应该变到的zoom值
    public int getVirtualZoom(int rectX) {
        int blurZoom = 6;
        float newAngle = (float) rectX / (float) 1920 * UVC_Horizontal_angle;

        //Log.d("画框命令传输", "newAngle："+newAngle);
        if (newAngle >= Qx30U_Zoom_H19) {
            if (newAngle > Qx30U_Zoom_H8)
                blurZoom = 8;
            else if (newAngle > Qx30U_Zoom_H9)
                blurZoom = 9;
            else if (newAngle > Qx30U_Zoom_H10)
                blurZoom = 10;
            else if (newAngle > Qx30U_Zoom_H11)
                blurZoom = 11;
            else if (newAngle > Qx30U_Zoom_H12)
                blurZoom = 12;
            else if (newAngle > Qx30U_Zoom_H13)
                blurZoom = 13;
            else if (newAngle > Qx30U_Zoom_H14)
                blurZoom = 14;
            else if (newAngle > Qx30U_Zoom_H15)
                blurZoom = 15;
            else if (newAngle > Qx30U_Zoom_H16)
                blurZoom = 16;
            else if (newAngle > Qx30U_Zoom_H17)
                blurZoom = 17;
            else if (newAngle > Qx30U_Zoom_H18)
                blurZoom = 18;
            else
                blurZoom = 19;
        } else {
            if (newAngle > Qx30U_Zoom_H20)
                blurZoom = 20;
            else if (newAngle > Qx30U_Zoom_H21)
                blurZoom = 21;
            else if (newAngle > Qx30U_Zoom_H22)
                blurZoom = 22;
            else if (newAngle > Qx30U_Zoom_H23)
                blurZoom = 23;
            else if (newAngle > Qx30U_Zoom_H24)
                blurZoom = 24;
            else if (newAngle > Qx30U_Zoom_H25)
                blurZoom = 25;
            else if (newAngle > Qx30U_Zoom_H26)
                blurZoom = 26;
            else if (newAngle > Qx30U_Zoom_H27)
                blurZoom = 27;
            else if (newAngle > Qx30U_Zoom_H28)
                blurZoom = 28;
            else if (newAngle > Qx30U_Zoom_H29)
                blurZoom = 29;
            else if (newAngle > Qx30U_Zoom_H30)
                blurZoom = 30;
            else
                blurZoom = 31;
        }
        return blurZoom;
    }

    private WaypointMission createWaypointMission() {
        WaypointMission.Builder builder = new WaypointMission.Builder();
        builder.autoFlightSpeed(5f);
        builder.maxFlightSpeed(10f);
        //当飞机跟遥控器失去连接的时候是否停止航点飞行
        builder.setExitMissionOnRCSignalLostEnabled(false);
        builder.finishedAction(WaypointMissionFinishedAction.NO_ACTION);
        //normal 直线飞行 curved 圆弧飞行
        builder.flightPathMode(WaypointMissionFlightPathMode.NORMAL);
        //飞往第一个航点的飞行方式 safely 飞到跟第一个航点一样高后再飞 POINT_TO_POINT 直接从当前地方飞到第一个航点
        builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY);
        //以机头为正方向
        builder.headingMode(WaypointMissionHeadingMode.AUTO);
        builder.repeatTimes(1);

        //List<Waypoint> waypointList = new ArrayList<>();

        Waypoint eachWaypoint = new Waypoint(realPoint.getLatitude(), realPoint.getLongitude(), realPoint.getAltitude());
        eachWaypoint.addAction(new WaypointAction(WaypointActionType.ROTATE_AIRCRAFT, (int) realPoint.getToward()));
        eachWaypoint.addAction(new WaypointAction(WaypointActionType.STAY, 3000));
        //waypointList.add(eachWaypoint);
        //builder.waypointList(waypointList).waypointCount(waypointList.size());
        builder.addWaypoint(eachWaypoint);
        return builder.build();
    }

    private void rebackMsg(int type, String rebackContent, String noteLog) {
        object = new JSONObject();
        try {
            object.put("result", rebackContent);
        } catch (Exception e) {
            object = new JSONObject();
        }
        messServer.setInfomation((byte) type, object);
        FileUtils.writeLogFile(2, noteLog);
    }

    private void rebackMsg(int type, String rebackContent, List<String> mPicNameList) {
        try {
            object = new JSONObject();
            object.put("result", "picname");
            object.put(rebackContent, mPicNameList);
        } catch (Exception e) {
            object = new JSONObject();
        }
        messServer.setInfomation((byte) type, object);
        FileUtils.writeLogFile(2, "返回拍照名称列表:"+mPicNameList.toString());
    }

    private JSONObject getWindWarning() {
        FileUtils.writeLogFile(0, "call getWindWarning() method.");
        JSONObject dataObject = new JSONObject();
        try {
            if (flightController != null) {
                if (mFControlState == null) {
                    mFControlState = flightController.getState();
                }
                FlightWindWarning flightWindWarning = mFControlState.getFlightWindWarning();
                dataObject.put("result", "success");
                dataObject.put("alarmLevel", flightWindWarning.toString());
            } else {
                dataObject.put("result", "fail");
            }

        } catch (JSONException e) {
            sendErrorMessage("程序异常");
            FileUtils.writeLogFile(2, "call getWindWarning() error is " + e.getMessage());
        }
        return dataObject;
    }

    public void unInitListener() {
        KeyManager.getInstance().removeListener(getDataListener);
        DJIPayloadUsbDataManager.getInstance().setDataListener(null);
    }

    private void stopTimer() {
        if (zoomTimer != null) {
            zoomTimer.cancel();
            zoomTimer = null;
        }
        if (zoomTimerTask != null) {
            zoomTimerTask.cancel();
            zoomTimerTask = null;
        }
    }

    private void myUploadPoint() {
        MAVLinkPacket packet = new MAVLinkPacket();
        msg_waypoint_upload pointUpload = new msg_waypoint_upload(packet);
        pointUpload.towerNum = Integer.parseInt(hfdWayPointList.get(pointNum).getTowerNum().substring(1));
        pointUpload.pointType = (byte) hfdWayPointList.get(pointNum).getPointType();
        pointUpload.variety = (byte) hfdWayPointList.get(pointNum).getVariety();
        pointUpload.seqNum = pointNum;
        pointUpload.latitude = hfdWayPointList.get(pointNum).getLatitude();
        pointUpload.longitude = hfdWayPointList.get(pointNum).getLongitude();
        pointUpload.altitude = hfdWayPointList.get(pointNum).getAltitude();
        pointUpload.toward = hfdWayPointList.get(pointNum).getToward();
        pointUpload.pitch = hfdWayPointList.get(pointNum).getApitch();
        pointUpload.angle = hfdWayPointList.get(pointNum).getAngle();
        pointUpload.objType = (byte) hfdWayPointList.get(pointNum).getObject();
        pointUpload.side = (byte) hfdWayPointList.get(pointNum).getSide();
        pointUpload.trend = (byte) hfdWayPointList.get(pointNum).getOrientation();
        pointUpload.leftZoomPosition = (byte) hfdWayPointList.get(pointNum).getLeftZoomPosition();
        pointUpload.zoomPosition = (byte) hfdWayPointList.get(pointNum).getZoomPosition();
        pointUpload.rightZoomPosition = (byte) hfdWayPointList.get(pointNum).getRightZoomPosition();
        pointUpload.totalNum = hfdWayPointList.size();
        packet = pointUpload.pack();
        packet.generateCRC();
        sendUserData(packet.encodePacket());
    }
}

class WarningTimerTask extends TimerTask {
    @Override
    public void run() {
        JSONObject dataObject = new JSONObject();
        try {
            if (HFDManager.flightController != null) {
                if (HFDManager.mFControlState == null) {
                    HFDManager.mFControlState = HFDManager.flightController.getState();
                }
                FlightWindWarning flightWindWarning = HFDManager.mFControlState.getFlightWindWarning();
                dataObject.put("result", "success");
                dataObject.put("alarmLevel", flightWindWarning.toString());
                HFDManager.messServer.setInfomation((byte) 18, dataObject);
            } else {
                FileUtils.writeLogFile(2, "flightController is null " + HFDManager.flightController);
            }

        } catch (JSONException e) {
            FileUtils.writeLogFile(2, "timer run error is " + e.getMessage());
        }
    }
}

/**
 * 初始化后每隔一秒发送一次获取zoom值信息，诱发天空端下发对时命令
 *
 * @author Arvin zeng
 * @Time 2020-6-20 16:03
 */
class ZoomTimerTask extends TimerTask {
    @Override
    public void run() {
        MAVLinkPacket packet = new MAVLinkPacket();
        msg_camera_realzoom realzoom = new msg_camera_realzoom(packet);
        packet = realzoom.pack();
        packet.generateCRC();
        HFDManager.sendUserData1(packet.encodePacket());
    }
}