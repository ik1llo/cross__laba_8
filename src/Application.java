import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Application {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(7080);
            registry.rebind("remote", new ServiceServer());
        } catch (Exception e) { e.printStackTrace(); }
    }
}
