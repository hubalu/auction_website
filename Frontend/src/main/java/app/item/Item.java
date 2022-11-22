package app.item;

import lombok.Value;

@Value
public class Item {
    String ItemID;
    String ItemName;
    String Description;
    String Category;

    public Item(String id, String name,String description, String category){
        this.ItemID = id;
        this.ItemName = name;
        this.Description = description;
        this.Category = category;
    }
}
