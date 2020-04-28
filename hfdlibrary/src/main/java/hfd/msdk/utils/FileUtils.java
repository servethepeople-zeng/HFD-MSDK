package hfd.msdk.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hfd.msdk.hfdlibrary.HFDManager;
import hfd.msdk.model.TowerPoint;
import hfd.msdk.model.WayPoint;

import static hfd.msdk.hfdlibrary.HFDManager.logLevelType;
import static hfd.msdk.hfdlibrary.HFDManager.sendErrorMessage;

/**
 * Created by Arvin zeng on 19/12/20
 */
public class FileUtils {

    private static String filePath = Environment.getExternalStorageDirectory() + "/HFD";
    private static String logFilePath = Environment.getExternalStorageDirectory() + "/HFD/HFDMSDK.log";
//        private static String filePath = "/HFD";
//    private static String logFilePath = "/HFD/HFDMSDK.log";

    /**
     * 创建保存图片的文件夹,检查日志文件，超过20M了清空
     */
    public static void initLogFile() {
        File fileDir = new File(filePath);
        File logFileDir = new File(logFilePath);
        if(fileDir.exists()){
            if(logFileDir.exists()){
                if(getlogFileSize(logFileDir)){
                    logFileDir.delete();
                    try {
                        logFileDir.createNewFile();
                    }catch(Exception e){
                        HFDManager.sendErrorMessage("创建SDK日志文件失败");
                        e.printStackTrace();
                    }
                }
            }else{
                try {
                    logFileDir.createNewFile();
                }catch(Exception e){
                    HFDManager.sendErrorMessage("创建SDK日志文件失败");
                    e.printStackTrace();
                }
            }
        }else{
            fileDir.mkdir();
            try {
                logFileDir.createNewFile();
            }catch(Exception e){
                HFDManager.sendErrorMessage("创建SDK日志文件失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断日志文件是否大于20M，大于则清空
     *
     * @param file File实例
     * @return boolean
     */
    private static boolean getlogFileSize(File file) {
        long logSize = file.length();
        //B
        if (logSize < 1024) {
            return false;
        } else {
            logSize = logSize / 1024;
        }
        //KB
        if (logSize < 1024) {
            return false;
        } else {
            logSize = logSize / 1024;
        }
        //MB
        if(logSize > 20){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 往log文件中写入日志数据
     *
     * @param mContent
     */
    public static void writeLogFile(int logType, String mContent) {

        if (logType >= logLevelType) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(logFilePath, true);

                StringBuffer sb = new StringBuffer();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                if(logType == 0) {
                    sb.append(sdf.format(new Date()) + ":" +"info is ");
                }else if(logType == 1){
                    sb.append(sdf.format(new Date()) + ":" +"debug is ");
                }else if(logType == 1){
                    sb.append(sdf.format(new Date()) + ":" +"error is ");
                }else{
                    sb.append(sdf.format(new Date()) + ":");
                }
                sb.append(mContent + "\n");
                out.write(sb.toString().getBytes("utf-8"));
            } catch (IOException ex) {
                HFDManager.sendErrorMessage("写入日志文件失败");
                ex.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();   //关闭流
                    }
                } catch (IOException e) {
                    HFDManager.sendErrorMessage("写入日志文件失败");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载xml文件 只查找一个对应的文件
     * /mnt/internal_sd/
     * @param towerList
     * @return List<WayPoint>
     */
    public static List<WayPoint> loadXml(List<TowerPoint> towerList){
        List<WayPoint> listMarkPoint = new ArrayList<WayPoint>();
//        List<WayPoint> tempMarkPoint = new ArrayList<WayPoint>();
//        List<WayPoint> tempMarkPoint1 = new ArrayList<WayPoint>();
        String towerNames = "";
        for(int i=0;i<towerList.size();i++) {
            towerNames = towerNames+towerList.get(i).getTowerNum();
        }
        System.out.println(towerNames);
        try {
            File file = new File(filePath+"/"+towerNames+".xml");
            //File file = new File("F:\\"+towerNames+".xml");
            //System.out.println(file.getPath());
            InputStream inputStream = new FileInputStream(file);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            MySaxHandler handler = new MySaxHandler();
            saxParser.parse(inputStream, handler);
            inputStream.close();
            listMarkPoint = handler.getTowerPoints();
        } catch (Exception e) {
            e.printStackTrace();
            listMarkPoint.clear();
            sendErrorMessage("航点文件"+towerNames+".xml缺失或出错，请检查航点文件");
        }
        Collections.sort(listMarkPoint, new Comparator<WayPoint>() {
            @Override
            public int compare(WayPoint o1, WayPoint o2) {
                int i = o1.getSeqNumber() - o2.getSeqNumber();
                return i;
            }
        });

        return listMarkPoint;
    }

    /**
     * 新加载xml文件 根据杆塔号进行航点文件的拼接
     * /mnt/internal_sd/
     * @param towerList
     * @return List<WayPoint>
     */
    public static List<WayPoint> newLoadXml(List<TowerPoint> towerList){
        List<WayPoint> listMarkPoint = new ArrayList<WayPoint>();
        List<WayPoint> tempMarkPoint = new ArrayList<WayPoint>();
        for(int i=0;i<towerList.size();i++) {
            tempMarkPoint.clear();
            try {
                File file = new File(filePath+"/"+towerList.get(i).getTowerNum()+"A.xml");
                //File file = new File("F:\\"+towerNames+".xml");
                //System.out.println(file.getPath());
                InputStream inputStream = new FileInputStream(file);
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser saxParser = spf.newSAXParser();
                MySaxHandler handler = new MySaxHandler();
                saxParser.parse(inputStream, handler);
                inputStream.close();
                tempMarkPoint = handler.getTowerPoints();
            } catch (Exception e) {
                e.printStackTrace();
                listMarkPoint.clear();
                sendErrorMessage("航点文件"+towerList.get(i).getTowerNum()+"A.xml缺失或出错，请检查航点文件");
            }
            listMarkPoint.addAll(tempMarkPoint);
        }

        for(int i=towerList.size()-1;i>=0;i--) {
            tempMarkPoint.clear();
            try {
                File file = new File(filePath+"/"+towerList.get(i).getTowerNum()+"B.xml");
                //File file = new File("F:\\"+towerNames+".xml");
                //System.out.println(file.getPath());
                InputStream inputStream = new FileInputStream(file);
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser saxParser = spf.newSAXParser();
                MySaxHandler handler = new MySaxHandler();
                saxParser.parse(inputStream, handler);
                inputStream.close();
                tempMarkPoint = handler.getTowerPoints();
            } catch (Exception e) {
                e.printStackTrace();
                listMarkPoint.clear();
                sendErrorMessage("航点文件"+towerList.get(i).getTowerNum()+"B.xml缺失或出错，请检查航点文件");
            }
            listMarkPoint.addAll(tempMarkPoint);
        }

        return listMarkPoint;
    }
}
