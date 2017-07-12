package hr.eestec_zg.file.repository.client;

import hr.eestec_zg.file.repository.client.ex.FileClientException;
import hr.eestec_zg.file.repository.client.pojo.FileMetadataRequest;
import hr.eestec_zg.file.repository.client.resources.FileMetadataResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Collections;

public class FileRepositoryClientImpl implements FileRepositoryClient {

    private static final String API_V1_FILES_URL = "/api/v1/files";
    private static final String API_V1_FILES_METADATA_URL = "/api/v1/files/metadata";
    private static final int REST_TEMPLATE_CONNECT_TIMEOUT = 5000;

    private String username;

    private String password;

    private final String completeFilesUploadUrl;
    private final String completeFilesDownloadUrl;
    private final String completeFilesMetadataUrl;

    private RestTemplate restTemplate;

    public FileRepositoryClientImpl(String baseUrl, String username, String password) {
        this.username = username;
        this.password = password;

        this.completeFilesUploadUrl = baseUrl + API_V1_FILES_URL;
        this.completeFilesDownloadUrl = baseUrl + API_V1_FILES_URL;
        this.completeFilesMetadataUrl = baseUrl + API_V1_FILES_METADATA_URL;

        initRestTemplate();
    }

    private void initRestTemplate() {
        this.restTemplate = new RestTemplate();

        this.restTemplate.getMessageConverters().clear();
        this.restTemplate.getMessageConverters().add(new AllEncompassingFormHttpMessageConverter());
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        // need to set this so to prevent loading everything to memory
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        requestFactory.setConnectTimeout(REST_TEMPLATE_CONNECT_TIMEOUT);
        this.restTemplate.setRequestFactory(requestFactory);
    }

    @Override
    public FileMetadataResource uploadDocument(FileMetadataRequest metadata, Path path) throws FileClientException {

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

        HttpEntity<FileMetadataRequest> metadataEntity = buildJsonEntity(metadata);
        HttpEntity<FileSystemResource> documentEntity = buildFileEntity(path);

        // putting the two parts in one request
        multipartRequest.add("metadata", metadataEntity);
        multipartRequest.add("file", documentEntity);

        HttpHeaders rootHeader = buildRootEntity();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, rootHeader);

        try {
            return restTemplate.postForObject(this.completeFilesUploadUrl, requestEntity, FileMetadataResource.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new FileClientException(e);
        } catch (ResourceAccessException e) {
            throw new FileClientException(e);
        }
    }

    @Override
    public void downloadFile(Path target, String fileId) throws FileClientException {
        String url = completeFilesDownloadUrl + "/" + fileId;

        RequestCallback requestCallback = request -> {
            request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, getAuthorizationHeaderValue());
        };

        ResponseExtractor<Void> responseExtractor = response -> {
            Files.copy(response.getBody(), target, StandardCopyOption.REPLACE_EXISTING);

            return null;
        };

        try {
            restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new FileClientException(e);
        } catch (ResourceAccessException e) {
            throw new FileClientException(e);
        }
    }

    @Override
    public FileMetadataResource getFileMetadata(String fileId) throws FileClientException {

        String url = this.completeFilesMetadataUrl + "/" + fileId;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, getAuthorizationHeaderValue());

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    FileMetadataResource.class).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new FileClientException(e);
        } catch (ResourceAccessException e) {
            throw new FileClientException(e);
        }
    }


    private HttpHeaders buildRootEntity() {
        HttpHeaders rootHeader = new HttpHeaders();

        rootHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        rootHeader.add(HttpHeaders.AUTHORIZATION, getAuthorizationHeaderValue());

        return rootHeader;
    }

    private String getAuthorizationHeaderValue() {
        String basicAuthString = this.username + ":" + this.password;
        String base64AuthString = Base64.getEncoder().encodeToString(basicAuthString.getBytes());

        return "Basic " + base64AuthString;
    }

    private HttpEntity<FileSystemResource> buildFileEntity(Path path) {
        HttpHeaders documentHeader = new HttpHeaders();

        documentHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        FileSystemResource document = new FileSystemResource(path.toFile());

        return new HttpEntity<>(document, documentHeader);
    }

    private HttpEntity<FileMetadataRequest> buildJsonEntity(FileMetadataRequest metadata) {

        HttpHeaders metadataHeader = new HttpHeaders();
        metadataHeader.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(metadata, metadataHeader);
    }
}
