package org.plugin.hubtools;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataStorage {
    private final YamlConfigurationLoader loader;
    private final File dataFile; // 新增：直接保存File对象
    private final Map<UUID, String> playerData = new HashMap<>();

    public DataStorage(File dataFile) {
        this.dataFile = dataFile;
        this.loader = YamlConfigurationLoader.builder()
                .path(dataFile.toPath()) // 使用Path方式构建
                .build();
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        try {
            // 直接使用File对象检查存在性（而非通过loader）
            if (!dataFile.exists()) {
                save(); // 创建空配置
                return;
            }

            // 加载配置
            ConfigurationNode root = loader.load();

            // 遍历所有节点
            root.childrenMap().forEach((key, valueNode) -> {
                try {
                    UUID uuid = UUID.fromString(key.toString());
                    String server = valueNode.getString();
                    playerData.put(uuid, server);
                } catch (IllegalArgumentException e) {
                    // 忽略无效UUID
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("data.yml文件加载失败！", e);
        }
    }

    public void save() {
        try {
            // 创建根节点
            ConfigurationNode root = loader.createNode();

            // 填充数据（处理序列化异常）
            playerData.forEach((uuid, server) -> {
                try {
                    root.node(uuid.toString()).set(server);
                } catch (SerializationException e) {
                    throw new RuntimeException("数据序列化失败！", e);
                }
            });

            // 保存配置
            loader.save(root);
        } catch (IOException e) {
            throw new RuntimeException("data.yml文件保存失败！", e);
        }
    }

    // 保持原有方法不变
    public void addPlayer(UUID uuid, String server) {
        playerData.put(uuid, server);
        save();
    }

    public void removePlayer(UUID uuid) {
        playerData.remove(uuid);
        save();
    }

    public boolean containsPlayer(UUID uuid) {
        return playerData.containsKey(uuid);
    }

    public String getServer(UUID uuid) {
        return playerData.get(uuid);
    }
}