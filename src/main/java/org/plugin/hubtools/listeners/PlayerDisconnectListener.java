package org.plugin.hubtools.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import org.plugin.hubtools.HubTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerDisconnectListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HubTools plugin;

    public PlayerDisconnectListener(HubTools plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        Player player = (Player) event.getPlayer();
        logger.debug("玩家 {} 触发退出事件", player.getUsername());

        ServerConnection serverConnection = player.getCurrentServer().orElse(null);
        if (serverConnection == null) {
            logger.debug("玩家 {} 未连接到任何服务器", player.getUsername());
            return;
        }

        String serverName = serverConnection.getServerInfo().getName();
        String targetServer = plugin.getConfig().getServer();

        if (!serverName.equals(targetServer)) {
            plugin.getDataStorage().addPlayer(player.getUniqueId(), serverName);
            logger.info("玩家 {} 退出时记录: 来自服务器 {}", player.getUsername(), serverName);
        } else {
            logger.debug("玩家 {} 已在目标服务器 {}, 不记录退出信息", player.getUsername(), targetServer);
        }
    }
}