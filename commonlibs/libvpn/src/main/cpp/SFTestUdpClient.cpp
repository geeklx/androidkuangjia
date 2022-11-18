//
// Created by xxb on 2021/7/3.
//

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <thread>
#include "SFTestUdpClient.h"
#include "stdio.h"
#include "arpa/inet.h"
#include <string>
#include <sys/errno.h>
#include <sys/un.h>
#include "logging.h"
#include "native_lib.h"
//#include "iptool/IpTool.h"


#define TAG "UdpClient"

void udp_msg_sender(int fd, struct sockaddr* dst);
void udp_msg_sender_by_connect(int fd);
void udp_msg_sender_by_sendmsg(int fd, struct sockaddr* dst);
void _testUdpClientBySendto();
void _testUdpClientBySendmsg();
void _testUdpClientByConnect();
std::string dumpSockAddr(const sockaddr *addr, const socklen_t socklen);

std::string serverIp;
int serverPort;
std::string sendData;
int sendDataLen;
bool isLooper;

void StartUdpClientTest(int udpClientType, const char* ip, int port, const char* data,
                        int dataLen, bool looper)
{
    serverIp = ip;
    serverPort = port;
    sendData = data;
    sendDataLen = dataLen;
    isLooper = looper;

    switch (udpClientType) {
        case UDP_CLIENT_TYPE_SENDTO:
            _testUdpClientBySendto();
            break;
        case UDP_CLIENT_TYPE_SENDMSG:
            _testUdpClientBySendmsg();
            break;
        case UDP_CLIENT_TYPE_CONNECT:
            _testUdpClientByConnect();
            break;
        default:
            break;
    }
}

void _testUdpClientBySendtoFunctino()
{
    int client_fd;
    struct sockaddr_in ser_addr;
    client_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if(client_fd < 0)
    {
        LOGI(TAG, "create socket fail!\n");
        return ;
    }
    LOGI(TAG,"create socket suc!\n");
    memset(&ser_addr, 0, sizeof(ser_addr));
    ser_addr.sin_family = AF_INET;
    ser_addr.sin_addr.s_addr = inet_addr(serverIp.c_str());
//    ser_addr.sin_addr.s_addr = htonl(INADDR_ANY);  //注意网络序转换
    ser_addr.sin_port = htons(serverPort);  //注意网络序转换
    udp_msg_sender(client_fd, (struct sockaddr*)&ser_addr);
    close(client_fd);
}

void _testUdpClientBySendto()
{
    std::thread threadObj(_testUdpClientBySendtoFunctino);
    threadObj.detach();
//    std::cout << "Exit of Main function" << std::endl;
//    return 0;
}

void _testUdpClientBySendmsgFunction()
{
    int client_fd;
    struct sockaddr_in ser_addr;
    client_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if(client_fd < 0)
    {
        LOGI(TAG,"create socket fail!\n");
        return ;
    }
    LOGI(TAG,"create socket suc!\n");
    memset(&ser_addr, 0, sizeof(ser_addr));
    ser_addr.sin_family = AF_INET;
    ser_addr.sin_addr.s_addr = inet_addr(SERVER_IP);
    //ser_addr.sin_addr.s_addr = htonl(INADDR_ANY);  //注意网络序转换
    ser_addr.sin_port = htons(SERVER_PORT);  //注意网络序转换
    udp_msg_sender_by_sendmsg(client_fd, (struct sockaddr*)&ser_addr);
}

void _testUdpClientBySendmsg()
{
    std::thread threadObj(_testUdpClientBySendmsgFunction);
    threadObj.detach();
}

void _testUdpClientByConnectFunction()
{
    int client_fd;
    struct sockaddr_in ser_addr;
    client_fd = socket(AF_INET, SOCK_DGRAM, 0);
    if(client_fd < 0)
    {
        LOGI(TAG,"create socket fail!\n");
        return ;
    }
    LOGI(TAG,"create socket suc!\n");
    memset(&ser_addr, 0, sizeof(ser_addr));
    ser_addr.sin_family = AF_INET;
    ser_addr.sin_addr.s_addr = inet_addr(SERVER_IP);
    //ser_addr.sin_addr.s_addr = htonl(INADDR_ANY);  //注意网络序转换
    ser_addr.sin_port = htons(SERVER_PORT1);  //注意网络序转换

    if (connect(client_fd, (struct sockaddr *)(&ser_addr), sizeof(ser_addr)) < 0)
    {
        LOGI(TAG,"_testUdpClientByConnect connect failed");
        return;
    }
    udp_msg_sender_by_connect(client_fd);
    close(client_fd);
}

