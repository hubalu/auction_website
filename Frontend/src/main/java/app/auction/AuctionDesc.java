package app.auction;

import lombok.Value;

import java.io.Serializable;

@Value
public class AuctionDesc implements Serializable {
    String auctionId;
    String itemId;
    String itemName;
    Double startingPrice;
    Double buyNowPrice;
    Double currentBid;
    String bidderId;
    String startTime;
    String expireTime;
    String sellerId;

    public AuctionDesc(String auctionId, String itemId, String itemName, Double startingPrice,
                       Double buyNowPrice, Double currentBid, String bidderId,
                       String startTime, String expireTime, String sellerId) {
        this.auctionId = auctionId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.buyNowPrice = buyNowPrice;
        this.startingPrice = startingPrice;
        this.currentBid = currentBid;
        this.bidderId = bidderId;
        this.startTime = startTime;
        this.expireTime = expireTime;
        this.sellerId = sellerId;
    }
}