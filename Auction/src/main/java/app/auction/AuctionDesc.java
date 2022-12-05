package app.auction;

import lombok.Value;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Value
public class AuctionDesc implements Serializable, Comparable<AuctionDesc>{
    String auctionId;
    String itemId;
    String itemName;
    Double startingPrice;
    Double buyNowPrice;
    Double currentBid;
    String bidderId;
    Date startTime;
    Date expireTime;
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
        String time_to_end = expireTime.replace("T", " ");
        String time_to_start = startTime.replace("T", " ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date start_time = null, end_time = null;
        try{
            start_time = dateFormat.parse(time_to_start);
            end_time = dateFormat.parse(time_to_end);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.startTime = start_time;
        this.expireTime = end_time;
        this.sellerId = sellerId;
    }

    @Override
    public int compareTo(AuctionDesc ad){
        if (this.expireTime == null || ad.getExpireTime() == null){
            return 0;
        }
        return expireTime.compareTo(ad.getExpireTime());
    }
}