package hr.eestec_zg.file.repository.client.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

public class FileClientException extends Exception {

    private final HttpStatus httpStatus;

    public FileClientException(HttpStatusCodeException e) {
        super(e);

        this.httpStatus = e.getStatusCode();
    }

    public FileClientException(ResourceAccessException e) {
        super(e);

        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String toString() {
        return "FileClientException{" +
                "httpStatus=" + httpStatus +
                "} ";
    }
}
