package org.plugin.hubtools;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.plugin.hubtools.listeners.PlayerDisconnectListener;
import org.plugin.hubtools.listeners.PlayerServerConnectListener;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "hubtools", name = "HubTools", version = "1.2.0")
public class HubTools {
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;
    private HubToolsConfig config;
    private DataStorage dataStorage;

    @Inject
    public HubTools(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        logger.info("===================================");
        logger.info("HubTools v1.2 已加载");
        logger.info("作者：NSrank & Augment & Qwen");
        logger.info("===================================");
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        // 初始化配置文件
        File configFile = new File(dataDirectory.toFile(), "config.yml");
        this.config = new HubToolsConfig(configFile);

        // 初始化数据存储
        File dataFile = new File(dataDirectory.toFile(), "data.yml");
        this.dataStorage = new DataStorage(dataFile, logger);

        // 注册事件监听器
        proxyServer.getEventManager().register(this, new PlayerDisconnectListener(this));
        proxyServer.getEventManager().register(this, new PlayerServerConnectListener(this));

        // 注册插件通道 - 用于向Paper服务器发送消息
        MinecraftChannelIdentifier channel = MinecraftChannelIdentifier.create("hubtools", "teleport");
        proxyServer.getChannelRegistrar().register(channel);

        logger.info("已注册插件消息通道: {}", channel.getId());

        logger.info("HubTools 插件已加载");
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public HubToolsConfig getConfig() {
        return config;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }


}