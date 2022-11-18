//
// Created by xxb on 2021/7/3.
//

#ifndef SDKDEMO_UDPCLIENT_H
#define SDKDEMO_UDPCLIENT_H

#define SERVER_PORT 8888
#define SERVER_PORT1 8889
#define BUFF_LEN (1024 * 9 - 28)
//#define BUFF_LEN (40000 - 28)
#define SEND_LOOP_COUNT 1
#define SERVER_IP "172.22.72.26"
//#define SERVER_IP "127.0.0.1"

typedef enum _UdpClientType
{
    UDP_CLIENT_TYPE_CONNECT,
    UDP_CLIENT_TYPE_SENDTO,
    UDP_CLIENT_TYPE_SENDMSG
}UdpClientType;

#ifdef __cplusplus
extern "C"
{
#endif //__cplusplus

void StartUdpClientTest(int udpClientType, const char* ip, int port, const char* data, int dataLen
                        , bool looper);

#ifdef __cplusplus
}

#endif //__cplusplus


#endif //SDKDEMO_UDPCLIENT_H
