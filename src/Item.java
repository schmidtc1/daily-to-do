import javax.swing.JCheckBox;
import java.time.DayOfWeek;

public class Item {
    private String text;
    private DayOfWeek day;
    private JCheckBox checkbox;

    public Item(String _text, DayOfWeek _day, JCheckBox _checkbox) {
        text = _text;
        checkbox = _checkbox;
        day = _day;
    }

    public String getText() {
        return text;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public JCheckBox getCheckbox() {
        return checkbox;
    }

    public void setText(String _text) {
        text = _text;
    }

    public void setDay(DayOfWeek _day) {
        day = _day;
    }

    public void setCheckbox(JCheckBox _checkbox) {
        checkbox = _checkbox;
    }
}
