package com.sierrabase.siriusapi;


import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.repository.analysis.AnalysisCrackEntityRepository;
import com.sierrabase.siriusapi.service.album.AlbumResource;
import com.sierrabase.siriusapi.service.album.AlbumWorker;
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
import java.nio.file.Path;
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

	@Autowired
	AlbumResource albumResource;
	@Test
	void contextLoads() {
		exportAlbum(193);
	}

	public String exportAlbum(Integer albumId) {
		String basePath = URICreator.pathToString(repository_path, "album", String.valueOf(albumId));
		Path albumPath = URICreator.pathTopath(basePath,"origin");
		Path analysisPath = URICreator.pathTopath(basePath,"analysis");

		albumResource.initialize(albumId);
		albumResource.includeImagesOn(albumPath);

		if (Files.exists(analysisPath)) {
			albumResource.includeImagesOn(analysisPath);
		}

		String nginxUri = ftpConfig.getNginxUri();
		String newTargetPath = albumResource.getTargetPath().toString();
		newTargetPath = newTargetPath.substring(newTargetPath.indexOf("/album"));
		return URICreator.pathToString(nginxUri, newTargetPath);
	}
}
