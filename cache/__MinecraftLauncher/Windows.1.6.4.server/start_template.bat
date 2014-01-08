GOTO {PackType}

:Vanilla
ECHO Nothing
EXIT

:Forge
cd /d {AppData}
SET APPDATA={AppData}
java -Xmx{Memory}M -jar %APPDATA%\minecraftforge-universal-{MinecraftVersion}-{ForgeVersion}.jar
pause
EXIT