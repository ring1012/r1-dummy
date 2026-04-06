@echo off
setlocal ENABLEDELAYEDEXPANSION
set title=取之ai，用之小讯
title %title%
cd /d %~dp0

:connect
color 03
if not exist adb.exe goto adbfile_not
set /p ip=请输入音箱ip或设备名称（有线连接请输入usb，输入a可选择当前已连接设备）:
if "%ip%" == "" color 04&echo 输入不能为空！&choice /t 1 /d y /n > nul&goto connect
if "%ip%" == "cmd" echo 即将进入cmd！&cmd.exe&echo 已退出cmd，请按任意键返回！&pause > nul&goto connect
if "%ip%" == "a" set type=&goto connect_usb_success
if /i "%ip%" == "usb" goto connect_usb
goto connect_ip

:connect_ip_query
color 06
echo 正在查询设备连接状态...
adb devices > nul
choice /t 1 /d y /n > nul
(for /f "tokens=1 delims=" %%i in ('adb -s %ip% get-state') do (
    if "%%i" == "device" set type_id=true&set type=-s %ip%&goto test_connect
))
echo 连接失败，1秒后重新开始连接！
choice /t 1 /d y /n > nul
adb kill-server
goto connect_ip

:connect_usb
color 06
set type=-d
echo 开始连接USB设备...
adb %type% usb
choice /t 2 /d y /n > nul
adb devices > nul
choice /t 1 /d y /n > nul
goto connect_usb_success

:connect_ip
color 06
echo 开始连接设备...
choice /t 1 /d y /n > nul
adb connect %ip%
goto connect_ip_query

:connect_usb_success
color 06
echo 正在查询已连接设备列表...
call :get_devices
if %DeviceNum% lss 1 echo 当前未查询到任何设备！&pause&goto run_error
if %DeviceNum% gtr 1 echo 发现有多个设备，请选择一个设备进行连接！&goto select_device
echo 发现设备：%Device1%，正在自动连接...
set ip=%Device1%
set type=-s %ip%
goto test_connect

:select_device
color 06
set DeviceIndex=0
set /p DeviceIndex=输入序号：
if %DeviceIndex% lss 1 color 04&echo 序号错误，请重新选择！&choice /t 1 /d y /n > nul&goto select_device
if %DeviceIndex% gtr %DeviceNum% color 04&echo 序号错误，请重新选择！&choice /t 1 /d y /n > nul&goto select_device
set device=!Device%DeviceIndex%!
if "%device%" equ "" color 04&echo 序号错误，请重新选择！&choice /t 1 /d y /n > nul&goto select_device
echo 选择设备：%device%
set ip=%device%
set type=-s %ip%
goto test_connect

:get_devices
set DeviceNum=0
set DeviceList=none
echo -----已连接设备列表-----
(for /f "skip=1 tokens=1,2 delims=	" %%i in ('adb devices') do (
    set /a DeviceNum+=1
    set Device=%%i
    set DeviceStatus=%%j
    call :set_device
))
echo ------------------------
goto :eof

:set_device
if "%DeviceList%" neq "none" set DeviceList=%DeviceList%-/-\-%device%
if "%DeviceList%" equ "none" set DeviceList=%device%
set Device%DeviceNum%=%Device%
echo %DeviceNum%. %Device% %DeviceStatus%
goto :eof

:run_error
color 04
echo 连接失败，请按任意键重新连接！
pause > nul
goto connect

:test_connect
color 06
echo 正在测试连接，请稍候...
adb %type% shell ls > nul
if %errorlevel%==1 goto run_error
color 03
echo 连接成功，正在初始化...

(for /f %%i in ('adb %type% shell settings get secure install_non_market_apps') do (
    set install_non_market_apps=%%i
))
if "%install_non_market_apps%" neq "1" echo 执行允许安装未知来源应用...&adb %type% shell settings put secure install_non_market_apps 1 > nul

call :get_device_info

echo 设备名称：%hostname%
echo 固件版本：%ver%
echo 设备IP：%ipaddress%
echo 设备SN：%serialno%

goto install_all

:get_device_info
(for /f %%i in ('adb %type% shell getprop ro.serialno') do set serialno=%%i)
(for /f %%i in ('adb %type% shell getprop ro.build.version.incremental') do set ver=%%i)
(for /f %%i in ('adb %type% shell getprop dhcp.wlan0.ipaddress') do set ipaddress=%%i)

(for /f "tokens=*" %%i in ('adb %type% shell getprop ro.build.host') do set build_host=%%i)
(for /f "tokens=*" %%i in ('adb %type% shell getprop ro.product.model') do set build_model=%%i)
(for /f "tokens=*" %%i in ('adb %type% shell getprop net.hostname') do set hostname=%%i)
goto :eof

:install_all
color 03
echo ----------------------------
echo 开始安装 apps 目录下所有 APK 文件...
set CurrentPath=%~dp0apps\
if not exist "%CurrentPath%" md "%CurrentPath%"

set ListNum=0
for /f "delims=" %%f in ('dir "%CurrentPath%*.apk" /b') do (
    set /a ListNum+=1
    set File!ListNum!=%%f
)

if %ListNum% lss 1 (
    echo apps目录下没有APK文件，请先放入APK！
    pause > nul
    exit
)

:adb %type% shell /system/bin/pm uninstall com.phicomm.speaker.device > nul
:adb %type% shell /system/bin/pm uninstall com.phicomm.speaker.player > nul


for /l %%i in (1,1,%ListNum%) do (
    set apk=!File%%i!
    echo ----------------------------
    echo 安装 !apk! ...

    if exist tmp.apk del tmp.apk > nul
    copy "%CurrentPath%!apk!" tmp.apk > nul


    adb %type% push tmp.apk /data/local/tmp/
    if %errorlevel%==1 (
        echo 上传 !apk! 失败！
        del tmp.apk
        goto install_fail
    )
    del tmp.apk

    adb %type% shell /system/bin/pm install -r /data/local/tmp/tmp.apk > tmp
    type tmp | findstr /i "\<Success\>" > nul
    if %errorlevel%==1 (
        echo 安装 !apk! 失败！
        type tmp
        del tmp
        adb %type% shell rm /data/local/tmp/tmp.apk
        goto install_fail
    )
    del tmp
    adb %type% shell rm /data/local/tmp/tmp.apk
    echo 安装 !apk! 成功！
)

echo ----------------------------
echo 所有APK安装完毕，正在重启设备...
adb %type% reboot
echo 完成！
pause > nul
exit

:install_fail
echo 安装过程中出现错误，请检查设备或APK文件！
pause > nul
exit

:adbfile_not
color 04
echo adb文件不存在，请确认工具已完整解压！
pause > nul
exit