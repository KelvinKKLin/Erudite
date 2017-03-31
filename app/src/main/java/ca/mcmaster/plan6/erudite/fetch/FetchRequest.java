package ca.mcmaster.plan6.erudite.fetch;

public class FetchRequest {
    private String url,
                   payload,
                   authToken;

    public FetchRequest(String url, String payload) {
        this.url = url;
        this.payload = payload;
        this.authToken = null;
    }

    public FetchRequest(String url, String payload, String authToken) {
        this(url, payload);
        this.authToken = authToken;
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
