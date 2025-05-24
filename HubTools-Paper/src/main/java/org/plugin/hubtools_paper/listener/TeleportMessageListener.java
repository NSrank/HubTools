package org.plugin.hubtools_paper.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * 传送消息监听器
 * 处理来自Velocity代理服务器的传送指令
 * 
 * @author NSrank & Qwen2.5-Max & Kimi
 * @version 1.1.0
 */
public class TeleportMessageListener implements PluginMessageListener {
    private final String serverName;
    private final Logger logger;

    /**
     * 构造函数
     * @param serverName 当前服务器名称
     * @param logger 日志记录器
     */
    public TeleportMessageListener(String serverName, Logger logger) {
        this.serverName = serverName;
        this.logger = logger;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // 验证消息通道
        if (!channel.equals("hubtools:teleport")) {
            return;
        }

        logger.info("收到 Velocity 传送消息: 玩家=" + player.getName() + ", 数据长度=" + message.length);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(message);
             DataInputStream in = new DataInputStream(bais)) {

            // 解析传送数据
            String targetServer = in.readUTF(); // 目标服务器名称
            String worldName = in.readUTF();    // 世界名称
            double x = in.readDouble();         // X坐标
            double y = in.readDouble();         // Y坐标
            double z = in.readDouble();         // Z坐标

            // 记录详细日志
            logger.info("解析传送数据: 目标服务器=" + targetServer + 
                       ", 世界=" + worldName + 
                       ", 坐标=(" + x + ", " + y + ", " + z + ")");

            // 验证服务器名称
            if (!targetServer.equals(serverName)) {
                logger.info("忽略非本服务器的传送消息: 目标=" + targetServer + ", 当前=" + serverName);
                return;
            }

            // 验证世界存在性
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                logger.warning("目标世界不存在: " + worldName);
                player.sendMessage("§c传送失败：目标世界不存在！");
                return;
            }
            
            logger.info("目标世界验证成功: " + worldName);

            // 执行异步传送
            Location targetLocation = new Location(world, x, y, z);
            player.teleportAsync(targetLocation).thenAccept(success -> {
                if (success) {
                    logger.info("玩家 " + player.getName() + " 传送成功到 " + worldName + 
                               " (" + x + ", " + y + ", " + z + ")");
                    player.sendMessage("§a传送成功！已到达 " + worldName + " 的指定位置");
                } else {
                    logger.warning("玩家 " + player.getName() + " 传送失败");
                    player.sendMessage("§c传送失败，请联系管理员");
                }
            }).exceptionally(throwable -> {
                logger.severe("传送过程中发生异常: " + throwable.getMessage());
                player.sendMessage("§c传送过程中发生错误，请联系管理员");
                throwable.printStackTrace();
                return null;
            });

        } catch (IOException e) {
            logger.severe("传送数据解析失败: " + e.getMessage());
            player.sendMessage("§c传送数据解析失败，请联系管理员");
            e.printStackTrace();
        } catch (Exception e) {
            logger.severe("处理传送消息时发生未知错误: " + e.getMessage());
            player.sendMessage("§c传送处理失败，请联系管理员");
            e.printStackTrace();
        }
    }
}
