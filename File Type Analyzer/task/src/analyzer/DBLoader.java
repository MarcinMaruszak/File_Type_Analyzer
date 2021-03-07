package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DBLoader {
    private File file;

    public DBLoader(File file) {
        this.file = file;
    }

    public Set<Pattern> loadPatterns() {
        Set<Pattern> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                set.add(parsePattern(line));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    private Pattern parsePattern(String line) {
        String[] pattern = line.split(";");
        for (int i = 0; i < pattern.length; i++) {
            pattern[i] = pattern[i].replaceAll("\"", "");
        }
        return new Pattern(Integer.parseInt(pattern[0]), pattern[1], pattern[2] );
    }

}
