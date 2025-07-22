package study.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessage {
    private String sender;
    private String content;
    private String timestamp;

    public ChatMessage(String content, String sender, String timestamp) {
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    }
}
