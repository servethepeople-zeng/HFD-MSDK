package hfd.msdk.hfdlibrary;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

import dji.common.error.DJIError;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.PayloadKey;
import dji.keysdk.callback.ActionCallback;
import dji.keysdk.callback.KeyListener;
import dji.mop.common.Pipeline;
import dji.sdk.payload.Payload;
import dji.sdk.sdkmanager.DJISDKManager;
import hfd.msdk.mavlink.MAVLinkPacket;
import hfd.msdk.mavlink.msg_GC_mode;
import hfd.msdk.mavlink.msg_YT_degree;
import hfd.msdk.mavlink.msg_YT_reset;
import hfd.msdk.mavlink.msg_camera_realzoom;
import hfd.msdk.mavlink.msg_camera_zoom;
import hfd.msdk.mavlink.msg_get_medialist;
import hfd.msdk.mavlink.msg_get_mediavar;
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

public class HFDSDKManagerJX {

    private static final String TAG = HFDSDKManagerJX.class.getSimpleName();

    private static HFDSDKManagerJX hfdsdkManagerJX = null;
    private static DJIKey getDataKey, sendDataKey;
    private static MsgHandler msgHandler;
    private Payload payload;
    //初始化回调
    private Thread sendCalThread;
    //列表信息回调，变化回调，媒体文件接收回调
    private Thread callListThread, callChangeThread, callMediaThread;
    private Pipeline pipeline, psdkline;
    private boolean accuTime = false;
    //是否正在下载中
    private boolean downloading = false;
    //是否开启媒体文件及内存变化的实时反馈
    private boolean realFeedback = false;
    //是否拍照完成
    private boolean isTake = false;
    private int qZoom = 6;
    private HFDErrorCode hfdErrorCode;
    private LatLng latLng = new LatLng(36.686344, 117.131224);
    //媒体文件列表，媒体变化，图片buffer
    private ByteBuffer listBuff, changeBuff, picBuff;

    private Handler downloadHandler;
    private HandlerThread downloadThread;
    private HandlerThread uploadThread;
    private Handler uploadHandler;
    private byte[] listMD5;
    private byte[] changeMD5;
    private byte[] picMD5;
    private byte[] vidMD5;
    //1 下载媒体列表 2 下载变化反馈 3 下载缩略图 4 下载原图 5下载视频文件
    private int downloadFlag = 0;
    private long beginLoad = 0, beginLoad1 = 0, beginLoad2 = 0;
    private float gVertical, gHorizontal;
    //接收保存视频流
    private FileOutputStream videoOutput;
    //视频文件长度,已保存的长度
    private int videoFileLength = 0, videoFileLengthC = 0;
    private LinkedBlockingQueue<byte[]> changeLinkedBlockQueue;
    //获取从云台返回的psdk命令通道的信息
    private KeyListener getDataListener = new KeyListener() {
        @Override
        public void onValueChange(@Nullable Object oldValue, @Nullable final Object newValue) {
            if (newValue != null) {
                byte[] data = (byte[]) newValue;
                if (data.length >= 6 && "fd".equals(Integer.toHexString(data[0] & 0x0FF))
                        && "14".equals(Integer.toHexString(data[2] & 0x0FF))
                        && "ff".equals(Integer.toHexString(data[3] & 0x0FF))) {
                    if ("be".equals(Integer.toHexString(data[4] & 0x0FF))) {
                        if ("14".equals(Integer.toHexString(data[5] & 0x0FF))) {
                            Log.d("hfdsdkmanager", "对时完成");
                            if (!accuTime) {
                                accuTime = true;
                                //对时完成，获取payload 并设置数据回调
                                Message msg = new Message();
                                msg.what = 1;
                                msgHandler.sendMessage(msg);
                            }
                        } else if ("66".equals(Integer.toHexString(data[5] & 0x0FF))) {
                            //发送对时命令
                            sendUserData(createMAVLink(4, 0));
                        } else if ("4a".equals(Integer.toHexString(data[5] & 0x0FF))) {
                            sendUserData(createMAVLink(10, 1));
                        }
                    } else if ("bf".equals(Integer.toHexString(data[4] & 0x0FF))) {
                        //反馈命令处理 以下表示拍照完成
                        if ("6".equals(Integer.toHexString(data[5] & 0x0FF))) {
                            isTake = true;
                        }
                    }
                }
                Log.d("hfdsdkmanage", "receive byte:" + Helper.byte2hex(data) + "\n");
            }
        }
    };

