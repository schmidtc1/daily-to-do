import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.io.*;
import java.sql.*;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;



public class gui {

    static String[] options = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    static int panelMargins = 5;
    static int topCheckMargin = 4, sideCheckMargin = 6;
    static int frameWidth = 400, frameHeight = 400;

    private JMenuBar mb;
    private JMenu m1, m2;
    private JMenuItem mi1, mi2, mi3;
    private JCheckBoxMenuItem mi4;
    private JButton addItemButton, doneButton, deleteButton;

    private boolean editMode = false, deleteMode = false;

    private JFrame frame;
    
    private JLabel day;
    private DayOfWeek today = LocalDate.now().getDayOfWeek();

    private JPanel panel, date, lp, list, bp;

    private Checklist checklist = new Checklist();
    private ChecklistModel checklistModel = new ChecklistModel(this.checklist);

    

    private void buildWindow() {
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
    }

    private void buildMenu() {
        mb = new JMenuBar();
        mb.setMaximumSize(new Dimension(frameWidth, 30));
        m1 = new JMenu("File");
        m2 = new JMenu("Theme");
        mb.add(m1);
        mb.add(m2);
        mi1 = new JMenuItem("Add item");
        mi2 = new JMenuItem("Edit item");
        mi3 = new JMenuItem("Delete item");
        mi4 = new JCheckBoxMenuItem("Dark mode", false);
        
        
        mi1.addActionListener(addItem);
        mi2.addActionListener(editItem);
        mi3.addActionListener(deleteItem);

        mi4.addActionListener(toggleDark);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m2.add(mi4);

        frame.setJMenuBar(mb);
    }

    private void buildDate() {
        date = new JPanel();
        date.setMaximumSize(new Dimension(frameWidth, 40));
        date.setBorder(BorderFactory.createEmptyBorder(0, 0, panelMargins, 0));

        
        if (checklistModel.getCurrDayOfWeek() == null) checklistModel.setDay(today);
        day = new JLabel(checklistModel.getCurrDayOfWeek().toString(), SwingConstants.CENTER);
        day.setPreferredSize(new Dimension(100, 30));
        day.setMinimumSize(new Dimension(100, 30));
        day.setMaximumSize(new Dimension(100, 30));


        JButton prev = new JButton("←");
        prev.addActionListener(decrementDate);
        prev.setPreferredSize(new Dimension(80, 30));
        prev.setBorder(BorderFactory.createLineBorder(Color.black));
        JButton next = new JButton("→");
        next.addActionListener(incrementDate);
        next.setPreferredSize(new Dimension(80, 30));
        next.setBorder(BorderFactory.createLineBorder(Color.black));
        date.add(prev);
        date.add(day);
        date.add(next);
    }

    private void buildChecklist() {
        lp = new JPanel();
        lp.setLayout(new BorderLayout());
        lp.setBorder(BorderFactory.createEmptyBorder(panelMargins, panelMargins, panelMargins, panelMargins));
        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
        list.setBorder(BorderFactory.createLineBorder(Color.black));

        lp.add(list, BorderLayout.CENTER);
        list.setPreferredSize(new Dimension(frameWidth, 330));

    }

    private void buildAddButton() {
        addItemButton = new JButton("Add item");
        //addItemButton.setBackground(new Color(0, 100, 0));
        addItemButton.setBorder(BorderFactory.createLineBorder(Color.black));

        bp = new JPanel(new BorderLayout());
        bp.setBorder(BorderFactory.createEmptyBorder(panelMargins, panelMargins, 0, panelMargins));
        bp.add(addItemButton);
        bp.setPreferredSize(new Dimension(frameWidth - 40, 50));
        addItemButton.addActionListener(addItem);
    }

    private void addListItem(String todo, DayOfWeek day) {
        JCheckBox chk = new JCheckBox(todo);
        chk.setSize(frameWidth - 40, 20);
        chk.setMargin(new Insets(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
        checklistModel.add(day, new Item(todo, day, chk));

        if (day.equals(checklistModel.getCurrDayOfWeek())) {
            list.add(chk);
            list.revalidate();
        }
    }

    private void editList() {
        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                JTextField field = new JTextField(l.get(i).getText());
                field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                list.add(field);
            }
        }
        lp.add(list);
        doneButton = new JButton("Done");
        doneButton.setBackground(new Color(35, 127, 183));
        doneButton.addActionListener(doneEditing);

        addItemButton.setVisible(false);
        bp.add(doneButton);
        
        panel.revalidate();
        panel.repaint();
        
        editMode = true;
    }

