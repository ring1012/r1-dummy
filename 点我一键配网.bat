@echo off
setlocal enabledelayedexpansion
cd /d %~dp0
title 点我一键配网

echo ============================================
echo           斐讯 R1 音箱一键配网工具
echo ============================================
echo.
echo 请长按 R1 音箱顶部按键约 5 秒，
echo 直至底部环形灯变为白光闪烁状态，
echo 表示音箱已进入配网模式。
echo.
pause

:input_ssid
set ssid=
set /p ssid=请输入需要连接的WiFi SSID：
if "!ssid!"=="" goto input_ssid

:input_password
set password=
set /p password=请输入WiFi密码：
if "!password!"=="" goto input_password

:input_secure
set secure=WPA
set /p secure=请输入加密类型（默认WPA）：
if "!secure!"=="" set secure=WPA

echo.
echo WiFi配置信息：
echo   SSID：!ssid!
echo   密码：!password!
echo   类型：!secure!
echo.

:confirm_phcomm
set confirm=
set /p confirm=是否已连接上 @PHICOMM_ 开头的WiFi？（y/n）：
if /i "!confirm!"=="y" goto do_config
if /i "!confirm!"=="n" (
    echo 请先连接 @PHICOMM_ 开头的WiFi后再试。
    pause
    exit
)
goto confirm_phcomm

:do_config
echo.
echo 正在发送配网请求...

set "PW_SSID=!ssid!"
set "PW_PASS=!password!"
set "PW_SECURE=!secure!"

powershell -NoProfile -ExecutionPolicy Bypass -Command "& { $body = @{ssid=$env:PW_SSID; mac='; level='; secure=$env:PW_SECURE; password=$env:PW_PASS} | ConvertTo-Json -Compress; Write-Host ('JSON: ' + $body); try { $r = Invoke-WebRequest -Uri 'http://192.168.43.1:8989/api/configwifi' -Method POST -Body $body -ContentType 'application/json' -UseBasicParsing; Write-Host ('状态码: ' + $r.StatusCode); Write-Host ('响应: ' + $r.Content) } catch { Write-Host ('请求失败: ' + $_.Exception.Message) } }"

echo.
echo 配网请求已发送，请观察音箱状态。
pause