
#### Setting up Admin User
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
