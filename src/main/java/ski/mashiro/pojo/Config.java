package ski.mashiro.pojo;

import lombok.Data;

/**
 * @author MashiroT
 */
@Data
public class Config {
    private String proxyUrl = "127.0.0.1";
    private Integer port = 7890;
    private boolean chatGptEnable = false;
    private String chatGptToken = "xxxxxx";
    private String chatGptApi = "https://api.openai.com/v1/chat/completions";
}
