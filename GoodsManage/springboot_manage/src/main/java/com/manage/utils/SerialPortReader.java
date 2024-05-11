package com.manage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.manage.entity.SensorData;

public class SerialPortReader implements Runnable {
    private static boolean running = true;

    @Override
    public void run() {
        SerialPort serialPort = SerialPort.getCommPort("COM7"); // 替换为您的串口号
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0); // 设置超时时间为100毫秒

        StringBuilder message = new StringBuilder();

        while (running) {
            while (!serialPort.isOpen()) {
                boolean opened = serialPort.openPort();
                if (!opened) {
                    try {
                        Thread.sleep(1000); // 如果打开失败，等待一秒再尝试
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("串口连接成功！");
                }
            }

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

            // 处理读取到的数据，判断是否遇到换行符
            for (int i = 0; i < numRead; i++) {
                if (readBuffer[i] == '\n') {
                    String[] parts = message.toString().split(" ");
                    SensorData data = new SensorData();
                    for (String part : parts) {
                        String[] keyValue = part.split(":");
                        switch (keyValue[0]) {
                            case "Humidity":
                                data.setHumidity(Double.parseDouble(keyValue[1]));
                                break;
                            case "Temperature":
                                data.setTemperature(Double.parseDouble(keyValue[1]));
                                break;
                            case "SmokeSensorValue":
                                data.setSmokeSensorValue(Double.parseDouble(keyValue[1]));
                                break;
                            case "UID":
                                data.setCardUid(keyValue[1]);
                                break;
                        }
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        String json = mapper.writeValueAsString(data);
                        WebSocketServer.sendMessage(json); // 发送数据给前端
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
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
