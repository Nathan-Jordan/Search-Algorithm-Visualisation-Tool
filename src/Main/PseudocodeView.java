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

        //Get pseudocode of the search algorithm and repaint the panel
        pseudocodeLines = searchAlgorithm.getPseudocodeLines();
        repaint();
    }

    public void setCurrentLineIndex(int lineIndex) {
        currentLineIndex = lineIndex;
    }

    public void changeToBackTracking() {
        //Change the pseudocode to the backtracking pseudocode and repaint the panel
        pseudocodeLines = searchAlgorithm.getBacktrackPseudocode();
        repaint();
    }

    public void changeToPseudocode() {
        //Change the pseudocode to the pseudocode of the algorithm and repaint the panel
        pseudocodeLines = searchAlgorithm.getPseudocodeLines();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //Clear panel
        removeAll();
        Graphics2D g2 = (Graphics2D) g;

        //Draw background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.BLACK);


        //Line highlighter indicator
        int topLine = lineHeight * currentLineIndex + lineStartY;
        int middle = lineHeight * currentLineIndex + 35;
        int bottomLine = topLine + lineHeight;
        //Draw line highlight
        g2.drawLine(4, topLine, getWidth(), topLine);
        g2.fillOval(4, middle, 10, 10);
        g2.drawLine(4, bottomLine, getWidth(), bottomLine);


        //Draw dividing line
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(2, 0, 2, getHeight());

        //Draw pseudocode
        drawPseudocode();
    }

    public void drawPseudocode() {
        //Create and wrap fist line of pseudocode (name) with html tags
        JLabel pseudocodeLine = new JLabel("<HTML>" + pseudocodeLines.get(0).text + "</HTML>");

        //Set the font and position of the pseudocode name
        Font font = new Font("Consolas", Font.BOLD, 16);
        pseudocodeLine.setFont(font);
        pseudocodeLine.setBounds(10, 2, getWidth(), lineHeight);

        //'Draw' the pseudocode name on the panel
        add(pseudocodeLine);


        //For the rest of the pseudocode lines
        for (int i = 1; i < pseudocodeLines.size(); i++) {
            PseudocodeLine line = pseudocodeLines.get(i);

            //Wrap the pseudocode with html tags
            pseudocodeLine = new JLabel("<html>" + line.text + "</html>");

            //Set the font and position of the pseudocode
            pseudocodeLine.setFont(font);
            pseudocodeLine.setBounds(indentWidth * line.indent, lineStartY + (lineHeight * (i - 1)), getWidth(), lineHeight);

            //'Draw' the pseudocode on the panel
            add(pseudocodeLine);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(460, 300);
    }
}