    public static HFDSDKManagerJX getInstance() {
        if (hfdsdkManagerJX == null)
            hfdsdkManagerJX = new HFDSDKManagerJX();
        return hfdsdkManagerJX;
    }

    ;

    //初始化SDK相关信息
    public void initSDK(final MediaCallbacks.CommonCallback<HFDErrorCode> commonCallback) {
        msgHandler = new MsgHandler();
        getDataKey = PayloadKey.create(PayloadKey.GET_DATA_FROM_PAYLOAD);
        sendDataKey = PayloadKey.create(PayloadKey.SEND_DATA_TO_PAYLOAD);
        if (KeyManager.getInstance() != null) {
            KeyManager.getInstance().addListener(getDataKey, getDataListener);
        }
        uploadThread = new HandlerThread("upload");
        downloadThread = new HandlerThread("download");
        uploadThread.start();
        downloadThread.start();
        uploadHandler = new Handler(uploadThread.getLooper());
        downloadHandler = new Handler(downloadThread.getLooper());
        listMD5 = new byte[16];
        changeMD5 = new byte[16];
        picMD5 = new byte[16];
        vidMD5 = new byte[16];

        //回调线程，返回回调信息
        sendCalThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!accuTime) {
                        Log.d("hfdsdkmanager", "send data");
                        sendUserData(createMAVLink(4, 0));

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("hfdsdkmanager", "hfdErrorCode =" + hfdErrorCode);
                    if (hfdErrorCode == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        commonCallback.onResult(hfdErrorCode);
                        break;
                    }
                }
                hfdErrorCode = null;
            }
        });
        sendCalThread.start();
    }

    //获取相机SD卡中中媒体文件列表
    public void getMediaFileListCallback(final MediaCallbacks.MediaDataCallbacks<String> mediaCallbacks) {
        if (downloading) {
            mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            return;
        }
        downloading = true;
        sendUserData(createMAVLink(11, 0));
        downloadFlag = 1;
        listBuff = ByteBuffer.allocate(1);
        beginLoad = System.currentTimeMillis();
        //回调线程，返回回调信息
        callListThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (System.currentTimeMillis() - beginLoad > 1500) {
                        Log.d("hfdsdkmanager", "receive medial list file listBuff capacity" + listBuff.capacity() + ",limit =" + listBuff.limit() + ",position =" + listBuff.position());
                        if (listBuff.capacity() > 1 && listBuff.position() == listBuff.capacity()) {
                            byte[] listByte = new byte[listBuff.capacity()];
                            listBuff.flip();
                            listBuff.get(listByte);
                            //根据收到的内容生成MD5
                            byte[] newListD5 = Helper.getMD5(listByte);
                            //比较自己生成的消息摘要和接收到的消息摘要是否相同
                            if (Arrays.equals(listMD5, newListD5)) {
                                //验证相同返回获取到的消息
                                mediaCallbacks.onSuccess(Helper.byteToString(listByte));
                                break;
                            }
                            mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING);
                            break;
                        }
                        if(listBuff.position() > 0){
                            mediaCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
                            break;
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                downloading = false;
                downloadFlag = realFeedback ? 2 : 0;
                listBuff = ByteBuffer.allocate(1);
            }
        });
        callListThread.start();
    }

    //设置相机媒体文件变化监听回调
    public void setMediaFileCallback(boolean isTap, MediaCallbacks.MediaDataCallbacks<String> mediaDataCallbacks) {
        Log.d("hfdsdkmanager", "isTap = " + isTap);
        sendUserData(createMAVLink(12, isTap ? 1 : 0));
        if(!isTap){
            downloadFlag = 0;
            return;
        }
        downloadFlag = 2;
        changeLinkedBlockQueue = new LinkedBlockingQueue<byte[]>(10);
        changeBuff = ByteBuffer.allocate(1);
        beginLoad1 = System.currentTimeMillis();
        //回调线程，返回回调信息
        callChangeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isTap) {
                    Log.d("hfdsdkmanager", "deal change data");
                    if (System.currentTimeMillis() - beginLoad1 > 3000) {
                        Log.d("hfdsdkmanager", "deal change data changeBuff.position() = " + changeBuff.position());
                        /*if (changeBuff.capacity() > 1 && changeBuff.position() == changeBuff.capacity()) {
                            byte[] changeByte = new byte[changeBuff.capacity()];
                            changeBuff.flip();
                            changeBuff.get(changeByte);
                            //根据收到的内容生成MD5
                            byte[] newChangeD5 = Helper.getMD5(changeByte);
                            //比较自己生成的消息摘要和接收到的消息摘要是否相同
                            if (Arrays.equals(changeMD5, newChangeD5)) {
                                //验证相同返回获取到的消息
                                mediaDataCallbacks.onSuccess(Helper.byteToString(changeByte));
                            } else {
                                mediaDataCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING);
                            }
                            changeBuff = ByteBuffer.allocate(1);
                            break;
                        } else {
                            mediaDataCallbacks.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
                            break;
                        }*/
                        while(changeLinkedBlockQueue.size() > 0){
                            byte[] changeByte = changeLinkedBlockQueue.poll();
                            mediaDataCallbacks.onSuccess(Helper.byteToString(changeByte));
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        callChangeThread.start();
    }

    //获取图片或视频数据
    public void getMediaData(String filePath, final MediaCallbacks.MediaDataCallbacks<String> dataCallback) {

        if (downloading) {
            dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
            return;
        }
        downloading = true;
        int filetype = 0;

        if (filePath.contains("thumbnail")) {
            filetype = 3;
        } else if (filePath.contains(".jpg")) {
            //capacity 指的是元素个数，并不是空间大小
            //picLinkedBlockQueue = new LinkedBlockingQueue<byte[]>(6_000);
            filetype = 4;
            //picBuff = ByteBuffer.allocate(4645152);
            //Log.d("hfdsdkmanager", "picLinkedBlockQueue.size() = "+picLinkedBlockQueue.size());
        } else if (filePath.contains(".mp4")) {
            filetype = 5;
            File videoFile = new File(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
            try {
                if (!videoFile.exists()) {
                    if (!videoFile.getParentFile().exists())
                        videoFile.getParentFile().mkdirs();
                    videoFile.createNewFile();
                } else {
                    videoFile.delete();
                    videoFile.createNewFile();
                }
                videoOutput = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/HFD" + filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            dataCallback.onFailure(HFDErrorCode.GIMBAL_DOWN_FAIL);
            return;
        }

        byte[] filepathByte = filePath.getBytes();
        byte[] imgFormatOrder = new byte[10 + filepathByte.length];
        imgFormatOrder[0] = (byte) 253;
        imgFormatOrder[1] = (byte) filepathByte.length;
        imgFormatOrder[2] = (byte) 20;
        imgFormatOrder[3] = (byte) 255;
        imgFormatOrder[4] = (byte) 190;
        imgFormatOrder[5] = (byte) 81;
        imgFormatOrder[6] = (byte) 3;
        imgFormatOrder[7] = (byte) filetype;
        imgFormatOrder[8 + filepathByte.length] = (byte) 45;
        imgFormatOrder[9 + filepathByte.length] = (byte) 54;
        System.arraycopy(filepathByte, 0, imgFormatOrder, 8, filepathByte.length);
        //Log.d("hfdsdkmanager", "send file path cmd = " + Helper.byteToString(imgFormatOrder));
        sendUserData(imgFormatOrder);

        downloadFlag = filetype;
        picBuff = ByteBuffer.allocate(1);
        beginLoad2 = System.currentTimeMillis();

        //回调线程，返回回调信息
        callMediaThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (downloadFlag == 3) {
                        if (System.currentTimeMillis() - beginLoad2 > 1000) {
                            Log.d("hfdsdkmanager", "receive medial file picBuff capacity" + picBuff.capacity() + ",limit =" + picBuff.limit() + ",position =" + picBuff.position());
                            if (picBuff.capacity() > 1 && picBuff.position() == picBuff.capacity()) {
                                byte[] listByte = new byte[picBuff.capacity()];
                                picBuff.flip();
                                picBuff.get(listByte);
                                //根据收到的内容生成MD5
                                byte[] newListD5 = Helper.getMD5(listByte);
                                //比较自己生成的消息摘要和接收到的消息摘要是否相同
                                if (Arrays.equals(picMD5, newListD5)) {
                                    //验证相同返回thumb文件路径
                                    File thumbFile = new File(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
                                    try {
                                        if (!thumbFile.exists()) {
                                            if (!thumbFile.getParentFile().exists())
                                                thumbFile.getParentFile().mkdirs();
                                            thumbFile.createNewFile();
                                        } else {
                                            thumbFile.delete();
                                            thumbFile.createNewFile();
                                        }
                                        FileOutputStream fe = new FileOutputStream(thumbFile, true);
                                        fe.write(picBuff.array());
                                        fe.flush();
                                        fe.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    dataCallback.onSuccess(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
                                    break;
                                }
                                dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING);
                                break;
                            } else {
                                dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_SENDFAIL);
                                break;
                            }
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (downloadFlag == 4) {
                        if (System.currentTimeMillis() - beginLoad2 > 2000) {
                            Log.d("hfdsdkmanager", "deal pic data");
                            Log.d("hfdsdkmanager", "receive medial file picBuff capacity" + picBuff.capacity() + ",limit =" + picBuff.limit() + ",position =" + picBuff.position());
                            //延时可用
                            if (picBuff.capacity() > 1 && picBuff.position() == picBuff.capacity()) {
                                byte[] listByte = new byte[picBuff.capacity()];
                                picBuff.flip();
                                picBuff.get(listByte);
                                //根据收到的内容生成MD5
                                byte[] newListD5 = Helper.getMD5(listByte);
                                //比较自己生成的消息摘要和接收到的消息摘要是否相同
                                if (Arrays.equals(picMD5, newListD5)) {
                                    //验证相同返回thumb文件路径
                                    File thumbFile1 = new File(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
                                    try {
                                        if (!thumbFile1.exists()) {
                                            if (!thumbFile1.getParentFile().exists())
                                                thumbFile1.getParentFile().mkdirs();
                                            thumbFile1.createNewFile();
                                        } else {
                                            thumbFile1.delete();
                                            thumbFile1.createNewFile();
                                        }
                                        FileOutputStream fe = new FileOutputStream(thumbFile1, true);
                                        fe.write(picBuff.array());
                                        fe.flush();
                                        fe.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    dataCallback.onSuccess(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
                                    break;
                                }
                                dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING);
                                break;
                            }
                            dataCallback.onFailure(HFDErrorCode.FLIGHT_MOP_DOWNING);
                            break;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (downloadFlag == 5) {
                        Log.d("hfdsdkmanager", "deal video data");
                        if (System.currentTimeMillis() - beginLoad2 > 2000) {
                            Log.d("hfdsdkmanager", "receive video file length = " + videoFileLength);
                            //比较长度
                            File tempFile = new File(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
                            Log.d("hfdsdkmanager", "deal video data1");
                            if (tempFile.length() != videoFileLength) {
                                Log.d("hfdsdkmanager", "deal video data2");
                                dataCallback.onFailure(HFDErrorCode.GIMBAL_DOWNFILE_LENGTH);
                                break;
                            }
                            Log.d("hfdsdkmanager", "deal video data3");
                            //文件生成MD5
                            byte[] newFileMD5 = Helper.getFileMD5(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
                            //比较MD5
                            if (Arrays.equals(vidMD5, newFileMD5)) {
                                //验证相同返回获取到的消息
                                dataCallback.onSuccess(Environment.getExternalStorageDirectory() + "/HFD" + filePath);
                                break;
                            }
                            dataCallback.onFailure(HFDErrorCode.GIMBAL_DOWNFILE_Fail);
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                downloadFlag = realFeedback ? 2 : 0;
                picBuff = ByteBuffer.allocate(1);
                downloading = false;
            }
        });
        callMediaThread.start();
    }

    //拍照
    public void takePic(LatLng recLatLng, final MediaCallbacks.MediaDataCallbacks<String> mediaDataCallbacks) {
        latLng = recLatLng;
        sendUserData(createMAVLink(1, 0));

        uploadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("hfdsdkmanager", "send take pic wait istake = " + isTake);
                long startTime = System.currentTimeMillis();
                while (!isTake) {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (System.currentTimeMillis() - startTime > 5000)
                        break;
                }
                Log.d("hfdsdkmanager", "send take pic wait return istake = " + isTake);
                if (isTake) {
                    mediaDataCallbacks.onSuccess("take photo success");
                } else {
                    mediaDataCallbacks.onFailure(HFDErrorCode.GIMBAL_TAK_PIC_FAIL);
                }
                isTake = false;
            }
        }, 1000);
    }

    //录像0开始 1停止
    public void recordVideo(int type) {
        sendUserData(createMAVLink(type + 2, 0));
    }

    //视角切换 广角长焦模式，默认0 UVC，1 30x, 2 uvc+30x, 3 30x+uvc
    public void changeView(int type) {
        sendUserData(createMAVLink(5, type));
    }

    //获取焦距
    public int getZoom() {
        return qZoom;
    }

    //设置焦距6-31
    public void setZoom(int zoom) {
        sendUserData(createMAVLink(6, zoom));
    }

    //云台回中
    public void gimbalReset() {
        sendUserData(createMAVLink(8, 0));
    }

    //云台转动
    public void gimbalTurn(float wAngle, float hAngle) {
        sendUserData(createMAVLink(9, 0));
    }

    //注销sdk
    public void destorySdk() {

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
                //hfdErrorCode = HFDErrorCode.GIMBAL_SYS_DOWN;
            }
        }, data);
    }

    private void startGetStream() {
        hfdErrorCode = HFDErrorCode.INIT_SUCCESS;
        Log.d("hfdsdkmanager", "payload setStreamDataCallback");
        payload.setStreamDataCallback(new Payload.StreamDataCallback() {
            @Override
            public void onGetStreamData(byte[] bytes, int len) {
                //Log.d("hfdsdkmanager", "receive data length = "+bytes.length);
                if (downloadFlag == 1) {
                    Log.d("hfdsdkmanager", "receive data list length = " + bytes.length);
                    if (bytes[0] == (byte) 253 && bytes[1] == (byte) 253 && bytes[2] == (byte) 1) {
                        int contentLenth = 0;
                        contentLenth |= bytes[3] & (int) 0xFF;
                        contentLenth |= (bytes[4] & (int) 0xFF) << 8;
                        contentLenth |= (bytes[5] & (int) 0xFF) << 16;
                        contentLenth |= (bytes[6] & (int) 0xFF) << 24;
                        System.arraycopy(bytes, 7, listMD5, 0, 16);
                        listBuff = ByteBuffer.allocate(contentLenth);
                        if (contentLenth + 23 < bytes.length) {
                            for (int i = 23; i < contentLenth + 23; i++) {
                                listBuff.put(bytes[i]);
                            }
                        } else {
                            for (int i = 23; i < bytes.length; i++) {
                                listBuff.put(bytes[i]);
                            }
                        }
                    } else {
                        if (bytes.length + listBuff.position() > listBuff.capacity()) {
                            byte[] tailBytes = new byte[listBuff.capacity() - listBuff.position()];
                            System.arraycopy(bytes, 0, tailBytes, 0, tailBytes.length);
                            listBuff.put(tailBytes);
                        } else
                            listBuff.put(bytes);
                    }
                } else if (downloadFlag == 2) {
                    Log.d("hfdsdkmanager", "receive data =" + Helper.byte2hex(bytes));
                    Log.d("hfdsdkmanager", "receive change length = " + bytes.length);
                    if (bytes[0] == (byte) 253 && bytes[1] == (byte) 253 && bytes[2] == (byte) 2) {
                        int contentLenth = 0;
                        contentLenth |= bytes[3] & (int) 0xFF;
                        contentLenth |= (bytes[4] & (int) 0xFF) << 8;
                        contentLenth |= (bytes[5] & (int) 0xFF) << 16;
                        contentLenth |= (bytes[6] & (int) 0xFF) << 24;
                        System.arraycopy(bytes, 7, changeMD5, 0, 16);
                        changeBuff = ByteBuffer.allocate(contentLenth);
                        Log.d("hfdsdkmanager", "receive change data length = " + contentLenth);
                        if (contentLenth + 23 <= bytes.length) {
                            for (int i = 23; i < contentLenth + 23; i++) {
                                changeBuff.put(bytes[i]);
                            }
                            try {
                                changeLinkedBlockQueue.put(changeBuff.array());
                                Log.d("hfdsdkmanager", "changeLinkedBlockQueue size = " + changeLinkedBlockQueue.size());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            for (int i = 23; i < bytes.length; i++) {
                                changeBuff.put(bytes[i]);
                            }
                        }
                    } else {
                        if (bytes.length + changeBuff.position() >= changeBuff.capacity()) {
                            byte[] tailBytes = new byte[changeBuff.capacity() - changeBuff.position()];
                            System.arraycopy(bytes, 0, tailBytes, 0, tailBytes.length);
                            changeBuff.put(tailBytes);
                            try {
                                changeLinkedBlockQueue.put(changeBuff.array());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else
                            changeBuff.put(bytes);
                    }
                } else if (downloadFlag == 3) {
                    //Log.d("hfdsdkmanager", "receive thumb data length = "+bytes.length);
                    if (bytes[0] == (byte) 253 && bytes[1] == (byte) 253 && bytes[2] == (byte) 3) {
                        //Log.d("hfdsdkmanager", "receive thumb head data");
                        int contentLenth = 0;
                        contentLenth |= bytes[3] & (int) 0xFF;
                        contentLenth |= (bytes[4] & (int) 0xFF) << 8;
                        contentLenth |= (bytes[5] & (int) 0xFF) << 16;
                        contentLenth |= (bytes[6] & (int) 0xFF) << 24;
                        System.arraycopy(bytes, 7, picMD5, 0, 16);
                        picBuff = ByteBuffer.allocate(contentLenth);
                        for (int i = 23; i < bytes.length; i++) {
                            picBuff.put(bytes[i]);
                        }
                    } else {
                        if (bytes.length + picBuff.position() > picBuff.capacity()) {
                            byte[] tailBytes = new byte[picBuff.capacity() - picBuff.position()];
                            System.arraycopy(bytes, 0, tailBytes, 0, tailBytes.length);
                            picBuff.put(tailBytes);
                        } else
                            picBuff.put(bytes);
                    }

                } else if (downloadFlag == 4) {
                    //发送延时可用
                    //Log.d("hfdsdkmanager", "receive video data length = "+bytes.length);
                    beginLoad2 = System.currentTimeMillis();
                    if (bytes[0] == (byte) 253 && bytes[1] == (byte) 253 && bytes[2] == (byte) 4) {
                        Log.d("hfdsdkmanager", "receive pic head data");
                        int contentLenth = 0;
                        contentLenth |= bytes[3] & (int) 0xFF;
                        contentLenth |= (bytes[4] & (int) 0xFF) << 8;
                        contentLenth |= (bytes[5] & (int) 0xFF) << 16;
                        contentLenth |= (bytes[6] & (int) 0xFF) << 24;
                        System.arraycopy(bytes, 7, picMD5, 0, 16);
                        picBuff = ByteBuffer.allocate(contentLenth);
                        for (int i = 23; i < bytes.length; i++) {
                            picBuff.put(bytes[i]);
                        }
                    } else {
                        if (bytes.length + picBuff.position() > picBuff.capacity()) {
                            byte[] tailBytes = new byte[picBuff.capacity() - picBuff.position()];
                            System.arraycopy(bytes, 0, tailBytes, 0, tailBytes.length);
                            picBuff.put(tailBytes);
                        } else
                            picBuff.put(bytes);
                    }
                    //Log.d("hfdsdkmanag", "receive data ="+Helper.byte2hex(bytes));
                    //picLinkedBlockQueue.offer(bytes);
                    //picBuff.put(bytes);
                } else if (downloadFlag == 5) {
                    //Log.d("hfdsdkmanager", "receive video data length = "+bytes.length);
                    beginLoad2 = System.currentTimeMillis();
                    try {
                        if (bytes[0] == (byte) 253 && bytes[1] == (byte) 253 && bytes[2] == (byte) 5) {
                            Log.d("hfdsdkmanager", "receive video data");
                            videoFileLength = 0;
                            videoFileLengthC = 0;
                            videoFileLength |= bytes[3] & (int) 0xFF;
                            videoFileLength |= (bytes[4] & (int) 0xFF) << 8;
                            videoFileLength |= (bytes[5] & (int) 0xFF) << 16;
                            videoFileLength |= (bytes[6] & (int) 0xFF) << 24;
                            Log.d("hfdsdkmanager", "receive video data videoFileLength = " + videoFileLength);
                            System.arraycopy(bytes, 7, vidMD5, 0, 16);
                            byte[] firByte = new byte[bytes.length - 23];
                            System.arraycopy(bytes, 23, firByte, 0, firByte.length);
                            videoFileLengthC = firByte.length;
                            Log.d("hfdsdkmanager", "receive video data videoFileLengthC = " + videoFileLengthC);
                            videoOutput.write(firByte);
                        } else {
                            videoFileLengthC = videoFileLengthC + bytes.length;
                            if (videoFileLengthC > videoFileLength) {
                                //截图可用数据
                                byte[] lsByte = new byte[videoFileLength + bytes.length - videoFileLengthC];
                                System.arraycopy(bytes, 0, lsByte, 0, lsByte.length);
                                videoOutput.write(lsByte);
                                videoOutput.flush();
                                videoOutput.close();
                            } else if (videoFileLengthC == videoFileLength) {
                                videoOutput.write(bytes);
                                videoOutput.flush();
                                videoOutput.close();
                            } else {
                                videoOutput.write(bytes);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
            case 11:
                msg_get_medialist mediaList = new msg_get_medialist(packet);
                mediaList.cmdType = (byte) 01;
                packet = mediaList.pack();
                packet.generateCRC();
                break;
            case 12:
                msg_get_mediavar mediavar = new msg_get_mediavar(packet);
                mediavar.cmdType = (byte) 02;
                mediavar.stateType = (byte) content;
                packet = mediavar.pack();
                packet.generateCRC();
                break;
            default:
        }
        return packet.encodePacket();
    }

    //获取负载
    private Payload getPayload() {
        if (DJISDKManager.getInstance().getProduct() == null) {
            return null;
        }
        return DJISDKManager.getInstance().getProduct().getPayload();
    }

    public String bytebufferToString(ByteBuffer bb) {

        Charset charset = Charset.forName("UTF-8");
        return charset.decode(bb).toString();

    }

    //消息处理
    public class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1:
                        payload = getPayload();
                        Log.d("hfdsdkmanager", "payload =" + payload);
                        if (payload == null) {
                            uploadHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    payload = getPayload();
                                    Log.d("hfdsdkmanager", "payload =" + payload);
                                    if (payload == null) {
                                        hfdErrorCode = HFDErrorCode.GIMBAL_NOT_FOUND;
                                    } else {
                                        startGetStream();
                                    }
                                }
                            }, 2500);

                        } else {
                            startGetStream();
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}