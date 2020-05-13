package com.example.jiraautomation;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class JiraAutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiraAutomationApplication.class, args);
		try {
			List<String> argsList = Arrays.asList(args);
			MyJiraClient.test("JEA", 10001L, "Issue"+ RandomUtils.nextLong() +" created for test1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
