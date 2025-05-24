@echo off
echo ===================================
echo HubTools 项目构建脚本
echo ===================================

echo.
echo 正在构建 Velocity 端插件...
call mvn clean package -q
if %errorlevel% neq 0 (
    echo Velocity 端构建失败！
    pause
    exit /b 1
)
echo Velocity 端构建成功！

echo.
echo 正在构建 Paper 端插件...
cd HubTools-Paper
call mvn clean package -q
if %errorlevel% neq 0 (
    echo Paper 端构建失败！
    cd ..
    pause
    exit /b 1
)
echo Paper 端构建成功！
cd ..

echo.
echo ===================================
echo 构建完成！
echo ===================================
echo Velocity 插件: target\HubTools-1.2-SNAPSHOT.jar
echo Paper 插件: HubTools-Paper\target\HubTools-Paper-1.1.0.jar
echo.
echo 请将对应的 jar 文件放入相应服务器的 plugins 目录
echo ===================================
pause
