import java.time.LocalDate;

import javax.swing.JCheckBox;

public class Item {
    private String text;
    private LocalDate date;
    private JCheckBox checkbox;

    public Item(String _text, LocalDate _date, JCheckBox _checkbox) {
        text = _text;
        checkbox = _checkbox;
        date = _date;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public JCheckBox getCheckbox() {
        return checkbox;
    }

    public void setText(String _text) {
        text = _text;
    }

    public void setDate(LocalDate _date) {
        date = _date;
    }

    public void setCheckbox(JCheckBox _checkbox) {
        checkbox = _checkbox;
    }
}
