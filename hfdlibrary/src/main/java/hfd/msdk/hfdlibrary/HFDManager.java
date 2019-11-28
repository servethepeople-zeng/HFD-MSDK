package hfd.msdk.hfdlibrary;

import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.PayloadKey;
import dji.keysdk.callback.KeyListener;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import hfd.msdk.internal.HFDEventListener;
import hfd.msdk.model.HFDEvent;
import hfd.msdk.model.TowerPoint;
import hfd.msdk.utils.ToastUtils;

import static hfd.msdk.utils.Helper.BytesToHexString;

public class HFDManager {

    private static final String TAG = HFDManager.class.getSimpleName()+"1";
    //获取飞机
    private Aircraft aircraft = null;
    private FlightController flightController;
    private static DJIKey getDataKey, sendDataKey;
    private CommonCallbacks.CompletionCallbackWith  mDJICompletionCallbackc;
    private int getHomeFlag = 0;
    private  HFDEventListener hfdEventListener;

    public HFDManager(){
        Log.d(TAG, "HFDManager init");
        //dji回调函数们
        settingCallback();
        getFlightThread();
        initDJIkey();
        HFDEvent hfdEvent = new HFDEvent("00","00");
        hfdEvent.send();
    }
    public void takePhoto(){
        Log.d(TAG, "takePhoto");
    }
    public void takeRecord(int type){
        Log.d(TAG, "takeRecord");
    }
    public void zooming(int zoomNum){
        Log.d(TAG, "zoomNum");
    }
    public void changeView(int viewType){
        Log.d(TAG, "changeView");
    }
    public void getSDStorage(){
        Log.d(TAG, "getSDStorage");
    }
    public void returnCenter(){
        Log.d(TAG, "returnCenter");
    }
    public void pointDirection (int xAxis,int yAxis){
        Log.d(TAG, "pointDirection");
    }
    public void frameDirection (int xAxis,int yAxis,int frameLength){
        Log.d(TAG, "frameDirection");
    }
    public void moveDirection (float xDegree,float yDegree){
        Log.d(TAG, "moveDirection");
    }
    public void startTakeOff (){
        //flightController.startTakeoff();
        Log.d(TAG, "takeOff");
    }
    public void cancelTakeOff (){
        //flightController.startTakeoff();
        Log.d(TAG, "cancelTakeOff");
    }
    public void startLanding (){
        Log.d(TAG, "startLanding");
    }
    public void cancelLanding (){
        //flightController.startTakeoff();
        Log.d(TAG, "cancelLanding");
    }

    public List<TowerPoint> loadTower(List<TowerPoint> towerList){
        //flightController.startTakeoff();
        Log.d(TAG, "loadTower");
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
                                        //Log.d("aircraft", "picOrMap1" + picOrMap);
                                        double latitude = flightControllerState.getAircraftLocation().getLatitude();
                                        double longitude = flightControllerState.getAircraftLocation().getLongitude();
                                        Log.d("aircraft", "latitude" + latitude);
                                        Log.d("aircraft", "longitude" + longitude);
//                                        liveLatLng = new LatLng(latitude, longitude);
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
}
