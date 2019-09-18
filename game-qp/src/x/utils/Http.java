package x.utils;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import x.executor.ExecutorDispatcher;

/**
 * Created by Milo on 2017/11/11.
 */
public class Http {
    private static final Logger logger = LoggerFactory.getLogger(Http.class, ExecutorDispatcher.LogActor);

    static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(3000).setConnectionRequestTimeout(3000)
            .setSocketTimeout(3000).build();

    public static String get(String url, String param) {
        HttpGet request = new HttpGet(url + "?" + param);
        CloseableHttpClient client = HttpClients.createDefault();
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(),"utf-8");
                return result;
            }
        } catch (Exception e) {
            logger.error("http get error", e);
        }
        return "";
    }

    public static String get(String api) {
        HttpGet request = new HttpGet(api);
        CloseableHttpClient client = HttpClients.createDefault();
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(),"utf-8");
                return result;
            }
        } catch (Exception e) {
            logger.error("http get error", e);
        }
        return "";
    }
}