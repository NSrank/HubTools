package org.plugin.hubtools.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.plugin.hubtools.HubTools;

public class PlayerConnectListener {
    private final HubTools plugin;

    public PlayerConnectListener(HubTools plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        if (plugin.getDataStorage().containsPlayer(player.getUniqueId())) {
            RegisteredServer targetServer = plugin.getProxyServer()
                    .getServer(plugin.getConfig().getServer())
                    .orElse(null);
            if (targetServer != null) {
                event.setInitialServer(targetServer);
            }
        }
    }
}
