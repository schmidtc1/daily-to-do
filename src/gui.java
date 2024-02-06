import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.*;


public class gui {

    private JMenuBar mb;
    private JMenu m1, m2;
    private JMenuItem mi1, mi2;

    private JFrame frame;
    
    private JLabel currDay;

    private JPanel panel, date, lp, list;



    int count = 2;

    private void buildWindow() {
        frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
    }

    private void buildMenu() {
        mb = new JMenuBar();
        mb.setMaximumSize(new Dimension(400, 30));
        m1 = new JMenu("FILE");
        m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        mi1 = new JMenuItem("Add item");
        mi2 = new JMenuItem("Edit item");

        
        
        mi1.addActionListener(addItem);

        m1.add(mi1);
        m1.add(mi2);

        frame.setJMenuBar(mb);
    }

    private void buildDate() {
        date = new JPanel();
        date.setMaximumSize(new Dimension(400, 40));
        date.setBorder(BorderFactory.createLineBorder(Color.black));

        JButton prev = new JButton("←");
        currDay = new JLabel(LocalDate.now().getDayOfWeek().toString());
        JButton next = new JButton("→");
        date.add(prev);
        date.add(currDay);
        date.add(next);
    }

    private void buildChecklist() {
        lp = new JPanel();
        lp.setLayout(new BorderLayout());
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
        list.setBorder(BorderFactory.createLineBorder(Color.black));
        
        list.add(new JCheckBox("1"));
        list.add(new JCheckBox("2"));
        
    
        lp.add(list, BorderLayout.WEST);
        list.setPreferredSize(new Dimension(400, 330));

    }

    private void addListItem() {
        list.add(new JCheckBox(Integer.toString(count)));
        list.revalidate();
    }

    public gui() {
        buildWindow();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        buildMenu();
        buildDate();
        buildChecklist();

        //panel.add(mb);
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

    private ActionListener addItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            count++;
            addListItem();
        }
    };
}
