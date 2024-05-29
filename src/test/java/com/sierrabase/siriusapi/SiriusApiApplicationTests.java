package com.sierrabase.siriusapi;


import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.repository.analysis.AnalysisCrackEntityRepository;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class SiriusApiApplicationTests {

	@Value("${path.repository.base}")
	private String repository_path;

	@Value("${path.worker.base}")
	private String worker_path;
	@Autowired
	private FtpConfig ftpConfig;
	@Autowired
	private AnalysisCrackEntityRepository analysisCrackEntityRepository;
	@Test
	void contextLoads() {

		String sourcePath = URICreator.pathToString(repository_path,"model","25","elevation","49","analysis");
		File sourceFolder = Paths.get(sourcePath).toFile();
		File[] files = sourceFolder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (!(file.getName().endsWith("_seg_points.json") || file.getName().endsWith("_skel.json"))) { // file.getName().matches(pattern)
					try {
						String jsonContent = new String(Files.readAllBytes(Paths.get(file.getPath())));
						int crackCount;
						if (jsonContent.length() != 5) {
							crackCount = new JSONArray(jsonContent).length();
						} else {
							crackCount = 0;
						}

						AnalysisCrackModel analysisCrackModel = new AnalysisCrackModel(132, 156,
								ftpConfig.getNginxUri()+file.getPath().substring(file.getPath().indexOf("/model")),crackCount);
						AnalysisCrackEntity entity = new AnalysisCrackEntity(analysisCrackModel);
						analysisCrackEntityRepository.save(entity);
					} catch (IOException e) {
//						log.error("Can not read elevation json :"+file.getPath());
					}
				}
			}
		}

	}
}
