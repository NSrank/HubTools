package org.plugin.hubtools_paper.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class TeleportMessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("hubtools:teleport")) return;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(message);
             DataInputStream in = new DataInputStream(bais)) {

            // 读取数据（顺序需与Velocity端一致）
            String worldName = in.readUTF();
            double x = in.readDouble();
            double y = in.readDouble();
            double z = in.readDouble();

            // 验证世界存在性
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                player.sendMessage("§c目标世界不存在！");
                return;
            }

            // 执行传送
            Location target = new Location(world, x, y, z);
            player.teleportAsync(target).thenAccept(success -> {
                if (success) {
                    player.sendMessage("§a已传送到 " + worldName + " 的坐标 " + x + "," + y + "," + z);
                } else {
                    player.sendMessage("§c传送失败，请联系管理员");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("§c传送数据解析失败");
        }
    }
}
