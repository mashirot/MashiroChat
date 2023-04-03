package ski.mashiro.chat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import ski.mashiro.Main;
import ski.mashiro.pojo.Message;
import ski.mashiro.util.FileUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author MashiroT
 */
public class Chatgpt {

    /**
     * 负责对话交互部分
     */
    public void chat() {
        try {
            List<Map<String, String>> msgList = new ArrayList<>();
            while (true) {
                Main.LOGGER.info("\n你:");
                String input = Main.SCANNER.nextLine();
                if ("exit".equals(input)) {
                    return;
                }
                Main.LOGGER.info("请输入本次回复的等待时间(s):");
                var delay = Integer.parseInt(Main.SCANNER.nextLine());
                msgList.add(Map.of("role", "user", "content", input));
                String resp = sendReq(msgList, delay);
                if (resp == null) {
                    Main.LOGGER.warn("请求错误");
                    return;
                }
                String chatgptReply = resp.substring(resp.indexOf("\"message\":") + 10, resp.indexOf(",\"finish_reason"));
                Message message = FileUtil.OBJECT_MAPPER.readValue(chatgptReply, Message.class);
                msgList.add(Map.of("role", message.getRole(), "content", message.getContent()));
                Main.LOGGER.info("Chatgpt:");
                Main.LOGGER.info(message.getContent());
            }
        } catch (NumberFormatException e) {
            Main.LOGGER.warn("输入格式有误, 请输入数字");
        } catch (InterruptedIOException e) {
            Main.LOGGER.warn("等待回复超时, 请确认代理状态或增加等待时间");
        } catch (IOException e) {
            Main.LOGGER.warn("回复数据格式化失败");
        }
    }

    /**
     * 发送消息请求
     * @param msgList 消息列表，包含历史对话
     * @param delay 等待回复延迟
     * @return ChatGPT回复消息
     * @throws IOException Json格式化异常, 返回包execute()异常
     */
    private static String sendReq(List<Map<String, String>> msgList, int delay) throws IOException {
        assert Main.CONFIG != null;
        var httpClient = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Main.CONFIG.getProxyUrl(), Main.CONFIG.getPort())))
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(delay, TimeUnit.SECONDS)
                .readTimeout(delay, TimeUnit.SECONDS)
                .build();
        var reqBody = Map.of("model", "gpt-3.5-turbo"
                , "messages", msgList
                , "temperature", 0.7);
        var req = new Request.Builder()
                .url(Main.CONFIG.getChatGptApi())
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + Main.CONFIG.getChatGptToken())
                .post(RequestBody.create(FileUtil.OBJECT_MAPPER.writeValueAsString(reqBody).getBytes(StandardCharsets.UTF_8)))
                .build();
        try (var resp = httpClient.newCall(req).execute()) {
            if (resp.isSuccessful()) {
                assert resp.body() != null;
                return resp.body().string();
            } else {
                return null;
            }
        }
    }
}
