package org.plugin.hubtools_paper;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.plugin.hubtools_paper.listener.TeleportMessageListener;

/**
 * HubTools Paper端插件主类
 * 用于接收来自Velocity的传送指令并执行玩家传送
 * 
 * @author NSrank & Qwen2.5-Max & Kimi
 * @version 1.1.0
 */
public class HubTools_Paper extends JavaPlugin {
    private String serverName;

    @Override
    public void onEnable() {
        // 初始化配置文件
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        // 加载服务器名称
        serverName = config.getString("server-name", "lobby");

        // 注册插件消息通道 - 接收来自Velocity的传送指令
        getServer().getMessenger().registerIncomingPluginChannel(
            this, 
            "hubtools:teleport", 
            new TeleportMessageListener(serverName, getLogger())
        );

        getLogger().info("===================================");
        getLogger().info("HubTools Paper版 v1.1 已加载");
        getLogger().info("作者：NSrank & Qwen2.5-Max & Augment");
        getLogger().info("已加载服务器名: " + serverName);
        getLogger().info("已注册消息通道: hubtools:teleport");
        getLogger().info("===================================");
    }

    @Override
    public void onDisable() {
        // 注销插件消息通道
        getServer().getMessenger().unregisterIncomingPluginChannel(this, "hubtools:teleport");
        getLogger().info("HubTools Paper版已卸载");
    }

    /**
     * 获取服务器名称
     * @return 服务器名称
     */
    public String getServerName() {
        return serverName;
    }
}
