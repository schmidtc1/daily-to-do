import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.swing.*;



public class Gui {

    static String[] options = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

    private JMenuBar mb;
    private JMenu m1, m2;
    private JMenuItem mi1, mi2;

    private JFrame frame;
    
    private JLabel day;
    private DayOfWeek today = LocalDate.now().getDayOfWeek();

    private JPanel panel, date, lp, list;

    private Checklist checklist = new Checklist();
    private ChecklistModel checklistModel = new ChecklistModel(this.checklist);

    

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

        
        checklistModel.setDay(today);
        day = new JLabel(today.toString());

        JButton prev = new JButton("←");
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
    
        lp.add(list, BorderLayout.WEST);
        list.setPreferredSize(new Dimension(400, 330));

    }

    private void addListItem(String todo, DayOfWeek day) {
        JCheckBox chk = new JCheckBox(todo);
        checklistModel.add(day, new Item(todo, day, chk));

        if (day.equals(today)) {
            list.add(chk);
            list.revalidate();
        }
    }

    public Gui() {
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
                    new Gui();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ActionListener addItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String todo = JOptionPane.showInputDialog(panel, "Input an item", null);
            
            int day = JOptionPane.showOptionDialog(panel, "Select a day:", "Which day?", 0, 3, null, options, options[0]);
            if (todo != null) addListItem(todo, DayOfWeek.of(day + 1)); 
        }
    };
}
