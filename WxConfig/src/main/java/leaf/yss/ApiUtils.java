package leaf.yss;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

public class ApiUtils {
//    public static void  refund(Order order) throws Exception {
//        try {
//            KeyStore clientStore = KeyStore.getInstance("PKCS12");
//            // 读取本机存放的PKCS12证书文件
//            FileInputStream instream = new FileInputStream("****\\apiclient_cert.p12");
//            try {
//                // 指定PKCS12的密码(商户ID)
//                clientStore.load(instream, "***".toCharArray());
//            } finally {
//                instream.close();
//            }
//            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(clientStore, "***".toCharArray()).build();
//            // 指定TLS版本
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
//                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//            // 设置httpclient的SSLSocketFactory
//            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//            try {
//                HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund"); // 设置响应头信息
////                httpost.addHeader("Connection", "keep-alive");
////                httpost.addHeader("Accept", "*/*");
////                httpost.addHeader("Content-Type", CONTENT_TYPE_FORM.toString());
////                httpost.addHeader("X-Requested-With", "XMLHttpRequest");
////                httpost.addHeader("Cache-Control", "max-age=0");
////                httpost.addHeader("User-Agent", DEFAULT_USER_AGENT);
//                httpost.setEntity(new StringEntity(getXmlStr(order), "UTF-8"));
//                CloseableHttpResponse response = httpclient.execute(httpost);
//                try {
//                    HttpEntity entity = response.getEntity();
//                    String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
//                    EntityUtils.consume(entity);
//                    System.out.println(jsonStr);
//                } finally {
//                    response.close();
//                }
//            } finally {
//                httpclient.close();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
