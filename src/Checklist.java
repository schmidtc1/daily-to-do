import java.util.LinkedList;
import java.util.List;

public class Checklist {
    private List<Item> list = new LinkedList<Item>();
    
    public void add(Item item) {
        this.list.add(item);
    }

    public int size() {
        return list.size();
    }
    
    public Item at(int index) {
        return list.get(index);
    }
}
