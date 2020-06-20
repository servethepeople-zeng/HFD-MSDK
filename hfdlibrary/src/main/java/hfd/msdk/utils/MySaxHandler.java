package hfd.msdk.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import hfd.msdk.model.NewWayPoint;

import static hfd.msdk.model.IConstants.loadTowerId;

public class MySaxHandler extends DefaultHandler{

    private List<NewWayPoint> wayPoint;
    private NewWayPoint currentPoint;
    private String tagName;
    private String pointName;
    private StringBuffer sb;

    public List<NewWayPoint> getTowerPoints() {
        return wayPoint;
    }

    /**
     * 当SAX解析器解析到XML文档开始时，会调用的方法
     */
    @Override
    public void startDocument() throws SAXException {
        wayPoint = new ArrayList<>();
    }


    /**
     * 当SAX解析器解析到某个元素开始时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        sb = new StringBuffer();
        if (localName.equals("Point")) {
            currentPoint = new NewWayPoint();
            pointName = "Point";
        }
        this.tagName = localName;
    }

    /**
     * 当SAX解析器解析到某个属性值时，会调用的方法
     * 其中参数ch记录了这个属性值的内容
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if("Point".equals(pointName)&&tagName != null) {
            String temp = new String(ch, start, length);
            temp = temp.trim();
            sb.append(temp);
            String data = sb.toString();
            if ("id".equals(tagName)) {
                currentPoint.setId(loadTowerId);
                currentPoint.setTowerNum("#"+data);
            }
            else if ("type".equals(tagName))
                currentPoint.setPointType(Integer.parseInt(data));
            else if ("variety".equals(tagName))
                currentPoint.setVariety(Integer.parseInt(data));
            else if ("number".equals(tagName))
                currentPoint.setSeqNumber(Integer.parseInt(data));
            else if ("Latitude".equals(tagName))
                currentPoint.setLatitude(Float.parseFloat(data));
            else if ("Longitude".equals(tagName))
                currentPoint.setLongitude(Float.parseFloat(data));
            else if ("Altitude".equals(tagName))
                currentPoint.setAltitude(Float.parseFloat(data));
            else if ("Yaw".equals(tagName))
                currentPoint.setToward(Float.parseFloat(data));
            else if ("Pitch".equals(tagName))
                currentPoint.setApitch(Float.parseFloat(data));
            else if ("angle".equals(tagName))
                currentPoint.setAngle(Float.parseFloat(data));
            else if ("object".equals(tagName))
                currentPoint.setObject(Integer.parseInt(data));
            else if ("Orientation".equals(tagName))
                currentPoint.setOrientation(Integer.parseInt(data));
            else if ("ZoomPos".equals(tagName))
                currentPoint.setZoomPosition(Integer.parseInt(data));
            else if ("LeftZp".equals(tagName))
                currentPoint.setLeftZoomPosition(Integer.parseInt(data));
            else if ("RightZp".equals(tagName))
                currentPoint.setRightZoomPosition(Integer.parseInt(data));
            else if ("side".equals(tagName)) {
                if ("A".equals(data))
                    currentPoint.setSide(1);
                else if ("B".equals(data))
                    currentPoint.setSide(2);
                else
                    currentPoint.setSide(3);
            }
        }

//            if (tagName.equals("name")) {
//                this.currentPerson.setName(data);
//            }


//
//            currentPoint.setVariety(Integer.parseInt(attributes.getValue("variety")));
//            currentPoint.setTowerNumber(Integer.parseInt(attributes.getValue("number")));
//            currentPoint.setLatitude(Float.parseFloat(attributes.getValue("Latitude")));
//            currentPoint.setLongitude(Float.parseFloat(attributes.getValue("Longitude")));
//            currentPoint.setAltitude(Float.parseFloat(attributes.getValue("Altitude")));
//            currentPoint.setToward(Float.parseFloat(attributes.getValue("Yaw")));
//            currentPoint.setApitch(Float.parseFloat(attributes.getValue("Pitch")));
//            currentPoint.setAngle(Float.parseFloat(attributes.getValue("angle")));
//            currentPoint.setObject(Integer.parseInt(attributes.getValue("object")));
//
//            if("A".equals(attributes.getValue("side")))
//                currentPoint.setSide(1);
//            else if("B".equals(attributes.getValue("side")))
//                currentPoint.setSide(2);
//            else
//                currentPoint.setSide(3);
    }

    /**
     * 当SAX解析器解析到某个元素结束时，会调用的方法
     * 其中localName记录的是元素属性名
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endDocument();
        if("Point".equals(localName)){
            wayPoint.add(currentPoint); //将Point对象加入到List中
            currentPoint = null;
            pointName = null;
        }
        tagName=null;
    }

    /**
     * 当SAX解析器解析到XML文档结束时，会调用的方法
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}