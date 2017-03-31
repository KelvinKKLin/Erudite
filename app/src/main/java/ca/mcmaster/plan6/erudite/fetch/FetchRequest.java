package ca.mcmaster.plan6.erudite.fetch;

public class FetchRequest {
    private String url,
                   payload,
                   authToken;

    public FetchRequest(String url, String authToken, String payload) {
        this.url = url;
        this.payload = (payload != null) ? payload : null;
        this.authToken = (payload != null) ? authToken : null;
    }

    public String getUrl() {
        return url;
    }

    public String getPayload() {
        return payload;
    }

    public String getAuthToken() {
        return authToken;
    }
}
