package Main;

public class PseudocodeLine {

    String text;
    int indent;


    public PseudocodeLine(String text) {
        this.text = text;
        this.indent = 0;
    }
    public PseudocodeLine(String text, int indent) {
        this.text = text;
        this.indent = indent;
    }

    public int getIndent() {
        return indent;
    }
}
