@echo off
setlocal ENABLEDELAYEDEXPANSION
set apk=new_uni.apk
set title=取之ai，用之小讯
title %title%
cd /d %~dp0
:connect
color 03
if not exist adb.exe goto adbfile_not
set /p ip=请输入音箱ip或设备名称（有线连接请输入usb，输入a可选择当前已连接设备）:
if "%ip%" == "" color 04&echo 输入不能为空！&choice /t 1 /d y /n > nul&goto connect
if "%ip%" == "cmd" echo 即将将进入cmd！&cmd.exe&echo 已退出cmd，请按任意键返回！&pause > nul&goto connect
if "%ip%" == "a" set type=&goto connect_usb_success
if "%ip%" == "usb" goto connect_usb
if "%ip%" == "USB" goto connect_usb
goto connect_ip

::set query=true
::goto connect_ip_query

:connect_ip_query
color 06
echo 正在查询设备连接状态。。。
adb devices > nul
choice /t 1 /d y /n > nul
(for /f "tokens=1 delims=" %%i in ('adb -s %ip% get-state') do (
if "%%i" == "device" set type_id=true&set type=-s %ip%&goto test_connect
))
if "%query%" neq "true" echo 连接失败，1秒后重新开始连接！&choice /t 1 /d y /n > nul&adb kill-server
set query=
goto connect_ip


:connect_usb
color 06
set type=-d
echo 开始连接usb设备。。。
adb %type% usb
choice /t 2 /d y /n > nul
adb devices > nul
choice /t 1 /d y /n > nul
goto connect_usb_success

:connect_ip
color 06
::echo 检查设备列表。。。
::set HideDeviceList=true
::call :get_devices
::if %DeviceNum% gtr 0 echo 发现有[%DeviceNum%]个设备，正在重启adb服务。。。&adb kill-server&choice /t 1 /d y /n > nul
echo 开始连接设备。。。
choice /t 1 /d y /n > nul
adb connect %ip%
goto connect_ip_query

:connect_ip_success
goto connect_ip_query


:connect_usb_success
color 06
echo 正在查询已连接设备列表。。。
call :get_devices
if %DeviceNum% lss 1 echo 当前未查询到任何设备！&goto run_error
if %DeviceNum% gtr 1 echo 发现有多个设备，请选择一个设备进行连接！&goto select_device
echo 发现设备：%Device1%，正在自动为您连接。。。
set ip=%Device1%
set type=-s %ip%
goto test_connect
pause > nul
exit

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
goto connect_ip_query


:connect_usb_success_bak
echo 正在查询已连接设备列表。。。
echo -----已连接设备列表-----
adb devices|findstr /i "\<device\>"||goto connect_fail
echo ------------------------
color 06
echo 提示：如以上列表有多个设备，请先断开后其他设备后再使用本工具，否则可能会导致无法区分设备！
goto test_connect
pause > nul
exit

:get_devices
set DeviceNum=0
set DeviceList=none
if "%HideDeviceList%" neq "true" echo -----已连接设备列表-----
(for /f "skip=1 tokens=1,2 delims=	" %%i in ('adb devices') do (
set /a DeviceNum+=1
set Device=%%i
set DeviceStatus=%%j
call :set_device
))
if "%HideDeviceList%" neq "true" echo ------------------------
set HideDeviceList=
goto :eof

:set_device
if "%DeviceList%" neq "none" set DeviceList=%DeviceList%-/-\-%device%
if "%DeviceList%" equ "none" set DeviceList=%device%
set Device%DeviceNum%=%Device%
if "%HideDeviceList%" neq "true" echo %DeviceNum%.%Device% %DeviceStatus%
set Device=
goto :eof

:run_error
color 04
echo 抱歉，发生以上错误，请按任意键尝试重新连接！
pause > nul
goto connect

:connect_fail
color 04
echo 连接设备失败，请按任意键重试！
pause > nul
goto connect

:test_connect
color 06
echo 正在测试连接，请稍候。。。
adb %type% shell ls > nul
if %errorlevel%==1 goto run_error
color 03
echo 连接成功，正在初始化。。。
echo 正在查询设备信息。。。

(for /f %%i in ('adb %type% shell settings get secure install_non_market_apps') do (
set install_non_market_apps=%%i
))

if "%install_non_market_apps%" neq "1" echo 执行允许安装未知来源应用。。。&adb %type% shell settings put secure install_non_market_apps 1 > nul

call :get_device_info

if "%build_host%" == "phicomm" if "%build_model%" == "rk322x-box" set r1=true

color 03
if "%hostname%" neq "" echo 设备名称：%hostname%
if "%ver%" neq "" echo 固件版本：%ver%
if "%ipaddress%" neq "" echo 设备IP：%ipaddress%
if "%serialno%" neq "" echo 设备SN：%serialno%
if %ver% gtr 2999 if %ver% neq 3448 (
color 06
if "%r1%" == "true" goto r1_low_ver
)
if "%apk%" neq "" goto install_apk
goto install


:get_device_info

