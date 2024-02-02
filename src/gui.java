import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;
public class gui{
    public static void main(String args[]) {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JMenuBar mb = new JMenuBar();
        mb.setMaximumSize(new Dimension(400, 30));
        
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem mi1 = new JMenuItem("Add item");
        JMenuItem mi2 = new JMenuItem("Edit item");
        m1.add(mi1);
        m1.add(mi2);

        JPanel date = new JPanel();
        date.setMaximumSize(new Dimension(400, 40));
        date.setBorder(BorderFactory.createLineBorder(Color.black));
        JButton prev = new JButton("←");
        JLabel day = new JLabel("Friday");
        JButton next = new JButton("→");
        date.add(prev);
        date.add(day);
        date.add(next);

        JPanel lp = new JPanel();
        lp.setLayout(new BorderLayout());
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
        list.setBorder(BorderFactory.createLineBorder(Color.black));
        
        list.add(new JCheckBox("Placeholder"));
        list.add(new JCheckBox("number 2"));

        lp.add(list, BorderLayout.WEST);
        list.setPreferredSize(new Dimension(400, 330));

        panel.add(mb);
        panel.add(date);
        panel.add(lp);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
