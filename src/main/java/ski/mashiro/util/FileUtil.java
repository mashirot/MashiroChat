package ski.mashiro.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import ski.mashiro.Main;
import ski.mashiro.pojo.Config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author MashiroT
 */
public class FileUtil {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final File CONFIG_FILE = new File("Config.json");

    public static Config loadConfig() {
        try {
            initConfig();
            return OBJECT_MAPPER.readValue(CONFIG_FILE, Config.class);
        } catch (IOException e) {
            Main.LOGGER.warn("配置文件初始化失败");
            return null;
        }
    }

    private static void initConfig() throws IOException {
        if (!CONFIG_FILE.exists()) {
            if (CONFIG_FILE.createNewFile()) {
                FileUtils.writeStringToFile(CONFIG_FILE, OBJECT_MAPPER.writeValueAsString(new Config()), StandardCharsets.UTF_8);
            }
        }
    }
}
