package org.plugin.hubtools.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import org.plugin.hubtools.HubTools;

public class PlayerDisconnectListener {
    private final HubTools plugin;

    public PlayerDisconnectListener(HubTools plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        Player player = (Player) event.getPlayer();
        ServerConnection serverConnection = player.getCurrentServer().orElse(null);
        if (serverConnection == null) return;

        String serverName = serverConnection.getServerInfo().getName();
        if (!serverName.equals(plugin.getConfig().getServer())) {
            plugin.getDataStorage().addPlayer(player.getUniqueId(), serverName);
        }
    }
}