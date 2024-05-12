import java.io.Serializable;

public class Response implements Serializable {
    protected boolean ok;
    protected String message;

    public Response(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }
}
