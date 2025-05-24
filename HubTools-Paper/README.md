# HubTools-Paper - Paper端传送接收插件

![Paper](https://img.shields.io/badge/Paper-1.20.x-green) ![Java](https://img.shields.io/badge/Java-17-green) ![License](https://img.shields.io/badge/License-GPLv3-green.svg)

**HubTools-Paper** 是HubTools插件的Paper端组件，用于接收来自Velocity代理服务器的传送指令并执行玩家传送。

> **注意**：此插件必须与HubTools Velocity插件配合使用。

---

## 功能特性

- **接收传送指令**：监听来自Velocity的插件消息通道
- **安全传送执行**：验证传送数据的完整性和安全性
- **世界验证**：确保目标世界存在后再执行传送
- **异步传送**：使用异步API避免阻塞主线程
- **详细日志**：提供完整的传送过程日志记录

---

## 安装步骤

### 1. 前置要求
- Paper 1.16.5+ 服务器
- 已安装并配置HubTools Velocity插件

### 2. 安装插件
将 `HubTools-Paper.jar` 文件放入Paper服务器的 `plugins/` 目录中。

### 3. 配置文件
首次启动会生成 `plugins/HubTools-Paper/config.yml`：

```yaml
server-name: lobby  # 当前服务器名称，必须与Velocity配置中的服务器名称一致
```

### 4. 重启服务器
重启Paper服务器以加载插件。

---

## 配置说明

### config.yml
- `server-name`: 当前Paper服务器在Velocity中注册的名称，必须与Velocity配置一致

---

## 工作原理

1. **消息接收**：监听 `hubtools:teleport` 插件消息通道
2. **数据解析**：解析来自Velocity的传送数据（服务器名、世界名、坐标）
3. **服务器验证**：检查传送目标是否为当前服务器
4. **世界验证**：确认目标世界存在
5. **执行传送**：使用异步API将玩家传送到指定坐标

---

## 消息格式

插件接收的消息数据格式：
```
String targetServer  // 目标服务器名称
String worldName     // 目标世界名称
double x            // X坐标
double y            // Y坐标  
double z            // Z坐标
```

---

## 故障排除

### 常见问题

1. **收不到传送指令**
   - 检查服务器名称配置是否与Velocity一致
   - 确认插件消息通道已正确注册
   - 查看Velocity端日志是否有发送成功的记录

2. **传送失败**
   - 检查目标世界是否存在
   - 确认坐标是否有效
   - 查看控制台错误日志

3. **插件无法加载**
   - 确认Paper版本兼容性
   - 检查Java版本是否为17+

---

## 开发信息

- **主类**: `org.plugin.hubtools_paper.HubTools_Paper`
- **消息通道**: `hubtools:teleport`
- **API版本**: 1.20
- **Java版本**: 17+

---

## 许可证

本插件基于 GNU General Public License v3.0 开源协议发布。

---

## 技术支持

如遇问题请提交 [GitHub Issue](https://github.com/NSrank/HubTools/issues)
