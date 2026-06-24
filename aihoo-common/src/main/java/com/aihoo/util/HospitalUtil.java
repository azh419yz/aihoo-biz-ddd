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

@Slf4j
public class HospitalUtil {

    private static final String HexDigIts[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static final String URL = "https://wbyy.huadonghospital.com:8088/invoke.asmx";

    private static final String TOKEN = "DfKGhBXjRwEL8EVRMa3k09caDoSly4Si";

    
    public static JSONObject SplicingRequest(String method, Map<String, Object> hashMap) throws Exception {

        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);

        Element element = xml.createElement("Xml");
        xml.appendChild(element);

        Element head = xml.createElement("Head");
        Element request = xml.createElement("Request");
        element.appendChild(head);
        element.appendChild(request);

        String guid = new RandomGUID().toString();

        String s = insertRequest(guid, hashMap, method);

        Element version = xml.createElement("Version");
        version.setTextContent("1.0");
        Element securityMode = xml.createElement("SecurityMode");
        securityMode.setTextContent("MD5");
        Element checkSum = xml.createElement("CheckSum");
        checkSum.setTextContent(s);
        Element from = xml.createElement("From");
        from.setTextContent(InetAddress.getLocalHost().getHostAddress());
        Element to = xml.createElement("To");
        to.setTextContent("Booking.Interface.2");
        Element channelCode = xml.createElement("ChannelCode");
        channelCode.setTextContent("1210001");
        Element customer = xml.createElement("Customer");
        customer.setTextContent("2");
        Element poolId = xml.createElement("PoolId");
        poolId.setTextContent(hashMap.get("PoolId").toString());
        Element dataFormat = xml.createElement("DataFormat");
        dataFormat.setTextContent("0");
        Element messageId = xml.createElement("MessageId");
        messageId.setTextContent(guid);
        Element messageTime = xml.createElement("MessageTime");
        messageTime.setTextContent(new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date()));

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

        Element apiName = xml.createElement("ApiName");
        apiName.setTextContent(method);

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

    private static String insertRequest(String guid, Map<String, Object> hashMap, String method) {

        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);

        Element element = xml.createElement("Xml");
        xml.appendChild(element);

        Element request = xml.createElement("Request");
        element.appendChild(request);

        Element apiName = xml.createElement("ApiName");
        apiName.setTextContent(method);

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

        request.appendChild(apiName);
        request.appendChild(parameters);
        String result = XmlUtil.toStr(xml, "utf-8", false);
        String substring = result.substring(result.indexOf("<Request>"), result.indexOf("</Request>") + 10).replaceAll("\\s", "");
        return encodeByMD5(guid + substring + TOKEN);
    }

    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {

                MessageDigest md = MessageDigest.getInstance("MD5");

                byte[] results = md.digest(originString.getBytes());

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