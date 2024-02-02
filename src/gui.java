import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
public class gui{
    public static void main(String args[]) {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JMenuBar mb = new JMenuBar();
        
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem mi1 = new JMenuItem("Add item");
        JMenuItem mi2 = new JMenuItem("Edit item");
        m1.add(mi1);
        m1.add(mi2);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        JButton prev = new JButton("←");
        JLabel date = new JLabel("Friday");
        JButton next = new JButton("→");
        panel.add(prev);
        panel.add(date);
        panel.add(next);

        JPanel checklist = new JPanel();
        checklist.setBorder(BorderFactory.createLineBorder(Color.black));
        JCheckBox ca = new JCheckBox("Placeholder");
        checklist.add(ca);        


        frame.getContentPane().add(mb, BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(checklist, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
