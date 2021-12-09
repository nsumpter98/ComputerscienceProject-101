package Models;

import java.util.ArrayList;
import java.util.List;

public class ItemInformation{
    public int iD;
    public double taxRate;
    public List<Item> items;

    public ItemInformation(){
        this.items = new ArrayList<>();
    }

}