void _testUdpClientByConnect()
{
    std::thread threadObj(_testUdpClientByConnectFunction);
    threadObj.detach();
}




void udp_msg_sender(int fd, struct sockaddr* dst)
{
    socklen_t len;
    struct sockaddr_in src;
    int sendtoCount = SEND_LOOP_COUNT;
    char buf[BUFF_LEN] = {};

    memcpy(buf, sendData.c_str(), sendData.length());

    for (;sendtoCount>0 || isLooper;sendtoCount--)
    {
        if (sendData.empty())
        {
            snprintf(buf, BUFF_LEN - 1, "client sendto:%d", sendtoCount);
        }

        len = sizeof(*dst);
        LOGI(TAG,"udp_msg_sender:%s\n",buf);  //打印自己发送的信息
        dumpSockAddr(dst, len);
        int sendCount = sendDataLen;
        if (sendto(fd, buf, sendCount, 0, dst, len) <= 0) {
            LOGW(TAG,"udp_msg_sender sendto error ret:%s!!\n", strerror(errno));
        }
        if (!isLooper)
        {
            memset(buf, 0, BUFF_LEN);
            int recvcount = recvfrom(fd, buf, BUFF_LEN, 0, (struct sockaddr*)&src, &len);
            if (recvcount <= 0)  //接收来自server的信息
            {
                LOGW(TAG,"udp_msg_sender recvfrom error ret:%s!!\n", strerror(errno));
            }
            dumpSockAddr((struct sockaddr*)&src, len);
            LOGI(TAG,"udp_msg_sender, len(%d), msgLast5bits(%s), msg:%s\n",recvcount, buf + recvcount - 5, buf);
            onRecvData(inet_ntoa(src.sin_addr), ntohs(src.sin_port), buf, recvcount);
        }
        usleep(1000 * 10);
//        break;
//        sleep(1);  //一秒发送一次消息

    }
}

void udp_msg_sender_by_sendmsg(int fd, struct sockaddr* dst)
{
    socklen_t len;
    int sendtoCount = SEND_LOOP_COUNT;
    char buf[BUFF_LEN] = {};
    
    struct msghdr msgsend;
    struct iovec iovsend[1];
    
    struct sockaddr_in src;
    
    for (;sendtoCount>0;sendtoCount--)
    {
        snprintf(buf, BUFF_LEN - 1, "client sendmsg:%d", sendtoCount);

        memset(&msgsend, 0, sizeof(msgsend));
        memset(iovsend, 0, sizeof(iovsend));
        msgsend.msg_name = dst;
        len = sizeof(*dst);
        msgsend.msg_namelen = len;
        msgsend.msg_iov = iovsend;
        msgsend.msg_iovlen = 1;
        iovsend[0].iov_base = buf;
        iovsend[0].iov_len = BUFF_LEN;

        LOGI(TAG,"udp_msg_sender_by_sendmsg client:%s\n",buf);  //打印自己发送的信息
        dumpSockAddr(dst, len);
        if (sendmsg(fd, &msgsend, 0) <= 0) {
            LOGI(TAG,"udp_msg_sender_by_sendmsg send error ret:%s!!\n", strerror(errno));
        }
        memset(buf, 0, BUFF_LEN);
        
        //接收需要使用另外一个地址接收，否则接收的地址或者端口如果被修改了（代理的情况下会修改）会覆盖send的地址
        msgsend.msg_name = &src;
        len = sizeof(src);
        if (recvmsg(fd, &msgsend, 0) <= 0) {
            LOGI(TAG,"udp_msg_sender_by_sendmsg send error ret:%s!!\n", strerror(errno));
        }
        memcpy(buf, msgsend.msg_iov->iov_base, msgsend.msg_iov->iov_len < BUFF_LEN ? msgsend.msg_iov->iov_len : BUFF_LEN);
        dumpSockAddr((struct sockaddr*)msgsend.msg_name, len);
        LOGI(TAG,"udp_msg_sender_by_sendmsg recv server:%s\n",buf);
//        break;
//        sleep(1);  //一秒发送一次消息
    }
}

