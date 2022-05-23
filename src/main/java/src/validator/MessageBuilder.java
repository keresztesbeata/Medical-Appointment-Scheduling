package src.validator;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a message composed of multiple sentences, allowing to extend it gradually instead of having to provide all the arguments at the beginning.
 * The final message is constructed and retrieved when the build method is called.
 * Implements the Builder design pattern.
 */
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

