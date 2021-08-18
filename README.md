# GoblinExchangeTickerSync
通过 WebSocket 接口订阅 OkEx 的公共频道，监听数据并保存到 Redis 中。因为 OkEx 被墙的原因，运行本项目需要在本地有代理服务器，并修改配制文件中的代理服务器端口以及密码，如下：
```properties
http.proxy.host=127.0.0.1
http.proxy.port=1080
```