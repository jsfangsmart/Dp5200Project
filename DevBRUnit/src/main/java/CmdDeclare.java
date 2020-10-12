import org.junit.Test;

import java.util.Calendar;

/**
 * Description:
 * Auther:smart
 * Date: 2020/10/9 下午7:26
 */
public class CmdDeclare {

    // 特定标志
    public static final byte CMD_CRU_DLE = 0x10;
    public static final byte CMD_CRU_STX = 0x02;
    public static final byte CMD_CRU_MSG_ID1 = 0x00;
    public static final byte CMD_CRU_MSG_ID2 = 0x00;
    public static final byte CMD_CRU_MSG_ID3 = 0x00;
    public static final byte CMD_CRU_ETX = 0x03;
    public static final byte CMD_CRU_ENQ = 0x05;
    public static final byte CMD_CRU_ACK = 0x06;
    public static final byte CMD_CRU_NAK = 0x15;

    public static final byte  CMD_CRU_CTRL				    = 0x48;	// 控制命令
    public static final byte  CMD_CRU_SERV					= 0x64;	// 维护命令

    /* 硬币机控制命令 */
    public static final byte CMD_CRU_CTRL_INIT              = 0x01;	// 初始化
    public static final byte CMD_CRU_CTRL_PREPARE_DEPOSIT   = 0x02;	// 准备存款
    public static final byte CMD_CRU_CTRL_PREPARE_DISPENSE  = 0x03;	// 准备取款
    public static final byte CMD_CRU_CTRL_DEPOSIT           = 0x04;	// 存款
    public static final byte CMD_CRU_CTRL_DISPENSE          = 0x05;	// 取款
    public static final byte CMD_CRU_CTRL_PILOTLED          = 0x06;	// LED控制
    public static final byte CMD_CRU_CTRL_ACTION_STATUS	    = 0x07;	// 获取执行状态-配合存取款使用
    public static final byte CMD_CRU_CTRL_DEPOSIT_RESULT    = 0x08;	// 获取存款结果
    public static final byte CMD_CRU_CTRL_DISPENSE_RESULT   = 0x09;	// 获取取款结果
    public static final byte CMD_CRU_CTRL_CLEARUNIT         = 0x0A;	// 清机
    public static final byte CMD_CRU_CTRL_CLEARUNIT_RESULT  = 0x0B;	// 获取清机结果
    public static final byte CMD_CRU_CTRL_SET_UNITINFO      = 0x10;	// 设置机芯参数
    public static final byte CMD_CRU_CTRL_GET_UNITINFO      = 0x11;	// 获取机芯参数
    public static final byte CMD_CRU_CTRL_GET_CASSETTEINFO  = 0x12;	// 获取钱箱信息
    public static final byte CMD_CRU_CTRL_SET_CASSETTEINFO  = 0x13;	// 设置钱箱信息
    public static final byte CMD_CRU_CTRL_GET_COININFO      = 0x14;	// 获取上笔交易钞票信息
    public static final byte CMD_CRU_CTRL_GET_FIRMWAVEVER   = 0x15;	// 获取整机固件版本
    public static final byte CMD_CRU_CTRL_GET_HARDWAVEVER   = 0x16;	// 获取整机硬件版本
    public static final byte CMD_CRU_CTRL_GET_UNITSTATUS    = 0x20;	// 获取机芯状态

    /**
     * 16位CRC校验
     */
    public static int CRC16Modbus(byte[] pBuf, int iLen) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < iLen; i++) {
            CRC ^= ((int) pBuf[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return CRC;
    }
    /**
     * 数据进行封装再发送
     * byFunction 命令类型
     * byCommand  命令字
     * pBuf	  发送缓冲区
     * iDataLen  包括内容长度
     */
    public static int SendDataPacked(byte byFunction,byte byCommand,byte[] pBuf,int iDataLen)
    {
        //创建Calendar对象
        Calendar cal=Calendar.getInstance();

        //用Calendar类提供的方法获取年、月、日、时、分、秒
        int year  =cal.get(Calendar.YEAR);   //年
        int month =cal.get(Calendar.MONTH)+1;  //月  默认是从0开始  即1月获取到的是0
        int day   =cal.get(Calendar.DAY_OF_MONTH);  //日，即一个月中的第几天
        int hour  =cal.get(Calendar.HOUR_OF_DAY);  //小时
        int minute=cal.get(Calendar.MINUTE);   //分
        int second=cal.get(Calendar.SECOND);  //秒


        int iPos = 0;
        int i16CRC = 0;
        pBuf[iPos++] = CMD_CRU_DLE;
        pBuf[iPos++] = CMD_CRU_STX;
        pBuf[iPos++] = CMD_CRU_MSG_ID1;
        pBuf[iPos++] = CMD_CRU_MSG_ID2;
        pBuf[iPos++] = CMD_CRU_MSG_ID3;

        iDataLen += 8;
        pBuf[iPos++] = (byte)(iDataLen & 0xFF);
        pBuf[iPos++] = (byte)((iDataLen >> 8) & 0xFF);
        pBuf[iPos++] = byFunction;
        pBuf[iPos++] = byCommand;
        pBuf[iPos++] = (byte)((year%100)&0xFF);
        pBuf[iPos++] = (byte)(month & 0xFF);
        pBuf[iPos++] = (byte)(day & 0xFF);
        pBuf[iPos++] = (byte)(hour & 0xFF);
        pBuf[iPos++] = (byte)(minute & 0xFF);
        pBuf[iPos++] = (byte)(second & 0xFF);

        pBuf[iDataLen + 7] = CMD_CRU_DLE;
        pBuf[iDataLen + 7 + 1] = CMD_CRU_ETX;
        i16CRC = CRC16Modbus(pBuf,iDataLen + 9);
        pBuf[iDataLen + 7 + 2] = (byte)(i16CRC & 0xFF);
        pBuf[iDataLen + 7 + 3] = (byte)((i16CRC >> 8)&0xFF);

        return iDataLen+11;
    }
}
