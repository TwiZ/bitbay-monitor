package pl.baranski.bitbay;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class BitBay {

    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom().build();

        try {
            HttpHost proxy = new HttpHost("www-le.dienste.telekom.de", 80, "http");
            RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpget = new HttpGet("https://bitbay.net/API/Public/BTCUSD/trades.json");
            httpget.setConfig(config);

            System.out.println("Executing request " + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } finally {
            httpclient.close();
        }
    }

    //    public static void main(String[] args) throws Exception {
    //        SSLContext sslContext = new SSLContextBuilder()
    //                .loadTrustMaterial(null, (certificate, authType) -> true).build();
    //
    //        HttpHost proxy = new HttpHost("www-le.dienste.telekom.de", 443, "https");
    //        RequestConfig config = RequestConfig.custom()
    //                .setProxy(proxy)
    //                .build();
    //
    //        CloseableHttpClient client = HttpClients.custom()
    //                .setSSLContext(sslContext)
    //                .setSSLHostnameVerifier(new NoopHostnameVerifier())
    //                .build();
    //        HttpGet httpGet = new HttpGet("https://bitbay.net/API/Public/BTCUSD/trades.json");
    //        httpGet.setConfig(config);
    //        httpGet.setHeader("Accept", "application/xml");
    //
    //        HttpResponse response = client.execute(httpGet);
    //        System.out.println(response.getStatusLine().getStatusCode());
    //
    //    }

}