package com.sierrabase.siriusapi;


import com.sierrabase.siriusapi.common.URICreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SiriusApiApplicationTests {

	@Value("${path.repository.base}")
	private String repository_path;

	@Value("${path.worker.base}")
	private String worker_path;


	@Test
	void contextLoads() {
		System.out.println(repository_path);
		System.out.println(worker_path);
	}
}
