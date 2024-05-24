#include "DHT.h"
#include <SPI.h>
#include <MFRC522.h>

#define DHTTYPE DHT11
#define DHTPIN 2
#define RST_PIN 9
#define SS_PIN 10
#define MQ2pin (0)

#define BUZZER_PIN 4 // 定义蜂鸣器引脚


DHT dht(DHTPIN, DHTTYPE);
MFRC522 rfid(SS_PIN, RST_PIN);

void setup() {
  Serial.begin(9600);
  dht.begin();
  SPI.begin();
  rfid.PCD_Init();

  pinMode(BUZZER_PIN, OUTPUT);
  digitalWrite(BUZZER_PIN, LOW);
}

void readSensors() {
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  if (isnan(h) || isnan(t)) {
    Serial.print("Failed to read from DHT sensor! ");
    return;
  }
  Serial.print("Humidity:");
  Serial.print(h);
  Serial.print(" ");
  Serial.print("Temperature:");
  Serial.print(t);
  Serial.print(" ");
  readSmokeSensor();
  Serial.print(" ");
}

void readSmokeSensor() {
  float SmokeSensorValue = analogRead(MQ2pin);
  Serial.print("SmokeSensorValue:");
  Serial.print(SmokeSensorValue);
}

void readRFID() {
  
  if (!rfid.PICC_IsNewCardPresent())
    return;
  if (!rfid.PICC_ReadCardSerial())
    return;

  Serial.print(F("UID:"));
  for (uint8_t i = 0; i < 4; i++) {
    Serial.print((char)toupper(rfid.uid.uidByte[i] >> 4), HEX); // 高四位
    Serial.print((char)toupper(rfid.uid.uidByte[i] & 0x0F), HEX); // 低四位
  }
  rfid.PICC_HaltA();
  rfid.PCD_StopCrypto1();
}
void start(){
  readSensors();
  readRFID();
  Serial.println();
}
void loop() {
  start();
  if (Serial.available()) {
    char command = Serial.read();
    if (command == '1' || command == '0') {
      if (command == '1') {
        digitalWrite(BUZZER_PIN, HIGH); // 打开蜂鸣器
      } else if (command == '0') {
        digitalWrite(BUZZER_PIN, LOW); // 关闭蜂鸣器
      }
    } else {
      // 忽略无效命令
      while (Serial.available()) {
        Serial.read(); // 清除串口缓冲区中的无效数据
      }
    }
  }
  delay(1500);
}
