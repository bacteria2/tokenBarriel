package message;

public interface Message<T> {

    //unit Byte
    int getSize();
    T getMessage();
}
