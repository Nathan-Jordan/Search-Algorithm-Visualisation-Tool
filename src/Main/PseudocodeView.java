package Main;

import Algorithms.SearchAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PseudocodeView extends JComponent {

    public ArrayList<PseudocodeLine> pseudocodeLines;

    public SearchAlgorithm searchAlgorithm;

    public int currentLineIndex;

    public int indentWidth = 20;
    public int lineHeight = 22;
    public int lineStartY = 30;



    public void setAlgorithm(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
        pseudocodeLines = searchAlgorithm.getPseudocodeLines();
        repaint();
    }

    public void addText() {
        removeAll();

        JLabel lbl = new JLabel("<html>" + pseudocodeLines.get(0).text + "</html>");
        Font font = new Font("Consolas", Font.BOLD, 16);
        lbl.setFont(font);
        lbl.setBounds(10, 2, getWidth(), lineHeight);
        add(lbl);


        for (int i = 1; i < pseudocodeLines.size(); i++) {
            PseudocodeLine line = pseudocodeLines.get(i);

            String lineText = "<html>" + line.text + "</html>";
            lbl = new JLabel(lineText);
            lbl.setFont(font);
            lbl.setBounds(indentWidth * line.indent, lineStartY + (lineHeight * (i - 1)), getWidth(), lineHeight);
            add(lbl);
        }
    }

    public void setCurrentLineIndex(int lineIndex) {
        currentLineIndex = lineIndex;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(460, 300);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.BLACK);


        //Line indicator
        int topLine = lineHeight * currentLineIndex + lineStartY;
        int middle = lineHeight * currentLineIndex + 35;
        int bottomLine = topLine + lineHeight;

        g2.drawLine(4, topLine, getWidth(), topLine);
        g2.fillOval(4, middle, 10, 10);
        g2.drawLine(4, bottomLine, getWidth(), bottomLine);


        //Dividing line
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(2, 0, 2, getHeight());
        addText();
    }

    public void changeToBackTracking() {
        pseudocodeLines = searchAlgorithm.getBacktrackPseudocode();
    }

    public void changeToPseudocode() {
        pseudocodeLines = searchAlgorithm.getPseudocodeLines();
    }
}
