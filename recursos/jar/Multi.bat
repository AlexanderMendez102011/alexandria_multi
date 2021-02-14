@echo off
:loop
title PRIVATEMU Login
"C:\Program Files\BellSoft\LibericaJRE-15\bin\java.exe" -Dlogback.configurationFile=logback.xml -Xmx1024M -jar multi.jar -o true
goto loop
PAUSE
