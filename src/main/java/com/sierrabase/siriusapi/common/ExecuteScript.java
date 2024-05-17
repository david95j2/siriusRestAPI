package com.sierrabase.siriusapi.common;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
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
        Process process = processBuilder.start();

        // 출력 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
            }
        }

        // 표준 에러 읽기
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println("Error: " + line);  // 에러 메시지는 System.err를 통해 출력
            }
        }


        int exitCode = process.waitFor();

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
