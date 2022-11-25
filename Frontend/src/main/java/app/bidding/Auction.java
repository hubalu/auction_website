package app.bidding;

import lombok.Value;

@Value
public class Auction {
    Integer ItemID;
    String ItemName;
    Integer AuctionID;
    Integer StartingPrice;
    Integer CurrentPrice;
    Integer BuyNowPrice;
    String StartTime;
    String EndTime;

    public Auction(int itemId, String itemName, int id, int startPrice, int currentPrice, int buyNowPrice, String startTime, String endTime){
        this.ItemID = itemId;
        this.ItemName = itemName;
        this.AuctionID = id;
        this.StartingPrice = startPrice;
        this.CurrentPrice = currentPrice;
        this.BuyNowPrice = buyNowPrice;
        this.StartTime = startTime;
        this.EndTime = endTime;
    }
}
