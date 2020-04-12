package lucky.xjz.rpc.tcpclient;

import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import static lucky.xjz.rpc.core.protocol.ProtocolConstant.RPC_VERSION;

/**
 * rpc请求
 */
public class Request {

    private final String rpcVersion = RPC_VERSION.toString();//rpc版本

    private String method;//远程调用的方法名

    private List<byte[]> params;//参数序列化后的字节数组列表

    private String id;//消息唯一id

    private SocketAddress socketAddress;//远程地址

    private Request() {
    }

    public Request(SocketAddress socketAddress, String method, List<byte[]> params){
        this.socketAddress = socketAddress;
        this.method = method;
        //ID 由远程地址和UUID组成
        this.id = socketAddress+UUID.randomUUID().toString();
        this.params = params;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<byte[]> getParams() {
        return params;
    }

    public void setParams(List<byte[]> params) {
        this.params = params;
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

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public String toString() {
        return "Request{" +
                "rpcVersion='" + rpcVersion + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                ", id='" + id + '\'' +
                ", socketAddress=" + socketAddress +
                '}';
    }
}
