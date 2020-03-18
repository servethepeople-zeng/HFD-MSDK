package hfd.msdk.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hfd.msdk.hfdlibrary.HFDManager;

import static hfd.msdk.hfdlibrary.HFDManager.logLevelType;

/**
 * Created by Arvin zeng on 19/12/20
 */
public class FileUtils {

    private static String filePath = Environment.getExternalStorageDirectory() + "/HFD";
    private static String logFilePath = Environment.getExternalStorageDirectory() + "/HFD/HFDMSDK.log";

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
}
