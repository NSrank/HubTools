package org.plugin.hubtools;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.plugin.hubtools.listeners.PlayerConnectListener;
import org.plugin.hubtools.listeners.PlayerDisconnectListener;
import org.plugin.hubtools.listeners.PlayerServerConnectListener;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "hubtools", name = "HubTools", version = "1.0.0")
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
        logger.info("HubTools v1.0 已加载");
        logger.info("作者：NSrank & Qwen2.5-Max");
        logger.info("===================================");
    }

    public void init() {
        // 加载配置
        config = new HubToolsConfig(dataDirectory.resolve("config.yml").toFile());
        dataStorage = new DataStorage(dataDirectory.resolve("data.yml").toFile());

        // 注册事件监听器
        proxyServer.getEventManager().register(this, new PlayerDisconnectListener(this));
        proxyServer.getEventManager().register(this, new PlayerConnectListener(this));
        proxyServer.getEventManager().register(this, new PlayerServerConnectListener(this));

        // 注册插件通道
        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.create("hubtools", "teleport"));
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