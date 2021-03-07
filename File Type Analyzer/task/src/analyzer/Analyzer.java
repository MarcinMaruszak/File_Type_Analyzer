package analyzer;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Analyzer {
    private final File folder;
    private final Set<Pattern> patterns;

    public Analyzer(File folder, File patternFile) {
        this.folder = folder;
        DBLoader dbLoader = new DBLoader(patternFile);
        this.patterns = dbLoader.loadPatterns();
    }

    public void analyze() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Callable<String>> callableList = new ArrayList<>();

        for (File file1 : folder.listFiles()) {
            try (InputStream inputStream = new FileInputStream(file1)){
                String text = new String(inputStream.readAllBytes());
                callableList.add(() -> RabinKarp(text , file1.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            List<Future<String>> futureList = executor.invokeAll(callableList);
            futureList.forEach(f -> {
                try {
                    System.out.println(f.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public  long charToLong(char ch) {
        return ch - '!' + 1;
    }

    public  String RabinKarp(String text , String fileName) {
        int a = 49157;
        long m = 10_000_000_000_009L;

        ArrayList<Pattern> occurrences = new ArrayList<>();

        for (Pattern patt : patterns) {
            String pattern = patt.getPattern();
            if (pattern.length()<=text.length()) {
                long patternHash = 0;
                long currSubstrHash = 0;
                long pow = 1;
                for (int i = 0; i < pattern.length(); i++) {
                    patternHash += charToLong(pattern.charAt(i)) * pow;
                    patternHash %= m;

                    currSubstrHash += charToLong(text.charAt(text.length() - pattern.length() + i)) * pow;
                    currSubstrHash %= m;

                    if (i != pattern.length() - 1) {
                        pow = pow * a % m;
                    }
                }
                for (int i = text.length(); i >= pattern.length(); i--) {
                    if (patternHash == currSubstrHash) {
                        boolean patternIsFound = true;

                        for (int j = 0; j < pattern.length(); j++) {
                            if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                                patternIsFound = false;
                                break;
                            }
                        }
                        if (patternIsFound) {
                            occurrences.add(patt);
                        }
                    }
                    if (i > pattern.length()) {
                        currSubstrHash = (currSubstrHash - charToLong(text.charAt(i - 1)) * pow % m + m) * a % m;
                        currSubstrHash = (currSubstrHash + charToLong(text.charAt(i - pattern.length() - 1))) % m;
                    }
                }
            }
        }
        return getPatternsFromFile(fileName, occurrences);
    }



    private String getPatternsFromFile(String fileName, List<Pattern> patternList) {
        if (patternList.isEmpty()) {
            return fileName + ": Unknown file type";
        }
        Optional<Pattern> pattern = patternList.stream().max(Comparator.comparingInt(Pattern::getValue));

        return fileName + ": " +pattern.get().getName();
    }
}
