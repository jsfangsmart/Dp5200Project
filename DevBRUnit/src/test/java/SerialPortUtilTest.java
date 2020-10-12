
import gnu.io.*;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * @name: SerialPortUtilTest
 * @author: tuacy.
 * @date: 2019/6/26.
 * @version: 1.0
 * @Description: 串口测试代码
 */
public class SerialPortUtilTest {



    /**
     * 测试获取串口列表
     */
    @Test
    public void getSystemPortList() {

        List<String> portList = SerialPortUtil.getSerialPortList();
        System.out.println(portList);

    }

    /**
     * 测试串口打开，读，写操作
     */
    @Test
    public void serialPortAction() {
        try {
            final SerialPort serialPort = SerialPortUtil.openSerialPort("/dev/ttyS1", 115200);
            //启动一个线程每2s向串口发送数据，发送1000次hello
            new Thread(){
                @Override
                public void run() {
                    int i = 1;
                    while (i < 10) {
                        int iLen = 4;
                        byte[] pBuf= new byte[255];
                        iLen = CmdDeclare.SendDataPacked(CmdDeclare.CMD_CRU_CTRL,CmdDeclare.CMD_CRU_CTRL_GET_UNITSTATUS,pBuf,iLen);

                        byte[] pSend = new byte[iLen+1];
                        System.arraycopy(pBuf, 0, pSend, 0, iLen);

                        SerialPortUtil.sendData(serialPort, pSend);//发送数据
                        i++;
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            //设置监听
            SerialPortUtil.setListenerToSerialPort(serialPort, new SerialPortEventListener(){
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    if(serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//数据通知
                        byte[] bytes = SerialPortUtil.readData(serialPort);
                        System.out.println("收到的数据长度：" + bytes.length);
                        System.out.println("收到的数据：" + new String(bytes));
                    }

                }
            });

            try {
                // sleep 一段时间保证线程可以执行完
                Thread.sleep(3 * 30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | TooManyListenersException e) {
            e.printStackTrace();
        }
    }

}
