package me.tud.weiner;

import me.tud.weiner.lang.function.DefaultFunctions;
import me.tud.weiner.script.Script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final String FILE_SUFFIX = ".wr";

    public static void main(String[] args) throws IOException {
        new DefaultFunctions();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("stop"))
                break;
            try {
                if (!line.endsWith(FILE_SUFFIX))
                    line += FILE_SUFFIX;
                Script script = new Script(new File(System.getProperty("user.dir"), "src/main/resources/" + line));
                if (script.parse())
                    script.run();
            } catch (FileNotFoundException e) {
                System.err.println("File " + line + " does not exist");
            }
        }
    }

}
