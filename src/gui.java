import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;



public class gui {

    static String[] options = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

    private JMenuBar mb;
    private JMenu m1, m2;
    private JMenuItem mi1, mi2;
    private JCheckBoxMenuItem mi3;
    private JButton addItemButton;


    private JFrame frame;
    
    private JLabel day;
    private DayOfWeek today = LocalDate.now().getDayOfWeek();

    private JPanel panel, date, lp, list, bp;

    private Checklist checklist = new Checklist();
    private ChecklistModel checklistModel = new ChecklistModel(this.checklist);

    

    private void buildWindow() {
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
    }

    private void buildMenu() {
        mb = new JMenuBar();
        mb.setMaximumSize(new Dimension(400, 30));
        m1 = new JMenu("File");
        m2 = new JMenu("Theme");
        mb.add(m1);
        mb.add(m2);
        mi1 = new JMenuItem("Add item");
        mi2 = new JMenuItem("Edit item");
        mi3 = new JCheckBoxMenuItem("Dark mode", false);
        
        
        mi1.addActionListener(addItem);
        mi2.addActionListener(editItem);
        mi3.addActionListener(toggleDark);

        m1.add(mi1);
        m1.add(mi2);
        m2.add(mi3);

        frame.setJMenuBar(mb);
    }

    private void buildDate() {
        date = new JPanel();
        date.setMaximumSize(new Dimension(400, 40));
        date.setBorder(BorderFactory.createLineBorder(Color.black));

        
        if (checklistModel.getCurrDayOfWeek() == null) checklistModel.setDay(today);
        day = new JLabel(checklistModel.getCurrDayOfWeek().toString(), SwingConstants.CENTER);
        day.setPreferredSize(new Dimension(100, 30));
        day.setMinimumSize(new Dimension(100, 30));
        day.setMaximumSize(new Dimension(100, 30));


        JButton prev = new JButton("←");
        prev.addActionListener(decrementDate);
        prev.setPreferredSize(new Dimension(80, 30));
        JButton next = new JButton("→");
        next.addActionListener(incrementDate);
        next.setPreferredSize(new Dimension(80, 30));
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

    private void buildAddButton() {
        addItemButton = new JButton("Add item");
        bp = new JPanel(new BorderLayout());
        bp.add(addItemButton);
        addItemButton.addActionListener(addItem);
    }

    private void addListItem(String todo, DayOfWeek day) {
        JCheckBox chk = new JCheckBox(todo);
        checklistModel.add(day, new Item(todo, day, chk));

        if (day.equals(checklistModel.getCurrDayOfWeek())) {
            list.add(chk);
            list.revalidate();
        }
    }

    private void editList() {
        Component[] cList = lp.getComponents();
        for (Component c : cList) {
            if (c instanceof JPanel) {
                lp.remove(c);
            }
        }

        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                JTextField field = new JTextField(l.get(i).getText());
                field.setMaximumSize(new Dimension(panel.getWidth(), l.get(i).getCheckbox().getHeight()));
                list.add(field);
            }
        }
        lp.add(list);
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(editingList);
        bp.removeAll();
        bp.add(doneButton);
        
        panel.revalidate();
        panel.repaint();
        
        
    }

    private void updateList() {
        Component[] cList = lp.getComponents();
        for (Component c : cList) {
            if (c instanceof JPanel) {
                lp.remove(c);
            }
        }

        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                list.add(l.get(i).getCheckbox());
            }
        }
        lp.add(list);
        panel.revalidate();
        panel.repaint();
    }

    public gui() {
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        buildWindow();
        
        buildMenu();
        buildDate();
        buildChecklist();
        buildAddButton();

        
        //panel.add(mb);
        panel.add(date);
        panel.add(lp);
        panel.add(bp);
        
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (Frame frame : Frame.getFrames()) {
            updateLAFRecursively(frame);
        }
        frame.revalidate();
        frame.repaint();
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
            String todo = JOptionPane.showInputDialog(panel, "Input an item", null);
            
            if (todo != null && todo.trim().length() != 0) {
                int day = JOptionPane.showOptionDialog(panel, "Select a day:", "Which day?", 0, 3, null, options, options[0]);
                addListItem(todo, DayOfWeek.of(day + 1)); 
            }
            else {
                JOptionPane.showMessageDialog(panel, "Error: Item is empty", "Error", 0);
            }
        }
    };

    private ActionListener editItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            editList();
        }
    };

    private ActionListener editingList = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component[] cList = lp.getComponents();
            for (Component c : cList) {
                if (c instanceof JPanel) {
                    lp.remove(c);
                }
            }
            List<Item> l = checklistModel.getList();
            for (int i = 0; i < l.size(); i++) {
                if (list.getComponent(i) instanceof JTextField) {
                    l.get(i).setCheckbox(new JCheckBox(((JTextField) list.getComponent(i)).getText()));
                }
            }
            updateList();
            bp.removeAll();
            bp.add(addItemButton);
        }
    };

    private ActionListener decrementDate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            checklistModel.decrementDay();
            day.setText(checklistModel.getCurrDayOfWeek().toString());
            
            updateList();
        }
    };

    private ActionListener incrementDate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            checklistModel.incrementDay();
            day.setText(checklistModel.getCurrDayOfWeek().toString());

            updateList();
        }
    };

    private ActionListener toggleDark = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (mi3.getState()) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                }
                else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                for (Frame frame : Frame.getFrames()) {
                    updateLAFRecursively(frame);
                }
                frame.revalidate();
                frame.repaint();
            } catch (UnsupportedLookAndFeelException er) {
                // TODO Auto-generated catch block
                er.printStackTrace();
            }
        }
    };

    public static void updateLAFRecursively(Window window) {
        for (Window child : window.getOwnedWindows()) {
            updateLAFRecursively(child);
        }
        SwingUtilities.updateComponentTreeUI(window);
    }
}
