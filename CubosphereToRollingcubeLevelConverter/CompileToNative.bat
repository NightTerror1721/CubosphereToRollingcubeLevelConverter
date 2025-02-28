@echo off

D:\Java\graalvm-jdk-22.0.1+8.1\bin\native-image -H:NativeLinkerOption=/SUBSYSTEM:WINDOWS -H:NativeLinkerOption=/ENTRY:mainCRTStartup -jar target\CubosphereToRollingcubeLevelConverter-0.3-Beta.jar
"C:\Program Files\Microsoft Visual Studio\2022\Preview\VC\Tools\MSVC\14.40.33807\bin\Hostx64\x64\editbin.exe" /SUBSYSTEM:WINDOWS "CubosphereToRollingcubeLevelConverter-0.3-Beta.exe"