package pl.baranski.bitbay.service;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.baranski.bitbay.so.TradesSO;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Service
public class TradesService {

    private RestTemplate restTemplate = restTemplate();

    @Bean
    public RestTemplate restTemplate() {
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
