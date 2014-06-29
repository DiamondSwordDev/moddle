CD /d "{APPDATA}.minecraft\"
SET APPDATA={APPDATA}
JAVA -Xms{MEMORY}m -Xmx{MEMORY}m -cp "%APPDATA%\.minecraft\bin\minecraft.jar;%APPDATA%\.minecraft\bin\jinput.jar;%APPDATA%\.minecraft\bin\lwjgl.jar;%APPDATA%\.minecraft\bin\lwjgl_util.jar" -Djava.library.path="%APPDATA%\.minecraft\bin\natives" net.minecraft.client.Minecraft {USERNAME} {SESSIONID}
PAUSE