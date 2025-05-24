package org.plugin.hubtools;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataStorage {
    private final File dataFile;
    private final Map<UUID, String> playerData = new HashMap<>();
    private ConfigurationNode rootNode;
    private final Logger logger;

    public DataStorage(File dataFile, Logger logger) {
        this.dataFile = dataFile;
        this.logger = logger;
        load();
    }

    private void load() {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .file(dataFile)
                .build();

        try {
            if (!dataFile.exists()) {
                // 确保目录存在
                if (!dataFile.getParentFile().exists() && !dataFile.getParentFile().mkdirs()) {
                    throw new RuntimeException("无法创建插件目录: " + dataFile.getParentFile().getAbsolutePath());
                }

                // 创建文件
                if (!dataFile.createNewFile()) {
                    throw new RuntimeException("无法创建数据文件: " + dataFile.getAbsolutePath());
                }

                // 初始化空节点
                rootNode = loader.createNode();
                save(); // 保存初始内容
                logger.info("数据文件已初始化: {}", dataFile.getAbsolutePath());
                return;
            }

            // 加载现有配置
            rootNode = loader.load();
            rootNode.childrenMap().forEach((key, valueNode) -> {
                try {
                    UUID uuid = UUID.fromString(key.toString());
                    String server = valueNode.getString();
                    playerData.put(uuid, server);
                } catch (IllegalArgumentException e) {
                    logger.warn("无效的UUID: {}", key);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("加载 data.yml 失败", e);
        }
    }

    public void save() {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .file(dataFile)
                .build();

        try {
            rootNode = loader.createNode();
            playerData.forEach((uuid, server) -> {
                try {
                    rootNode.node(uuid.toString()).set(server);
                } catch (SerializationException e) {
                    throw new RuntimeException(e);
                }
            });
            loader.save(rootNode);
            logger.debug("数据文件已保存: {}", dataFile.getName());
        } catch (IOException e) {
            throw new RuntimeException("保存 data.yml 失败", e);
        }
    }

    public void addPlayer(UUID uuid, String server) {
        playerData.put(uuid, server);
        save();
        logger.info("玩家 {} 的数据已添加: {}", uuid, server);
    }

    public void removePlayer(UUID uuid) {
        playerData.remove(uuid);
        save();
        logger.info("玩家 {} 的数据已移除", uuid);
    }

    public boolean containsPlayer(UUID uuid) {
        return playerData.containsKey(uuid);
    }

    public String getServer(UUID uuid) {
        return playerData.get(uuid);
    }
}