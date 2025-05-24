package org.plugin.hubtools.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
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

        // 获取玩家当前连接的服务器
        var serverConnection = player.getCurrentServer().orElse(null);
        if (serverConnection == null) {
            logger.warn("玩家 {} 没有当前服务器连接", player.getUsername());
            return;
        }

        RegisteredServer currentServer = serverConnection.getServer();
        String targetServer = plugin.getConfig().getServer();

        logger.info("玩家 {} 连接到服务器: {}, 目标服务器: {}",
                player.getUsername(), currentServer.getServerInfo().getName(), targetServer);

        if (currentServer.getServerInfo().getName().equals(targetServer)) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(baos)) {

                // 构建传送数据
                var config = plugin.getConfig();
                out.writeUTF(targetServer);       // 目标服务器名称
                out.writeUTF(config.getWorld());  // 世界名称
                out.writeDouble(config.getX());   // X坐标
                out.writeDouble(config.getY());   // Y坐标
                out.writeDouble(config.getZ());   // Z坐标

                // 关键修复：使用ServerConnection发送消息给Paper服务器，而不是发送给客户端
                boolean success = serverConnection.sendPluginMessage(channel, baos.toByteArray());

                // 记录详细日志
                if (success) {
                    logger.info("成功发送传送指令到Paper服务器: 玩家={}, 服务器={}, 世界={}, 坐标=({}, {}, {})",
                            player.getUsername(), targetServer, config.getWorld(), config.getX(), config.getY(), config.getZ());

                    // 传送指令发送成功后，清理玩家数据
                    plugin.getDataStorage().removePlayer(player.getUniqueId());
                    logger.info("已清理玩家 {} 的传送记录", player.getUsername());
                } else {
                    logger.error("发送传送指令失败: 玩家={}, 服务器={}", player.getUsername(), targetServer);
                }

            } catch (IOException e) {
                logger.error("传送数据构建失败: 玩家={}", player.getUsername(), e);
            }
        } else {
            logger.debug("玩家 {} 当前不在目标服务器 {}, 当前服务器: {}",
                    player.getUsername(), targetServer, currentServer.getServerInfo().getName());
        }
    }
}