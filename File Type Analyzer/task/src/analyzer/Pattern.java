package analyzer;

public class Pattern {
    private int value;
    private String pattern;
    private String name;


    public Pattern() {
    }

    public Pattern(int value, String pattern, String name) {
        this.value = value;
        this.pattern = pattern;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
