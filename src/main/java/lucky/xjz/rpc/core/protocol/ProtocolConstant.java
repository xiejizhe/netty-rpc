package lucky.xjz.rpc.core.protocol;

public enum ProtocolConstant {

    HEADER("syy940203"),
    RPC_VERSION("1.0"),
    CLIENT_HELLO("chello"),
    SERVER_HELLO("shello");
    String constant;

    ProtocolConstant(String constant) {

          this.constant=constant;
    }

    public String getConstant(){
        return constant;
    }

    }
