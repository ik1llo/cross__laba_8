import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServiceServer extends UnicastRemoteObject implements RemoteInterface {
    private ArrayList<ClientData> clients;

    public ServiceServer() throws RemoteException {
        super();
        this.clients = new ArrayList<ClientData>();
    }

    @Override
    public Response add_user(ClientData client_data) throws RemoteException {
        try {
            for (ClientData client : this.clients)
                if (client.first_name.equals(client_data.first_name) && client.last_name.equals(client_data.last_name))
                    return new Response(false, "user already exists...");

            this.clients.add(client_data);
            return new Response(true, "");
        } catch (Exception err) { return new Response(false, err.getMessage()); }
     }

    @Override
    public Response export_users_XML(String absolute_file_path) throws RemoteException {
        try {
            JAXBContext context = JAXBContext.newInstance(JAXBArrayList.class, ClientData.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(new JAXBArrayList<>(this.clients), new File(Paths.get(absolute_file_path, "output.xml").toString()));
            return new Response(true, "");
        } catch (Exception err) { return new Response(false, err.getMessage()); }
    }

    @Override
    public Response import_users_XML(String absolute_file_path) throws RemoteException {
        try {
            File file = new File(absolute_file_path);
            if (!file.exists()) { return new Response(false, "file does not exist..."); }

            JAXBContext context = JAXBContext.newInstance(JAXBArrayList.class, ClientData.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            JAXBArrayList<ClientData> imported_data = (JAXBArrayList<ClientData>) unmarshaller.unmarshal(file);
            this.clients.addAll(imported_data.get_array_list());

            return new Response(true, "");
        } catch (Exception err) { return new Response(false, err.getMessage()); }
    }
}


@XmlRootElement(name="ArrayList")
class JAXBArrayList<T> {
    protected ArrayList<T> array_list;

    public JAXBArrayList() { this.array_list = new ArrayList<>(); }

    public JAXBArrayList(ArrayList<T> array_list) { this.array_list = array_list; }

    @XmlElement(name="Item")
    public ArrayList<T> get_array_list(){ return this.array_list; }
}
