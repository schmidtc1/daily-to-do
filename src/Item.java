import java.time.LocalDate;

public class Item {
    private String text;
    private LocalDate date;

    public Item(String _text, LocalDate _date) {
        text = _text;
        date = _date;
    }

    public String getText() {
        return text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setText(String _text) {
        text = _text;
    }

    public void setDate(LocalDate _date) {
        date = _date;
    }
}