(for /f %%i in ('adb %type% shell getprop ro.serialno') do (
set serialno=%%i
))

(for /f %%i in ('adb %type% shell getprop ro.build.version.incremental') do (
set ver=%%i
))


(for /f %%i in ('adb %type% shell getprop dhcp.wlan0.ipaddress') do (
set ipaddress=%%i
))

(for /f "tokens=1,2,3,4,5,6 delims= " %%i in ('adb %type% shell getprop ro.build.host') do (
set tmp_text=%%i
if "%%j" neq "" set tmp_text=%tmp_text% %%j
if "%%k" neq "" set tmp_text=%tmp_text% %%k
if "%%l" neq "" set tmp_text=%tmp_text% %%l
if "%%m" neq "" set tmp_text=%tmp_text% %%m
if "%%n" neq "" set tmp_text=%tmp_text% %%n
))
set build_host=%tmp_text%

(for /f  "tokens=1,2,3,4,5,6 delims= " %%i in ('adb %type% shell getprop ro.product.model') do (
set tmp_text=%%i
if "%%j" neq "" set tmp_text=%tmp_text% %%j
if "%%k" neq "" set tmp_text=%tmp_text% %%k
if "%%l" neq "" set tmp_text=%tmp_text% %%l
if "%%m" neq "" set tmp_text=%tmp_text% %%m
if "%%n" neq "" set tmp_text=%tmp_text% %%n
))
set build_model=%tmp_text%

(for /f "tokens=1,2,3,4,5,6 delims= " %%i in ('adb %type% shell getprop net.hostname') do (
set tmp_text=%%i
if "%%j" neq "" set tmp_text=%tmp_text% %%j
if "%%k" neq "" set tmp_text=%tmp_text% %%k
if "%%l" neq "" set tmp_text=%tmp_text% %%l
if "%%m" neq "" set tmp_text=%tmp_text% %%m
if "%%n" neq "" set tmp_text=%tmp_text% %%n
))
set hostname=%tmp_text%

goto :eof


:r1_low_ver
echo 注意：您的R1固件版本非最新版本，安装小讯修改版可能会不兼容，您可使用以下工具进行固件升级：https://xfdown.lanzoui.com/icLUkq76mxe 密码:9d7f 
echo 请按任意键继续！
pause > nul
if "%apk%" neq "" goto install_apk
goto install

:install
color 03
echo ------------------------
if not exist apps  (md apps
echo 检测到apps目录不存在，已自动创建！
)
echo 提示：请将要安装的软件包复制到apps目录下！
echo 提示：按下键盘上的Tab键可自动遍历读取apps目录下的文件！
echo 提示：输入list可列出apps目录下的软件列表！
echo ------------------------
set CurrentPath=%~dp0
set CurrentPath=%CurrentPath%apps\
call :check_files
cd /d %CurrentPath%
set apk=
set /p apk=请输入要安装的软件(输入help查看可用命令):
goto start_install

:install_apk
if not exist apps\%apk% goto install
color 03
set CurrentPath=%~dp0
set CurrentPath=%CurrentPath%apps\
cd /d %CurrentPath%
echo 按下任意键开始安装%apk%！
pause > nul
goto start_install

:check_files
if "%check_files%" == "true" goto :eof
set check_files=true
set ListNum=0
(for /f "tokens=1 delims=	" %%i in ('dir "%CurrentPath%*.apk" /b') do (
set /a ListNum+=1
))
if %ListNum% lss 1 (
color 04
echo apps目录下没有发现软件包，已自动打开，请将要安装的软件复制到此目录下！
explorer %CurrentPath%
echo 请按任意键继续！
pause > nul
color 03
)
goto :eof

:help
echo ---帮助---
echo 直接输入文件名或拖入文件即可安装（文件名内不能有中文或特殊字符）
echo cmd :进入cmd控制台
echo list :列出apps目录下的软件列表
echo shell :进入shell控制台
echo reboot :重启设备
echo scrcpy :投屏
echo exit :退出工具
echo ------------------------
echo 请按任意键返回！
pause > nul
goto install


:start_install
cd /d %~dp0
if "%apk%" == "" color 04&echo 输入不能为空！&choice /t 1 /d y /n > nul&goto install
if "%apk%" == "help" goto help
if "%apk%" == "cmd" echo 即将将进入cmd！&cmd.exe&echo 已退出cmd，请按任意键返回！&pause > nul&goto install
if "%apk%" == "list" goto list
if "%apk%" == "shell" goto shell
if "%apk%" == "reboot" goto reboot
echo %apk%|echo %apk%|findstr /v "\<^*scrcpy.*\>" > nul||goto scrcpy
if "%apk%" == "exit" goto exit
echo %apk%|findstr /i "\<*.apk\>" > nul||set apk=%apk%.apk
echo %apk%|findstr /i "\<*.apk\>" > nul||goto apk_error
echo %apk%|findstr /v \ > nul||goto is_path
echo %apk%|findstr /v \/ > nul||goto is_path
if not exist apps\%apk% goto apk_not_exist

