# netty-rpc
Netty 实现rpc 编解码使用google protobuf  请求、握手、发消息无缝衔接

启动ServerRun 自行设置端口号
启动test 自行设置ip和port

下面是模块的基本介绍 

lucky.xjz.rpc.core.protocol

lucky.xjz.rpc.core.protocol.HandShakeHandler  握手处理器  主要负责客户端与服务端握手的逻辑

lucky.xjz.rpc.core.protocol.MessageDecoder  所有的消息解码器

lucky.xjz.rpc.core.protocol.MsgCode   错误码枚举类

lucky.xjz.rpc.core.protocol.ProtoCodec  编解码接口

lucky.xjz.rpc.core.protocol.ProtocolConstant  协议常量

lucky.xjz.rpc.core.protocol.ProtocolWrapper  协议包装类  将ByteBuf 消息上增加协议信息

lucky.xjz.rpc.core.systemconfig 系统配置

lucky.xjz.rpc.core.util 工具类

lucky.xjz.rpc.core.CallBack 回调接口 

lucky.xjz.rpc.service  业务层 根据需要自己编写

lucky.xjz.rpc.tcpclient     客户端代码

lucky.xjz.rpc.tcpserver    服务端代码
