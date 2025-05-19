package org.plugin.hubtools;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.plugin.hubtools.listeners.PlayerConnectListener;
import org.plugin.hubtools.listeners.PlayerDisconnectListener;
import org.plugin.hubtools.listeners.PlayerServerConnectListener;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "hubtools", name = "HubTools", version = "1.1.0")
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
        logger.info("HubTools v1.1 已加载");
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
    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        // 创建插件目录
        File pluginDir = dataDirectory.toFile();
        if (!pluginDir.exists() && !pluginDir.mkdirs()) {
            logger.error("无法创建插件目录");
            return;
        }

        // 创建配置文件
        File configFile = new File(pluginDir, "config.yml");
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
        } else {
            // 加载现有配置
            config = new HubToolsConfig(configFile);
        }
    }

    private void createDefaultConfig(File configFile) {
        YamlConfigurationLoader loader = null;
        try {
            loader = YamlConfigurationLoader.builder()
                    .file(configFile)
                    .build();

            ConfigurationNode root = loader.createNode();
            root.node("server").set("lobby");
            root.node("world").set("world");
            root.node("x").set(0.0);
            root.node("y").set(64.0);
            root.node("z").set(0.0);
            loader.save(root);
            logger.info("默认配置文件已生成");

        } catch (IOException e) {
            logger.error("配置文件创建失败", e);
        } finally {
        }
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