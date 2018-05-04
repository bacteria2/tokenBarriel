package message;

public interface Producer <T> {
  void  produce(T message) throws Exception;
}
