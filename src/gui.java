import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;

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
    private ImageIcon deleteIcon = createImageIcon("./img/delete.png", "Delete icon");
    private ImageIcon editIcon = createImageIcon("./img/edit.png", "Edit icon");


    private boolean editMode = false, deleteMode = false;

    private JFrame frame;
    
    private JLabel day;
    private DayOfWeek today = LocalDate.now().getDayOfWeek();

    private JPanel panel, date, lp, list, bp;
    private JScrollPane scrollPane;

    private Checklist checklist = new Checklist();
    private ChecklistModel checklistModel = new ChecklistModel(this.checklist);

    

    private void buildWindow() {
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
    }

    /* Builds menu, attached to JFrame, two buttons for File and Theme; File contains more options */
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

    /* Builds date panel, contains left and right buttons with day in the middle */
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

    /* Builds checklist panel, middle of screen
     * Structure of this: lp surrounds list, items are added to list
     *   scrollpane surrounds lp, as items are added to list, lp expands, which causes scrollbar to appear
    */
    private void buildChecklist() {
        lp = new JPanel();
        lp.setLayout(new BorderLayout());

        list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
        
        lp.add(list, BorderLayout.CENTER);

        scrollPane = new JScrollPane(lp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new CompoundBorder(
            BorderFactory.createEmptyBorder(panelMargins, panelMargins, panelMargins, panelMargins), 
            BorderFactory.createLineBorder(Color.black)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(frameHeight / (panelMargins * 5));
        scrollPane.setPreferredSize(new Dimension(frameWidth, 260));
        scrollPane.setMinimumSize(new Dimension(frameWidth, 260));
        scrollPane.setMaximumSize(new Dimension(frameWidth, 260));


    }

    /* Builds the Add button below the checklist which is used to add items
     * Replaced during editMode and deleteMode by Done button
     */
    private void buildAddButton() {
        addItemButton = new JButton("Add item");
        addItemButton.setBorder(BorderFactory.createLineBorder(Color.black));
        addItemButton.addActionListener(addItem);

        bp = new JPanel(new BorderLayout());
        bp.setBorder(BorderFactory.createEmptyBorder(panelMargins, panelMargins, 0, panelMargins));
        bp.add(addItemButton);
        bp.setPreferredSize(new Dimension(frameWidth - 40, 80));
    }

    /* Function for adding a checklist item to checklistModel
     * Checks if the item day matches the current day of the UI, if so: display it
     */
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

    /* Function for changing the checklist items to editMode
     * editMode: changes the icon of the checkbox to an 'edit' icon. Creates a field to the right of this icon
     * Attempts to maintain the spacing of the normal checklist item, but may have some marginal differences
     */
    private void editList() {
        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                JTextField field = new JTextField(l.get(i).getText());
                field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                field.setMargin(new Insets(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                field.setEditable(false);
                field.setBorder(BorderFactory.createEmptyBorder());
                
                JButton editB = new JButton(new ImageIcon(editIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
                editB.setBorder(BorderFactory.createEmptyBorder(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                editB.setContentAreaFilled(false);
                editB.addActionListener(edit);
                editB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                JPanel editPanel = new JPanel();
                editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.LINE_AXIS));
                editPanel.add(editB);
                editPanel.add(field);
                list.add(editPanel);
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

    /* Function for changing the checklist items to deleteMode
     * deleteMode: changes the icon of the checkbox to a 'delete' icon. Creates a field to the right of this icon
     * Attempts to maintain the spacing of the normal checklist item, but may have some marginal differences
     */
    private void deleteList() {
        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                JTextField field = new JTextField(l.get(i).getText());
                field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                field.setMargin(new Insets(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                field.setEditable(false);
                field.setBorder(BorderFactory.createEmptyBorder());
                
                JButton del = new JButton(new ImageIcon(deleteIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
                del.setBorder(BorderFactory.createEmptyBorder(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                del.setContentAreaFilled(false);
                del.addActionListener(delete);
                del.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                JPanel delPanel = new JPanel();
                delPanel.setLayout(new BoxLayout(delPanel, BoxLayout.LINE_AXIS));
                delPanel.add(del);
                delPanel.add(field);
                list.add(delPanel);
            }
        }
        lp.add(list);
        deleteButton = new JButton("Done");
        deleteButton.setBackground(new Color(210, 43, 43));
        deleteButton.addActionListener(doneDeleting);

        addItemButton.setVisible(false);
        bp.add(deleteButton);
        
        panel.revalidate();
        panel.repaint();
        
        deleteMode = true;
    }

    /* Function for updating the UI in three cases
     * 1. Day of the week is switched
     * 2. Exiting edit/delete mode
     * 3. Importing data from the SQL db
     */
    private void updateList() {
        list.removeAll();
        List<Item> l = checklistModel.getList();
        if (l != null) {
            if (editMode) {
                for (int i = 0; i < l.size(); i++) {
                    JTextField field = new JTextField(l.get(i).getText());
                    field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                    field.setMargin(new Insets(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                    field.setEditable(false);
                    field.setBorder(BorderFactory.createEmptyBorder());

                    JButton editB = new JButton(new ImageIcon(editIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
                    editB.setBorder(BorderFactory.createEmptyBorder(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                    editB.setContentAreaFilled(false);
                    editB.addActionListener(edit);
                    editB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    
                    JPanel editPanel = new JPanel();
                    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.LINE_AXIS));
                    editPanel.add(editB);
                    editPanel.add(field);
                    list.add(editPanel);
                }
            }
            else if (deleteMode) {
                for (int i = 0; i < l.size(); i++) {
                    JTextField field = new JTextField(l.get(i).getText());
                    field.setMaximumSize(new Dimension(frameWidth, l.get(i).getCheckbox().getHeight()));
                    field.setMargin(new Insets(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                    field.setEditable(false);
                    field.setBorder(BorderFactory.createEmptyBorder());

                    JButton del = new JButton(new ImageIcon(deleteIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
                    del.setBorder(BorderFactory.createEmptyBorder(topCheckMargin, sideCheckMargin, topCheckMargin, sideCheckMargin));
                    del.setContentAreaFilled(false);
                    del.addActionListener(delete);
                    
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

    /* Function called when the program is closed
     * Overwrites current table with a new one
     * Saves day, item name (contents), and whether it is checked
     */
    private void writeDB() {
        Connection conn = null;
        // Initialize statement strings
        /* PROBLEM:
            * Currently using DROP TABLE as a crutch to update/overwrite checklist with no duplicates.
            * Think of inputting some sort of ID for each checklist item instead.
            */
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

     /* Function for reading any save data that may exist already and importing it into the program */
    private void readDB() {
        Connection conn = null;
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

    /* Initiates the program, builds the GUI using helper functions and other properties */
    public gui() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(panelMargins, panelMargins, panelMargins, panelMargins));
        
        buildWindow();
        buildMenu();
        buildDate();
        buildChecklist();
        buildAddButton();
        
        /* GridBagConstraints used for the layout 
         * gridwidth/gridheight specifies how much space the component takes up on the grid
         * weightx/y specifies how much of the row/column the component should take up
         * gridx/y specifies which position on the grid to place the component
         * fill specifies which way to fill the grid
         * ipady specifies size of a component
         * anchor specifies where to anchor the component
        */
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 3;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(date, c);
        c.weightx = 0.0;
        c.weighty = 0.5;
        c.ipady = 0;
        c.gridy = 2;
        panel.add(scrollPane, c);
        c.weighty = 0.5;
        c.ipady = 25;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        panel.add(bp, c);

        readDB();
        
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        
        /* Sets the theme of the UI to the FlatLightLaf theme by default */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        for (Frame frame : Frame.getFrames()) {
            updateLAFRecursively(frame);
        }
        checklistModel.updateTheme();
        frame.revalidate();
        frame.repaint();
    }
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    gui g = new gui();
                    /* ShutdownHook triggers when program closes */
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                        public void run() {
                            g.writeDB();
                        }
                    }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /* ActionListener for the Add item menu option
     * Asks the user for content to place on the checklist item
     */
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

    /* ActionListener for the Edit item menu option
     * Switches user to edit mode
     */
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

    /* ActionListener for the Delete item menu option
     * Switches user to delete mode
     */
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

    /* ActionListener for the done button in edit mode
     * Switches the user back to the normal mode and updates the list/add item button
     */
    private ActionListener doneEditing = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            editMode = false;
            updateList();
            bp.removeAll();
            bp.add(addItemButton);
            addItemButton.setVisible(true);
        }
    };

    /* ActionListener for the edit icon next to the checklist text
     * When the icon is clicked, the user is given a prompt to update the item content
     * The action then updates the checklist model to match if necessary
     */
    private ActionListener edit = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Item> l = checklistModel.getList();
            if (l != null) {
                JButton b = (JButton) e.getSource();
                JPanel p = (JPanel) b.getParent();
                for (int i = 0; i < l.size(); i++) {
                    if (list.getComponent(i) instanceof JPanel) {
                        if (p == list.getComponent(i)) {
                            String s = JOptionPane.showInputDialog(panel, "Update item", checklistModel.getElementAt(i).getText());
                            if (s == null) {
                                break;
                            }
                            else if (s.trim().length() == 0) {
                                JOptionPane.showMessageDialog(panel, "Error: Item is empty", "Error", 0);
                            }
                            else {
                                checklistModel.getElementAt(i).setText(s);
                                break;
                            }
                        }
                    }
                }
            }
            updateList();
        }
    };

    /* ActionListener for the done button in delete mode
     * Switches the user back to the normal mode and updates the list/add item button
     */
    private ActionListener doneDeleting = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteMode = false;
            updateList();
            bp.removeAll();
            bp.add(addItemButton);
            addItemButton.setVisible(true);
        }
    };

    /* ActionListener for the edit icon next to the checklist text
     * When the icon is clicked, the checklist item is removed from the UI and the checklist model
     */
    private ActionListener delete = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Item> l = checklistModel.getList();
            if (l != null) {
                JButton b = (JButton) e.getSource();
                JPanel p = (JPanel) b.getParent();
                for (int i = 0; i < l.size(); i++) {
                    if (list.getComponent(i) instanceof JPanel) {
                        if (p == list.getComponent(i)) {
                            checklistModel.remove(i);
                            list.remove(i);
                            break;
                        }
                    }
                }
            }
            updateList();
        }
    };

    /* ActionListener for the left arrow button
     * Decreases the day (moves left in the calendar week)
     * Gets different list depending on day of the week
     */
    private ActionListener decrementDate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            checklistModel.decrementDay();
            day.setText(checklistModel.getCurrDayOfWeek().toString());
            updateList();
        }
    };

    /* ActionListener for the right arrow button
     * Increases the day (moves right in the calendar week)
     * Gets different list depending on day of the week
     */
    private ActionListener incrementDate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            checklistModel.incrementDay();
            day.setText(checklistModel.getCurrDayOfWeek().toString());
            updateList();
        }
    };

    /* ActionListener for the Dark mode button in the theme menu option
     * Switches between light and dark mode for the UI
     * Updates components recursively based on parent frame/panel/component
     * Updates checklist items that are not currently visible/exist, but are still stored in checklist model
     *      This is because the items exist on different calendar days, 
     *      so they are not active in the window and must be manually updated
     */
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
                er.printStackTrace();
            }
        }
    };

    /* Helper function for executing recursive updating of the component tree */
    public static void updateLAFRecursively(Window window) {
        for (Window child : window.getOwnedWindows()) {
            updateLAFRecursively(child);
        }
        SwingUtilities.updateComponentTreeUI(window);
    }

    /* Helper function for creating image icons */
    protected ImageIcon createImageIcon(String path, String description) {
        if (path != null) {
            return new ImageIcon(path, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
