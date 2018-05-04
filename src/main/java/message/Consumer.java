package message;

public interface Consumer <T>{
    T consume() throws Exception;
}
