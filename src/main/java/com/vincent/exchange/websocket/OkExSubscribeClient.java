package com.vincent.exchange.websocket;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
@Component
public class OkExSubscribeClient {

    @Value("${okex.subscribe.url}")
    private String subscribeUrl;

    @Value("${http.proxy.host}")
    private String host;

    @Value("${http.proxy.port}")
    private Integer port;

    @Resource
    private OkExWebSocketListener webSocketListener;

    public void connect() {
        log.info("connecting okex...");
        Proxy localProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        OkHttpClient client = new OkHttpClient.Builder().proxy(localProxy).retryOnConnectionFailure(true).build();
        Request request = new Request.Builder().url(subscribeUrl).build();
        client.dispatcher().cancelAll();
        client.newWebSocket(request, webSocketListener);
        log.info("connected okex...");
    }

}
