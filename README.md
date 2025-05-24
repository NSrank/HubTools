# HubTools - Velocity 跨服传送管理插件

![Velocity](https://img.shields.io/badge/Velocity-3.4.x-blue ) ![Java](https://img.shields.io/badge/Java-17-green ) ![License](https://img.shields.io/badge/License-GPLv3-green.svg )

**HubTools** 是一个专为 Minecraft Velocity 服务端设计的跨服传送管理插件。当玩家从非指定服务器退出时，下次登录时将被自动传送至预设的目标服务器坐标。

> **注意**：本插件由 AI 开发，旨在简化跨服传送流程并优化玩家体验。

---

## 功能特性

- **智能传送机制**：
    - 记录玩家退出时的服务器位置
    - 若退出时不在目标服务器，下次登录时强制传送至预设坐标
- **多条件检测**：
    - 检测玩家退出时的服务器名称与配置文件匹配性
    - 支持动态坐标（X/Y/Z）和目标世界名称配置
- **自动数据清理**：
    - 传送完成后自动删除玩家记录，节省存储空间
- **实时同步**：
    - 通过插件消息通道与下游服务器实时通信
- **配置灵活性**：
    - 支持自定义目标服务器、世界名称和传送坐标
    - 兼容 Velocity 3.4.0+ 和 Paper 1.16.5+ 服务端

---

## 安装步骤

### 1. 下载插件
从 [GitHub](https://github.com/NSrank/HubTools ) 或其他分发渠道下载最新版本的 `HubTools.jar`。

### 2. 安装Velocity端插件
将 `HubTools.jar` 文件放入 Velocity 服务端的 `plugins/` 目录中。

### 3. 安装Paper端插件（必需）
将配套的 `HubTools-Paper.jar` 放入所有下游服务器的 `plugins/` 目录（用于接收传送坐标）。
**注意**：Paper端插件是独立项目，位于 `HubTools-Paper/` 目录中，需要单独构建。

### 4. 启动服务器
首次启动 Velocity 服务端会自动生成配置文件 `plugins/HubTools/config.yml`。

---

## 配置文件（`config.yml`）
```yaml
server: lobby      # 目标下游服务器名称
world: world       # 目标世界名称
x: 0.5            # 传送坐标（支持小数）
y: 64.0
z: 0.5
```

## 参数说明
- `server`：Velocity注册的下游服务器名称，默认为 `"lobby"`。
- `world`：目标世界名称，默认为 `"world"`。
- `x`：传送坐标的 X 轴值，默认为 `0.5`。
- `y`：传送坐标的 Y 轴值，默认为 `64.0`。
- `z`：传送坐标的 Z 轴值，默认为 `0.5`。

---

## 故障排除

### 常见问题

#### 1. Velocity与Paper端无法通信
**症状**：玩家传送指令发送失败，Paper端收不到消息
**解决方案**：
- 确保使用了修复后的版本（v1.2.0+）
- 检查Velocity端日志是否显示"成功发送传送指令到Paper服务器"
- 确认Paper端配置中的`server-name`与Velocity配置一致
- 验证插件消息通道`hubtools:teleport`已正确注册

#### 2. 传送失败
**症状**：收到传送指令但传送不成功
**解决方案**：
- 检查目标世界是否存在
- 确认传送坐标是否有效（不在虚空中）
- 查看Paper端控制台的详细错误信息

#### 3. 玩家数据未清理
**症状**：玩家重复传送或数据残留
**解决方案**：
- 确保传送成功后会自动清理玩家记录
- 手动删除`plugins/HubTools/data.yml`中的残留数据

### 技术细节

#### 修复的关键问题
1. **消息发送方向错误**：原版本使用`player.sendPluginMessage()`发送给客户端，现已修复为使用`ServerConnection.sendPluginMessage()`发送给Paper服务器
2. **项目结构混乱**：分离了Velocity端和Paper端代码，避免构建冲突
3. **错误处理改进**：增加了详细的日志记录和异常处理

---

### 技术支持与反馈
如果您在使用插件过程中遇到任何问题，或希望提出改进建议，请通过以下方式联系我：

- **GitHub Issues** : [提交问题](https://github.com/NSrank/HubTools/issues)

---

### 版权声明
- 开发声明 ：本插件由 AI 开发，旨在为 Minecraft Velocity 社区提供高效的封禁管理工具。
- 许可证 ：本插件遵循 GNU General Public License v3.0 许可证，您可以自由使用、修改和分发，但需遵守许可证条款。
- 免责条款 ：开发者不对因使用本插件而导致的任何问题负责。

---

### 特别感谢
感谢以下技术和工具对本插件的支持：

- [Velocity API](https://papermc.io/software/velocity)
- [Configurate](https://github.com/SpongePowered/configurate?spm=a2ty_o01.29997173.0.0.26755171KBaIXA)
- [Adventure API](https://github.com/KyoriPowered/adventure?spm=a2ty_o01.29997173.0.0.7c5733f51H3mj8)

---

# HubTools - Cross-Server Teleport Management Plugin for Velocity

![Velocity](https://img.shields.io/badge/Velocity-3.4.x-blue) ![Java](https://img.shields.io/badge/Java-17-green) ![License](https://img.shields.io/badge/License-GPLv3-green.svg)

**HubTools** is a cross-server teleport management plugin designed for Minecraft Velocity proxies. When players log out from a non-designated server, they will be automatically teleported to predefined coordinates on a target server upon their next login.

> **Note**: This plugin is AI-developed to streamline cross-server teleportation and enhance player experience.

---

## Features

- **Smart Teleportation**:
  - Records the server where a player logged out
  - Forces teleportation to preset coordinates if the logout occurred outside the target server
- **Multi-Condition Checks**:
  - Validates server name against configuration file
  - Supports dynamic coordinates (X/Y/Z) and customizable target world
- **Automatic Data Cleanup**:
  - Automatically removes player records after teleportation to save storage
- **Real-Time Sync**:
  - Uses plugin messaging channels for real-time communication with downstream servers
- **Configurable Flexibility**:
  - Customizable target server, world name, and coordinates
  - Compatible with Velocity 3.4.0+ and Paper 1.16.5+

---

## Installation

### 1. Download
Get the latest `HubTools.jar` from [GitHub](https://github.com/NSrank/HubTools) or other distribution channels.

### 2. Install on Velocity
Place `HubTools.jar` in the `plugins/` directory of your Velocity proxy.

### 3. Install on Paper (Optional)
Place `HubTools-Paper.jar` in the `plugins/` directory of **all downstream servers** (for receiving teleport coordinates).

### 4. Start Servers
The configuration file `plugins/HubTools/config.yml` will be auto-generated on first launch.

---

## Configuration (`config.yml`)
```yaml
server: lobby      # Target downstream server name
world: world       # Target world name
x: 0.5            # Teleport coordinates (supports decimals)
y: 64.0
z: 0.5
```
## Parameters
- `server`: The name of the downstream server registered in Velocity, default is `"lobby"`.
- `world`: The target world name, default is `"world"`.
- `x`: The X coordinate for teleportation, default is `0.5`.
- `y`: The Y coordinate for teleportation, default is `64.0`.
- `z`: The Z coordinate for teleportation, default is `0.5`.

---

### Support & Feedback
For issues or suggestions, contact us via:
- **GitHub Issues**: [Submit an issue](https://github.com/NSrank/HubTools/issues)

---

### License & Copyright
- Development Statement: This plugin is AI-developed for the Minecraft Velocity community to provide efficient cross-server teleportation management tools.
- License: This plugin is licensed under the GNU General Public License v3.0. You are free to use, modify, and distribute it, subject to the terms of the license.
- Disclaimer: The developer is not responsible for any issues caused by using this plugin.

---
## Acknowledgements
Special thanks to:
- [Velocity API](https://papermc.io/software/velocity)
- [Configurate](https://github.com/SpongePowered/configurate?spm=a2ty_o01.29997173.0.0.26755171KBaIXA)
- [Adventure API](https://github.com/KyoriPowered/adventure?spm=a2ty_o01.29997173.0.0.7c5733f51H3mj8)

---