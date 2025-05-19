package org.plugin.hubtools.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.plugin.hubtools.HubTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerServerConnectListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HubTools plugin;
    private final MinecraftChannelIdentifier channel;

    public PlayerServerConnectListener(HubTools plugin) {
        this.plugin = plugin;
        this.channel = MinecraftChannelIdentifier.create("hubtools", "teleport");
    }

    @Subscribe
    public void onServerPostConnect(ServerPostConnectEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getDataStorage().containsPlayer(player.getUniqueId())) return;

        player.getCurrentServer().ifPresent(currentServer -> {
            if (currentServer.getServerInfo().getName().equals(plugin.getConfig().getServer())) {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     DataOutputStream out = new DataOutputStream(baos)) {

                    // 构建消息数据
                    out.writeUTF(plugin.getConfig().getWorld());
                    out.writeDouble(plugin.getConfig().getX());
                    out.writeDouble(plugin.getConfig().getY());
                    out.writeDouble(plugin.getConfig().getZ());

                    // 发送插件消息
                    player.sendPluginMessage(channel, baos.toByteArray());
                    plugin.getDataStorage().removePlayer(player.getUniqueId());

                    logger.info("Teleported {} to {}", player.getUsername(), plugin.getConfig().getServer());

                } catch (IOException e) {
                    logger.error("发送传送消息失败！", e);
                }
            }
        });
    }
}