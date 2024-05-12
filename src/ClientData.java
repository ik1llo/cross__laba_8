import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "ClientData")
public class ClientData implements Serializable {
    @XmlElement
    protected String first_name;
    @XmlElement
    protected String last_name;

    public ClientData() {}

    public ClientData(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }
}