import javax.swing.AbstractListModel;

public class ChecklistModel extends AbstractListModel<Item> {
    private Checklist list;

    public ChecklistModel(Checklist list) {
        this.list = list;
    }

    public void add(Item item) {
        this.list.add(item);
        this.fireContentsChanged(this, getSize() - 1, getSize() - 1);
    }

    @Override
    public int getSize() {
        return this.list.size();
    }

    @Override
    public Item getElementAt(int index) {
        return this.list.at(index);
    }


}
