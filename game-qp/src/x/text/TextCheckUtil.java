package x.text;

import org.dom4j.DocumentHelper;
import x.utils.ServerInitConfig;
import x.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TextCheckUtil {

    public static String HTTP_POST(String URL1, String Data) throws Exception {
        BufferedReader In = null;
        PrintWriter Out = null;
        HttpURLConnection HttpConn = null;
        try {
            URL url = new URL(URL1);
            HttpConn = (HttpURLConnection) url.openConnection();
            HttpConn.setRequestMethod("POST");
            HttpConn.setDoInput(true);
            HttpConn.setDoOutput(true);

            Out = new PrintWriter(HttpConn.getOutputStream());
            Out.println(Data);
            Out.flush();

            if (HttpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer content = new StringBuffer();
                String tempStr = "";
                In = new BufferedReader(new InputStreamReader(HttpConn.getInputStream()));
                while ((tempStr = In.readLine()) != null) {
                    content.append(tempStr);
                }
                In.close();
                return content.toString();
            } else {
                throw new Exception("HTTP_POST_ERROR_RETURN_STATUS：" + HttpConn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Out.close();
            HttpConn.disconnect();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String HTTP_BACK_MESSAGE = "";
//        HTTP_BACK_MESSAGE = HTTP_POST("http://service2.winic.org/Service.asmx/SendMessages", getData("13162810242"));
//        System.out.println(HTTP_BACK_MESSAGE);
        String aa = "<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">0808110200455775</string>";//
        int num = aa.indexOf("<string");
        int num1 = aa.indexOf("</string>");
        String newStr = aa.substring(num, num1);
        num = aa.indexOf(">");
        System.out.println(newStr.substring(num));
        System.out.println(newStr.substring(num));
        switch (HTTP_BACK_MESSAGE) {
            case "-01":
                break;


        }
    }


//16位短信编号 短信提交成功 请注意查收短信
//-01 账号余额不足 登录web.900112.com充值
//-02 未开通接口授权 联系相关的客户经理，开通产品授权
//-03 账号密码错误 联系相关的客户经理，核对注册资料，重置密码
//-04 参数个数不对或者参数类型错误 检查参数是否乱码或者参数传了空值
//-110 IP被限制 联系技术支持
//-12 其他错误 联系技术支持
/*

    public static String getData(String num, String code) {
        String message = "尊敬的客户你好,你获取的验证码为:" + code + " 请输入及时验证,谢谢!";
        return "uid=" + ServerInitConfig.getA() + "&pwd=" + ServerInitConfig.getP() + "&tos=" + num + "&msg=" + message +
                "&otime=";
    }
*/


    public static Document readXml(String filePath) throws DocumentException {
        return readXml(new File(filePath));
    }


    public static Document readXml(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }

    public static Document readXml(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(inputStream);
    }


    public static String getAttribute(Element element, String attrName) {
        return element.attributeValue(attrName);
    }

    public static String getText(Element element) {
        return element.getText();
    }

    public static List<Element> getElements(Element element, String tagName) {
        return element.elements(tagName);
    }


}
