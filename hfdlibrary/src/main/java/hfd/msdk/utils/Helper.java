package hfd.msdk.utils;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import dji.common.product.Model;
import dji.sdk.sdkmanager.DJISDKManager;
import hfd.msdk.model.Point;

import static hfd.msdk.model.IConstants.Ea;
import static hfd.msdk.model.IConstants.Eb;

public class Helper {
	
	public Helper() {
		
	}
	
	/**
	 * Shows message on current activity.
	 * 
	 * @param activity The activity the user want to show toast.
	 * @param msg The String that the user want to put in the message.
	 */
	public static void showToast(final Activity activity, final String msg) {
		activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showText(final Activity activity, final TextView tv, final String msg) {
        if(tv == null)
        {
            showToast(activity, "The current textView is null. ");
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                tv.setText(msg);
            }
        });
    }
	
	/**
	 * Transfers to list from enum array.
	 * 
	 * @param o An object array
	 * @return An ArrayList object of String for enum.
	 */
	public ArrayList<String> makeList(Object[] o) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < o.length; i++) {
            list.add(o[i].toString());
        }
        return list;
    }
    public ArrayList<String> makeList(int[] o) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < o.length; i++) {
            list.add(Integer.valueOf(o[i]).toString());
        }
        return list;
    }

    public ArrayList<String> makeList(List o) {
        ArrayList<String> list = new ArrayList<String>();
        Iterator iterator = o.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().toString());
        }
        return list;
    }
    public static String getString(byte[] bytes) {
        if (null == bytes) {
            return "";
        }
        // 去除NULL字符
        byte zero = 0x00;
        byte no = (byte)0xFF;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == zero || bytes[i] == no) {
                bytes = readBytes(bytes, 0, i);
                break;
            }
        }
        return getString(bytes, "GBK");
    }

    /**
     * 将byte数组转成string
     *
     * @param bytes 数组
     * @return String
     */
    public static String byteToString(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return "";
        }
        String strContent = "";
        try {
            strContent = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strContent;
    }

    private static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static byte[] readBytes(byte[] source, int from, int length) {
        byte[] result = new byte[length];
        System.arraycopy(source, from, result, 0, length);
        return result;
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }


    private static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }


    public static String getStringUTF8(byte[] bytes, int start, int length) {
        if (null == bytes || bytes.length == 0) {
            return "";
        }
        // 去除NULL字符
        byte zero = 0x00;
        for (int i = start; i < length && i < bytes.length; i++) {
            if (bytes[i] == zero) {
                length = i-start;
                break;
            }
        }
        return getString(bytes, start, length, "UTF-8");
    }
    private static String getString(byte[] bytes, int start, int length, String charsetName) {
        return new String(bytes, start, length, Charset.forName(charsetName));
    }

    public static String byte2hex(byte[] buffer) {
        String h = "";
        if (null == buffer) {
            return h;
        }
        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }
        return h;
    }

    public static String timeStamp2Date(String format) {

        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd-HH-mm-ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long time = System.currentTimeMillis();
        return sdf.format(new Date(time));
    }

    public static boolean isMultiStreamPlatform() {
	    if (DJISDKManager.getInstance() == null){
	        return false;
        }
        Model model = DJISDKManager.getInstance().getProduct().getModel();
        return model != null && (model == Model.INSPIRE_2
                || model == Model.MATRICE_200
                || model == Model.MATRICE_210
                || model == Model.MATRICE_210_RTK
                || model == Model.MATRICE_600
                || model == Model.MATRICE_600_PRO
                || model == Model.A3
                || model == Model.N3);
    }

    public static String BytesToHexString(byte[] b, int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String hex = Integer.toHexString(b[i] & 0x0FF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            ret +=  hex.toUpperCase() + " ";

        }
        return ret;
    }

    /**
     * 根据经纬度 距离 角度 计算下一点坐标
     *
     * @param lat 纬度
     * @param lng 经度
     * @param distance 距离
     * @param angle 角度 正负都可用
     * @return Point
     */
    public static Point GetPoint(double lat, double lng, double distance, double angle)
    {

        double dx = distance  * Math.sin(angle * Math.PI / 180.0);
        double dy = distance  * Math.cos(angle * Math.PI / 180.0);

        double ec = Eb + (Ea - Eb) * (90.0 - lat) / 90.0;
        double ed = ec * Math.cos(lat * Math.PI / 180.0);

        double newLon = (dx / ed + lng * Math.PI / 180.0) * 180.0 / Math.PI;
        double newLat = (dy / ec + lat * Math.PI / 180.0) * 180.0 / Math.PI;

        Point p = new Point();
        p.setLatitude(newLat);
        p.setLongitude(newLon);
        return p;
    }

    /**
     * 根据两点经纬度 获取偏航角度
     *
     * @param lat1 纬度
     * @param lng1 经度
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return Aangle(0-360)
     */
    public static double myGetAngel(Double lat1,Double lng1,Double lat2,Double lng2){
        double a = Math.toRadians(90-lat2);

        double b = Math.toRadians(90-lat1);

        double ab = Math.toRadians(lng2-lng1);

        double cosc = Math.cos(a)*Math.cos(b)+Math.sin(a)*Math.sin(b)*Math.cos(ab);

        if (cosc < -1.0) cosc = -1.0;

        if (cosc >1.0) cosc =1.0;

        double c = Math.acos(cosc);

        double sinA = (Math.sin(a)*Math.sin(ab))/Math.sin(c);

        if (sinA < -1.0) sinA = -1.0;

        if (sinA >1.0) sinA =1.0;

        double A = Math.asin(sinA);

        double Aangle = Math.toDegrees(A);
        if (lng2 == lng1){

        }else if (lat2 == lat1){

        }else if (lng2 > lng1 && lat2 > lat1){//B相对于A来说位于第一象限

        }else if (lng2 < lng1 && lat2 > lat1){//第二象限

            Aangle =360+Aangle;

        }else{//第三，四象限

            Aangle =180-Aangle;

        }
        return Aangle;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return 距离
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * Ea;
        s = Math.round(s * 10000d) / 10000d;
        return s;
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 将byte数组进行MD5加密
     *
     * @param cheByte 数组
     * @return byte[]
     */
    public static byte[] getMD5(byte[] cheByte) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(cheByte);
            return md.digest();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将指定路径的文件进行MD5加密
     *
     * @param path 文件路径
     * @return byte[]
     */
    public static byte[] getFileMD5(String path) {
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            File mFile = new File(path);
            FileInputStream fis = new FileInputStream(mFile);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
