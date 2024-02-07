import javax.swing.AbstractListModel;
import java.time.DayOfWeek;

public class ChecklistModel extends AbstractListModel<Item> {
    private Checklist list;
    private DayOfWeek curr;

    public ChecklistModel(Checklist list) {
        this.list = list;
    }

    public void setDay(DayOfWeek _curr) {
        curr = _curr;
    }

    public void add(DayOfWeek day, Item item) {
        this.list.add(day, item);
        this.fireContentsChanged(this, getSize() - 1, getSize() - 1);
    }

    @Override
    public int getSize() {
        return this.list.size();
    }

    @Override
    public Item getElementAt(int index) {
        return this.list.get(curr, index);
    }


}
