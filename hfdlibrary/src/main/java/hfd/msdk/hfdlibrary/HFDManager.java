package hfd.msdk.hfdlibrary;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dji.common.battery.BatteryState;
import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.PayloadKey;
import dji.keysdk.callback.ActionCallback;
import dji.keysdk.callback.KeyListener;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import hfd.msdk.mavlink.MAVLinkPacket;
import hfd.msdk.mavlink.msg_GC_mode;
import hfd.msdk.mavlink.msg_YT_degree;
import hfd.msdk.mavlink.msg_YT_reset;
import hfd.msdk.mavlink.msg_YT_sdegree;
import hfd.msdk.mavlink.msg_camera_auto_takepic;
import hfd.msdk.mavlink.msg_camera_zoom;
import hfd.msdk.mavlink.msg_get_storage;
import hfd.msdk.mavlink.msg_picture_press;
import hfd.msdk.mavlink.msg_picture_zoom;
import hfd.msdk.mavlink.msg_radio_end;
import hfd.msdk.mavlink.msg_radio_start;
import hfd.msdk.model.TowerPoint;
import hfd.msdk.utils.FileUtils;
import hfd.msdk.utils.Helper;

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
import static hfd.msdk.model.IConstants.latLng;
import static hfd.msdk.utils.Helper.BytesToHexString;
import static hfd.msdk.utils.PointUtils.mulTGeneratePoints;
import static hfd.msdk.utils.PointUtils.oneTGeneratePoints;
import static hfd.msdk.utils.PointUtils.twoTGeneratePoints;

public class HFDManager {

    private static final String TAG = HFDManager.class.getSimpleName()+"1";
    //获取飞机
    private Aircraft aircraft = null;
    private FlightController flightController;
    private static DJIKey getDataKey, sendDataKey;
    private CommonCallbacks.CompletionCallbackWith  mDJICompletionCallbackc;
    private int getHomeFlag = 0;
    private static MessServer  messServer;
    private static JSONObject object;
    public static int logLevelType = 0;
    private LatLng liveLatLng = new LatLng(latLng.latitude, latLng.longitude);
    private int gcMode = 2,qZoom = 6,realSeq = 0;
    public static List<TowerPoint> backPointList = new ArrayList<TowerPoint>();
    public static List<TowerPoint> tempPointList = new ArrayList<TowerPoint>();
    private TowerPoint realPoint = new TowerPoint();
    private WaypointMission mission;
    private WaypointMissionOperator waypointMissionOperator;
    private WaypointMissionOperatorListener listener;
    private float currentAltidude = 0,compassData = 0;
    private int battery1 = 0, battery2 = 0;
    private String missonName = "";
    //自定义航点飞行状态 0 等待飞行 执行停止操作或者未开始 READY_TO_UPLOAD. 1 READY_TO_EXECUTE 2 executing已开始航点飞行 3暂停航点飞行 4继续航点飞行 5执行拍照操作中 6拍照完成后等待上传航点数据 7 拍照完成后暂停
    private int myWayPointMissonState = 0;

    public HFDManager(MessServer  messServer){
        //dji回调函数们
        settingCallback();
        getFlightThread();
        initDJIkey();
        this.messServer = messServer;
        object = new JSONObject();

        waypointMissionOperator = MissionControl.getInstance().getWaypointMissionOperator();
        setUpListener();

        FileUtils.initLogFile();
        FileUtils.writeLogFile(0, "HFDManager init success.");
    }

    public static void main(String args[]){
        String a = "abcd";
        System.out.println(a.length());
        List<TowerPoint> towerLists = new ArrayList<TowerPoint>();
        TowerPoint tower1 = new TowerPoint();
        tower1 = new TowerPoint();
        tower1.setAltitude(56.5f);
        tower1.setTowerNum("abcde");
        tower1.setTowerTypeName("zx");
        tower1.setTowerNumber("#4");
        tower1.setLatitude(36.0972);
        tower1.setLongitude(117.16058);
        towerLists.add(tower1);

        tower1 = new TowerPoint();
        tower1.setAltitude(55.5f);
        tower1.setTowerNum("abcde");
        tower1.setTowerTypeName("zx");
        tower1.setTowerNumber("#3");
        tower1.setLatitude(36.10067);
        tower1.setLongitude(117.15577);
        towerLists.add(tower1);

        tower1 = new TowerPoint();
        tower1.setAltitude(54.5f);
        tower1.setTowerNum("abcde");
        tower1.setTowerTypeName("nz");
        tower1.setTowerNumber("#2");
        tower1.setLatitude(36.10067);
        tower1.setLongitude(117.1522);
        towerLists.add(tower1);

        tower1.setAltitude(53.5f);
        tower1.setTowerNum("abcd");
        tower1.setTowerTypeName("zx");
        tower1.setTowerNumber("#1");
        tower1.setLatitude(36.10194);
        tower1.setLongitude(117.14923);
        towerLists.add(tower1);
        //loadTower(towerLists);


        List<TowerPoint> mPointList = new ArrayList<TowerPoint>();
        mPointList = loadTower(towerLists);
        System.out.println("航点个数="+mPointList.size());
        System.out.println("航点号："+mPointList.get(0).getId()+",随机塔号="+mPointList.get(0).getTowerNum()+",塔号="+mPointList.get(0).getTowerNumber()+",塔类型="+mPointList.get(0).getTowerTypeName()+",高度="+mPointList.get(0).getAltitude()+",经度="+mPointList.get(0).getLongitude()+"，纬度="+mPointList.get(0).getLatitude());
        System.out.println("航点号："+mPointList.get(1).getId()+",随机塔号="+mPointList.get(1).getTowerNum()+",塔号="+mPointList.get(1).getTowerNumber()+",塔类型="+mPointList.get(1).getTowerTypeName()+",高度="+mPointList.get(1).getAltitude()+",经度="+mPointList.get(1).getLongitude()+"，纬度="+mPointList.get(1).getLatitude());
        String missionName ="12abcd";
        byte[] buffer = new byte[5 + missionName.length()];
        int i = 0;
        buffer[i++] = (byte) 253;
        buffer[i++] = (byte) missionName.length();
        buffer[i++] = (byte) 20;
        buffer[i++] = (byte) 255;
        buffer[i++] = (byte) 190;
        for(int j=i;j<5 + missionName.length();j++)
            buffer[j] = (byte) (missionName.charAt(j-i) & 0xFF);
            //System.out.println();
//            buffer[j++] = (byte)missionName.charAt(j-i);
//
//        char[] chars = missionName.toCharArray();
//        for (int ii = 0; ii < chars.length; ii++) {
//            if(i != chars.length - 1)
//            {
//                sbu.append((int)chars[i]).append(",");
//            }
//        }
        System.out.println(BytesToHexString(buffer, buffer.length));
        System.out.println(buffer[1] & 0x0FF);

    }

