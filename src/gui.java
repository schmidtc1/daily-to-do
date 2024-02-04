import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class gui extends Frame implements ActionListener {

    private JMenuBar mb;

    private JFrame frame;

    private JPanel panel;
    private JPanel date;
    private JPanel lp;
    private JPanel list;

    private void buildWindow() {
        frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
    }

    private void buildMenu() {
        mb = new JMenuBar();
        mb.setMaximumSize(new Dimension(400, 30));
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem mi1 = new JMenuItem("Add item");
        JMenuItem mi2 = new JMenuItem("Edit item");
        
        mi1.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
    }

    private void buildDate() {
        date = new JPanel();
        date.setMaximumSize(new Dimension(400, 40));
        date.setBorder(BorderFactory.createLineBorder(Color.black));
        JButton prev = new JButton("←");
        JLabel day = new JLabel("Friday");
        JButton next = new JButton("→");
        date.add(prev);
        date.add(day);
        date.add(next);
    }

    private void buildChecklist() {
        lp = new JPanel();
        lp.setLayout(new BorderLayout());
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
        list.setBorder(BorderFactory.createLineBorder(Color.black));
        
        list.add(new JCheckBox("Placeholder"));
        list.add(new JCheckBox("number 2"));
    
        lp.add(list, BorderLayout.WEST);
        list.setPreferredSize(new Dimension(400, 330));
    }

    private void addListItem() {
        list.add(new JCheckBox("New item"));
    }

    public gui() {
        buildWindow();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        buildMenu();
        buildDate();
        buildChecklist();

        panel.add(mb);
        panel.add(date);
        panel.add(lp);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new gui();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        if (e.getActionCommand() == "Add item") {
            System.out.println("test");
            addListItem();
        }
    }
}
