package app.user;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.sql.SQLException;
import java.util.List;

import app.rmiManagement.RemoteUserManagement;

public class UserManagement extends java.rmi.server.UnicastRemoteObject implements RemoteUserManagement {

    private Database db;
    int port;
    String address;
    Registry registry;

    public UserManagement(String databaseName) throws RemoteException {
        try {
            // get the address of this host.
            address = (InetAddress.getLocalHost()).toString();
        } catch (Exception e) {
            throw new RemoteException("can't get inet address.");
        }
        port = 54321;  // our port
        System.out.println("using address=" + address + ",port=" + port);
        try {
            // create the registry and bind the name and object.
            registry = LocateRegistry.createRegistry(port);
            registry.rebind("userManagement", this);
        } catch (RemoteException e) {
            throw e;
        }

        this.db = new Database(databaseName);
        this.db.createDatabase();
        this.db.createTableIfNotExists();
    }

    public boolean deleteUser(Integer id, Integer operatorId, UserType operatorRole) {
        try {
            if (operatorId.equals(id) || operatorRole == UserType.Admin) {
                this.db.deleteUser(id);
            }else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public int createUser(String username, UserType userType, String email,String phoneNumber) {
        try {
            if (this.db.checkIfUserExist(username) || this.db.checkIfEmailExist(email)){
                return -1;
            } else {
                this.db.insertIntoTable(username, userType, email, phoneNumber);
                return this.db.findId(username);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public boolean updateUser(Integer id, String username, String email,String phoneNumber) {
        try {
            this.db.updateUser(id, username, email, phoneNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean suspendUser(Integer id, UserType operatorRole) {
        if (operatorRole == UserType.Admin) {
            try {
                this.db.suspendUser(id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public List<UserInfo> getAllUser() throws RemoteException{
        try {
            return db.getAllUser();
        } catch (SQLException throwables) {
            return null;
        }
    }

    public UserInfo getOneUser(Integer id) throws RemoteException{
        try {
            return db.getOneUser(id);
        } catch (SQLException throwables) {
            return null;
        }
    }
}