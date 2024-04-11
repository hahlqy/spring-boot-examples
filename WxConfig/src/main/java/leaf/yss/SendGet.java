package leaf.yss;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

public class SendGet {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat sdf_datetime_format = new SimpleDateFormat(DATETIME_FORMAT);

    /**
     * 获取商户开户意愿确认状态
     * @return
     */
    public String doGet(){
        HashMap<String, String> reqParams = new HashMap<String, String>();
        // 时间戳
        String tempStr = sdf_datetime_format.format(Calendar.getInstance().getTime());
        //随机串
        String nonce_str = "593BEC0C930BF1AFEB40B4A08C8FB242";
        // 请求签名串
        String signatureStr = "GET\n/v3/apply4subject/applyment/merchants/470563354/state\n"+ tempStr +"\n" + nonce_str + "\n";

        reqParams.put("signature",sign(signatureStr));
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

            FileInputStream instream = new FileInputStream("/dev/ideaProject/apiclient_cert.p12");
            try {
                // 指定PKCS12的密码(商户ID)
                clientStore.load(instream, "1900009211".toCharArray());
            } finally {
                instream.close();
            }
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(clientStore, "1900009211".toCharArray()).build();

            // 指定TLS版本
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
            // 设置httpclient的SSLSocketFactory
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            String subMchid = "470563354";
            String url = "https://api.mch.weixin.qq.com/v3/apply4subject/applyment/merchants/"+subMchid+"/state";
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("user-agent","Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            httpGet.setHeader("accept","*/*");

            String serialNo = "488E58D539B483AFA6A6F24E8AFC0A3609B71735";

            // 拼接Authorization
            String AuthorizationStr = "WECHATPAY2-SHA256-RSA2048" + "mchid=\"1900009191\",nonce_str=\"593BEC0C930BF1AFEB40B4A08C8FB242\",signature=" + "\""+ reqParams.get("signature") +"\"timestamp=\"" + tempStr + "\",serial_no=\"" + serialNo +"\"";
            httpGet.setHeader("Authorization",AuthorizationStr);

            CloseableHttpResponse response = null;
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                httpGet.abort();
            }
            HttpEntity entity = response.getEntity();
            byte[] restByte = EntityUtils.toByteArray(entity);
            String respStr = new String(restByte, Charset.forName("UTF-8"));
            System.out.println(respStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成Authorization签名
     */
    public String Authorization(){

        return "";
    }

    private String sign(String signContent){
        String sign = "";
        try {
            PrivateKey privateKey = this.getPrivateKey("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC66v2H6AuLXEagY2PXO862qM9ThvTMd0U9zL80mHf55VKq33EdADPq+t1zqSA24s9+v5tOX/erB9OrxVA1Kg/PfbyP3wx8XgFNeovaCoTngo00uZKYA3YdoxnBgKewTvIQszeAwXXHdbJEBRVaF4T0PGWTM9y1FBWGLS9I8WitEoYfd+p3fIrzmoaq6LL6mUKpsM7UUmhTP03ne0W7uutOS+PXq+UCeMXP/OFg/A0r2SGdCoVGMv00/eU0ZGTzADN5fbHP09j8WNQrgEpFnW2pRiLu770a1u46JkyOe078oNd1QRnLaaNcNPcb4dmd75zrMNVBGFgxSh+h0Yq7Fo8pAgMBAAECggEBAIOpUMpxbehNKEaDm+kY7KOHRvqr/jUj8jb0yN4wY5o3qUJJP7DQ2sEy0dhNki53FvdN2gSb3qWvTq8YvkWkihx6kd65m17jDIHZxzbf3/v+3p379UF0drerklHV4Mh041dWFctw+hh0VAxLlF2wYWt0O4wzpMw8CGlJ3JvByBzX0g7eW8p7vgz7YL0vkXqOEsjgHibXQnd079PU5SPCdi6tiyTDb2Itv0jeiD6t+YyOwue9y3HO0JkP5C1pKF8CkignZhq8owGZv+wP98dugpgmKrcvZivqOtRU93ATD6jdz3zpOhk9n2h8GUpSubtKiSOeMp+BKRGCZ5eg1zmCzz0CgYEA44/T4PYBtTkixKR+CQrWj2gXXatyIg1yhRn649iHOpxyq/17JPNA1VV9Iz+zE2tZ5WvEusBhLfzXzfRBEyOVRKKmlyJpRarVLEomyY3MJaVaQ9OhIDVzHkVd5EvpQnqXI2f5smq0YVMqd/qy8FR/upmxzviwhAkKsmj+sTFvihcCgYEA0kbhcIVnM1sQppaIe/O4+hnkx0V+lQQG+8jPvVX4ObVQ5eXxnmIkAYtaXWzdR4f2unqXm/9kwllH5TBkaiYW9giJWpsfd70uzNevLsTb7FDStNvMcgKsfnMq3VI5DhmVpMws8Ljjv5YbN708Q8M/3j8+ba576V7vfgZcs8gSuL8CgYEAmX3RD4M/4an5TqQZMyYxVSeD4zWaVWe2oBrHBNpvVFRkcKgYYPMG/x6buqPjuSyGxV2so77RjcBnG88t+afdrh77uSOH2GsDQM1/XiIb2iSvPuLHYr9Kt9dYHn4phyLDQSmH3hVlN+Rt1+GswrhsM0QqEA3kmhqzMWXqrUBIGN8CgYAR/yECSViYy510NIP/388gEKFE8MyxokOavwgQk1AIWF1RH8n7HTkprKySPGP/4EIcmatqHYSZIZ0Gn4qojxP+AKMIzyf1Toq322nR3eVYp14xPnKT+2iZ9e9gO85IR6ZIEXVzMM/FMOqpbZe/0PEha1ZDeuB+C7MBnTfH50K67wKBgAQPZle992k15Hws5hpFSnlNCBbjeQa8W093J94IyqfDkGPSzEblnZenK3Gzim0VNtrn/aZH/xIx2QcC3e/yUX5flr3MKJ6wWHlvO6io7qBICr+ZFDwt5vYjGqVcR3g571U8Inmrxfj2Q/EfWIuatKtcPUOBQSiM5HzIaUVWWNwy");
            sign = new String(Base64.getEncoder().encode(signByte(signContent.getBytes("utf-8"),privateKey,"SHA256WithRSA")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sign;
    }

    /**
     * 将通过base64编码后的String类型的私钥字符串转换成为privatekey对象
     * @param strPriKey
     * @return
     * @throws Exception
     */
    private PrivateKey getPrivateKey(String strPriKey) throws Exception{
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(strPriKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(priKeySpec);
        return priKey;
    }


    private byte[] signByte(byte[] data,PrivateKey privateKey, String algorithmType)throws Exception{
        if(algorithmType == null || algorithmType.trim().equals("")){
            algorithmType = "MD5withRSA";
        }
        Signature signature = Signature.getInstance(algorithmType);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }



    public static void main(String[] args) {
        SendGet sendGet = new SendGet();
        String doGet = sendGet.doGet();
        System.out.println(doGet);
    }
}
