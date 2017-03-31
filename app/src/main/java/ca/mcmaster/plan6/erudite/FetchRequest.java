package ca.mcmaster.plan6.erudite;

public class FetchRequest {
    private String url,
                   payload;

    public FetchRequest(String url, String payload) {
        this.url = url;
        this.payload = payload;
    }

    public String getUrl() {
        return url;
    }

    public String getPayload() {
        return payload;
    }
}