    public void takePhoto(){
        createMAVLink(1,0);
        FileUtils.writeLogFile(0, "call takePhoto() method.");
    }
    public void takeRecord(int type){
        if(type  == 0){
            createMAVLink(2,0);
        }else if(type == 1){
            createMAVLink(3,0);
        }
        FileUtils.writeLogFile(0, "call takeRecord() method.");
    }
    public void zooming(int zoomNum){
        if(zoomNum < 32){
            createMAVLink(4,zoomNum);
        }else if(zoomNum == 32){
            createMAVLink(7,zoomNum);
        }else if(zoomNum == 33){
            createMAVLink(8,zoomNum);
        }else if(zoomNum == 34){
            createMAVLink(5,zoomNum);
        }else if(zoomNum == 35){
            createMAVLink(6,zoomNum);
        }
        FileUtils.writeLogFile(0, "call zooming() method.");
    }
    public void changeView(int viewType){
        createMAVLink(9,viewType);
        gcMode = viewType;
        FileUtils.writeLogFile(0, "call changeView() method.");
    }
    public void getSDStorage(){
        createMAVLink(10,0);
        FileUtils.writeLogFile(0, "call getSDStorage() method.");
    }
    public void setMissionName(String missionName){
        this.missonName = missionName;
        byte[] buffer = new byte[6 + missionName.length()];
        int i = 0;
        buffer[i++] = (byte) 253;
        buffer[i++] = (byte) missionName.length();
        buffer[i++] = (byte) 20;
        buffer[i++] = (byte) 255;
        buffer[i++] = (byte) 190;
        buffer[i++] = (byte) 70;
        for(int j=i;j<6 + missionName.length();j++)
            buffer[j] = (byte) (missionName.charAt(j-i) & 0xFF);
        sendUserData(buffer);
    }
    public void returnCenter(){
        createMAVLink(11,0);
        FileUtils.writeLogFile(0, "call returnCenter() method.");
    }
    public void pointDirection (int xAxis,int yAxis){
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
    public void frameDirection (int xAxis,int yAxis,int frameLength){
        MAVLinkPacket packet = new MAVLinkPacket();
        msg_YT_sdegree rect = new msg_YT_sdegree(packet);
        rect.zoomValue = (byte)getVirtualZoom(frameLength);
        //以将屏幕中心为坐标系零点，计算框的中心坐标
        int coordinateX = (2*xAxis+frameLength-1920)/2;
        int coordinateY = (1080-2*yAxis-9*frameLength/16)/2;
        //Log.e("画框命令传输", "wnatural："+getDegree(1,(float)coordinateX/(float)viewWidth)+",hnatural:"+getDegree(2,(float)coordinateY/(float)viewHeight));
        rect.wnatural = getDegree(1,(float)coordinateX/(float)1920);
        rect.hnatural = getDegree(2,(float)coordinateY/(float)1080);
        packet = rect.pack();
        packet.generateCRC();
        byte[] bytes = packet.encodePacket();
        sendUserData(bytes);
        FileUtils.writeLogFile(0, "call frameDirection() method.");
    }
    public void moveDirection (float xDegree,float yDegree){
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
    public void startTakeOff (){
        if(flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        }else{
            flightController.startTakeoff(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(3,"success","call startTakeOff() method success");
                    } else {
                        rebackMsg(3,"自动起飞失败，请查看飞机状态","call startTakeOff() method error message is "+djiError.getDescription());
                    }
                }
            });
        }
    }
    public void cancelTakeOff (){
        if(flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        }else{
            flightController.cancelTakeoff(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(4,"success","call cancelTakeOff() method success");
                    } else {
                        rebackMsg(4,"停止自动起飞失败，请查看飞机状态","call cancelTakeOff() method error message is "+djiError.getDescription());
                    }
                }
            });
        }
    }
    public void startLanding (){
        if(flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        }else{
            flightController.startLanding(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(5,"success","call startLanding() method success");
                    } else {
                        rebackMsg(5,"自动降落失败，请查看飞机状态","call startLanding() method error message is "+djiError.getDescription());
                    }
                }
            });
        }
    }
    public void cancelLanding (){
        if(flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        }else{
            flightController.cancelLanding(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(6,"success","call cancelLanding() method success");
                    } else {
                        rebackMsg(6,"停止自动降落失败，请查看飞机状态","call cancelLanding() method error message is "+djiError.getDescription());
                    }
                }
            });
        }
    }

    public static List<TowerPoint> loadTower(List<TowerPoint> towerList){
        //FileUtils.writeLogFile(0, "call loadTower() method.");
        if(towerList.size() == 0){
            //sendErrorMessage("杆塔数据为空");
            return null;
        } else if(towerList.size() == 1){
            //sendErrorMessage("只上传了一个直线塔，生成的航点会在塔的北向和南向，请注意飞行安全！");
            return oneTGeneratePoints(towerList);
            //有两个塔
        }else if(towerList.size()==2){
            return twoTGeneratePoints(towerList);
        } else {
            System.out.println("航点个数大于2");
            return mulTGeneratePoints(towerList);
        }
    }

    public List<TowerPoint> loadMarkPoint(List<TowerPoint> towerList){
        //flightController.startTakeoff();
        FileUtils.writeLogFile(0, "call loadMarkPoint() method.");
        return null;
    }

    public void uploadPoint(List<TowerPoint> towerList){
        if(flightController != null) {
            if(myWayPointMissonState==0) {
                backPointList.clear();
                backPointList = towerList;
                realPoint = new TowerPoint();
                realPoint = backPointList.get(0);
                mission = createWaypointMission(1000);
                DJIError djiError = waypointMissionOperator.loadMission(mission);
                if (djiError == null) {
                    FileUtils.writeLogFile(0, "start uploadPoint().");
                    if (WaypointMissionState.READY_TO_RETRY_UPLOAD.equals(waypointMissionOperator.getCurrentState())
                            || WaypointMissionState.READY_TO_UPLOAD.equals(waypointMissionOperator.getCurrentState())) {
                        waypointMissionOperator.uploadMission(new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                if (djiError == null) {
                                    realSeq = 1;
                                    myWayPointMissonState = 1;
                                    try {
                                        object.put("result ", "success");
                                    } catch (Exception e) {
                                        object = null;
                                    }
                                    messServer.setInfomation((byte) 7, object);
                                    FileUtils.writeLogFile(0, "end uploadPoint()");
                                } else {
                                    rebackMsg(7, "上传航点到飞机失败", "call uploadPoint() errorMsg is 上传航点到飞机失败---" + djiError.getDescription());
                                }
                            }
                        });
                    } else {
                        rebackMsg(7, "飞机尚未准备就绪,稍等再试", "call uploadPoint() errorMsg is 飞机尚未准备就绪---" + djiError.getDescription());
                    }
                } else {
                    rebackMsg(7, "航点出错，请检查航点信息", "call uploadPoint() errorMsg is 航点出错，请检查航点信息---" + djiError.getDescription());
                }
            }else{
                sendErrorMessage("飞机执行任务中，无法上传新航点");
            }
        }else{
            sendErrorMessage("飞机未连接，无法上传航点");
        }
    }

    public void startMission(){
        //flightController.startTakeoff();
        FileUtils.writeLogFile(0, "call startMission() method.");
        if(flightController != null) {
            if (mission != null) {
                if (WaypointMissionState.READY_TO_EXECUTE.equals(waypointMissionOperator.getCurrentState())&& myWayPointMissonState==1) {
                    waypointMissionOperator.startMission(new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (djiError == null) {
                                myWayPointMissonState = 2;
                                try {
                                    object.put("result ", "start");
                                    object.put("tower ", realPoint.getTowerNum());
                                    object.put("point ", realPoint.getId());
                                } catch (Exception e) {
                                    object = null;
                                }
                                messServer.setInfomation((byte) 15, object);
                                FileUtils.writeLogFile(0, "start point "+realPoint.getTowerNum()+","+realPoint.getId());
                            } else {
                                rebackMsg(8, "开始自动飞行失败", "call startMission() errormsg is " + djiError.getDescription());
                            }
                        }
                    });
                }else{
                    rebackMsg(8, "飞行航线飞行状态不对，请重新规划上传航线", "call startMission() 飞行航线飞行状态不对，请重新规划上传航线");
                }
            } else {
                rebackMsg(8, "航线任务为不能空，请上传航点", "call startMission() 航线任务为不能空，请上传航点");
            }
        }else{
            sendErrorMessage("飞机未连接，无法进行航点飞行");
        }
    }
    public void pauseMission(){
        FileUtils.writeLogFile(0, "call pauseMission() method.");
        if(flightController != null) {
            if (WaypointMissionState.EXECUTING.equals(waypointMissionOperator.getCurrentState()) && myWayPointMissonState==2) {
                waypointMissionOperator.pauseMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError == null) {
                            myWayPointMissonState = 3;
                            rebackMsg(9, "success", "call pauseMission() success now flight state is " + waypointMissionOperator.getCurrentState());
                        } else {
                            rebackMsg(9, "暂停航点飞行发送错误", "call pauseMission() error message is " + djiError.getDescription());
                        }
                    }
                });
            } else if(myWayPointMissonState == 5){
                myWayPointMissonState = 7;
            } else{
                rebackMsg(9, "飞机状态错误，不能执行暂停飞行方法", "call pauseMission() 无法执行暂停方法，飞机当前状态为" + waypointMissionOperator.getCurrentState());
            }
        }else{
            sendErrorMessage("飞机未连接，无法暂停航点飞行");
        }
    }
    public void resumeMission(){
        FileUtils.writeLogFile(0, "call resumeMission() method.");
        if(flightController != null) {
            if (WaypointMissionState.EXECUTION_PAUSED.equals(waypointMissionOperator.getCurrentState())&&myWayPointMissonState==3) {
                waypointMissionOperator.resumeMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError == null) {
                            myWayPointMissonState = 4;
                            rebackMsg(10, "success", "call resumeMission() success now flight state is " + waypointMissionOperator.getCurrentState());
                        } else {
                            rebackMsg(10, "继续航点飞行发送错误", "call resumeMission() error message is " + djiError.getDescription());
                        }
                    }
                });
            } else if(myWayPointMissonState == 7){
                myUploadMisson();
            }else{
                rebackMsg(10, "飞机状态错误，不能继续航点飞行", "call resumeMission() 无法执行继续飞行方法，飞机当前状态为" + waypointMissionOperator.getCurrentState());
            }
        }else{
            sendErrorMessage("飞机未连接，无法继续航点飞行");
        }
    }
    public void breakpointMission(){
        FileUtils.writeLogFile(0, "call breakpointMission() method.");
        if(flightController != null) {
            if (realSeq <= backPointList.size()) {
//                if (WaypointMissionState.EXECUTING.equals(waypointMissionOperator.getCurrentState())) {
//                    rebackMsg(11, "success", "call breakpointMission() success now flight state is " + waypointMissionOperator.getCurrentState());
//                }
                for(int i=realSeq;i<backPointList.size();i++){
                    if(backPointList.get(i).getTowerNum().equals(realPoint.getTowerNum()))
                        backPointList.get(i).setPointType(1);
                }
                rebackMsg(11, "success", "call breakpointMission() success now flight state is " + waypointMissionOperator.getCurrentState());
            } else {
                rebackMsg(11, "跳过杆塔失败，航线飞行已完成", "call breakpointMission() success now flight state is " + waypointMissionOperator.getCurrentState());
            }
        }else{
            sendErrorMessage("飞机未连接，无法跳过当前杆塔进行航点飞行");
        }
    }
    public void stopMission(){
        FileUtils.writeLogFile(0, "call stopMission() method.");
        if(flightController != null) {
            if (WaypointMissionState.EXECUTING.equals(waypointMissionOperator.getCurrentState())
                    || WaypointMissionState.EXECUTION_PAUSED.equals(waypointMissionOperator.getCurrentState())) {
                waypointMissionOperator.stopMission(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError == null) {
                            myWayPointMissonState = 0;
                            rebackMsg(12, "success", "call stopMission() success now flight state is " + waypointMissionOperator.getCurrentState());
                        } else {
                            rebackMsg(12, "停止航点飞行发送错误", "call stopMission() error message is " + djiError.getDescription());
                        }
                    }
                });
            } else {
                rebackMsg(12, "飞机状态错误，无法执行停止航线飞行操作", "call resumeMission() 无法执行继续飞行方法，飞机当前状态为" + waypointMissionOperator.getCurrentState());
            }
        }else{
            sendErrorMessage("飞机未连接，无法停止航点飞行");
        }
    }
    public void startGoHome(){
        if(flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        }else{
            flightController.startGoHome(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(13,"success","call startGoHome() method success");
                    } else {
                        rebackMsg(13,"返航，请查看飞机状态","call startGoHome() method error message is "+djiError.getDescription());
                    }
                }
            });
        }
    }
    public void stopGoHome(){
        if(flightController == null) {
            sendErrorMessage("无法获取飞机");
            //flightController.startTakeoff();
        }else{
            flightController.cancelGoHome(new CommonCallbacks.CompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        rebackMsg(14,"success","call stopGoHome() method success");
                    } else {
                        rebackMsg(14,"停止返航失败，请查看飞机状态","call stopGoHome() method error message is "+djiError.getDescription());
                    }
                }
            });
        }
    }
    public JSONObject getFlightData(){
        FileUtils.writeLogFile(0, "call getFlightData() method.");
        JSONObject dataObject = new JSONObject();
        try {
            if(flightController == null) {
                sendErrorMessage("无法获取飞机");
                dataObject.put("speed", "0");
                dataObject.put("altitude", "0");
                dataObject.put("longitude", "0");
                dataObject.put("latitude", "0");
                dataObject.put("battery1", "0");
                dataObject.put("battery2", "0");
                dataObject.put("compass", "0");
            }else {
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
                float speed = (float)Math.sqrt(Math.pow(speedx,2) + Math.pow(speedx,2) + Math.pow(speedz,2));
                dataObject.put("speed", speed+"");
                dataObject.put("altitude", ""+currentAltidude);
                dataObject.put("longitude", "0"+liveLatLng.longitude);
                dataObject.put("latitude", "0"+liveLatLng.latitude);
                dataObject.put("battery1", battery1);
                dataObject.put("battery2", battery2);
                dataObject.put("compass", compassData);
            }
        } catch (JSONException e) {
            sendErrorMessage("程序异常");
            FileUtils.writeLogFile(2, "call getFlightData() error is "+e.getMessage());
        }
        return dataObject;
    }

    public void setInspectionDir(int h,int v){
        TowHor = h;
        TowVer = v;
    }

    public void setLogLevel(int mLogLevel){
        logLevelType = mLogLevel;
    }

    private void getFlightThread() {
        new Thread(new Runnable() {
            public void run() {
                Log.d(TAG, "getFlightThread");
                while (true) {
                    try {
                        if(aircraft != null && flightController != null) {
                            //Log.d("aircraft is", "aircraft"+aircraft);
                            //获取home点
                            //Log.d("aircraft home is", "getHomeFlag"+getHomeFlag);
                            if(getHomeFlag ==0){
                                flightController.getHomeLocation(mDJICompletionCallbackc);
                            }else {
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
                        }else{
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
        getDataKey = PayloadKey.create(PayloadKey.GET_DATA_FROM_PAYLOAD,0);
        sendDataKey = PayloadKey.create(PayloadKey.SEND_DATA_TO_PAYLOAD,0);

        if (KeyManager.getInstance() != null) {
            KeyManager.getInstance().addListener(getDataKey, getDataListener);
        }

    }

    private KeyListener getDataListener = new KeyListener() {
        @Override
        public void onValueChange(@Nullable Object oldValue, @Nullable final Object newValue) {
            if (newValue instanceof byte[]) {
                byte[] data = (byte[]) newValue;
                Log.d("receivedata","receive data success! " + BytesToHexString(data, data.length));
                FileUtils.writeLogFile(0, "receive data success! " + BytesToHexString(data, data.length));
                if(data.length>7) {
                    if ("fd".equals(Integer.toHexString(data[0] & 0x0FF))) {
                        if ("14".equals(Integer.toHexString(data[2] & 0x0FF))) {
                            if ("ff".equals(Integer.toHexString(data[3] & 0x0FF))) {
                                if ("be".equals(Integer.toHexString(data[4] & 0x0FF))) {
                                    //内存卡容量
                                    if("42".equals(Integer.toHexString(data[5] & 0x0FF))){
                                        JSONObject dataObject = new JSONObject();
                                        int mStorage = 0;
                                        mStorage |= (data[7] & 0xFF);
                                        mStorage |= (data[8] & 0xFF) << 8;
                                        mStorage |= (data[9] & 0xFF) << 16;
                                        mStorage |= (data[10] & 0xFF) << 24;
                                        try {
                                            dataObject.put("totalStorage", (data[6] & 0x0FF)+"G");
                                            dataObject.put("usedStorage", mStorage+"M");
                                        } catch (JSONException e) {
                                            sendErrorMessage("程序异常");
                                            FileUtils.writeLogFile(2, "call getSDStorage() error is "+e.getMessage());
                                        }
                                    }else if ("17".equals(Integer.toHexString(data[5] & 0x0FF))) {
                                        //拍照执行完成
                                        myWayPointMissonState = 6;
                                        if ("1".equals(Integer.toHexString(data[6] & 0x0FF))) {
                                            FileUtils.writeLogFile(1, "智能识别 未识别到有效数据");
                                        } else if ("2".equals(Integer.toHexString(data[6] & 0x0FF))) {
                                            FileUtils.writeLogFile(1, "智能识别 识别拍照中");
                                        } else if ("3".equals(Integer.toHexString(data[6] & 0x0FF))) {
                                            FileUtils.writeLogFile(1, "智能识别 识别拍照结束");
                                        }
                                        //发送航点结束命令
                                        object = new JSONObject();
                                        try {
                                            object.put("result ", "end");
                                            object.put("tower ", realPoint.getTowerNum());
                                            object.put("point ", realPoint.getId());
                                        } catch (Exception e) {
                                            object = null;
                                        }
                                        messServer.setInfomation((byte) 15, object);
                                        FileUtils.writeLogFile(0, "end point "+realPoint.getTowerNum()+","+realPoint.getId());

                                        //任务完成
                                        if(backPointList.size() == realSeq){
                                            myWayPointMissonState = 0;
                                            rebackMsg(12,"result","success");
                                            FileUtils.writeLogFile(1, "自动巡检完成");
                                        }else{
                                            //load upload startmisson
                                            myUploadMisson();
                                        }
                                    }
                                }else if ("bf".equals(Integer.toHexString(data[4] & 0x0FF))) {
                                    //反馈命令处理 以下表示拍照完成
                                    if ("6".equals(Integer.toHexString(data[5] & 0x0FF))) {
                                        rebackMsg(1,"result","takephoto success");
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    };

    //回调函数们
    private void settingCallback(){
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
                //ToastUtils.setResultToToast(var1.getDescription());
            }
        };
    }

    public void sendUserData(byte[] data) {
        final byte[] showData = data;
        KeyManager.getInstance().performAction(sendDataKey, new ActionCallback() {
            @Override
            public void onSuccess() {
                //Log.d("senddata",""+ Helper.byte2hex(showData));
                FileUtils.writeLogFile(1, "senddata success:"+Helper.byte2hex(showData));
            }
            @Override
            public void onFailure(@NonNull DJIError error) {
                //ToastUtils.setResultToToast("Not found payload device,please restart the app！");
                //Log.d("senddata",""+Helper.byte2hex(showData));
                sendErrorMessage("没有发现云台");
                FileUtils.writeLogFile(1, "senddata fail:"+Helper.byte2hex(showData));
            }
        }, data);
    }

    public static void sendErrorMessage(String mesContent){
        try{
            object.put("errorMsg",mesContent);
        }catch (Exception e){
            object = null;
        }
        messServer.setInfomation((byte)0,object);

        FileUtils.writeLogFile(2, mesContent);
    }

    private void createMAVLink(int type,int content) {
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
                if(content<6)
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
                if(content==0){
                    gcMode.type = (byte) 01;
                    gcMode.fieldView = (byte) 02;
                }else if(content == 1){
                    gcMode.type = (byte) 01;
                    gcMode.fieldView = (byte) 01;
                }else if(content == 2){
                    gcMode.type = (byte) 02;
                    gcMode.fieldView = (byte) 02;
                }else if(content == 3){
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
                msg_camera_auto_takepic autoTakepic= new msg_camera_auto_takepic(packet);
                autoTakepic.autoNum = (byte)1;
                autoTakepic.towerNum = (byte)content;
                packet = autoTakepic.pack();
                packet.generateCRC();
                sendUserData(packet.encodePacket());
                break;
        }
    }

    public float getDegree(int type,float perWHeight){
        //Log.d("命令msg_YT_degree", "type="+type+",百分比perWHeight="+perWHeight);
        //Log.d("命令msg_YT_degree", "gcM="+gcMode+",qZoom="+qZoom);
        float moveDegree = 0.00f;
        //水平角
        if(type == 1) {
            //广角
            if (gcMode == 0 || gcMode == 2) {
                //moveDegree = (float)(65.76*perWHeight);
                //自己测量37.85305 厂商提供35.8
                //moveDegree = (float)(37.85305*perWHeight);
                moveDegree = UVC_Horizontal_angle*perWHeight;
            } else {
                if (qZoom <= 6){
                    moveDegree = Qx30U_Zoom_H6*perWHeight;
                }
                else if (qZoom == 7){
                    moveDegree = Qx30U_Zoom_H7*perWHeight;
                }
                else if (qZoom == 8){
                    moveDegree = Qx30U_Zoom_H8*perWHeight;
                }
                else if (qZoom == 9){
                    moveDegree = Qx30U_Zoom_H9*perWHeight;
                }
                else if (qZoom == 10){
                    moveDegree = Qx30U_Zoom_H10*perWHeight;
                }
                else if (qZoom == 11){
                    moveDegree = Qx30U_Zoom_H11*perWHeight;
                }
                else if (qZoom == 12){
                    moveDegree = Qx30U_Zoom_H12*perWHeight;
                }
                else if (qZoom == 13){
                    moveDegree = Qx30U_Zoom_H13*perWHeight;
                }
                else if (qZoom == 14){
                    moveDegree = Qx30U_Zoom_H14*perWHeight;
                }
                else if (qZoom == 15){
                    moveDegree = Qx30U_Zoom_H15*perWHeight;
                }
                else if (qZoom == 16){
                    moveDegree = Qx30U_Zoom_H16*perWHeight;
                }
                else if (qZoom == 17){
                    moveDegree = Qx30U_Zoom_H17*perWHeight;
                }
                else if (qZoom == 18){
                    moveDegree = Qx30U_Zoom_H18*perWHeight;
                }
                else if (qZoom == 19){
                    moveDegree = Qx30U_Zoom_H19*perWHeight;
                }
                else if (qZoom == 20){
                    moveDegree = Qx30U_Zoom_H20*perWHeight;
                }
                else if (qZoom == 21){
                    moveDegree = Qx30U_Zoom_H21*perWHeight;
                }
                else if (qZoom == 22){
                    moveDegree = Qx30U_Zoom_H22*perWHeight;
                }
                else if (qZoom == 23){
                    moveDegree = Qx30U_Zoom_H23*perWHeight;
                }
                else if (qZoom == 24){
                    moveDegree = Qx30U_Zoom_H24*perWHeight;
                }
                else if (qZoom == 25){
                    moveDegree = Qx30U_Zoom_H25*perWHeight;
                }
                else if (qZoom == 26){
                    moveDegree = Qx30U_Zoom_H26*perWHeight;
                }
                else if (qZoom == 27){
                    moveDegree = Qx30U_Zoom_H27*perWHeight;
                }
                else if (qZoom == 28){
                    moveDegree = Qx30U_Zoom_H28*perWHeight;
                }
                else if (qZoom == 29){
                    moveDegree = Qx30U_Zoom_H29*perWHeight;
                }
                else if (qZoom == 30){
                    moveDegree = Qx30U_Zoom_H30*perWHeight;
                }
                else if (qZoom == 31){
                    moveDegree = Qx30U_Zoom_H31*perWHeight;
                }
            }
            //俯仰角
        }else{
            //广角
            if (gcMode == 0 || gcMode == 2) {
                //moveDegree = (float)49.32*perWHeight;
                //自己测量21.34598 厂商提供21.6
                //moveDegree = (float)21.34598*perWHeight;
                moveDegree = UVC_Vertical_angle*perWHeight;
            } else {
                if (qZoom <= 6){
                    moveDegree = Qx30U_Zoom_V6*perWHeight;
                }
                else if (qZoom == 7){
                    moveDegree = Qx30U_Zoom_V7*perWHeight;
                }
                else if (qZoom == 8){
                    moveDegree = Qx30U_Zoom_V8*perWHeight;
                }
                else if (qZoom == 9){
                    moveDegree = Qx30U_Zoom_V9*perWHeight;
                }
                else if (qZoom == 10){
                    moveDegree = Qx30U_Zoom_V10*perWHeight;
                }
                else if (qZoom == 11){
                    moveDegree = Qx30U_Zoom_V11*perWHeight;
                }
                else if (qZoom == 12){
                    moveDegree = Qx30U_Zoom_V12*perWHeight;
                }
                else if (qZoom == 13){
                    moveDegree = Qx30U_Zoom_V13*perWHeight;
                }
                else if (qZoom == 14){
                    moveDegree = Qx30U_Zoom_V14*perWHeight;
                }
                else if (qZoom == 15){
                    moveDegree = Qx30U_Zoom_V15*perWHeight;
                }
                else if (qZoom == 16){
                    moveDegree = Qx30U_Zoom_V16*perWHeight;
                }
                else if (qZoom == 17){
                    moveDegree = Qx30U_Zoom_V17*perWHeight;
                }
                else if (qZoom == 18){
                    moveDegree = Qx30U_Zoom_V18*perWHeight;
                }
                else if (qZoom == 19){
                    moveDegree = Qx30U_Zoom_V19*perWHeight;
                }
                else if (qZoom == 20){
                    moveDegree = Qx30U_Zoom_V20*perWHeight;
                }
                else if (qZoom == 21){
                    moveDegree = Qx30U_Zoom_V21*perWHeight;
                }
                else if (qZoom == 22){
                    moveDegree = Qx30U_Zoom_V22*perWHeight;
                }
                else if (qZoom == 23){
                    moveDegree = Qx30U_Zoom_V23*perWHeight;
                }
                else if (qZoom == 24){
                    moveDegree = Qx30U_Zoom_V24*perWHeight;
                }
                else if (qZoom == 25){
                    moveDegree = Qx30U_Zoom_V25*perWHeight;
                }
                else if (qZoom == 26){
                    moveDegree = Qx30U_Zoom_V26*perWHeight;
                }
                else if (qZoom == 27){
                    moveDegree = Qx30U_Zoom_V27*perWHeight;
                }
                else if (qZoom == 28){
                    moveDegree = Qx30U_Zoom_V28*perWHeight;
                }
                else if (qZoom == 29){
                    moveDegree = Qx30U_Zoom_V29*perWHeight;
                }
                else if (qZoom == 30){
                    moveDegree = Qx30U_Zoom_V30*perWHeight;
                }
                else if (qZoom == 31){
                    moveDegree = Qx30U_Zoom_V31*perWHeight;
                }
            }
        }
        return (float)(Math.round(moveDegree*100))/100;
    }

    //根据画的框的大小返回应该变到的zoom值
    public int getVirtualZoom(int rectX){
        int blurZoom = 6;
        float newAngle = (float)rectX/(float) 1920*UVC_Horizontal_angle;

        //Log.d("画框命令传输", "newAngle："+newAngle);
        if(newAngle>=Qx30U_Zoom_H19){
            if(newAngle>Qx30U_Zoom_H8)
                blurZoom = 8;
            else if(newAngle>Qx30U_Zoom_H9)
                blurZoom = 9;
            else if(newAngle>Qx30U_Zoom_H10)
                blurZoom = 10;
            else if(newAngle>Qx30U_Zoom_H11)
                blurZoom = 11;
            else if(newAngle>Qx30U_Zoom_H12)
                blurZoom = 12;
            else if(newAngle>Qx30U_Zoom_H13)
                blurZoom = 13;
            else if(newAngle>Qx30U_Zoom_H14)
                blurZoom = 14;
            else if(newAngle>Qx30U_Zoom_H15)
                blurZoom = 15;
            else if(newAngle>Qx30U_Zoom_H16)
                blurZoom = 16;
            else if(newAngle>Qx30U_Zoom_H17)
                blurZoom = 17;
            else if(newAngle>Qx30U_Zoom_H18)
                blurZoom = 18;
            else
                blurZoom = 19;
        }else{
            if(newAngle>Qx30U_Zoom_H20)
                blurZoom = 20;
            else if(newAngle>Qx30U_Zoom_H21)
                blurZoom = 21;
            else if(newAngle>Qx30U_Zoom_H22)
                blurZoom = 22;
            else if(newAngle>Qx30U_Zoom_H23)
                blurZoom = 23;
            else if(newAngle>Qx30U_Zoom_H24)
                blurZoom = 24;
            else if(newAngle>Qx30U_Zoom_H25)
                blurZoom = 25;
            else if(newAngle>Qx30U_Zoom_H26)
                blurZoom = 26;
            else if(newAngle>Qx30U_Zoom_H27)
                blurZoom = 27;
            else if(newAngle>Qx30U_Zoom_H28)
                blurZoom = 28;
            else if(newAngle>Qx30U_Zoom_H29)
                blurZoom = 29;
            else if(newAngle>Qx30U_Zoom_H30)
                blurZoom = 30;
            else
                blurZoom = 31;
        }
        return blurZoom;
    }

    private WaypointMission createWaypointMission(int stayTime){
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

        Waypoint eachWaypoint = new Waypoint(realPoint.getLatitude(),realPoint.getLongitude(), realPoint.getAltitude());
        if(realPoint.getPointType() != 1)
            eachWaypoint.addAction(new WaypointAction(WaypointActionType.ROTATE_AIRCRAFT, (int)realPoint.getToward()));
        eachWaypoint.addAction(new WaypointAction(WaypointActionType.STAY, stayTime));
        //waypointList.add(eachWaypoint);
        //builder.waypointList(waypointList).waypointCount(waypointList.size());
        builder.addWaypoint(eachWaypoint);
        return builder.build();
    }

    private void rebackMsg(int type,String rebackContent, String noteLog){
        try{
            object.put("result ",rebackContent);
        }catch (Exception e){
            object = null;
        }
        messServer.setInfomation((byte)type,object);
        FileUtils.writeLogFile(2, noteLog);
    }

    private void setUpListener() {
        // Example of Listener
        listener = new WaypointMissionOperatorListener() {
            @Override
            public void onDownloadUpdate(@NonNull WaypointMissionDownloadEvent waypointMissionDownloadEvent) {
                // Example of Download Listener
                if (waypointMissionDownloadEvent.getProgress() != null
                        && waypointMissionDownloadEvent.getProgress().isSummaryDownloaded
                        && waypointMissionDownloadEvent.getProgress().downloadedWaypointIndex == 2) {
                    //ToastUtils.setResultToToast("Download successful!");
                }
                updateWaypointMissionState();
            }

            @Override
            public void onUploadUpdate(@NonNull WaypointMissionUploadEvent waypointMissionUploadEvent) {
                // Example of Upload Listener
                if (waypointMissionUploadEvent.getProgress() != null
                        && waypointMissionUploadEvent.getProgress().isSummaryUploaded
                        && waypointMissionUploadEvent.getProgress().uploadedWaypointIndex == 2) {
                    //ToastUtils.setResultToToast("Upload successful!");
                }
                updateWaypointMissionState();
            }

            @Override
            public void onExecutionUpdate(@NonNull WaypointMissionExecutionEvent waypointMissionExecutionEvent) {
                // Example of Execution Listener
                Log.d(TAG,
                        (waypointMissionExecutionEvent.getPreviousState() == null
                                ? ""
                                : waypointMissionExecutionEvent.getPreviousState().getName())
                                + ", "
                                + waypointMissionExecutionEvent.getCurrentState().getName()
                                + (waypointMissionExecutionEvent.getProgress() == null
                                ? ""
                                : waypointMissionExecutionEvent.getProgress().targetWaypointIndex));
                updateWaypointMissionState();
            }

            @Override
            public void onExecutionStart() {
                //ToastUtils.setResultToToast("Execution started!");
                updateWaypointMissionState();
            }

            @Override
            public void onExecutionFinish(@Nullable DJIError djiError) {

                if(backPointList.size() == realSeq){

                    myWayPointMissonState = 0;
                    rebackMsg(12,"result","success");
                    FileUtils.writeLogFile(1, "Current Waypointmission state : "+ waypointMissionOperator.getCurrentState().getName());
                }else{
                    if(realPoint.getPointType()==4){
                        //智能识别
                        myWayPointMissonState = 5;
                        createMAVLink(12,Integer.parseInt(realPoint.getTowerNumber().substring(1)));
                    }else{
                        try {
                            object.put("result ", "end");
                            object.put("tower ", realPoint.getTowerNum());
                            object.put("point ", realPoint.getId());
                        } catch (Exception e) {
                            object = null;
                        }
                        messServer.setInfomation((byte) 15, object);
                        FileUtils.writeLogFile(0, "end point "+realPoint.getTowerNum()+","+realPoint.getId());
                        //load upload startmisson
                        myUploadMisson();
                    }
                }

                updateWaypointMissionState();
            }
        };

        if (waypointMissionOperator != null && listener != null) {
            // Example of adding listeners
            waypointMissionOperator.addListener(listener);
        }
    }

    private void updateWaypointMissionState(){
        if (waypointMissionOperator != null && waypointMissionOperator.getCurrentState() != null) {
            Log.d("waypoint","Current Waypointmission state : "+ waypointMissionOperator.getCurrentState().getName());
            FileUtils.writeLogFile(1, "Current Waypointmission state : "+ waypointMissionOperator.getCurrentState().getName());
        } else {
            Log.d("waypoint","null Current Waypointmission state : "+ waypointMissionOperator.getCurrentState().getName());
            FileUtils.writeLogFile(1, "null Current Waypointmission state : "+ waypointMissionOperator.getCurrentState().getName());
        }
    }

    private void myUploadMisson(){
        if(flightController != null) {
            realPoint = new TowerPoint();
            realPoint = backPointList.get(realSeq++);
            if(realPoint.getPointType() ==4)
                mission = createWaypointMission(1000);
            else
                mission = createWaypointMission(3000);
            DJIError djiError = waypointMissionOperator.loadMission(mission);
            if (djiError == null) {
                FileUtils.writeLogFile(0, "start uploadPoint().");
                if (WaypointMissionState.READY_TO_RETRY_UPLOAD.equals(waypointMissionOperator.getCurrentState())
                        || WaypointMissionState.READY_TO_UPLOAD.equals(waypointMissionOperator.getCurrentState())) {
                    waypointMissionOperator.uploadMission(new CommonCallbacks.CompletionCallback() {
                            @Override
                            public void onResult(DJIError djiError) {
                                if (djiError == null) {
                                    myStartMission();
                                    FileUtils.writeLogFile(0, "myStartMisson uploadMission success");
                                } else {
                                    rebackMsg(7, "航点错误", "call myStartMisson() errorMsg is 上传航点到飞机失败---" + djiError.getDescription());
                                }
                            }
                        });
                    } else {
                        rebackMsg(7, "飞机状态出现错误", "call myStartMisson() errorMsg is 飞机尚未准备就绪---" + djiError.getDescription());
                    }
                } else {
                    rebackMsg(7, "航点"+realPoint.getId()+"出错，请检查航点信息", "call myStartMisson() errorMsg is 航点出错，请检查航点信息---" + djiError.getDescription());
            }
        }else{
            sendErrorMessage("飞机未连接，无法进行航点飞行");
        }
    }

    public void myStartMission(){
        //flightController.startTakeoff();
        FileUtils.writeLogFile(0, "call myStartMission() method.");
        if(flightController != null) {
            if (mission != null) {
                if (WaypointMissionState.READY_TO_EXECUTE.equals(waypointMissionOperator.getCurrentState())&& myWayPointMissonState==1) {
                    waypointMissionOperator.startMission(new CommonCallbacks.CompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (djiError == null) {
                                myWayPointMissonState = 2;
                                try {
                                    object.put("result ", "start");
                                    object.put("tower ", realPoint.getTowerNum());
                                    object.put("point ", realPoint.getId());
                                } catch (Exception e) {
                                    object = null;
                                }
                                messServer.setInfomation((byte) 15, object);
                                FileUtils.writeLogFile(0, "start point "+realPoint.getTowerNum()+","+realPoint.getId());
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
        }else{
            sendErrorMessage("飞机未连接，已经无法进行航点飞行");
        }
    }
}
