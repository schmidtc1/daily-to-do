import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Checklist {
    // private List<Item> list = new LinkedList<Item>();

    private HashMap<DayOfWeek, List<Item>> map = new HashMap<DayOfWeek, List<Item>>();
    
    public void add(DayOfWeek day, Item item) {
        if (this.map.containsKey(day)) {
            if (this.map.get(day).isEmpty()) {
                List<Item> list = new LinkedList<Item>();
                list.add(item);
                this.map.put(day, list);
            }
            else {
                this.map.get(day).add(item);
            }
        }
        else {
            List<Item> list = new LinkedList<Item>();
            list.add(item);
            this.map.put(day, list);
        }
    }

    public int size() {
        return this.map.size();
    }
    
    public Item get(DayOfWeek day, int index) {
        return this.map.get(day).get(index);
    }
}
