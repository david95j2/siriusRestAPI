package com.sierrabase.siriusapi;


import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SiriusApiApplicationTests {

	@Value("${path.repository.base}")
	private String repository_path;

	@Value("${path.worker.base}")
	private String worker_path;
	@Autowired
	private FtpConfig ftpConfig;

	@Test
	void contextLoads() {
		System.out.println(repository_path);
		System.out.println(worker_path);
		System.out.println(ftpConfig.getNginxUri());
		System.out.println(ftpConfig.getNginxUri()+"/"+URICreator.pathToString("album/124","3D_modeling/downsampled_model.glb"));
	}
}
