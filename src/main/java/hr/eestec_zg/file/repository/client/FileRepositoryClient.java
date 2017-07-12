package hr.eestec_zg.file.repository.client;

import hr.eestec_zg.file.repository.client.pojo.FileMetadataRequest;
import hr.eestec_zg.file.repository.client.resources.FileMetadataResource;
import hr.eestec_zg.file.repository.client.ex.FileClientException;

import java.nio.file.Path;

public interface FileRepositoryClient {

    FileMetadataResource uploadDocument(FileMetadataRequest metadata, Path path) throws FileClientException;

    void downloadFile(Path target, String documentId) throws FileClientException;

    FileMetadataResource getFileMetadata(String documentId) throws FileClientException;
}
