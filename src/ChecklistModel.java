import javax.swing.AbstractListModel;
import java.time.DayOfWeek;
import java.util.List;

public class ChecklistModel extends AbstractListModel<Item> {
    private Checklist list;
    private DayOfWeek curr;

    public ChecklistModel(Checklist list) {
        this.list = list;
    }

    public void setDay(DayOfWeek _curr) {
        curr = _curr;
    }

    public DayOfWeek getCurrDayOfWeek() {
        return curr;
    }

    public void decrementDay() {
        curr = curr.minus(1);
    }
    
    public void incrementDay() {
        curr = curr.plus(1);
    }

    public void add(DayOfWeek day, Item item) {
        this.list.add(day, item);
        this.fireContentsChanged(this, getSize() - 1, getSize() - 1);
    }

    public void remove(int index) {
        this.list.getList(curr).remove(index);
    }

    public void updateTheme() {
        for (int i = 1; i <= DayOfWeek.values().length; i++) {
            List<Item> l = this.list.getList(DayOfWeek.of(i));
            if (l != null) {
                for (int j = 0; j < l.size(); j++) {
                    l.get(j).getCheckbox().updateUI();
                }
            }
        }
    }

    public List<Item> getList() {
        return this.list.getList(curr);
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
