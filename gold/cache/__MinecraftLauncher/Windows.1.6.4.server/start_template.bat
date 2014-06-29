GOTO {PackType}

:Vanilla
ECHO Nothing
EXIT

:Forge
cd /d {AppData}
SET APPDATA={AppData}
"{JavaPath}\java.exe" -Xmx{Memory}M -jar %APPDATA%\minecraftforge-universal-{MinecraftVersion}-{ForgeVersion}.jar
pause
EXIT