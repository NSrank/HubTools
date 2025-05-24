package org.plugin.hubtools;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class HubToolsConfig {

    private final File configFile;
    private ConfigurationNode rootNode;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public HubToolsConfig(File configFile) {
        this.configFile = configFile;
        load();
    }

    private void load() {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .file(configFile)
                .build();

        try {
            if (!configFile.exists()) {
                // 确保目录存在
                if (!configFile.getParentFile().exists() && !configFile.getParentFile().mkdirs()) {
                    throw new RuntimeException("无法创建插件目录: " + configFile.getParentFile().getAbsolutePath());
                }

                // 创建文件并写入默认配置
                if (!configFile.createNewFile()) {
                    throw new RuntimeException("无法创建配置文件: " + configFile.getAbsolutePath());
                }

                rootNode = loader.createNode();
                rootNode.node("server").set("lobby");
                rootNode.node("world").set("world");
                rootNode.node("x").set(0.0);
                rootNode.node("y").set(64.0);
                rootNode.node("z").set(0.0);
                loader.save(rootNode);

                logger.info("默认配置文件已生成: {}", configFile.getAbsolutePath());
                return;
            }

            // 加载现有配置
            rootNode = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("加载 config.yml 失败", e);
        }
    }

    public String getServer() {
        return rootNode.node("server").getString("lobby");
    }

    public String getWorld() {
        return rootNode.node("world").getString("world");
    }

    public double getX() {
        return rootNode.node("x").getDouble(0.0);
    }

    public double getY() {
        return rootNode.node("y").getDouble(64.0);
    }

    public double getZ() {
        return rootNode.node("z").getDouble(0.0);
    }
}