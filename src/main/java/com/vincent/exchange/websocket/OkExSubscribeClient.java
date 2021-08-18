package com.vincent.exchange.websocket;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
@Component
public class OkExSubscribeClient {

    private static final String WSS_URL = "wss://ws.okex.com:8443/ws/v5/public";
    private static final String LOCAL_PROXY_HOST = "127.0.0.1";
    private static final Integer LOCAL_PROXY_PORT = 1087;

    @Resource
    private OkExWebSocketListener webSocketListener;

    public void connect() {
        log.info("connecting okex...");
        Proxy localProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(LOCAL_PROXY_HOST, LOCAL_PROXY_PORT));
        OkHttpClient client = new OkHttpClient.Builder().proxy(localProxy).retryOnConnectionFailure(true).build();
        Request request = new Request.Builder().url(WSS_URL).build();
        client.dispatcher().cancelAll();
        client.newWebSocket(request, webSocketListener);
        log.info("connected okex...");
    }

}
