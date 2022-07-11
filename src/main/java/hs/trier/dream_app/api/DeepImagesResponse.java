package hs.trier.dream_app.api;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
final class DeepImagesResponse {
    private final String message;
    private final String status;

    public DeepImagesResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String message() {
        return message;
    }

    public String status() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DeepImagesResponse) obj;
        return Objects.equals(this.message, that.message) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, status);
    }

    @Override
    public String toString() {
        return "DeepImagesResponse[" +
                "message=" + message + ", " +
                "status=" + status + ']';
    }

}
