package com.aihoo.util;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 华山医院的接口调用包
 **/
@Slf4j
public class HospitalUtil {

    private static final String HexDigIts[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static final String URL = "https://wbyy.huadonghospital.com:8088/invoke.asmx";

    private static final String TOKEN = "DfKGhBXjRwEL8EVRMa3k09caDoSly4Si";

    /**
     * 调用方法
     *
     * @param method  方法名称
     * @param hashMap 入参
     * @return JSONObject
     * @throws Exception
     */
    public static JSONObject SplicingRequest(String method, Map<String, Object> hashMap) throws Exception {
        //创建xml
        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);
        //插入根节点
        Element element = xml.createElement("Xml");
        xml.appendChild(element);
        //插入二层节点
        Element head = xml.createElement("Head");
        Element request = xml.createElement("Request");
        element.appendChild(head);
        element.appendChild(request);

        //GUID创建
        String guid = new RandomGUID().toString();
        //调用生成校验码
        String s = insertRequest(guid, hashMap, method);
        //插入head三层节点
        Element version = xml.createElement("Version");//版本号
        version.setTextContent("1.0");
        Element securityMode = xml.createElement("SecurityMode");//安全控制（None,MD5）
        securityMode.setTextContent("MD5");
        Element checkSum = xml.createElement("CheckSum");//校验码
        checkSum.setTextContent(s);
        Element from = xml.createElement("From");//发送信息者（获取当前系统的ip）
        from.setTextContent(InetAddress.getLocalHost().getHostAddress());
        Element to = xml.createElement("To");//目标接受者
        to.setTextContent("Booking.Interface.2");
        Element channelCode = xml.createElement("ChannelCode");//通道编码（医院提供）
        channelCode.setTextContent("1210001");
        Element customer = xml.createElement("Customer");//医院提供的医院代码（医院提供）（2）
        customer.setTextContent("2");
        Element poolId = xml.createElement("PoolId");//医院提供的资源池编号（医院提供）(2是专家预约 13是特需预约  16科室)
        poolId.setTextContent(hashMap.get("PoolId").toString());
        Element dataFormat = xml.createElement("DataFormat");//xml格式0/1json格式
        dataFormat.setTextContent("0");
        Element messageId = xml.createElement("MessageId");//唯一id
        messageId.setTextContent(guid);
        Element messageTime = xml.createElement("MessageTime");//信息发送时间
        messageTime.setTextContent(new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date()));
        //将三层节点插入二层节点中
        head.appendChild(version);
        head.appendChild(securityMode);
        head.appendChild(checkSum);
        head.appendChild(from);
        head.appendChild(to);
        head.appendChild(channelCode);
        head.appendChild(customer);
        head.appendChild(poolId);
        head.appendChild(dataFormat);
        head.appendChild(messageId);
        head.appendChild(messageTime);

        //方法名字
        Element apiName = xml.createElement("ApiName");
        apiName.setTextContent(method);
        //参数节点，内有所有键值对的参数内容
        Element parameters = xml.createElement("Parameters");
        if (hashMap.size() != 0) {
            for (String key : hashMap.keySet()) {
                if (!key.equals("PoolId")) {
                    String value = hashMap.get(key).toString();
                    Element xmlElement = xml.createElement(key);
                    xmlElement.setTextContent(value);
                    parameters.appendChild(xmlElement);
                }
            }
        } else {
            Element nodeId = xml.createElement("NodeId");
            nodeId.setTextContent(" ");
            parameters.appendChild(nodeId);
        }
        //讲节点注入request中
        request.appendChild(apiName);
        request.appendChild(parameters);
        String result = XmlUtil.toStr(xml, "utf-8", false);
        SoapClient client = SoapClient.create(URL)
                .setMethod("Processor", "http://tempuri.org/")
                .setParam("requestXml", result);
        String send = client.send(true);
        Document document = XmlUtil.parseXml(send);
        String textContent = XmlUtil.transElements(document.getElementsByTagName("ProcessorResult")).get(0).getTextContent();
        JSONObject jsonObject = JSONUtil.xmlToJson(textContent);
        JSONObject object = jsonObject.getJSONObject("Xml");
        return object.getJSONObject("Response");
    }

    //生成识别码
    private static String insertRequest(String guid, Map<String, Object> hashMap, String method) {
        //创建xml
        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);
        //插入根节点
        Element element = xml.createElement("Xml");
        xml.appendChild(element);
        //插入二层节点
        Element request = xml.createElement("Request");
        element.appendChild(request);

        //方法名
        Element apiName = xml.createElement("ApiName");
        apiName.setTextContent(method);
        //入参参数
        Element parameters = xml.createElement("Parameters");
        if (hashMap.size() != 0) {
            for (String key : hashMap.keySet()) {
                if (!key.equals("PoolId")) {
                    String value = hashMap.get(key).toString();
                    Element xmlElement = xml.createElement(key);
                    xmlElement.setTextContent(value);
                    parameters.appendChild(xmlElement);
                }
            }
        } else {
            Element nodeId = xml.createElement("NodeId");
            nodeId.setTextContent(" ");
            parameters.appendChild(nodeId);
        }

        //将三层节点插入二层节点中
        request.appendChild(apiName);  //实际执行的内容 方法名字吧
        request.appendChild(parameters); //参数
        String result = XmlUtil.toStr(xml, "utf-8", false);
        String substring = result.substring(result.indexOf("<Request>"), result.indexOf("</Request>") + 10).replaceAll("\\s", "");
        return encodeByMD5(guid + substring + TOKEN);
    }

    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (byte b1 : b) {
            resultSb.append(byteToHexString(b1));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HexDigIts[d1] + HexDigIts[d2];
    }

}