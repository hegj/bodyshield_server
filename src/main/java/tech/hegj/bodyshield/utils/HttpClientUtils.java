package tech.hegj.bodyshield.utils;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hegj on 2017/3/23.
 */
public class HttpClientUtils {

    static final int timeOut = 10 * 1000;

    private static CloseableHttpClient httpClient = null;

    private final static Object syncLock = new Object();

    private static void config(HttpRequestBase httpRequestBase) {
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
        httpRequestBase.setConfig(requestConfig);
    }

    /**
     * 获取HttpClient对象
     *
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient(100, 40, 100, hostname, port);
                }
            }
        }
        return httpClient;
    }

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 创建HttpClient对象
     *
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    private static CloseableHttpClient createHttpClient(int maxTotal,int maxPerRoute, int maxRoute, String hostname, int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", plainsf).register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 2) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(httpRequestRetryHandler).build();
        return httpClient;
    }

    public static String doPost(String url,List<NameValuePair> params) throws Exception{
        String result="";
        CloseableHttpResponse response=null;
        try{
            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"UTF-8");
            httpPost.setEntity(entity);
            config(httpPost);
            response = getHttpClient(url).execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity respEntity = response.getEntity();
                result = EntityUtils.toString(respEntity,"utf-8");
            }else{
                System.out.println("HTTP_CODE:"+response.getStatusLine().getStatusCode());
            }
        }catch(Exception ex){
            throw ex;
        }finally{
            if(response !=null){
                response.close();
            }
        }
        return result;
    }

    /**
     * 转发post请求
     * @author hegj
     * @param url
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static String doPost(String url,HttpServletRequest request) throws Exception{
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Iterator entries = request.getParameterMap().entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String)entry.getKey();
            String[] value = (String[])entry.getValue();
            int length = value.length;
            for(int i =0 ;i<length;i++){
                params.add(new BasicNameValuePair(key, value[i]));
            }
        }
        return doPost(url, params);
    }

    public static String doPost(String url, String body) throws Exception {
        String result="";
        CloseableHttpResponse response=null;
        try{
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(body, "UTF-8");
            httpPost.setEntity(stringEntity);
            config(httpPost);
            response = getHttpClient(url).execute(httpPost);
            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity respEntity = response.getEntity();
                result = EntityUtils.toString(respEntity,"utf-8");
            }else{
                System.out.println("HTTP_CODE:"+response.getStatusLine().getStatusCode());
            }
        }catch(Exception ex){
            throw ex;
        }finally{
            if(response !=null){
                response.close();
            }
        }
        return result;
    }

    public static String doGet(String url) throws Exception{
        String result="";
        CloseableHttpResponse response = null;
        try{
            HttpGet httpGet = new HttpGet(url);
            config(httpGet);
            response = getHttpClient(url).execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity respEntity = response.getEntity();
                result = EntityUtils.toString(respEntity,"utf-8");
            }else{
                System.out.println("HTTP_CODE:"+response.getStatusLine().getStatusCode());
            }
        }catch(Exception ex){
            throw ex;
        }finally{
            if(response!=null){
                response.close();
            }
        }
        return result;
    }
    /**
     * 转发get请求
     * @author hegj
     * @param url
     * @param request
     * @return
     * @throws Exception
     */
    public static String doGet(String url,HttpServletRequest request) throws Exception{
        StringBuilder urlbd = new StringBuilder(url);
        Map<String, String[]> params = request.getParameterMap();
        if(params != null && !params.isEmpty()){
            urlbd.append("?");
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    urlbd.append(key).append("=").append(value).append("&");
                }
            }
            urlbd.append("1=1");
        }
        return doGet(url);
    }
}