if exist tmp.apk del tmp.apk > nul
echo 正在复制软件为临时文件，请稍候。。。
copy apps\%apk% tmp.apk > nul
goto start_install1

:start_install1
if "%type_id%" == "true" (
echo 提示：IP连接方式上传安装包速度较慢，请耐心等待。。。
)

if "%type%" == "" (
echo 提示：如当前为IP连接方式，上传安装包速度可能较慢，请耐心等待。。。
)

echo 开始上传%apk%。。。
adb %type% shell /system/bin/pm uninstall com.phicomm.speaker.device > nul
adb %type% shell /system/bin/pm uninstall com.phicomm.speaker.player > nul
adb %type% push tmp.apk /data/local/tmp/
if %errorlevel%==1 goto upload_fail
del tmp.apk
echo 上传成功，开始安装。。。
adb %type% shell /system/bin/pm install -r /data/local/tmp/tmp.apk > tmp
type tmp|findstr /i "\<Success\>"||goto install_fail
del tmp
echo 安装成功！
echo 删除已上传的安装包。。。
adb %type% shell rm /data/local/tmp/tmp.apk

echo 正在重启设备...
adb %type% reboot

echo %apk%|findstr /v EchoService > nul||goto start_EchoService
echo %apk%|findstr /v Unisound > nul||goto start_Unisound



echo 请按任意键返回！
pause > nul
goto install


:scrcpy
cd /d %~dp0
if not exist scrcpy\scrcpy.exe echo scrcpy目录下未找到scrcpy.exe！&echo 请按任意键返回！&pause > nul&goto install
echo 投屏开始。。。
scrcpy\%apk% %type% --disable-screensaver
echo 投屏结束！
echo 请按任意键返回！
pause > nul
goto install

:exit
echo 请按任意键退出！
pause > nul
exit

:list
color 03
echo ---软件列表---
set ListNum=0
(for /f "tokens=1 delims=	" %%i in ('dir "%CurrentPath%*.apk" /b') do (
set /a ListNum+=1
set ListFile!ListNum!=%%i
echo !ListNum!.%%i
))

if %ListNum% lss 1 (
color 04
echo 软件列表为空！
echo ------------------------
echo 请按任意键返回！
pause > nul
color 03
goto install
)

echo ------------------------

set /p ListIndex=请输入要安装的软件序号(输入exit退出列表)：
if "%ListIndex%" == "" color 04&echo 输入不能为空！&choice /t 1 /d y /n > nul&goto list
if "%ListIndex%" == "exit" echo 已退出软件列表！&goto install
if %ListIndex% lss 1 color 04&echo 序号错误，请重新选择！&choice /t 1 /d y /n > nul&goto list
if %ListIndex% gtr %ListNum% color 04&echo 序号错误，请重新选择！&choice /t 1 /d y /n > nul&goto list

set file=!ListFile%ListIndex%!
if "%file%" equ "" call  :err 序号错误，请重新选择！
echo 选择软件：%file%
set apk=%file%
echo 请按任意键开始安装！
pause > nul
goto start_install

:shell
echo 下面将进入shell控制台！
if "%r1%" == "true" adb %type% shell sh
if "%r1%" neq "true" adb %type% shell
echo 已退出shell控制台！
echo 请按任意键返回！
pause > nul
goto install

:apk_error
color 04
echo 非apk文件！
choice /t 1 /d y /n > nul
goto install

:is_path
call :get_file !apk!

if "%FilePath%" neq "%CurrentPath%" (
	if exist tmp.apk del tmp.apk > nul
	echo 正在复制软件为临时文件，请稍候。。。
	copy "%FilePath%%FileName%" tmp.apk > nul
	goto start_install1
)

color 03
set apk=%FileName%

goto start_install


:get_file
set FilePath=%~dp1
set FileName=%~nx1
goto :eof

:apk_not_exist
color 04
echo apps目录下未找到%apk%，请先复制软件包到apps目录下，或直接拖入文件！
choice /t 1 /d y /n > nul
goto install

:upload_fail
color 04
del tmp.apk
echo 上传%apk%失败，发生以上错误！
choice /t 1 /d y /n > nul
echo 请按任意键返回！
pause > nul
goto install

:install_fail
color 04
echo 安装失败！
echo 错误代码：
type tmp
del tmp
echo 删除已上传的安装包。。。
adb %type% shell rm /data/local/tmp/tmp.apk
echo 请按任意键返回！
pause > nul
goto install

:reboot
echo 重启设备。。。
adb %type% reboot
echo 请按任意键返回！
pause > nul
goto install

:start_EchoService
echo 启动EchoService服务。。。
adb %type% shell am startservice com.phicomm.speaker.player/.EchoService
echo 请按任意键返回！
pause > nul
goto install

:start_Unisound
echo 启动Unisound。。。
adb %type% shell am start com.phicomm.speaker.device/.ui.MainActivity
echo 请按任意键返回！
pause > nul
goto install

:adbfile_not
color 04
echo adb文件不存在，请确定您已将工具全部解压！
pause > nul
exit
