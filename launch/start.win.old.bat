REM @ECHO OFF

IF EXIST "{JAVAPATH}\java.exe" GOTO startminecraft
ECHO No usable java installation found!
ECHO Check your PATH variable to ensure that it contains your java installation folder.
EXIT

:startminecraft

CD /d {GAMEDIR}
CD ..\
PYTHON .\{MODPACK}.py

SET APPDATA={GAMEDIR}

"{JAVAPATH}\java.exe" -Xms{MEMORY}m -Xmx{MEMORY}m -cp "%APPDATA%\.minecraft\bin\minecraft.jar;%APPDATA%\.minecraft\bin\jinput.jar;%APPDATA%\.minecraft\bin\lwjgl.jar;%APPDATA%\.minecraft\bin\lwjgl_util.jar" -Djava.library.path="%APPDATA%\.minecraft\bin\natives" net.minecraft.client.Minecraft {USERNAME} {SESSIONID}