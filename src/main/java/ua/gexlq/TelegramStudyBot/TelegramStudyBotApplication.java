package ua.gexlq.TelegramStudyBot; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramStudyBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramStudyBotApplication.class, args);
	}

}

/*

#db settings
spring.jpa.hibernate.ddl-auto=update
spring.datesource.url=jdbc:mysql://localhost:3306/tg-bot
spring.datasource.username=tgbot
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=true


		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.29</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
*/