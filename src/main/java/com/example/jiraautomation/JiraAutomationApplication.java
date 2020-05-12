package com.example.jiraautomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class JiraAutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiraAutomationApplication.class, args);
		try {
			MyJiraClient.test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
