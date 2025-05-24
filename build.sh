#!/bin/bash

echo "==================================="
echo "HubTools 项目构建脚本"
echo "==================================="

echo
echo "正在构建 Velocity 端插件..."
mvn clean package -q
if [ $? -ne 0 ]; then
    echo "Velocity 端构建失败！"
    exit 1
fi
echo "Velocity 端构建成功！"

echo
echo "正在构建 Paper 端插件..."
cd HubTools-Paper
mvn clean package -q
if [ $? -ne 0 ]; then
    echo "Paper 端构建失败！"
    cd ..
    exit 1
fi
echo "Paper 端构建成功！"
cd ..

echo
echo "==================================="
echo "构建完成！"
echo "==================================="
echo "Velocity 插件: target/HubTools-1.2-SNAPSHOT.jar"
echo "Paper 插件: HubTools-Paper/target/HubTools-Paper-1.1.0.jar"
echo
echo "请将对应的 jar 文件放入相应服务器的 plugins 目录"
echo "==================================="
