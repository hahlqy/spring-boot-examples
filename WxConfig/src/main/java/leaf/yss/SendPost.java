package leaf.yss;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Logger;

public class SendPost {
    /**
     * 配置查询
     * @return
     */

    public static String getMD5(String input) {
        byte[] source;
        source = input.getBytes(StandardCharsets.UTF_8);
        String md5String = null;
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte[] tmp = md.digest();
            char[] str = new char[16*2];
            int k = 0 ;
            for(int i=0;i<16;i++){
                byte b = tmp[i];
                str[k++] = hexDigits[b>>>4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            md5String = new String(str);
            return md5String.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSign(HashMap<String,Object> param,String key){
        Set<String> keys  = param.keySet();
        StringBuilder sb = new StringBuilder("");
        String[] list = new String[keys.size()];
        int index = 0;
        for(String s : keys){
           list[index++]  = s;
        }
        Arrays.sort(list);

        return null;
    }

    public String doPost00() {
        HashMap<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("appid", "wx2421b1c4370ec43b");
        reqParams.put("mch_id", "1900009211");
        reqParams.put("sub_mch_id", "469430045");
//        reqParams.put("sign_type", "RSA2");
        //签名
        Map<String, String> parseReqParams = this.paraFilter(reqParams);
        String realReqParams = this.createLinkString(parseReqParams);
        System.out.println(realReqParams);
        // 加签
        reqParams.put("sign",getMD5(realReqParams));

        System.out.println(getMD5(realReqParams));

        String reqParamsXml = this.toXml(reqParams);

        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

            FileInputStream instream = new FileInputStream("D:\\Code\\leafyss\\apiclient_cert.p12");
//            FileInputStream instream = new FileInputStream("/dev/ideaProject/apiclient_cert.p12");
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

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/mch/querysubdevconfig");
//            HttpPost httpPost = new HttpPost("https://101.91.0.140/secapi/mch/querysubdevconfig");
            StringEntity entityParams = new StringEntity(reqParamsXml, Charset.forName("UTF-8"));
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            CloseableHttpResponse response = null;
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
            }
            HttpEntity entity = response.getEntity();
            byte[] restByte = EntityUtils.toByteArray(entity);
            String respStr = new String(restByte,Charset.forName("UTF-8"));
            Map<String, String> dataMap = this.formatWXResponse(respStr.getBytes("UTF-8"));
            System.out.println(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  帮定appid配置
     * @return
     */
    public String doPost11(){
        HashMap<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("appid", "wx2421b1c4370ec43b");
        reqParams.put("mch_id", "1900009211");
        reqParams.put("sub_mch_id", "466923113");
        reqParams.put("sub_appid","wx2421b1c4370ec43b");
        reqParams.put("sign_type", "RSA2");
        //签名
        Map<String, String> parseReqParams = this.paraFilter(reqParams);
        String realReqParams = this.createLinkString(parseReqParams);
        // 加签
        reqParams.put("sign", "KknwAWcaMkXG/FLeMncIsMrHGRpAZa3Uo4riNjliZ0wo37FLh343xHhFUmEoR0mME5i50tjq/XRrl0zx3iKnODu+ayX427KW3nZK5cjXWiMDkckxoNAPbE7ya9AeEkK8NsLPEhW2sUgCNnEoZdJoSuRAmSWtYdVXHcr6hj3iInKCZYNdx1w47isOjVA/pJRJZHZmFXDJ34Ftb8Srg/hwoQ8IA7ryM6DuhlrooG+hpJW9p/u/eGp046kRmLYyJH6VGTCHF/FF7LokYPcEIZr43ojmpQiRl7t1ibU2MDA0nxyrdCafj42dHwGHr7j/aKLKZJt/NIqq3tPYGP2UJ6IF0w==");
        String reqParamsXml = this.toXml(reqParams);
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

//            FileInputStream instream = new FileInputStream("D:\\dev\\ideaProject\\apiclient_cert.p12");
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

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/mch/addsubdevconfig");
            StringEntity entityParams = new StringEntity(reqParamsXml, Charset.forName("UTF-8"));
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            CloseableHttpResponse response = null;
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
            }
            HttpEntity entity = response.getEntity();
            byte[] restByte = EntityUtils.toByteArray(entity);
            String respStr = new String(restByte,Charset.forName("UTF-8"));
            Map<String, String> dataMap = this.formatWXResponse(respStr.getBytes("UTF-8"));
            System.out.println(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 支付目录配置
     * @return
     */
    public String doPost22(){
        HashMap<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("appid", "wx2421b1c4370ec43b");
        reqParams.put("mch_id", "1900009211");
        reqParams.put("sub_mch_id", "466923113");
        reqParams.put("jsapi_path","http://www.qq.com/wechat/");
        reqParams.put("sign_type", "RSA2");
        //签名
        Map<String, String> parseReqParams = this.paraFilter(reqParams);
        String realReqParams = this.createLinkString(parseReqParams);
        // 加签
        reqParams.put("sign", "KknwAWcaMkXG/FLeMncIsMrHGRpAZa3Uo4riNjliZ0wo37FLh343xHhFUmEoR0mME5i50tjq/XRrl0zx3iKnODu+ayX427KW3nZK5cjXWiMDkckxoNAPbE7ya9AeEkK8NsLPEhW2sUgCNnEoZdJoSuRAmSWtYdVXHcr6hj3iInKCZYNdx1w47isOjVA/pJRJZHZmFXDJ34Ftb8Srg/hwoQ8IA7ryM6DuhlrooG+hpJW9p/u/eGp046kRmLYyJH6VGTCHF/FF7LokYPcEIZr43ojmpQiRl7t1ibU2MDA0nxyrdCafj42dHwGHr7j/aKLKZJt/NIqq3tPYGP2UJ6IF0w==");
        String reqParamsXml = this.toXml(reqParams);
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

//            FileInputStream instream = new FileInputStream("D:\\dev\\ideaProject\\apiclient_cert.p12");
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

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/mch/addsubdevconfig");
            StringEntity entityParams = new StringEntity(reqParamsXml, Charset.forName("UTF-8"));
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            CloseableHttpResponse response = null;
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
            }
            HttpEntity entity = response.getEntity();
            byte[] restByte = EntityUtils.toByteArray(entity);
            String respStr = new String(restByte,Charset.forName("UTF-8"));
            Map<String, String> dataMap = this.formatWXResponse(respStr.getBytes("UTF-8"));
            System.out.println(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 推荐关注公众号
     * @return
     */
    public String doPost33(){
        HashMap<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("sub_appid", "wx2421b1c4370ec43b");
        reqParams.put("mch_id", "1900009211");
        reqParams.put("sub_mch_id", "466923113");
        reqParams.put("subscribe_appid", "466923113");
        reqParams.put("nonce_str", "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        reqParams.put("sign_type", "RSA2");
        //签名
        Map<String, String> parseReqParams = this.paraFilter(reqParams);
        String realReqParams = this.createLinkString(parseReqParams);
        // 加签
        reqParams.put("sign", "KknwAWcaMkXG/FLeMncIsMrHGRpAZa3Uo4riNjliZ0wo37FLh343xHhFUmEoR0mME5i50tjq/XRrl0zx3iKnODu+ayX427KW3nZK5cjXWiMDkckxoNAPbE7ya9AeEkK8NsLPEhW2sUgCNnEoZdJoSuRAmSWtYdVXHcr6hj3iInKCZYNdx1w47isOjVA/pJRJZHZmFXDJ34Ftb8Srg/hwoQ8IA7ryM6DuhlrooG+hpJW9p/u/eGp046kRmLYyJH6VGTCHF/FF7LokYPcEIZr43ojmpQiRl7t1ibU2MDA0nxyrdCafj42dHwGHr7j/aKLKZJt/NIqq3tPYGP2UJ6IF0w==");
        String reqParamsXml = this.toXml(reqParams);
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

//            FileInputStream instream = new FileInputStream("D:\\dev\\ideaProject\\apiclient_cert.p12");
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

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/mkt/addrecommendconf");
            StringEntity entityParams = new StringEntity(reqParamsXml, Charset.forName("UTF-8"));
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            CloseableHttpResponse response = null;
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
            }
            HttpEntity entity = response.getEntity();
            byte[] restByte = EntityUtils.toByteArray(entity);
            String respStr = new String(restByte,Charset.forName("UTF-8"));
            Map<String, String> dataMap = this.formatWXResponse(respStr.getBytes("UTF-8"));
            System.out.println(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public Map<String,String> formatWXResponse(byte[] restByte){
        Map<String,String> results = null;
        try {
            results = this.toMap(restByte,"UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    private Map<String, String> toMap(byte[] restByte, String charset) throws Exception{
        SAXReader reader = new SAXReader(false);
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        reader.setFeature("http://xml.org/sax/features/external-general-entities",false);
        reader.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
        InputSource source = new InputSource(new ByteArrayInputStream(restByte));
        source.setEncoding(charset);
        Document doc = reader.read(source);
        return this.toMap(doc.getRootElement());
    }

    private Map<String, String> toMap(Element element) {
        Map<String,String> rest = new HashMap<String, String>();
        List<Element> els = element.elements();
        List<Element> tempEls = null;
        for (Element el : els) {
            tempEls = el.elements();
            if(tempEls == null || tempEls.isEmpty()){
                rest.put(el.getName().toLowerCase(Locale.ENGLISH),el.getText());
            } else {
                StringBuilder sb = new StringBuilder("<");
                sb.append(el.getName()).append(">");
                for (Element ele : tempEls) {
                    sb.append(ele.asXML());
                }
                sb.append("</").append(el.getName()).append(">");
                rest.put(el.getName().toLowerCase(Locale.ENGLISH),sb.toString());
            }
        }
        return rest;
    }

    private String toXml(HashMap<String, String> params) {
        String tag = "xml";
        StringBuilder buf = new StringBuilder();
        ArrayList<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        buf.append("<" + tag + ">");
        for (String key : keys) {
            buf.append("<").append(key).append(">");
            buf.append("<![CDATA[").append(params.get(key)).append("]]>");
            buf.append("</").append(key).append(">\n");
        }
        buf.append("</" + tag + ">");
        return buf.toString();
    }

    private String sign(Object o, String realReqParams) {
        return null;
    }

    /**
     * 把数组所有元素排序，并且按照"参数=参数值"的模式用"&"字符拼接成字符串
     *
     * @param params
     * @return
     */
    private String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String preStr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                preStr = preStr + key + "=" + value;
            } else {
                preStr = preStr + key + "=" + value + "&";
            }
        }
        preStr += "&key=qazxswedcvfrtgbnhyujmkiolpqazxsw";
        return preStr;
    }

    /**
     * 除去数组中的控制和签名参数
     *
     * @param sArray
     * @return
     */
    public Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() == 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (StringUtils.isEmpty(value) || "sign".equalsIgnoreCase(key)) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }


    public static void main(String[] args) {
        SendPost sendPost = new SendPost();
//        System.out.println(sendPost.getMD5("123").toUpperCase());
        String result00 = sendPost.doPost00();
        System.out.println(result00);


        /*String result11 = sendPost.doPost11();
        System.out.println(result11);*/

        /*String result22 = sendPost.doPost22();
        System.out.println(result22);*/

//        String result33 = sendPost.doPost33();
//        System.out.println(result33);
    }
}
