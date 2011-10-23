package net.mindengine.jeremy.client;

public class HttpResponse {
    
    private int status;
    private String content;
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
