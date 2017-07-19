package com.leo.weibo.crawler.util;


import com.google.gson.Gson;
import com.leo.weibo.commons.vo.Card;
import com.leo.weibo.commons.vo.FansVO;
import com.leo.weibo.commons.vo.UserDetailVO;
import com.leo.weibo.commons.vo.UserVO;
import com.leo.weibo.crawler.entity.RequestParamEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created by Leo on 2017/2/23.
 */
public class NetworkUtils {

    private static final Log LOG = LogFactory.getLog(NetworkUtils.class);
    private static final String UTF_8 = "utf-8";
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Mobile Safari/537.36";
    private static final String DEFAULT_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    private static final String DEFAULT_ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8";
    private static final String DEFAULT_HOST = "m.weibo.cn";
    private static final String DEFAULT_CONNECTION = "close";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(300);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public enum REQUEST_HEADER {

        USER_AGENT("User-Agent"), ACCEPT("Accept"), ACCEPT_LANGUAGE("Accept-Language"), HOST("Host"), CONNECTION("Connection");

        private String header;
        REQUEST_HEADER(String header){
            this.header = header;
        }

        public String value(){
            return header;
        }

    }


    private static HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                return false;
            }

            //总是重试
            if (true)
                return true;

            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                LOG.error("ERROR: NoHttpResponseException, will retry!");
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                LOG.error("ERROR: SSLHandshakeException, will NOT retry!");
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时
                LOG.error("ERROR: InterruptedIOException, will NOT retry!");
                return false;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                LOG.error("ERROR: UnknownHostException, will NOT retry!");
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                LOG.error("ERROR: ConnectTimeoutException, will NOT retry!");
                return false;
            }
            if (exception instanceof SSLException) {// ssl握手异常
                LOG.error("ERROR: SSLException, will NOT retry!");
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                LOG.info("HttpEntityEnclosingRequest, will retry!");
                return true;
            }
            return false;
        }
    };

    private static void configForWeiboMobile(HttpRequestBase httpRequestBase, RequestParamEntity paramEntity) {

        httpRequestBase.setHeader(REQUEST_HEADER.USER_AGENT.value(), StringUtils.isEmpty(paramEntity.getUserAgent()) ? DEFAULT_USER_AGENT : paramEntity.getUserAgent());
        httpRequestBase.setHeader(REQUEST_HEADER.ACCEPT.value(), DEFAULT_ACCEPT);
        httpRequestBase.setHeader(REQUEST_HEADER.ACCEPT_LANGUAGE.value(), DEFAULT_ACCEPT_LANGUAGE);
        httpRequestBase.setHeader(REQUEST_HEADER.HOST.value(), DEFAULT_HOST);
        httpRequestBase.setHeader(REQUEST_HEADER.CONNECTION.value(), DEFAULT_CONNECTION);

        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000);

        if(paramEntity.isViaProxy()) {
            HttpHost proxy = new HttpHost(paramEntity.getIp(), paramEntity.getPort());
            builder = builder.setProxy(proxy);
        }

        // 配置请求的超时设置, 从默认配置加载
        RequestConfig requestConfig = builder.build();

        httpRequestBase.setConfig(requestConfig);
    }

    public static String sendGetRequest(CloseableHttpClient httpClient, RequestParamEntity paramEntity) throws IOException {

        LOG.info("prepare to send GET request to :"+paramEntity.getRequestUrl());

        String responseContent;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpget = new HttpGet(paramEntity.getRequestUrl());
            configForWeiboMobile(httpget, paramEntity);
            response = httpClient.execute(httpget, HttpClientContext.create());

            LOG.info("Status Code : "+response.getStatusLine().getStatusCode()+ " for Url : "+paramEntity.getRequestUrl());

            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, UTF_8);
            //接受长连接的流式数据,用于彻底关闭连接
            EntityUtils.consume(entity);
        } catch (IOException e) {
            LOG.error("ERROR when send GET request, message is :"+e.getMessage());
            throw e;
        } finally {
            try {
                if(response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static CloseableHttpClient getAutoRetryHttpClientWithCookieStore(CookieStore cookieStore, RequestParamEntity paramEntity) {

        HttpClientBuilder builder = HttpClients
                .custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultCookieStore(cookieStore);

        //begin set proxy if need
        if (paramEntity.isViaProxy() && paramEntity.isAuthentication()) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(paramEntity.getIp(), paramEntity.getPort()),
                    new UsernamePasswordCredentials(paramEntity.getUsername(), paramEntity.getPassword()));
            builder = builder.setDefaultCredentialsProvider(credsProvider);
            LOG.info("set proxy for IP:"+paramEntity.getIp()+", Port:"+paramEntity.getPort()+", uname:"+paramEntity.getUsername()+", password:"+paramEntity.getPassword());
        }


        return builder.build();
    }

    public static CloseableHttpClient getAutoRetryHttpClient() {
        return HttpClients
                .custom()
                .setRetryHandler(httpRequestRetryHandler)
                .build();
    }

    public static void main(String[] args) {

        RequestParamEntity paramEntity = new RequestParamEntity();
        paramEntity.setRequestUrl("http://m.weibo.cn/container/getIndex?type=uid&value=1316653494");
        paramEntity.setUserAgent(null);
        paramEntity.setIp("106.3.92.153");
        paramEntity.setPort(29304);
        paramEntity.setUsername("fixyfoxy");
        paramEntity.setPassword("EsunupE6a");
        paramEntity.setViaProxy(true);
//        paramEntity.setAuthentication(true);

        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = getAutoRetryHttpClientWithCookieStore(cookieStore, paramEntity);
        Gson gson = new Gson();
        try {

            String content = sendGetRequest(httpClient, paramEntity);
            LOG.info(content);

            UserVO userVO = gson.fromJson(content, UserVO.class);
//            LOG.info(userVO.getCards().size());

            LOG.info(userVO.getProfileId());
//            LOG.info(userVO.findLocationOid());
//            LOG.info(userVO.findItemContent(UserDetailVO.CREDIT));
//            LOG.info(userVO.findItemContent(UserDetailVO.LEVEL));
//            LOG.info(userVO.findItemContent(UserDetailVO.LOCATION));
//            LOG.info(userVO.findItemContent(UserDetailVO.REGISTER));
//            LOG.info(userVO.findItemContent(UserDetailVO.SCHOOL));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
