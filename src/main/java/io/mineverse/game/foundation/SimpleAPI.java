package io.mineverse.game.foundation;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;

public class SimpleAPI {

    protected String endpoint;

    protected String bearer;

    public SimpleAPI(String endpoint, String bearerToken) {
        this.endpoint = StringUtils.stripEnd(endpoint, "/");
        this.bearer = bearerToken;
    }

    protected String join(String uri) {
        return this.endpoint + "/" + StringUtils.strip(uri, "/");
    }

    public Request get(String uri) {
        return Request.Get(join(uri)).addHeader("Authorization", "Bearer " + bearer);
    }

    public Request post(String uri) {
        return Request.Post(join(uri)).addHeader("Authorization", "Bearer " + bearer);
    }

    public Request put(String uri) {
        return Request.Put(join(uri)).addHeader("Authorization", "Bearer " + bearer);
    }

    public Request delete(String uri) {
        return Request.Delete(join(uri)).addHeader("Authorization", "Bearer " + bearer);
    }

    public Request patch(String uri) {
        return Request.Patch(join(uri)).addHeader("Authorization", "Bearer " + bearer);
    }

    public Request head(String uri) {
        return Request.Head(join(uri)).addHeader("Authorization", "Bearer " + bearer);
    }

    public Request options(String uri) {
        return Request.Options(join(uri)).addHeader("Authorization", "Bearer " + bearer);
    }
}
