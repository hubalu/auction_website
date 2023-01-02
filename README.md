## MPCS 51205 Topics in Software Engineering Project
### Group rm -rf
### Please contact hanzeh@uchicago.edu if there is any issue with running the auction website.

### Running our application
You can simply run our Application by running `docker compose up` and all dependencies should be sorted without issue. 

### Requirements
A list of requirements could be found (here)[https://www.classes.cs.uchicago.edu/archive/2022/fall/51205-1/AuctionSiteRequirements.html]

### Design and Implementation
Each microservice have their own separate database container that they interact with, all microservices except Messenger are connected to the Frontend via Remote Procedure calss (Java RMI), messenger uses a rabbit-mq queue to fetch emails to send and auction microservice is responsible for keeping timer and pushing email messages to the rabbit-mq queue when an event occurs (1 hour & 24 hours before an auction expires if it is on your watchlist, when someone outbids you on an auction, and when you successfully bid on an auction). 

#### Setting up Admin User
There is no frontend method to set up an admin user because we don't want any user to be able to register themselves as an admin. 
To set up an admin user, please set up a user as normal, you can use any name, password, phone number and email (email must not already be used), you can use admin as you username. <br>
Please then go to your docker container named `auction_website-mongo-frontend-1` and run the following commands to update your user to an Admin user:
```
#mongosh
```

Within mongo shell:
```
> use LoginService
> db.userData.updateOne({_id: 'admin'}, {$set:{userType: 'Admin'}})
```

#### Design Issues
There was a slight issue with designing our frontend to pure REST due to limits with forms in HTML being limited to only get and post requests, so we had to create separate url for deleting and creating objects such as items and users, instead of being able to use the same url with delete and put operations.
