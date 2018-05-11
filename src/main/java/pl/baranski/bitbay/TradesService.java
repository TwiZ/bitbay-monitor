package pl.baranski.bitbay;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.baranski.bitbay.so.TradesSO;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Component
public class TradesService {

    private RestTemplate restTemplate = restTemplate();

    public RestTemplate restTemplate() {
        if (restTemplate != null) {
            return restTemplate;
        }
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("www-le.dienste.telekom.de", 80));
        requestFactory.setProxy(proxy);

        return new RestTemplate(requestFactory);
    }

    public TradesSO[] getTrades(String crypto, String currency) {
        return restTemplate.getForObject(
                "https://bitbay.net/API/Public/" + crypto + currency + "/trades.json?sort=desc",
                TradesSO[].class);
    }
}
