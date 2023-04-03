## 使用
clone 后 `mvn package`

在 target 目录下找到 `MashiroChat-1.0-SNAPSHOT-jar-with-dependencies.jar`

复制到你的目录或在本地打开shell, 确保运行的jre版本在17以上

`java -jar ./MashiroChat-1.0-SNAPSHOT-jar-with-dependencies.jar`

第一次运行后直接退出, 修改 Config.json 中的 `chatGptToken` 为你的token, 并且将 `chatGptEnable` 改为true

之后再次运行即可