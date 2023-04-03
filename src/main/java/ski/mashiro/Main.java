package ski.mashiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ski.mashiro.pojo.Config;
import ski.mashiro.chat.Chatgpt;
import ski.mashiro.util.FileUtil;

import java.util.Scanner;

/**
 * @author MashiroT
 */
public class Main {
    public static final Config CONFIG = FileUtil.loadConfig();
    public static final Scanner SCANNER = new Scanner(System.in);
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (CONFIG == null) {
            LOGGER.warn("配置文件获取失败");
            return;
        }
        while (true) {
            LOGGER.info("=====请选择平台=====");
            LOGGER.info("1. ChatGpt [" + (CONFIG.isChatGptEnable() ? "enable" : "disable") + "]");
            LOGGER.info("对话中输入 exit 结束对话");
            String cmd = SCANNER.nextLine();
            if ("1".equals(cmd)) {
                if (!CONFIG.isChatGptEnable()) {
                    LOGGER.info("ChatGpt未启用, 请在配置文件中开启");
                } else {
                    LOGGER.info("请输入对话内容：");
                    new Chatgpt().chat();
                }
            } else if ("exit".equals(cmd)) {
                return;
            } else {
                LOGGER.warn("输入有误");
            }
        }
    }
}
