@echo off
setlocal enabledelayedexpansion

:menu
cls
echo 1. 恢复 ASR IP
echo 2. 修改/添加 ASR IP
echo 0. 退出脚本
echo.

set /p choice="请选择 (1/2/0): "

if "%choice%"=="0" exit /b
if "%choice%"=="" goto menu

:: 清理 adb 连接和进程
echo 正在清理 adb 进程...
adb disconnect >nul
taskkill /f /t /im adb.exe >nul
timeout /t 2 >nul

:: 输入音箱 IP 地址
set /p android_ip="请输入音箱IP: "

:: 连接音箱
echo 正在连接 %android_ip%...
adb connect %android_ip%
echo 连接完成！

set endpoint=http://%android_ip%:18888/api/message

if "%choice%"=="1" goto option1
if "%choice%"=="2" goto option2
goto invalid_option

:option1
echo.
echo 正在恢复 ASR IP...
curl -s -X POST -H "Content-Type: application/json" -d "{\"what\":65536,\"arg1\":1,\"arg2\":0,\"obj\":\"asrv3.hivoice.cn\"}" %endpoint%

echo.
echo 正在重启音箱...
adb -s %android_ip% reboot
echo 操作完成！
goto end_options

:option2
echo.
set /p asrip="请输入ASR IP (格式: 192.168.2.1:18888): "
curl -s -X POST -H "Content-Type: application/json" -d "{\"what\":65536,\"arg1\":1,\"arg2\":0,\"obj\":\"%asrip%\"}" %endpoint%

echo.
echo 正在重启音箱...
adb -s %android_ip% reboot
echo 操作完成！
goto end_options

:invalid_option
echo 无效选项！
goto end_options

:end_options
echo.
pause
goto menu