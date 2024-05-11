package com.manage;

import com.manage.utils.SerialPortReader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@MapperScan("com.manage.mapper")
@CrossOrigin
public class springbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(springbootApplication.class, args);

		SerialPortReader serialPortReader = new SerialPortReader();
			Thread thread = new Thread(serialPortReader);
			thread.start();
			System.out.println("开启串口数据监听");
	}

}
