package lucky.xjz.rpc.core.protocol;

/**
 * 返回结果提示码
 */
public enum MsgCode {

    SUCCESS(1024),
    CONNECTION_TIME_OUT(1000);
    int code;

    MsgCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
