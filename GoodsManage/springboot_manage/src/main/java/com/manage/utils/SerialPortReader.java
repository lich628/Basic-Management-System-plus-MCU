package com.manage.utils;

import com.fazecast.jSerialComm.SerialPort;

public class SerialPortReader implements Runnable {
    private static boolean running = true;

    @Override
    public void run() {
        SerialPort serialPort = SerialPort.getCommPort("COM7"); // 替换为您的串口号
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0); // 设置超时时间为100毫秒

        if (!serialPort.isOpen()) {
            System.out.println("串口打开失败！");
            return;
        }

        System.out.println("串口打开成功！");

        while (running) {
            while (serialPort.bytesAvailable() == 0) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            byte[] readBuffer = new byte[serialPort.bytesAvailable()];
            int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
            if (numRead == -1) {
                System.out.println("读取失败！");
                break;
            }

            StringBuilder message = new StringBuilder();
            // 处理读取到的数据，判断是否遇到换行符
            for (int i = 0; i < numRead; i++) {
                if (readBuffer[i] == '\n') {
                    System.out.println(message.toString());
                    WebSocketServer.sendMessage(message.toString()); // 发送数据给前端
                    message.setLength(0); // 清空message
                } else {
                    message.append((char) readBuffer[i]); // 添加字符到message
                }
            }
        }
        serialPort.closePort(); // 关闭串口

    }

    public static void stop() {
        running = false;
    }
}
