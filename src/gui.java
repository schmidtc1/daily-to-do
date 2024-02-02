import java.awt.BorderLayout;

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
        JMenuItem mi1 = new JMenuItem("Open");
        JMenuItem mi2 = new JMenuItem("Save as");
        m1.add(mi1);
        m2.add(mi2);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(10);
        JButton send = new JButton("Send");
        JButton reset = new JButton("Reset");
        panel.add(label);
        panel.add(tf);
        panel.add(send);
        panel.add(reset);

        JTextArea ta = new JTextArea();

        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.getContentPane().add(mb, BorderLayout.NORTH);
        frame.getContentPane().add(ta, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
