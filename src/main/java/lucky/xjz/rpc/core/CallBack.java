package lucky.xjz.rpc.core;
@FunctionalInterface
public interface CallBack<T> {

    void execute(T obj);
}
