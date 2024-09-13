package com.hakron;

import java.io.*;

public class Helpers {

    public static String getEnv(String envName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("src/.env")));

        while (br.ready()) {
            String line = br.readLine();

            String currentName = line.split("=")[0];
            String currentValue = line.split("=")[1];

            if (currentName.equals(envName)) {
                return currentValue;
            }
        }

        return null;
    }

}
