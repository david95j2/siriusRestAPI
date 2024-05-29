package com.sierrabase.siriusapi.common;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ExecuteScript {
    public static int executeShellScript(String run, String gpu, String scriptPath, String[] arguments) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        if(run != null) { // if venv
            command.add(run);
            if(gpu != null) {
                if (gpu.equals("-b -P")){
                    command.add("-b");
                    command.add("-P");
                } else {
                    command.add(gpu);
                }
            }
        }
        command.add(scriptPath);
        command.addAll(Arrays.asList(arguments));
        log.info("command all : "+command);
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        if (scriptPath.contains("analyzer.py")) {
            String parent = new File(scriptPath).getParent();
            File currentDirectory = new File(parent);
            processBuilder.directory(currentDirectory);
        }
        if (scriptPath.contains("GLBLoader")) {
            processBuilder.environment().put("DISPLAY", ":1.0");
        }

        Process process = processBuilder.start();


        // 표준 출력 스트림 읽기
        Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            } catch (IOException e) {
                log.error("Error reading stdout", e);
            }
        });

        // 표준 에러 스트림 읽기
        Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
//                    log.error("Error: " + line);
                }
            } catch (IOException e) {
                log.error("Error reading stderr", e);
            }
        });

        // 스레드 시작
        stdoutThread.start();
        stderrThread.start();

        int exitCode = process.waitFor();
        stdoutThread.join();
        stderrThread.join();

        if (exitCode != 0) {
//            throw new RuntimeException("script execution failed with exit code: " + exitCode);
            log.error("script execution failed with exit code: " + exitCode);
        }
        return exitCode;
    }

    public static double getProcessTime(Long startTime) {
        long endTimeSec = System.nanoTime();
        long timeElapsedSec = endTimeSec - startTime;
        return (double)timeElapsedSec / 1_000_000_000;
    }
}
