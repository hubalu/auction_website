package app.item;

import lombok.Value;

import java.io.Serializable;

@Value
public class Item implements Serializable {
    String ItemID;
    String UserID;
    String ItemName;
    String Description;
    String Category;

    String Flag;

    String UploadTime;

    public Item(String ItemId, String UserId, String name,String description, String category, boolean flag, String UploadTime){
        this.ItemID = ItemId;
        this.UserID = UserId;
        this.ItemName = name;
        this.Description = description;
        this.Category = category;
        this.Flag = flag ? "warning: inappropriate item!" : "healthy item";
        this.UploadTime = UploadTime;
    }
}
