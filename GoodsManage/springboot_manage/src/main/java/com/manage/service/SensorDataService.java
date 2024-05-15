package com.manage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.manage.entity.SensorData;
import com.manage.mapper.SensorDataMapper;
import com.manage.utils.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class SensorDataService {
    @Autowired
    private SensorDataMapper sensorDataMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    private volatile boolean reading = false;

    @PostConstruct
    public void startSerialPortReader() {
        reading = true;
        Thread serialPortThread = new Thread(() -> {
            SerialPort serialPort = null;
            List<String> possiblePorts = Arrays.asList("COM6", "COM7", "COM8", "COM9", "COM10");

            while (reading) {
                if (serialPort == null) {
                    for (String portName : possiblePorts) {
                        SerialPort port = SerialPort.getCommPort(portName);
                        if (port.openPort()) {
                            System.out.println("串口连接成功！串口号为：" + port.getSystemPortName());
                            serialPort = port;
                            break;
                        }
                    }

                    if (serialPort == null) {
                        try {
                            Thread.sleep(1000); // 等待一秒后再次尝试打开串口
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0); // 设置超时时间为100毫秒
                }

                StringBuilder message = new StringBuilder();

                try {
                    while (serialPort.bytesAvailable() == 0) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Thread.sleep(100); // 等待一秒后再次尝试打开串口
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
                                        data.setSmokeSensor(Double.parseDouble(keyValue[1]));
                                        break;
                                    case "UID":
                                        data.setCardUid(keyValue[1]);
                                        break;
                                }
                            }
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                String json = mapper.writeValueAsString(data);
                                webSocketServer.sendMessage(json); // 发送数据给前端
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            message.setLength(0); // 清空message
                        } else {
                            message.append((char) readBuffer[i]); // 添加字符到message
                        }
                    }
                } catch (Exception e) {
                    System.out.println("读取串口数据时发生错误，可能是设备被拔出。将尝试重新连接...");
                    if (serialPort != null) {
                        serialPort.closePort(); // 关闭串口
                        serialPort = null;
                    }
                }
            }

            if (serialPort != null) {
                serialPort.closePort(); // 关闭串口
            }
        });
        serialPortThread.start();
    }

    public void stopSerialPortReader() {
        reading = false;
    }

    public boolean isReading() {
        return reading;
    }

    public int insertSensorData(SensorData sensorData) {
        return sensorDataMapper.insert(sensorData);
    }

    public List<SensorData> getAllSensorData() {
        return sensorDataMapper.selectList(null);
    }

    public int deleteSensorDataById(int id) {
        return sensorDataMapper.deleteById(id);
    }

}
