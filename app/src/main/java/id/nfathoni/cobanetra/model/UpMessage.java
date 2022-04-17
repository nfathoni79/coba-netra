package id.nfathoni.cobanetra.model;

public class UpMessage {

    private String id;
    private String message;
    private int status;

    public UpMessage(String id, String message, int status) {
        this.id = id;
        this.message = message;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
