import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
    Response add_user(ClientData client_data) throws RemoteException;
    Response export_users_XML(String absolute_file_path) throws RemoteException;
    Response import_users_XML(String absolute_file_path) throws RemoteException;
}