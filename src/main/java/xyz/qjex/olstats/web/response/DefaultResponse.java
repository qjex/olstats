package xyz.qjex.olstats.web.response;

/**
 * Created by qjex on 8/17/16.
 */
public class DefaultResponse {

    private Object data;

    public DefaultResponse(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
