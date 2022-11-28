package app.rmiManagement;

import app.item.Item;
import app.user.UserInfo;
import app.user.UserType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteUserManagement extends Remote {

    boolean deleteUser(Integer id, Integer operatorId, UserType operatorRole) throws RemoteException;

    int createUser(String username, UserType userType, String email,String phoneNumber) throws RemoteException;

    boolean updateUser(Integer id, String username, String email,String phoneNumber) throws RemoteException;

    boolean suspendUser(Integer id, UserType operatorRole) throws RemoteException;

    List<UserInfo> getAllUser() throws RemoteException;

    UserInfo getOneUser(Integer id) throws RemoteException;
}
