package hfd.msdk.hfdlibrary;

import android.util.Log;

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

    public HFDManager(){
        Log.d(TAG, "HFDManager init");
        //dji回调函数们
        settingCallback();
        getFlightThread();
        initDJIkey();
    }
    public void takePhoto(){
        Log.d(TAG, "into getFlightThread");
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
