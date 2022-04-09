package src.validator;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageBuilder {

    private List<String> messages = new ArrayList<>();

    public MessageBuilder append(String message) {
        messages.add(message);
        return this;
    }

    public String build() {
        return String.join("\n", messages);
    }

    public int length() {
        return messages.size();
    }

}

