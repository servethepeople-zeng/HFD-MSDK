package hfd.msdk.utils;

import java.util.ArrayList;
import java.util.List;

import hfd.msdk.model.Point;
import hfd.msdk.model.TowerPoint;

import static hfd.msdk.model.IConstants.TowDistance;
import static hfd.msdk.model.IConstants.TowHeight;
import static hfd.msdk.model.IConstants.TowHor;
import static hfd.msdk.model.IConstants.TowNavHeight;
import static hfd.msdk.model.IConstants.TowVer;
import static hfd.msdk.utils.Helper.GetPoint;
import static hfd.msdk.utils.Helper.myGetAngel;

/**
 * Created by Arvin zeng on 20/01/02
 *
 */
public class PointUtils {

    public static List<TowerPoint> backPointList = new ArrayList<TowerPoint>();
    public static List<TowerPoint> backTempPointList = new ArrayList<TowerPoint>();

    public static List<TowerPoint> oneTGeneratePoints(List<TowerPoint> towerList){
        backPointList.clear();
        List<TowerPoint> backPointList = new ArrayList<TowerPoint>();
        if(towerList.get(0).getTowerTypeName().equals("zx")) {
            //第一个点
            TowerPoint firPoint = new TowerPoint();
            Point myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer == 1 ? 225 - TowHor * 90 : TowHor * 90 - 45);
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(towerList.get(0).getTowerNum());
            firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
            firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
            firPoint.setAltitude(towerList.get(0).getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward(TowVer == 1 ? 45 - TowHor * 90 : 135 - TowHor * 270);
            firPoint.setPointType(4);
            backPointList.add(firPoint);
            //第二个点
            firPoint = new TowerPoint();
            myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer == 1 ? 135 + TowHor * 90 : 45 - TowHor * 90);
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(towerList.get(0).getTowerNum());
            firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
            firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
            firPoint.setAltitude(towerList.get(0).getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward(TowVer == 1 ? TowHor * 90 - 45 : TowHor * 270 - 135);
            firPoint.setPointType(4);
            backPointList.add(firPoint);
            //第三个点
            firPoint = new TowerPoint();
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(towerList.get(0).getTowerNum());
            firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
            firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
            firPoint.setAltitude(towerList.get(0).getAltitude() + TowHeight + TowNavHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward(TowVer == 1 ? TowHor * 90 - 45 : TowHor * 270 - 135);
            firPoint.setPointType(2);
            backPointList.add(firPoint);
            //第四个点
            firPoint = new TowerPoint();
            myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer == 1 ? 45 - TowHor * 90 : 135 + TowHor * 90);
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(towerList.get(0).getTowerNum());
            firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
            firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
            firPoint.setAltitude(towerList.get(0).getAltitude() + TowHeight + TowNavHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward(TowVer == 1 ? TowHor * 270 - 135 : TowHor * 90 - 45);
            firPoint.setPointType(2);
            backPointList.add(firPoint);
            //第五个点
            firPoint = new TowerPoint();
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(towerList.get(0).getTowerNum());
            firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
            firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
            firPoint.setAltitude(towerList.get(0).getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward(TowVer == 1 ? TowHor * 270 - 135 : TowHor * 90 - 45);
            firPoint.setPointType(4);
            backPointList.add(firPoint);
            //第六个点
            firPoint = new TowerPoint();
            myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer == 1 ? TowHor * 90 - 45 : 225 - 90 * TowHor);
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(towerList.get(0).getTowerNum());
            firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
            firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
            firPoint.setAltitude(towerList.get(0).getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward(TowVer == 1 ? 135 - TowHor * 270 : 45 - TowHor * 90);
            firPoint.setPointType(4);
            backPointList.add(firPoint);
            return backPointList;
        }else{
            //sendErrorMessage("只上传了一个耐张塔，无法进行航线规划");
            return null;
        }
    }

    public static List<TowerPoint> twoTGeneratePoints(List<TowerPoint> towerList){
        backPointList.clear();
        List<TowerPoint> backPointList = new ArrayList<TowerPoint>();
        if(towerList.get(0).getTowerTypeName().equals("nz")||towerList.get(1).getTowerTypeName().equals("nz")){
            //sendErrorMessage("只上传两个塔的情况下，塔类型不能为耐张塔");
            return null;
        }else{
            //经度相同
            if(towerList.get(0).getLongitude()==towerList.get(1).getLongitude()){
                //第一个点在南边的话
                if(towerList.get(0).getLatitude()<towerList.get(1).getLatitude()){
                    List<TowerPoint> tempList = new ArrayList<TowerPoint>();
                    tempList.add(towerList.get(1));
                    tempList.add(towerList.get(0));
                    towerList.clear();
                    towerList = tempList;
                }
                TowerPoint firPoint = new TowerPoint();
                if(TowVer == 0){
                        //第一个点
                        Point myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?-45:45);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?135:-135);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第二个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?225:135);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?45:-45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第三个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?-45:45);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?135:-135);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第四个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?225:135);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?45:-45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第五个点
                        firPoint = new TowerPoint();
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight+TowNavHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?45:-45);
                        firPoint.setPointType(2);
                        backPointList.add(firPoint);
                        //第六个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?135:225);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight+TowNavHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-45:45);
                        firPoint.setPointType(2);
                        backPointList.add(firPoint);
                        //第七个点
                        firPoint = new TowerPoint();
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-45:45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第八个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?135:225);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?45:-45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第九个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?135:225);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-45:45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第十个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?45:-45);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-135:135);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                    }else{
                        //第一个点
                        firPoint = new TowerPoint();
                        Point myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?225:135);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?45:-45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第二个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?-45:45);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?135:-135);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第三个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?225:135);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?45:-45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第四个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?-45:45);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?135:-135);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第五个点
                        firPoint = new TowerPoint();
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight+TowNavHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?135:-135);
                        firPoint.setPointType(2);
                        backPointList.add(firPoint);
                        //第六个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?45:-45);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight+TowNavHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-135:135);
                        firPoint.setPointType(2);
                        backPointList.add(firPoint);
                        //第七个点
                        firPoint = new TowerPoint();
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-135:135);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第八个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowHor==0?135:225);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(0).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-45:45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第九个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?135:225);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?45:-45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                        //第十个点
                        firPoint = new TowerPoint();
                        myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowHor==0?135:225);
                        firPoint.setId(backPointList.size() + 1);
                        firPoint.setTowerNum(towerList.get(1).getTowerNum());
                        firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                        firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                        firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                        firPoint.setLongitude(myPoint.longitude);
                        firPoint.setLatitude(myPoint.latitude);
                        firPoint.setToward(TowVer==0?-45:45);
                        firPoint.setPointType(4);
                        backPointList.add(firPoint);
                    }

            }else{
                if(towerList.get(0).getLongitude()>towerList.get(1).getLongitude()){
                    List<TowerPoint> tempList = new ArrayList<TowerPoint>();
                    tempList.add(towerList.get(1));
                    tempList.add(towerList.get(0));
                    towerList.clear();
                    towerList = tempList;
                }
                TowerPoint firPoint = new TowerPoint();
                double myAngle = myGetAngel(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(),towerList.get(1).getLatitude(), towerList.get(1).getLongitude());
                //从西到东
                if(TowHor==0){
                    //第一个点
                    Point myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第二个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle>45?myAngle+135:myAngle-225):(float)(myAngle-135));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第三个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第四个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle<45?myAngle+135:myAngle-225):(float)(myAngle-135));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第五个点
                    firPoint = new TowerPoint();
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight+TowNavHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle<45?myAngle+135:myAngle-225):(float)(myAngle-135));
                    firPoint.setPointType(2);
                    backPointList.add(firPoint);
                    //第六个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight+TowNavHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle<45?myAngle+135:myAngle-225));
                    firPoint.setPointType(2);
                    backPointList.add(firPoint);
                    //第七个点
                    firPoint = new TowerPoint();
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle<45?myAngle+135:myAngle-225));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第八个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第九个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle>45?myAngle+135:myAngle-225));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第十个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                }else{
                    //第一个点
                    Point myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle<45?myAngle+135:myAngle-225):(float)(myAngle-135));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第二个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第三个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle>45?myAngle+135:myAngle-225):(float)(myAngle-135));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第四个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第五个点
                    firPoint = new TowerPoint();
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight+TowNavHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
                    firPoint.setPointType(2);
                    backPointList.add(firPoint);
                    //第六个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight+TowNavHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
                    firPoint.setPointType(2);
                    backPointList.add(firPoint);
                    //第七个点
                    firPoint = new TowerPoint();
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第八个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(0).getLatitude(), towerList.get(0).getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(0).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(0).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(0).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(0).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle>45?myAngle+135:myAngle-225));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第九个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                    //第十个点
                    firPoint = new TowerPoint();
                    myPoint = GetPoint(towerList.get(1).getLatitude(), towerList.get(1).getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
                    firPoint.setId(backPointList.size() + 1);
                    firPoint.setTowerNum(towerList.get(1).getTowerNum());
                    firPoint.setTowerNumber(towerList.get(1).getTowerNumber());
                    firPoint.setTowerTypeName(towerList.get(1).getTowerTypeName());
                    firPoint.setAltitude(towerList.get(1).getAltitude()+TowHeight);
                    firPoint.setLongitude(myPoint.longitude);
                    firPoint.setLatitude(myPoint.latitude);
                    firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle<45?myAngle+135:myAngle-225));
                    firPoint.setPointType(4);
                    backPointList.add(firPoint);
                }
            }
            return backPointList;
        }
    }

    public static List<TowerPoint> mulTGeneratePoints(List<TowerPoint> towerList){
        backPointList.clear();
        if(towerList.get(0).getTowerTypeName().equals("nz")||towerList.get(towerList.size()-1).getTowerTypeName().equals("nz")){
            //sendErrorMessage("上传的杆塔数据中起始点和终点不能为耐张塔");
            return null;
        }else{

            if(towerList.get(0).getLongitude()>=towerList.get(1).getLongitude()&&towerList.get(0).getLongitude()>=towerList.get(towerList.size()-1).getLongitude()){
                System.out.println("航点反向");
                List<TowerPoint> tempList = new ArrayList<TowerPoint>();
                for(int i=0;i<towerList.size();i++){
                    tempList.add(towerList.get(towerList.size()-i-1));
                }
                towerList.clear();
                towerList = tempList;
            }
            System.out.println("杆塔数为"+towerList.size());
            if(TowHor == 0){
                System.out.println("从西向东生成");
                for (int i = 0; i < towerList.size(); i++) {
                    //最后一个航点加两个跨塔点
                    if(i==towerList.size()-1){
                        System.out.println("最后一个塔 "+i);
                        lastTowerPoint(towerList.get(towerList.size()-2),towerList.get(towerList.size()-1));
                    }else{
                        if(towerList.get(i).getTowerTypeName().equals("zx")){
                            System.out.println("直线塔 "+i);
                            zxTowerPoint(towerList.get(i),towerList.get(i+1));
                        }else{
                            nzTowerPoint(towerList.get(i-1),towerList.get(i),towerList.get(i+1));
                        }
                    }
                }
            }else if(TowHor == 1){
                for (int i = towerList.size()-1; i > 0; i--) {
                    //最后一个航点加两个跨塔点
                    if(i==0){
                        lastTowerPoint1(towerList.get(0),towerList.get(1));
                    }else if(i == towerList.size()-1){
                        zxTowerPoint1(towerList.get(i-1),towerList.get(i));
                    }else{
                        if(towerList.get(i).getTowerTypeName().equals("zx")){
                            zxTowerPoint2(towerList.get(i),towerList.get(i+1));
                        }else{
                            nzTowerPoint1(towerList.get(i-1),towerList.get(i),towerList.get(i+1));
                        }
                    }
                }
            }
            for(int i=backTempPointList.size()-1;i>-1;i--){
                TowerPoint tempPoint = new TowerPoint();
                tempPoint.setId(backPointList.size() + 1);
                tempPoint.setTowerNum(backTempPointList.get(i).getTowerNum());
                tempPoint.setTowerNumber(backTempPointList.get(i).getTowerNumber());
                tempPoint.setTowerTypeName(backTempPointList.get(i).getTowerTypeName());
                tempPoint.setAltitude(backTempPointList.get(i).getAltitude());
                tempPoint.setLongitude(backTempPointList.get(i).getLongitude());
                tempPoint.setLatitude(backTempPointList.get(i).getLatitude());
                tempPoint.setToward(backTempPointList.get(i).getToward());
                tempPoint.setPointType(backTempPointList.get(i).getPointType());
                backPointList.add(tempPoint);
            }
            return backPointList;
        }
    }

    public static void lastTowerPoint(TowerPoint preOne,TowerPoint lastOne){
        double myAngle = myGetAngel(preOne.getLatitude(), preOne.getLongitude(),lastOne.getLatitude(), lastOne.getLongitude());
        TowerPoint firPoint = new TowerPoint();
        //第一个点
        Point myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第二个点
        firPoint = new TowerPoint();
        myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle<45?myAngle+135:myAngle-225):(float)(myAngle-135));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第三个点
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight+TowNavHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle<45?myAngle+135:myAngle-225):(float)(myAngle-135));
        firPoint.setPointType(2);
        backPointList.add(firPoint);
        //第四个点
        firPoint = new TowerPoint();
        myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight+TowNavHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle<45?myAngle+135:myAngle-225));
        firPoint.setPointType(2);
        backPointList.add(firPoint);
        //第五个点
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle<45?myAngle+135:myAngle-225));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第六个点
        firPoint = new TowerPoint();
        myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
    }

    public static void lastTowerPoint1(TowerPoint lastOne,TowerPoint preOne){
        double myAngle = myGetAngel(lastOne.getLatitude(), lastOne.getLongitude(),preOne.getLatitude(), preOne.getLongitude());
        TowerPoint firPoint = new TowerPoint();
        //第一个点
        Point myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>45?myAngle+135:myAngle-225):(float)(myAngle-135));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第二个点
        firPoint = new TowerPoint();
        myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第三个点
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight+TowNavHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
        firPoint.setPointType(2);
        backPointList.add(firPoint);
        //第四个点
        myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight+TowNavHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
        firPoint.setPointType(2);
        backPointList.add(firPoint);
        //第五个点
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第六个点
        myPoint = GetPoint(lastOne.getLatitude(), lastOne.getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(lastOne.getTowerNum());
        firPoint.setTowerNumber(lastOne.getTowerNumber());
        firPoint.setTowerTypeName(lastOne.getTowerTypeName());
        firPoint.setAltitude(lastOne.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle>45?myAngle+135:myAngle-225));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
    }

    public static void zxTowerPoint(TowerPoint currPoint,TowerPoint currPoint1){
        double myAngle = myGetAngel(currPoint.getLatitude(), currPoint.getLongitude(),currPoint1.getLatitude(), currPoint1.getLongitude());
        TowerPoint firPoint = new TowerPoint();
        //第一个点
        Point myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第二个点
        myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>45?myAngle+135:myAngle-225):(float)(myAngle-135));
        firPoint.setPointType(4);
        backPointList.add(firPoint);

        //倒数第一个点
        myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
        firPoint = new TowerPoint();
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
        firPoint.setPointType(4);
        backTempPointList.add(firPoint);
        //倒数第二个点
        myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
        firPoint = new TowerPoint();
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle>45?myAngle+135:myAngle-225));
        firPoint.setPointType(4);
        backTempPointList.add(firPoint);
    }

    public static void zxTowerPoint1(TowerPoint currPoint,TowerPoint currPoint1){
        double myAngle = myGetAngel(currPoint.getLatitude(), currPoint.getLongitude(),currPoint1.getLatitude(), currPoint1.getLongitude());
        TowerPoint firPoint = new TowerPoint();
        //第一个点
        Point myPoint = GetPoint(currPoint1.getLatitude(), currPoint1.getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint1.getTowerNum());
        firPoint.setTowerNumber(currPoint1.getTowerNumber());
        firPoint.setTowerTypeName(currPoint1.getTowerTypeName());
        firPoint.setAltitude(currPoint1.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle<45?myAngle+135:myAngle-225):(float)(myAngle-135));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第二个点
        myPoint = GetPoint(currPoint1.getLatitude(), currPoint1.getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint1.getTowerNum());
        firPoint.setTowerNumber(currPoint1.getTowerNumber());
        firPoint.setTowerTypeName(currPoint1.getTowerTypeName());
        firPoint.setAltitude(currPoint1.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
        firPoint.setPointType(4);
        backPointList.add(firPoint);

        //倒数第一个点
        myPoint = GetPoint(currPoint1.getLatitude(), currPoint1.getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint1.getTowerNum());
        firPoint.setTowerNumber(currPoint1.getTowerNumber());
        firPoint.setTowerTypeName(currPoint1.getTowerTypeName());
        firPoint.setAltitude(currPoint1.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle<45?myAngle+135:myAngle-225));
        firPoint.setPointType(4);
        backTempPointList.add(firPoint);
        //倒数第二个点
        myPoint = GetPoint(currPoint1.getLatitude(), currPoint1.getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint1.getTowerNum());
        firPoint.setTowerNumber(currPoint1.getTowerNumber());
        firPoint.setTowerTypeName(currPoint1.getTowerTypeName());
        firPoint.setAltitude(currPoint1.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
        firPoint.setPointType(4);
        backTempPointList.add(firPoint);

    }

    public static void zxTowerPoint2(TowerPoint currPoint,TowerPoint currPoint1){
        double myAngle = myGetAngel(currPoint.getLatitude(), currPoint.getLongitude(),currPoint1.getLatitude(), currPoint1.getLongitude());
        TowerPoint firPoint = new TowerPoint();
        //第一个点
        Point myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle-45:myAngle+45);
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle<45?myAngle+135:myAngle-225):(float)(myAngle-135));
        firPoint.setPointType(4);
        backPointList.add(firPoint);
        //第二个点
        myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle-135:myAngle+135);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle>135?myAngle-315:45+myAngle):(float)(myAngle-45));
        firPoint.setPointType(4);
        backPointList.add(firPoint);

        //倒数第一个点
        myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle+45:myAngle-45);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-135):(float)(myAngle<45?myAngle+135:myAngle-225));
        firPoint.setPointType(4);
        backTempPointList.add(firPoint);
        //倒数第二个点
        myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, TowVer==0?myAngle+135:myAngle-135);
        firPoint = new TowerPoint();
        firPoint.setId(backPointList.size() + 1);
        firPoint.setTowerNum(currPoint.getTowerNum());
        firPoint.setTowerNumber(currPoint.getTowerNumber());
        firPoint.setTowerTypeName(currPoint.getTowerTypeName());
        firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
        firPoint.setLongitude(myPoint.longitude);
        firPoint.setLatitude(myPoint.latitude);
        firPoint.setToward(TowVer==0?(float)(myAngle-45):(float)(myAngle>135?myAngle-315:45+myAngle));
        firPoint.setPointType(4);
        backTempPointList.add(firPoint);
    }

    public static void nzTowerPoint(TowerPoint prePoint,TowerPoint currPoint,TowerPoint nextPoint){
        double myAngle = myGetAngel(currPoint.getLatitude(), currPoint.getLongitude(),prePoint.getLatitude(), prePoint.getLongitude());
        double myAngle1 = myGetAngel(currPoint.getLatitude(), currPoint.getLongitude(),nextPoint.getLatitude(), nextPoint.getLongitude());
        System.out.println(myAngle+","+myAngle1);
        TowerPoint firPoint = new TowerPoint();
        if((myAngle-myAngle1)<180){
            //第一个点
            double fAngel = myAngle1 - (360 - (myAngle - myAngle1)) / 2 - 45;
            Point myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if (fAngel > 0) {
                firPoint.setToward((float) (-180 + fAngel));
            } else {
                firPoint.setToward((float) (180 + fAngel));
            }
            firPoint.setPointType(4);
            if (TowVer == 0)
                backPointList.add(firPoint);
            else
                backTempPointList.add(firPoint);
            //第二个点
            fAngel = myAngle1 - (360 - (myAngle - myAngle1)) / 2 + 45;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if (fAngel > 0) {
                firPoint.setToward((float) (-180 + fAngel));
            } else {
                firPoint.setToward((float) (180 + fAngel));
            }
            firPoint.setPointType(4);
            if (TowVer == 0)
                backPointList.add(firPoint);
            else
                backTempPointList.add(firPoint);

            //倒数第一个点
            fAngel = myAngle1 + (myAngle - myAngle1) / 2;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward((float) (fAngel - 180));
            firPoint.setPointType(4);
            if (TowVer == 0)
                backTempPointList.add(firPoint);
            else
                backPointList.add(firPoint);
        }else{
            //第一个点
            double fAngel = myAngle1-(360-(myAngle-myAngle1))/2;
            Point myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if(fAngel>0)
                firPoint.setToward((float)(fAngel-180));
            else
                firPoint.setToward((float)(fAngel+180));
            firPoint.setPointType(4);
            if(TowVer==0)
                backPointList.add(firPoint);
            else
                backTempPointList.add(firPoint);
            //倒数第一个点
            fAngel = myAngle+(myAngle1-myAngle)/2+45;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if(fAngel<180)
                firPoint.setToward((float)(fAngel-180));
            else
                firPoint.setToward((float)(180-fAngel));
            firPoint.setPointType(4);
            if(TowVer==0)
                backTempPointList.add(firPoint);
            else
                backPointList.add(firPoint);
            //倒数第二个点
            fAngel = myAngle+(myAngle1-myAngle)/2-45;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if(fAngel<180)
                firPoint.setToward((float)(fAngel-180));
            else
                firPoint.setToward((float)(180-fAngel));
            firPoint.setPointType(4);
            if(TowVer==0)
                backTempPointList.add(firPoint);
            else
                backPointList.add(firPoint);
        }
    }

    public static void nzTowerPoint1(TowerPoint prePoint,TowerPoint currPoint,TowerPoint nextPoint){
        double myAngle = myGetAngel(currPoint.getLatitude(), currPoint.getLongitude(),prePoint.getLatitude(), prePoint.getLongitude());
        double myAngle1 = myGetAngel(currPoint.getLatitude(), currPoint.getLongitude(),nextPoint.getLatitude(), nextPoint.getLongitude());
        TowerPoint firPoint = new TowerPoint();
        if((myAngle-myAngle1)<180){
            //第一个点
            double fAngel = myAngle1 - (360 - (myAngle - myAngle1)) / 2 + 45;
            Point myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if (fAngel > 0) {
                firPoint.setToward((float) (-180 + fAngel));
            } else {
                firPoint.setToward((float) (180 + fAngel));
            }
            firPoint.setPointType(4);
            if (TowVer == 0)
                backPointList.add(firPoint);
            else
                backTempPointList.add(firPoint);
            //第二个点
            fAngel = myAngle1 - (360 - (myAngle - myAngle1)) / 2 - 45;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setId(backPointList.size() + 1);
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if (fAngel > 0) {
                firPoint.setToward((float) (-180 + fAngel));
            } else {
                firPoint.setToward((float) (180 + fAngel));
            }
            firPoint.setPointType(4);
            if (TowVer == 0)
                backPointList.add(firPoint);
            else
                backTempPointList.add(firPoint);

            //倒数第一个点
            fAngel = myAngle1 + (myAngle - myAngle1) / 2;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude() + TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            firPoint.setToward((float) (fAngel - 180));
            firPoint.setPointType(4);
            if (TowVer == 0)
                backTempPointList.add(firPoint);
            else
                backPointList.add(firPoint);
        }else{
            //第一个点
            double fAngel = myAngle1-(360-(myAngle-myAngle1))/2;
            Point myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if(fAngel>0)
                firPoint.setToward((float)(fAngel-180));
            else
                firPoint.setToward((float)(fAngel+180));
            firPoint.setPointType(4);
            if(TowVer==0)
                backPointList.add(firPoint);
            else
                backTempPointList.add(firPoint);

            //倒数第一个点
            fAngel = myAngle+(myAngle-myAngle1)/2-45;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if(fAngel<180)
                firPoint.setToward((float)(fAngel-180));
            else
                firPoint.setToward((float)(180-fAngel));
            firPoint.setPointType(4);
            if(TowVer==0)
                backTempPointList.add(firPoint);
            else
                backPointList.add(firPoint);
            //倒数第二个点
            fAngel = myAngle+(myAngle-myAngle1)/2+45;
            myPoint = GetPoint(currPoint.getLatitude(), currPoint.getLongitude(), TowDistance, fAngel);
            firPoint = new TowerPoint();
            firPoint.setTowerNum(currPoint.getTowerNum());
            firPoint.setTowerNumber(currPoint.getTowerNumber());
            firPoint.setTowerTypeName(currPoint.getTowerTypeName());
            firPoint.setAltitude(currPoint.getAltitude()+TowHeight);
            firPoint.setLongitude(myPoint.longitude);
            firPoint.setLatitude(myPoint.latitude);
            if(fAngel<180)
                firPoint.setToward((float)(fAngel-180));
            else
                firPoint.setToward((float)(180-fAngel));
            firPoint.setPointType(4);
            if(TowVer==0)
                backTempPointList.add(firPoint);
            else
                backPointList.add(firPoint);

        }
    }
}
