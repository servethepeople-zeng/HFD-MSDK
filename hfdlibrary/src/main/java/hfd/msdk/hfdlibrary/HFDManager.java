package hfd.msdk.hfdlibrary;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.PayloadKey;
import dji.keysdk.callback.ActionCallback;
import dji.keysdk.callback.KeyListener;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import hfd.msdk.mavlink.MAVLinkPacket;
import hfd.msdk.mavlink.msg_GC_mode;
import hfd.msdk.mavlink.msg_YT_degree;
import hfd.msdk.mavlink.msg_YT_reset;
import hfd.msdk.mavlink.msg_YT_sdegree;
import hfd.msdk.mavlink.msg_camera_zoom;
import hfd.msdk.mavlink.msg_get_storage;
import hfd.msdk.mavlink.msg_picture_press;
import hfd.msdk.mavlink.msg_picture_zoom;
import hfd.msdk.mavlink.msg_radio_end;
import hfd.msdk.mavlink.msg_radio_start;
import hfd.msdk.model.TowerPoint;
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
import static hfd.msdk.model.IConstants.UVC_Horizontal_angle;
import static hfd.msdk.model.IConstants.UVC_Vertical_angle;
import static hfd.msdk.model.IConstants.latLng;
import static hfd.msdk.utils.Helper.BytesToHexString;

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
    private int gcMode = 2,qZoom = 6;

    public HFDManager(MessServer  messServer){
        //dji回调函数们
        settingCallback();
        getFlightThread();
        initDJIkey();
        this.messServer = messServer;
        object = new JSONObject();
        FileUtils.initLogFile();
        FileUtils.writeLogFile(0, "HFDManager init success.");
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
                        //ToastUtils.setResultToToast("限远距离成功");
                        try{
                            object.put("result ","success");
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)3,object);

                        FileUtils.writeLogFile(0, "call startTakeOff() method.");
                    } else {
                        try{
                            object.put("result ",djiError.getDescription());
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)3,object);

                        FileUtils.writeLogFile(2, djiError.getDescription());
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
                        //ToastUtils.setResultToToast("限远距离成功");
                        try{
                            object.put("result ","success");
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)4,object);

                        FileUtils.writeLogFile(0, "call cancelTakeoff() method.");
                    } else {
                        try{
                            object.put("result ",djiError.getDescription());
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)4,object);

                        FileUtils.writeLogFile(2, djiError.getDescription());
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
                        //ToastUtils.setResultToToast("限远距离成功");
                        try{
                            object.put("result ","success");
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)5,object);

                        FileUtils.writeLogFile(0, "call startLanding() method.");
                    } else {
                        try{
                            object.put("result ",djiError.getDescription());
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)5,object);

                        FileUtils.writeLogFile(2, djiError.getDescription());
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
                        //ToastUtils.setResultToToast("限远距离成功");
                        try{
                            object.put("result ","success");
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)6,object);

                        FileUtils.writeLogFile(0, "call cancelLanding() method.");
                    } else {
                        try{
                            object.put("result ",djiError.getDescription());
                        }catch (Exception e){
                            object = null;
                        }
                        messServer.setInfomation((byte)6,object);

                        FileUtils.writeLogFile(2, djiError.getDescription());
                    }
                }
            });
        }
    }

    public List<TowerPoint> loadTower(List<TowerPoint> towerList){
        for(int i=0;i<towerList.size();i++){

        }
        Log.d(TAG, "loadTower");
        return null;
    }

    public List<TowerPoint> loadMarkPoint(List<TowerPoint> towerList){
        //flightController.startTakeoff();
        Log.d(TAG, "loadMarkPoint");
        return null;
    }

    public void uploadPoint(List<TowerPoint> towerList){
        //flightController.startTakeoff();
        Log.d(TAG, "uploadPoint");
    }

    public void startMission(){
        //flightController.startTakeoff();
        Log.d(TAG, "startMission");
    }
    public void pauseMission(){
        //flightController.startTakeoff();
        Log.d(TAG, "pauseMission");
    }
    public void resumeMission(){
        //flightController.startTakeoff();
        Log.d(TAG, "resumeMission");
    }
    public void breakpointMission(){
        //flightController.startTakeoff();
        Log.d(TAG, "breakpointMission");
    }
    public void stopMission(){
        //flightController.startTakeoff();
        Log.d(TAG, "stopMission");
    }
    public void startGoHome(){
        //flightController.startTakeoff();
        Log.d(TAG, "stopMission");
    }
    public void stopGoHome(){
        //flightController.startTakeoff();
        Log.d(TAG, "stopMission");
    }
    public List<String> getFlightData(){
        Log.d(TAG, "getFlightData");
        return null;
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
//                                        currentAltidude = flightControllerState.getAircraftLocation().getAltitude();
                                    }
                                });
                            }

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
                ToastUtils.setResultToToast(var1.getDescription());
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
            object.put("errorMsg ",mesContent);
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

        Log.d("画框命令传输", "newAngle："+newAngle);
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
}
