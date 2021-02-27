package hfd.msdk.hfdlibrary;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.Calendar;

import dji.common.error.DJIError;
import dji.common.util.CommonCallbacks;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.PayloadKey;
import dji.keysdk.callback.ActionCallback;
import dji.keysdk.callback.KeyListener;
import dji.mop.common.Pipeline;
import dji.mop.common.PipelineError;
import dji.mop.common.TransmissionControlType;
import dji.sdk.payload.Payload;
import dji.sdk.sdkmanager.DJISDKManager;
import hfd.msdk.mavlink.MAVLinkPacket;
import hfd.msdk.mavlink.msg_GC_mode;
import hfd.msdk.mavlink.msg_YT_degree;
import hfd.msdk.mavlink.msg_YT_reset;
import hfd.msdk.mavlink.msg_camera_realzoom;
import hfd.msdk.mavlink.msg_camera_zoom;
import hfd.msdk.mavlink.msg_gimbal_version;
import hfd.msdk.mavlink.msg_picture_press;
import hfd.msdk.mavlink.msg_radio_end;
import hfd.msdk.mavlink.msg_radio_start;
import hfd.msdk.mavlink.msg_time_calibration;
import hfd.msdk.model.HFDErrorCode;
import hfd.msdk.utils.Helper;
import hfd.msdk.utils.MediaCallbacks;

import static hfd.msdk.model.IConstants.calDay;
import static hfd.msdk.model.IConstants.calHour;
import static hfd.msdk.model.IConstants.calMinute;
import static hfd.msdk.model.IConstants.calMonth;
import static hfd.msdk.model.IConstants.calSecond;
import static hfd.msdk.model.IConstants.calYear;

public class HFDSDKManager {

    private static final String TAG = HFDSDKManager.class.getSimpleName();

    private int psdkPipId = 49155, psdkKeyId = 49154;
    private static HFDSDKManager hfdsdkManager = null;
    private Payload payload;
    private Pipeline pipeline, psdkline;
    private boolean accuTime = false;
    private Thread recPsdkthread;
    private Thread sendCalThread;
    private Thread recChangeThread;
    private int downFlag = 0;
    private int qZoom = 6;
    private static DJIKey getDataKey, sendDataKey;
    private static MsgHandler msgHandler;
    private Handler downloadHandler;
    private HandlerThread downloadThread;
    private HandlerThread uploadThread;
    private Handler uploadHandler;
    private HFDErrorCode hfdErrorCode;
    //拍照是否成功
    private boolean isTake = false;
    //是否正在下载中
    private boolean downloading;
    //一次收取8192，收取次数
    private int downloadCount;
    private LatLng latLng = new LatLng(36.686344, 117.131224);
    private float gVertical,gHorizontal;

    public static HFDSDKManager getInstance(){
        if(hfdsdkManager == null)
            hfdsdkManager = new HFDSDKManager();
        return hfdsdkManager;
    }

