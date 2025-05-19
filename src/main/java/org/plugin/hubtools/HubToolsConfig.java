package org.plugin.hubtools;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class HubToolsConfig {
    private final File configFile;
    private String server;
    private String world;
    private double x;
    private double y;
    private double z;

    public HubToolsConfig(File configFile) {
        this.configFile = configFile;
        load();
    }

    private void load() {
        try {
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .file(configFile)
                    .build();
            ConfigurationNode rootNode = loader.load();
            this.server = rootNode.node("server").getString();
            this.world = rootNode.node("world").getString();
            this.x = rootNode.node("x").getDouble();
            this.y = rootNode.node("y").getDouble();
            this.z = rootNode.node("z").getDouble();
        } catch (IOException e) {
            throw new RuntimeException("配置加载失败！", e);
        }
    }

    public String getServer() {
        return server;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}