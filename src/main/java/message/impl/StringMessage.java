package message.impl;

import message.Message;

public class StringMessage implements Message<String> {

    private String message;

    public StringMessage(String message) {
        this.message = message;
    }

    @Override
    public int getSize() {
        return message.getBytes().length;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
