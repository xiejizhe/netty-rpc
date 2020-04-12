package lucky.xjz.rpc.core.util;
import lucky.xjz.rpc.tcpserver.Reponse;
import lucky.xjz.rpc.tcpclient.Request;
import lucky.xjz.rpc.core.protocol.MsgCode;

import static lucky.xjz.rpc.core.protocol.ProtocolConstant.RPC_VERSION;

/**
 * 根据request 的信息构造reponse 工具
 */
public class MakeReponse {

    private MakeReponse(){}
    public static Reponse getConnectTimeOutReponse(Request request){

        return new Reponse(request.getId(),request.getRpcVersion(), MsgCode.CONNECTION_TIME_OUT.getCode());

    }

    public static Reponse getConnectTimeOutReponse(String id){

        return new Reponse(id,RPC_VERSION.getConstant(), MsgCode.CONNECTION_TIME_OUT.getCode());

    }
}