    //初始化SDK相关信息
    public void initSDK(final MediaCallbacks.CommonCallback<HFDErrorCode> commonCallback){
        payload = getPayload();
        Log.d("hfdsdkmanager", "initSDK payload=" + payload);
        if (getPayload() == null) {
            commonCallback.onResult(HFDErrorCode.GIMBAL_NOT_FOUND);
            return;
        }
        msgHandler = new MsgHandler();

        uploadThread = new HandlerThread("upload");
        downloadThread = new HandlerThread("download");
        uploadThread.start();
        downloadThread.start();
        uploadHandler = new Handler(uploadThread.getLooper());
        downloadHandler = new Handler(downloadThread.getLooper());

        payload.getPipelines().connect(psdkPipId, TransmissionControlType.STABLE, error -> {
            Log.d("hfdsdkmanager", "first connect pipline error" + error);
            //连接失败有两个原因1、未对时 2、已对时但通道出现问题需要重启云台
            if (error == null) {
                Pipeline pipeline = payload.getPipelines().getPipeline(psdkPipId);
                Log.d("hfdsdkmanager", "first pipline =" + pipeline);
                if (pipeline == null) {
                    hfdErrorCode = HFDErrorCode.FLIGHT_MOP_DISCONNECT;
                }else{
                    payload.getPipelines().connect(psdkKeyId, TransmissionControlType.STABLE, error1 -> {
                        Log.d("hfdsdkmanager", "first connect psdkline error" + error1);
                        if (error1 == null) {
                            psdkline = payload.getPipelines().getPipeline(psdkKeyId);
                            Log.d("hfdsdkmanager", "first psdkline =" + psdkline);
                            if (psdkline != null) {
                                Message msg = new Message();
                                msg.what = 1;
                                msgHandler.sendMessage(msg);
                                hfdErrorCode = HFDErrorCode.INIT_SUCCESS;
                            }else
                                hfdErrorCode = HFDErrorCode.FLIGHT_MOP_DISCONNECT;
                        }else{
                            hfdErrorCode = HFDErrorCode.FLIGHT_MOP_DISCONNECT;
                        }
                    });
                }
            }else{
                //默认无法连接的原因是未对时 初始化psdk key
                Message keyMessage = new Message();
                keyMessage.what = 2;
                msgHandler.sendMessage(keyMessage);
            }
        });

        //回调线程，返回回调信息
        sendCalThread = new Thread(new Runnable() {
            @Override
            public void run() {
                /*while (!accuTime) {
                    Log.d("hfdsdkmanager", "send data");
                    sendUserData(createMAVLink(4, 0));

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                while (true){
                    Log.d("hfdsdkmanager", "hfdErrorCode =" +hfdErrorCode);
                    if(hfdErrorCode == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        commonCallback.onResult(hfdErrorCode);
                        break;
                    }
                }
            }
        });
        sendCalThread.start();
    };

    //获取从云台返回的psdk命令通道的信息
    private KeyListener getDataListener = new KeyListener() {
        @Override
        public void onValueChange(@Nullable Object oldValue, @Nullable final Object newValue) {
            if (newValue != null) {
                byte[] data = (byte[]) newValue;
                if (data.length >= 6 && "fd".equals(Integer.toHexString(data[0] & 0x0FF))
                        && "14".equals(Integer.toHexString(data[2] & 0x0FF))
                        && "ff".equals(Integer.toHexString(data[3] & 0x0FF))
                        && "be".equals(Integer.toHexString(data[4] & 0x0FF))) {
                    if ("14".equals(Integer.toHexString(data[5] & 0x0FF))) {
                        Log.d("hfdsdkmanager", "对时完成");
                        //对时完成，启动mop通道
                        uploadHandler.postDelayed(initMopRunnable,1500);
                    } else if ("66".equals(Integer.toHexString(data[5] & 0x0FF))) {
                        //发送对时命令
                        sendUserData(createMAVLink(4, 0));
                    }
                }
                Log.d("hfdsdkmanage", "receive byte:" + Helper.byte2hex(data) + "\n");
            }
        }
    };

    //获取相机SD卡中中媒体文件列表
    public void getMediaFileListCallback(final MediaCallbacks.MediaDataCallbacks<String> mediaCallbacks){
        Log.d("hfdsdkmanager", "getMediaFileList downloading =" + downloading);
        if(downloading){
            mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            return;
        }
        downloading = true;
        downFlag = 0;
        String requestMediaList = "{\"requestType\":1}";
        byte[] reMediaListData = requestMediaList.getBytes();
        if(pipeline == null){
            mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_DISCONNECT);
            downloading = false;
            return;
        }

        int result = pipeline.writeData(reMediaListData, 0, reMediaListData.length);
        Log.d("hfdsdkmanager", "getMediaFileList send data result =" + result);
        if(result < 0){
            mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            downloading = false;
            return;
        }

        downloadHandler.postDelayed(() -> {
            // 开始读取数据
            byte[] headBuff = new byte[4];
            int len = pipeline.readData(headBuff, 0, headBuff.length);
            Log.d("hfdsdkmanager", "getMediaFileList read data len =" + len);
            int number = 0;
            if (len <= 0) {
                downloading = false;
                mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_DISCONNECT);
                return;
            } else {
                number |= headBuff[0] & (int) 0xFF;
                number |= (headBuff[1] & (int) 0xFF) << 8;
                number |= (headBuff[2] & (int) 0xFF) << 16;
                number |= (headBuff[3] & (int) 0xFF) << 24;
            }
            Log.d("hfdsdkmanager", "getMediaFileList read data number =" + number);
            // 开始读取文件数据
            byte[] contentBuff = new byte[number];
            //If the return code greater than 0 it is the length of data read otherwise it is the error code.
            int len1 = pipeline.readData(contentBuff, 0, contentBuff.length);
            if (len1 <= 0) {
                mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING_FAIL);
                downloading = false;
                return;
            }

            mediaCallbacks.onSuccess(Helper.getString(contentBuff));
            downloading = false;
        },500);


    }

    //设置相机媒体文件变化监听回调
    public void setMediaFileCallback(boolean isTap,MediaCallbacks.MediaDataCallbacks<String> mediaDataCallbacks){
        String requestChange = "{\"requestType\":"+(isTap?2:3)+"}";
        byte[] changeData = requestChange.getBytes();
        int changeResult = pipeline.writeData(changeData, 0, changeData.length);
        if (changeResult <= 0) {
            mediaDataCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            return;
        }
        if(isTap){
            recChangeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        if(!downloading){
                            // 开始读取数据
                            byte[] headBuff = new byte[4];
                            int len = pipeline.readData(headBuff, 0, headBuff.length);
                            int number = 0;
                            if (len > 0) {
                                number |= headBuff[0] & (int) 0xFF;
                                number |= (headBuff[1] & (int) 0xFF) << 8;
                                number |= (headBuff[2] & (int) 0xFF) << 16;
                                number |= (headBuff[3] & (int) 0xFF) << 24;
                            }

                            // 开始读取文件数据
                            byte[] contentBuff = new byte[number];
                            //If the return code greater than 0 it is the length of data read otherwise it is the error code.
                            int len1 = pipeline.readData(contentBuff, 0, contentBuff.length);
                            if (len1 > 0) {
                                mediaDataCallbacks.onSuccess(contentBuff.toString());
                            }
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            recChangeThread.start();
        }else{
            recChangeThread.interrupt();
            mediaDataCallbacks.onSuccess("");
        }

    }

    //获取图片或视频数据
    public void getMediaData(String filePath,final MediaCallbacks.MediaDataCallbacks<byte[]> dataCallback){
        Log.d("hfdsdkmanager", "getMediaData downloading =" + downloading);
        if(downloading){
            dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            return;
        }

        downloading = true;
        String requestChange = "{\"requestType\":4,\"fileType\":"+(filePath.endsWith("mp4")?2:1)+",\"fileName\":\""+filePath+"\"}";
        byte[] changeData = requestChange.getBytes();
        if(pipeline == null){
            dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DISCONNECT);
            return;
        }
        int changeResult = pipeline.writeData(changeData, 0, changeData.length);
        if(changeResult < 0){
            dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            return;
        }
        downFlag = 1;
        Log.d("hfdsdkmanager", "getMediaData send commond success");
        downloadHandler.postDelayed(() -> {
            // 开始读取数据
            byte[] headBuff = new byte[4];
            int len = pipeline.readData(headBuff, 0, headBuff.length);
            long startRead = System.currentTimeMillis();
            while(len <= 0){
                len = pipeline.readData(headBuff, 0, headBuff.length);
                if(System.currentTimeMillis() - startRead >= 5000)
                    break;
            }
            Log.d("hfdsdkmanager", "getMediaData receive len = "+len);
            int number = 0;
            if (len <= 0) {
                downloading = false;
                dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DISCONNECT);
                return;
            } else {
                number |= headBuff[0] & (int) 0xFF;
                number |= (headBuff[1] & (int) 0xFF) << 8;
                number |= (headBuff[2] & (int) 0xFF) << 16;
                number |= (headBuff[3] & (int) 0xFF) << 24;
            }
            Log.d("hfdsdkmanager", "getMediaData receive number = "+number);
            if (number <= 8192) {
                // 开始读取文件数据
                byte[] contentBuff = new byte[number];
                //If the return code greater than 0 it is the length of data read otherwise it is the error code.
                int len1 = pipeline.readData(contentBuff, 0, contentBuff.length);
                if (len1 > 0) {
                    dataCallback.onSuccess(contentBuff);
                }else{
                    dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING_FAIL);
                }
                downloading = false;
                return;
            }

            downloadCount = (int) Math.ceil(new BigDecimal((float) number / 8192).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
            for (int i = 0; i < downloadCount - 1; i++) {
                byte[] contentBuff = new byte[8192];
                int len1 = pipeline.readData(contentBuff, 0, contentBuff.length);
                if (len1 <= 0) {
                    dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING_FAIL);
                    downloading = false;
                    return;
                } else {
                    dataCallback.onSuccess(contentBuff);
                }
            }
            byte[] contentBuff = new byte[number - (downloadCount - 1) * 8192];
            int len1 = pipeline.readData(contentBuff, 0, contentBuff.length);
            if (len1 <= 0) {
                dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING_FAIL);
                downloading = false;
                return;
            } else {
                dataCallback.onSuccess(contentBuff);
            }
            downloading = false;
        },500);
    }

    //拍照
    public void takePic(LatLng recLatLng,final MediaCallbacks.MediaDataCallbacks<String> mediaDataCallbacks){
        latLng = recLatLng;
        byte[] recByte = createMAVLink(1,0);
        int sendResult = psdkline.writeData(recByte, 0, recByte.length);
        //Log.d("pipeline", "writeData result =" + sendResult);
        //If the return code greater than 0 it is the length of data written otherwise it is the error code.
        Log.d("hfdsdkmanager","send take pic result" + sendResult);
        if (sendResult <= 0) {
            mediaDataCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            return;
        }
        uploadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("hfdsdkmanager","send take pic wait istake = " + isTake);
                long startTime = System.currentTimeMillis();
                while(!isTake){
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(System.currentTimeMillis()-startTime > 5000)
                        break;
                }
                Log.d("hfdsdkmanager","send take pic wait return istake = " + isTake);
                if(isTake) {
                    mediaDataCallbacks.onSuccess("take photo success");
                    isTake = false;
                }else{
                    mediaDataCallbacks.onFailure(HFDErrorCode.GIMBAL_TAK_PIC_FAIL);
                }
            }
        }, 2000);
    }

    //录像0开始 1停止
    public String recordVideo(int type){
        byte[] recByte = createMAVLink(type+2,0);
        int sendResult = psdkline.writeData(recByte, 0, recByte.length);
        //Log.d("pipeline", "writeData result =" + sendResult);
        //If the return code greater than 0 it is the length of data written otherwise it is the error code.
        if (sendResult > 0) {
            return null;
        }
        return "send commond to payload fail";
    }

    //视角切换 广角长焦模式，默认0 UVC，1 30x, 2 uvc+30x, 3 30x+uvc
    public String changeView(int type){
        byte[] recByte = createMAVLink(5,type);
        int sendResult = psdkline.writeData(recByte, 0, recByte.length);
        //Log.d("pipeline", "writeData result =" + sendResult);
        //If the return code greater than 0 it is the length of data written otherwise it is the error code.
        if (sendResult > 0) {
            return null;
        }
        return "send commond to payload fail";
    }

    //设置焦距6-31
    public String setZoom(int zoom){
        byte[] recByte = createMAVLink(6,zoom);
        int sendResult = psdkline.writeData(recByte, 0, recByte.length);
        //Log.d("pipeline", "writeData result =" + sendResult);
        //If the return code greater than 0 it is the length of data written otherwise it is the error code.
        if (sendResult > 0) {
            return null;
        }
        return "send commond to payload fail";
    }

    //获取焦距
    public int getZoom(){
        return qZoom;
    }

    //云台回中
    public String gimbalReset(){
        byte[] recByte = createMAVLink(8,0);
        int sendResult = psdkline.writeData(recByte, 0, recByte.length);
        //Log.d("pipeline", "writeData result =" + sendResult);
        //If the return code greater than 0 it is the length of data written otherwise it is the error code.
        if (sendResult > 0) {
            return null;
        }
        return "send commond to payload fail";
    }

    //云台转动
    public String gimbalTurn(float wAngle,float hAngle){
        gVertical = hAngle;
        gHorizontal = wAngle;
        byte[] recByte = createMAVLink(9,0);
        int sendResult = psdkline.writeData(recByte, 0, recByte.length);
        //Log.d("pipeline", "writeData result =" + sendResult);
        //If the return code greater than 0 it is the length of data written otherwise it is the error code.
        if (sendResult > 0) {
            return null;
        }
        return "send commond to payload fail";
    }

    //注销sdk
    public void destorySdk(){
        recPsdkthread.interrupt();
        Log.d("hfdsdkmanager", "destorySdk payload = "+payload);
        if (payload == null) {
            return;
        }
        Log.d("hfdsdkmanager", "destorySdk disconnect");
        payload.getPipelines().disconnect(psdkPipId, new CommonCallbacks.CompletionCallback<PipelineError>() {
            @Override
            public void onResult(PipelineError error) {
                if (error == null) {
                    payload.getPipelines().disconnect(psdkKeyId, new CommonCallbacks.CompletionCallback<PipelineError>() {
                        @Override
                        public void onResult(PipelineError error) {
                            if (error == null) {
                            }
                            Log.d("hfdsdkmanager", "destorySdk psdk pipeline disconnect error ="+error);
                        }
                    });
                }
                Log.d("hfdsdkmanager", "destorySdk data pipeline disconnect error ="+error);
            }
        });


    }

    //发送psdk命令通道命令
    public void sendUserData(byte[] data) {
        final byte[] showData = data;
        KeyManager.getInstance().performAction(sendDataKey, new ActionCallback() {
            @Override
            public void onSuccess() {
                Log.d("hfdsdkmanager", "send user data" + Helper.byte2hex(showData));
            }

            @Override
            public void onFailure(@NonNull DJIError error) {
                Log.d("hfdsdkmanager", "send user data fail");
                hfdErrorCode = HFDErrorCode.GIMBAL_SYS_DOWN;
            }
        }, data);
    }

    private Runnable initMopRunnable = new Runnable() {
        @Override
        public void run() {

            int pipFlag = 0;
            int pipPsdkFlag = 0;
            payload.getPipelines().connect(psdkPipId, TransmissionControlType.STABLE, error -> {
                Log.d("hfdsdkmanager", "connect pipline error" + error);
                if (error == null) {
                    pipeline = payload.getPipelines().getPipeline(psdkPipId);
                    Log.d("hfdsdkmanager", "pipline =" + pipeline);
                    if (pipeline == null) {
                        hfdErrorCode = HFDErrorCode.FLIGHT_MOP_DISCONNECT;
                    }else{
                        payload.getPipelines().connect(psdkKeyId, TransmissionControlType.STABLE, error1 -> {
                            Log.d("hfdsdkmanager", "connect psdkline error" + error1);
                            if (error1 == null) {
                                psdkline = payload.getPipelines().getPipeline(psdkKeyId);
                                Log.d("hfdsdkmanager", "psdkline =" + psdkline);
                                if (psdkline != null) {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msgHandler.sendMessage(msg);
                                    hfdErrorCode = HFDErrorCode.INIT_SUCCESS;
                                }else
                                    hfdErrorCode = HFDErrorCode.FLIGHT_MOP_DISCONNECT;
                            }else{
                                hfdErrorCode = HFDErrorCode.FLIGHT_MOP_DISCONNECT;
                            }
                        });
                    }
                }else{
                    hfdErrorCode = HFDErrorCode.FLIGHT_MOP_DISCONNECT;
                }
            });

        }
    };

    //消息处理
    public class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1:
                        recPsdkthread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("hfdsdkmanager", "recPsdkthread psdkline =" + psdkline);
                                byte[] recPsdkByte = new byte[9];
                                while (true) {
                                    Log.d("hfdsdkmanage", "recPsdkthread start read");
                                    int recResult = psdkline.readData(recPsdkByte, 0, recPsdkByte.length);
                                    Log.d("hfdsdkmanage", "recPsdkthread recResult =" + recResult);
                                    if (recResult > 0) {
                                        if ("fd".equals(Integer.toHexString(recPsdkByte[0] & 0x0FF))
                                                && "14".equals(Integer.toHexString(recPsdkByte[2] & 0x0FF))
                                                && "ff".equals(Integer.toHexString(recPsdkByte[3] & 0x0FF))) {
                                            if ("bf".equals(Integer.toHexString(recPsdkByte[4] & 0x0FF))) {
                                                if ("6".equals(Integer.toHexString(recPsdkByte[5] & 0x0FF))) {
                                                    //拍照完成
                                                    isTake = true;
                                                }
                                            } else if ("be".equals(Integer.toHexString(recPsdkByte[4] & 0x0FF))) {
                                                if ("65".equals(Integer.toHexString(recPsdkByte[5] & 0x0FF))) {
                                                    qZoom = Integer.parseInt(Integer.toHexString(recPsdkByte[6] & 0x0FF), 16);
                                                } else if("4a".equals(Integer.toHexString(recPsdkByte[5] & 0x0FF))){
                                                    sendMopData(0,createMAVLink(10,1));
                                                }
                                            }
                                        }
                                        Log.d("hfdsdkmanage", "psdk pipeline receive byte:" + Helper.byte2hex(recPsdkByte));
                                    }

                                    /*try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }*/

                                }
                            }
                        });
                        recPsdkthread.start();
                        break;
                    case 2:
                        getDataKey = PayloadKey.create(PayloadKey.GET_DATA_FROM_PAYLOAD);
                        sendDataKey = PayloadKey.create(PayloadKey.SEND_DATA_TO_PAYLOAD);
                        KeyManager.getInstance().addListener(getDataKey, getDataListener);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //创建mavlink命令
    private byte[] createMAVLink(int type, int content) {
        MAVLinkPacket packet = new MAVLinkPacket();
        switch (type) {
            case 1:
                msg_picture_press pressPicture = new msg_picture_press(packet);
                pressPicture.jingdu = (float) latLng.longitude;
                pressPicture.weidu = (float) latLng.latitude;
                //pressPicture.capture = 1;
                packet = pressPicture.pack();
                packet.generateCRC();
                break;
            case 2:
                msg_radio_start radioStart = new msg_radio_start(packet);
                packet = radioStart.pack();
                packet.generateCRC();
                break;
            case 3:
                msg_radio_end radioEnd = new msg_radio_end(packet);
                packet = radioEnd.pack();
                packet.generateCRC();
                break;
            //发送时间校准信息
            case 4:
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
                break;
            case 5:
                msg_GC_mode gcMode = new msg_GC_mode(packet);
                if (content == 0) {
                    gcMode.type = (byte) 01;
                    gcMode.fieldView = (byte) 01;
                } else if (content == 1) {
                    gcMode.type = (byte) 01;
                    gcMode.fieldView = (byte) 02;
                } else if (content == 2) {
                    gcMode.type = (byte) 02;
                    gcMode.fieldView = (byte) 01;
                } else if (content == 3) {
                    gcMode.type = (byte) 02;
                    gcMode.fieldView = (byte) 02;
                }
                packet = gcMode.pack();
                packet.generateCRC();
                break;
            case 6:
                msg_camera_zoom camera_zoom = new msg_camera_zoom(packet);
                if (content < 6)
                    content = 6;
                camera_zoom.type = (byte) content;
                packet = camera_zoom.pack();
                packet.generateCRC();
                break;
            case 7:
                msg_camera_realzoom realzoom = new msg_camera_realzoom(packet);
                packet = realzoom.pack();
                packet.generateCRC();
                break;
            case 8:
                msg_YT_reset reset = new msg_YT_reset(packet);
                packet = reset.pack();
                packet.generateCRC();
                break;
            case 9:
                msg_YT_degree degree = new msg_YT_degree(packet);
                degree.wnatural = gVertical;
                degree.hnatural = gHorizontal;
                packet = degree.pack();
                packet.generateCRC();
                break;
            case 10:
                msg_gimbal_version gimbalVersionMsg = new msg_gimbal_version(packet);
                gimbalVersionMsg.type = (byte) content;
                packet = gimbalVersionMsg.pack();
                packet.generateCRC();
                break;
            default:
        }
        return packet.encodePacket();
    }

    public void sendMopData(int type, byte[] data) {
        int sendResult = psdkline.writeData(data, 0, data.length);
        //If the return code greater than 0 it is the length of data written otherwise it is the error code.
        Log.d("pipeline", "writeData result =" + sendResult);
        if (sendResult > 0) {

        }
    }

    //获取负载
    private Payload getPayload() {
        if (DJISDKManager.getInstance().getProduct() == null) {
            return null;
        }
        return DJISDKManager.getInstance().getProduct().getPayload();
    }
}