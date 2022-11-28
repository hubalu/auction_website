package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.example.notification.EmailService;
import com.google.gson.*;

import java.nio.charset.StandardCharsets;

public class Main {

    private final static String QUEUE_NAME = "auction";
    public static void main(String[] args){
        // Recipient's email ID needs to be mentioned.
        EmailService emailService = new EmailService();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try{
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
                JsonObject jsonObj = JsonParser.parseString(message).getAsJsonObject();
                String type = jsonObj.get("type").getAsString();
                String receiver = jsonObj.get("email").getAsString();
                String user_id = jsonObj.get("user_id").getAsString();
                switch (type) {
                    case "new bid" -> {
                        String auction_id = jsonObj.get("auction_id").getAsString();
                        emailService.send_email(receiver, "New bid on your auction: " + auction_id,
                                "Dear customer " + user_id + ", there is a new bid from others on the auction: "
                                        + auction_id + ". Give us a higher price or go away, thank you.");

                        break;
                    }
                    case "alert one day" -> {
                        String auction_id = jsonObj.get("auction_id").getAsString();
                        emailService.send_email(receiver, "Alert! 24 hours before the auction expiration: " + auction_id, "Dear customer " + user_id + ", the auction: "
                                + auction_id + "is gonna end soon. Please bear in mind that anything could happen, have a good one.");

                        break;
                    }
                    case "alert one hour" -> {
                        String auction_id = jsonObj.get("auction_id").getAsString();
                        emailService.send_email(receiver, "Alert! One hour before the auction expiration: " + auction_id, "Dear customer " + user_id + ", the auction: "
                                + auction_id + "is gonna end very soon. Please bear in mind that anything could happen, have a good one.");

                        break;
                    }
                    default -> throw new RuntimeException("illegal json object content");
                }
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}