    private void deleteList() {
        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                JTextField field = new JTextField(l.get(i).getText());
                field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                field.setEditable(false);
                JCheckBox del = new JCheckBox();
                JPanel delPanel = new JPanel();
                delPanel.setLayout(new BoxLayout(delPanel, BoxLayout.LINE_AXIS));
                delPanel.add(del);
                delPanel.add(field);
                list.add(delPanel);
            }
        }
        lp.add(list);
        deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(210, 43, 43));
        deleteButton.addActionListener(doneDeleting);

        addItemButton.setVisible(false);
        bp.add(deleteButton);
        
        panel.revalidate();
        panel.repaint();
        
        deleteMode = true;
    }

    private void updateList() {
        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            if (editMode) {
                for (int i = 0; i < l.size(); i++) {
                    JTextField field = new JTextField(l.get(i).getText());
                    field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                    list.add(field);
                }
            }
            else if (deleteMode) {
                for (int i = 0; i < l.size(); i++) {
                    JTextField field = new JTextField(l.get(i).getText());
                    field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                    field.setEditable(false);
                    JCheckBox del = new JCheckBox();
                    JPanel delPanel = new JPanel();
                    delPanel.setLayout(new BoxLayout(delPanel, BoxLayout.LINE_AXIS));
                    delPanel.add(del);
                    delPanel.add(field);
                    list.add(delPanel);
                }
            }
            else {
                for (int i = 0; i < l.size(); i++) {
                    list.add(l.get(i).getCheckbox());
                }
            }
        }
        lp.add(list);
        panel.revalidate();
        panel.repaint();
    }

    private void writeFile() {
        FileWriter save;
        try {
            save = new FileWriter("sav/sav.txt");
            BufferedWriter buffer = new BufferedWriter(save);
            for (int i = 1; i <= DayOfWeek.values().length; i++) {
                List<Item> l = checklistModel.getListByDay(DayOfWeek.of(i));
                if (l != null) {
                    for (int j = 0; j < l.size(); j++) {
                        String text = l.get(j).getText();
                        boolean checked = l.get(j).getCheckbox().isSelected();
                        buffer.write(Integer.toString(i));
                        buffer.newLine();
                        buffer.write(text);
                        buffer.newLine();
                        if (checked) buffer.write("TRUE");
                        else buffer.write("FALSE");
                        buffer.newLine();
                    }
                }
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }

    /*
     * SAVE FILE STRUCTURE
     * 
     * DAY
     * String - Item text
     * Boolean - Item checked or not
     * repeat for every item in the list
     * repeat for every day of the week
     */

     private void readFile() {
        FileReader save;
        try {
            save = new FileReader("sav/sav.txt");
            if (save != null) {
                BufferedReader buffer = new BufferedReader(save);
                
                String day = buffer.readLine();
                while (day != null) {
                    String text = buffer.readLine();
                    boolean checked = Boolean.valueOf(buffer.readLine());
                    JCheckBox chk = new JCheckBox(text);
                    chk.setSize(frameWidth - 40, 20);
                    chk.setMargin(new Insets(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                    chk.setSelected(checked);
                    checklistModel.add(DayOfWeek.of(Integer.parseInt(day)), new Item(text, DayOfWeek.of(Integer.parseInt(day)), chk));
                    day = buffer.readLine();
                }
                buffer.close();
                updateList();
            }
        } catch (IOException e) {
            System.out.println("File not found, no save data will be loaded.");
        }
     }

     private void writeDB() {
        Connection conn = null;
        // String query = "CREATE TABLE IF NOT EXISTS checklist (\n" 
        //     + " id integer PRIMARY KEY,\n"
        //     + " name text NOT NULL\n" 
        //     + ");";
        String drop = "DROP TABLE IF EXISTS checklist;";
        String query = "CREATE TABLE IF NOT EXISTS checklist (\n"
            + " day INTEGER CHECK (day > 0 AND day < 8),\n"
            + " name TEXT,\n"
            + " checked BOOLEAN NOT NULL CHECK (checked IN (0, 1)),"
            + " PRIMARY KEY (day, name));";
        String insert = "INSERT INTO checklist(day, name, checked) VALUES(?, ?, ?)";
        try {
            // Get a connection to database
            conn = DriverManager.getConnection("jdbc:sqlite:sav/sav.db");
            // Create a statement
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(drop);
            stmt.execute(query);
            PreparedStatement pstmt = conn.prepareStatement(insert);
            // Execute SQLite query
            
            // Process the result set

            for (int i = 1; i <= DayOfWeek.values().length; i++) {
                List<Item> l = checklistModel.getListByDay(DayOfWeek.of(i));
                if (l != null) {
                    for (int j = 0; j < l.size(); j++) {
                        String text = l.get(j).getText();
                        boolean checked = l.get(j).getCheckbox().isSelected();
                        pstmt.setInt(1, i);
                        pstmt.setString(2, text);
                        pstmt.setBoolean(3, checked);
                        pstmt.executeUpdate();
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
     }

     private void readDB() {
        Connection conn = null;
        // String query = "CREATE TABLE IF NOT EXISTS checklist (\n" 
        //     + " id integer PRIMARY KEY,\n"
        //     + " name text NOT NULL\n" 
        //     + ");";
        String query = "SELECT day, name, checked FROM checklist;";
        try {
            // Get a connection to database
            conn = DriverManager.getConnection("jdbc:sqlite:sav/sav.db");
            // Create a statement
            Statement stmt = conn.createStatement();
            // Execute SQLite query
            ResultSet res = stmt.executeQuery(query);
            // Process the result set
            while (res.next()) {
                System.out.println(res.getInt("day") + ", " + res.getString("name") + ", " + res.getBoolean("checked"));
                int day = res.getInt("day");
                String text = res.getString("name");
                boolean checked = res.getBoolean("checked");
                JCheckBox chk = new JCheckBox(text);
                chk.setSize(frameWidth - 40, 20);
                chk.setMargin(new Insets(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                chk.setSelected(checked);
                checklistModel.add(DayOfWeek.of(day), new Item(text, DayOfWeek.of(day), chk));
            }
            updateList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
     }

    public gui() {
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(panelMargins, panelMargins, panelMargins, panelMargins));
        buildWindow();
        
        buildMenu();
        buildDate();
        buildChecklist();
        buildAddButton();

        
        //panel.add(mb);
        panel.add(date);
        panel.add(lp);
        panel.add(bp);

        //readFile();
        //writeDB();
        readDB();
        
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
        // try {
        //     // The newInstance() call is a work around for some
        //     // broken Java implementations
        //     Class.forName("com.sqlite.JDBC");
        //   } catch (Exception ex) {
        //     // handle the error
        //     ex.printStackTrace();
        //   }

        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    gui g = new gui();
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            // g.writeFile();
                            g.writeDB();
                        }
                    }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private ActionListener addItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (editMode) {
                JOptionPane.showMessageDialog(panel, "Cannot add items while editing them.", "Error", 0);
            }
            else if (deleteMode) {
                JOptionPane.showMessageDialog(panel, "Cannot add items while deleting them", "Error", 0);
            }
            else {
                String todo = JOptionPane.showInputDialog(panel, "Input an item", null);
                
                if (todo != null && todo.trim().length() != 0) {
                    int day = JOptionPane.showOptionDialog(panel, "Select a day:", "Which day?", 0, 3, null, options, options[0]);
                    addListItem(todo, DayOfWeek.of(day + 1)); 
                }
                else {
                    JOptionPane.showMessageDialog(panel, "Error: Item is empty", "Error", 0);
                }
            }
        }
    };

    private ActionListener editItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (editMode) {
                for (ActionListener a : doneButton.getActionListeners()) {
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
            else if (deleteMode) {
                JOptionPane.showMessageDialog(panel, "Cannot edit items while deleting them.", "Error", 0);
            }
            else {
                editList();
            }
        }
    };
    private ActionListener deleteItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (deleteMode) {
                for (ActionListener a : deleteButton.getActionListeners()) {
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
            else if (editMode) {
                JOptionPane.showMessageDialog(panel, "Cannot delete items while editing them.", "Error", 0);
            }
            else {
                deleteList();
            }
        }
    };

    private ActionListener doneEditing = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Item> l = checklistModel.getList();
            if (l != null) {
                for (int i = 0; i < l.size(); i++) {
                    if (list.getComponent(i) instanceof JTextField) {
                        l.get(i).getCheckbox().setText(((JTextField) list.getComponent(i)).getText());

                    }
                }
            }
            editMode = false;
            updateList();
            bp.removeAll();
            bp.add(addItemButton);
            addItemButton.setVisible(true);
        }
    };

    private ActionListener doneDeleting = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Item> l = checklistModel.getList();
            if (l != null) {
                for (int i = 0; i < l.size(); i++) {
                    if (list.getComponent(i) instanceof JPanel) {
                        for (Component c : ((JPanel) list.getComponent(i)).getComponents()) {
                            if (c instanceof JCheckBox) {
                                if (((JCheckBox) c).isSelected()) {
                                    checklistModel.remove(i);
                                    i--;
                                }
                            }
                        }
                    }
                }
            }
            deleteMode = false;
            updateList();
            bp.removeAll();
            bp.add(addItemButton);
            addItemButton.setVisible(true);
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
                if (mi4.getState()) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                }
                else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                for (Frame frame : Frame.getFrames()) {
                    updateLAFRecursively(frame);
                }

                checklistModel.updateTheme();

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