void udp_msg_sender_by_connect(int fd)
{
    int cCount = SEND_LOOP_COUNT;
    char buf[BUFF_LEN] = {};
    for (;cCount>0;cCount--)
    {
        snprintf(buf, BUFF_LEN - 1, "client connect:%d", cCount);
        LOGI(TAG,"udp_msg_sender_by_connect client:%s\n",buf);  //打印自己发送的信息
        if (send(fd, buf, BUFF_LEN, 0) <= 0) {
            LOGW(TAG,"udp_msg_sender_by_connect send error ret:%s!!\n",  strerror(errno));
        }
        memset(buf, 0, BUFF_LEN);
        if (recv(fd, buf, BUFF_LEN, 0) <= 0) {  //接收来自server的信息
            LOGW(TAG,"udp_msg_sender_by_connect recv error ret:%s!!\n",  strerror(errno));
        }
        LOGI(TAG,"udp_msg_sender_by_connect recv server:%s\n",buf);
        sleep(1);  //一秒发送一次消息
    }
}

std::string addrToStr(const uint32_t addr) {
    char buf[50];
    if (inet_ntop(AF_INET, (void *) &addr, buf, sizeof(buf))) {
        return std::string(buf);
    }
    LOGI(TAG,"addrToStr failed.addr:%p error:%s", addr, strerror(errno));
    return "";
}

std::string addrToStr(const struct in_addr &addr) {
    return addrToStr(addr.s_addr);
}

std::string addrToStr(const in6_addr &addr) {
    char buf[50];
    if (inet_ntop(AF_INET6, (void *) &addr, buf, sizeof(buf))) {
        return std::string(buf);
    }

    LOGI(TAG,"addrToStr failed.addr:%p error:%s", addr, strerror(errno));
    return "";
}


std::string familyToStr(int family) {
    switch (family) {
        case AF_INET:
            return "AF_INET";
        case AF_INET6:
            return "AF_INET6";
        case AF_UNIX:
            return "AF_UNIX";
        case AF_UNSPEC:
            return "AF_UNSPEC";
        default:
            char buf[30];
            snprintf(buf, sizeof(buf), "unknown family:%d", family);
            return buf;
    }
}

std::string dumpSockAddr(const sockaddr *addr, const socklen_t socklen) {
    if (addr == nullptr) {
        LOGI(TAG,"dumpSockAddr failed.\n");
        return "";
    }
    std::string ipStr;
    uint16_t port;
    char buf[200] = {0};
    int size = 0;
    switch (addr->sa_family) {
        case AF_INET: {
            if (socklen < sizeof(sockaddr_in)) {
                LOGI(TAG,"dumpSockAddr failed 2.\n");
                return "";
            }
            auto *in = (sockaddr_in *) addr;
            port = in->sin_port;
            ipStr = addrToStr(in->sin_addr);
            size = snprintf(buf, sizeof(buf), "dumpSockAddr ip:%s port:%d family:%s", ipStr.c_str(), ntohs(port),
                            familyToStr(addr->sa_family)
                                    .c_str());
        }
            break;
        case AF_INET6: {
            if (socklen < sizeof(sockaddr_in6)) {
                LOGI(TAG,"dumpSockAddr failed 3.\n");
                return "";
            }
            auto *in6 = (sockaddr_in6 *) addr;
            port = in6->sin6_port;
            ipStr = addrToStr(in6->sin6_addr);
            size = sprintf(buf, "ip:[%s] port:%d family:%s", ipStr.c_str(), ntohs(port),
                           familyToStr(addr->sa_family)
                                   .c_str());
        }
            break;
        case AF_UNIX: {
            if (socklen < sizeof(sockaddr_un)) {
                LOGI(TAG,"dumpSockAddr failed 4.\n");
                return "";
            }
            auto *un = (sockaddr_un *) addr;
            char *start = un->sun_path;
            if (*start == NULL) {
                start++;
                size = sprintf(buf, "unix socket,path:%s family:%s", start,
                               familyToStr(addr->sa_family)
                                       .c_str());
            } else {
                size = sprintf(buf, "abstract unix socket, path:%s family:%s", start, familyToStr
                        (addr->sa_family).c_str());
            }
        }
            break;
        default:
            size = sprintf(buf, "unknown family:%d, family:%s", addr->sa_family, familyToStr
                    (addr->sa_family).c_str());
            break;
    }
    if (size >= sizeof(buf) - 1) {
        LOGI(TAG,"dumpSockAddr failed 5.\n");
    }
    LOGI(TAG,"dumpSockAddr over,%s\n", buf);

    return std::string(buf);
}
