# 開発用サーバーを起動する
server:
	cd spigot && MCK_ENV=local java -Xms1G -Xmx1G -XX:+UseConcMarkSweepGC -jar spigot-1.12.2.jar

# コンパイルしてjarを配置
compile:
	mvn install && cp target/Life-1.0-SNAPSHOT.jar spigot/plugins/Life-1.0-SNAPSHOT.jar