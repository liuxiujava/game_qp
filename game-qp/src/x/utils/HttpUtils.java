package x.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import x.executor.ExecutorDispatcher;

/**
 * CTP处理工具类
 *
 * @author Administrator
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class, ExecutorDispatcher.LogActor);


    private static String LINE_END = "\r\n";
    private static String PREFIX = "--";
    private static String BOUNDARY = "*****";

    public static String sendMsg(JSONObject obj, String urlstr) {
        return sendMsg(obj, urlstr, "json");
    }

    public static String sendMsg(JSONObject obj, String urlstr, String type) {
        StringBuffer sb = new StringBuffer("");
        StringBuffer requestBb = new StringBuffer("");
        String contentType = "application/json";
        long begin = System.currentTimeMillis();
        try {
            // 创建连接
            URL url = new URL(urlstr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(1000);
            connection.setInstanceFollowRedirects(true);
            String request = "";
            if ("form".equals(type)) {
                connection.setRequestMethod("GET");
                contentType = "application/x-www-form-urlencoded;charset=UTF-8";
                for (String key : obj.keySet()) {
                    requestBb.append("&" + key + "=");
                    requestBb.append(obj.get(key));
                }
                if (requestBb.length() > 1) {
                    request = requestBb.toString().substring(1);
                }
            } else {
                requestBb.append(obj.toJSONString());
                request = requestBb.toString();
            }

            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            // post时form格式可以传中文，如为out.writeBytes()可能有问题
            out.write(request.toString().getBytes());
            out.flush();
            out.close();

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;

            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            logger.error(sb + "");
            reader.close();
            // 断开连接
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            long end = System.currentTimeMillis();
            logger.error((end - begin) + "");
        }

    }

    public static String uploadFile(JSONObject obj, String urlstr) throws Exception {

        HttpURLConnection conn = null;
        InputStream input = null;
        OutputStream os = null;
        BufferedReader br = null;
        StringBuffer buffer = null;
        try {
            URL url = new URL(urlstr);
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(1000 * 10);
            conn.setReadTimeout(1000 * 10);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.connect();

            // 往服务器端写内容 也就是发起http请求需要带的参数
            os = new DataOutputStream(conn.getOutputStream());
            // 请求参数部分
            writeParams(obj, os);
            // 请求上传文件部分
            writeFile(obj, os);
            // 请求结束标志
            String endTarget = PREFIX + BOUNDARY + PREFIX + LINE_END;
            os.write(endTarget.getBytes());
            os.flush();

            // 读取服务器端返回的内容
            logger.error("" + conn.getResponseCode() + "_" + conn.getResponseMessage());
            if (conn.getResponseCode() == 200) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }

            br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            logger.error("返回报文:{}" + buffer.toString());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }

                if (os != null) {
                    os.close();
                    os = null;
                }

                if (br != null) {
                    br.close();
                    br = null;
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
                throw new Exception(ex);
            }
        }
        return buffer.toString();
    }

    /**
     * ContentType
     *
     * @param file
     * @return
     * @throws IOException
     * @Description:
     */
    public static String getContentType(File file) throws Exception {
        String streamContentType = "application/octet-stream";
        String imageContentType = "";
        ImageInputStream image = null;
        try {
            image = ImageIO.createImageInputStream(file);
            if (image == null) {
                return streamContentType;
            }
            Iterator<ImageReader> it = ImageIO.getImageReaders(image);
            if (it.hasNext()) {
                imageContentType = "image/" + it.next().getFormatName();
                return imageContentType;
            }
        } catch (IOException e) {
            logger.error("method getContentType failed", e);
            throw new Exception(e);
        } finally {
            try {
                if (image != null) {
                    image.close();
                }
            } catch (IOException e) {
                logger.error("ImageInputStream close failed", e);
                ;
                throw new Exception(e);
            }

        }
        return streamContentType;
    }

    /**
     * 对post参数进行编码处理并写入数据流中
     *
     * @throws Exception
     * @throws IOException
     */
    private static void writeParams(JSONObject obj, OutputStream os) throws Exception {
        try {
            String msg = "请求参数部分:\n";
            if (obj == null || obj.isEmpty()) {
                msg += "空";
            } else {
                StringBuilder requestParams = new StringBuilder();
                for (String key : obj.keySet()) {
                    requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    requestParams.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END);
                    requestParams.append("Content-Type: text/plain; charset=utf-8").append(LINE_END);
                    requestParams.append("Content-Transfer-Encoding: 8bit").append(LINE_END);
                    requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
                    requestParams.append(obj.get(key));
                    requestParams.append(LINE_END);
                }
                os.write(requestParams.toString().getBytes());
                os.flush();

                msg += requestParams.toString();
            }

            logger.error(msg);
        } catch (Exception e) {
            logger.error("writeParams failed", e);
            throw new Exception(e);
        }
    }

    /**
     * 对post上传的文件进行编码处理并写入数据流中
     *
     * @throws IOException
     */
    private static void writeFile(JSONObject obj, OutputStream os) throws Exception {
        InputStream is = null;
        try {
            String msg = "请求上传文件部分:\n";
            if (obj == null || obj.isEmpty()) {
                msg += "空";
            } else {
                StringBuilder requestParams = new StringBuilder();

                requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
                requestParams.append("Content-Disposition: form-data; name=\"").append(obj.getString("file")).append("\"; filename=\"").append(obj.getString("file")).append("\"")
                        .append(LINE_END);
                File file = new File(obj.getString("file"));
                requestParams.append("Content-Type:").append(getContentType(file)).append(LINE_END);
                requestParams.append("Content-Transfer-Encoding: 8bit").append(LINE_END);
                requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容

                os.write(requestParams.toString().getBytes());

                is = new FileInputStream(obj.getString("file"));

                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.write(LINE_END.getBytes());
                os.flush();

                msg += requestParams.toString();

            }
            logger.error(msg);
        } catch (Exception e) {
            logger.error("writeFile failed", e);
            throw new Exception(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                logger.error("writeFile FileInputStream close failed", e);
                throw new Exception(e);
            }
        }
    }

    public static JSONObject xml2Json(String str) throws DocumentException {
        Document doc = DocumentHelper.parseText(str);
        Element root = doc.getRootElement();

        String code = root.elementText("code");
        String msg = root.elementText("msg");
        String smsid = root.elementText("smsid");
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        obj.put("msg", msg);
        obj.put("smsid", smsid);

        return obj;
    }

    public static String sendGetMsg(JSONObject obj, String apiurl) {
        // TODO Auto-generated method stub
        return sendMsg(obj, apiurl, "form");
    }

    private static String HTTP_URL = "http://112.74.59.2:8088";

    public static void main(String[] args) {
        String ADD_URL = HTTP_URL + "/manager/api/game/updateAlias";
        JSONObject obj = new JSONObject();
        obj.put("plaalias", "修改");
        obj.put("plaid", "722223");
        sendMsg(obj, ADD_URL);

    }
}
