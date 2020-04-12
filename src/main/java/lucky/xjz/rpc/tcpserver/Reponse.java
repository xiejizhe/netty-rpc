package lucky.xjz.rpc.tcpserver;


import java.net.SocketAddress;

/**
 * rpc返回结果
 */
public class Reponse {

    private String id; //请求消息的唯一id

    private  String rpcVersion; //rpc  版本

    private Object data; //请求返回的数据

    private int code;//返回码


    //private SocketAddress socketAddress;//数据返回的地址

    public Reponse(String id, String rpcVersion, Object data, int code) {
        this.id = id;
        this.rpcVersion = rpcVersion;
        this.data = data;
        this.code = code;
    }

    public Reponse(String id, String rpcVersion, int code) {
        this.id = id;
        this.rpcVersion = rpcVersion;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRpcVersion() {
        return rpcVersion;
    }

    public void setRpcVersion(String rpcVersion) {
        this.rpcVersion = rpcVersion;
    }

    public Object getData() {
        return data;
    }

    public void setDaat(Object daat) {
        this.data = daat;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


//    public SocketAddress getSocketAddress() {
//        return socketAddress;
//    }

//    public void setSocketAddress(SocketAddress socketAddress) {
//        this.socketAddress = socketAddress;
//    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id='" + id + '\'' +
                ", rpcVersion='" + rpcVersion + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
