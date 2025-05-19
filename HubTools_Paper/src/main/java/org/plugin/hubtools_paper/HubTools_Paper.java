package org.plugin.hubtools_paper;

import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.hubtools_paper.listener.TeleportMessageListener;

public class HubTools_Paper extends JavaPlugin {
    @Override
    public void onEnable() {
        // 注册插件消息通道
        getServer().getMessenger().registerIncomingPluginChannel(this, "hubtools:teleport", new TeleportMessageListener());
        getLogger().info("===================================");
        getLogger().info("HubTools Paper版 v1.0 已加载");
        getLogger().info("作者：NSrank & Qwen2.5-Max");
        getLogger().info("===================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("HubTools Paper版已卸载");
    }
}
