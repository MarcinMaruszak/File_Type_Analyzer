package analyzer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer(new File(args[0]), new File(args[1]));
        analyzer.analyze();
    }
}
