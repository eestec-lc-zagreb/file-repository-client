package hr.eestec_zg.file.repository.client.pojo;

public class FileMetadataRequest {

    private String name;
    private String fileType;

    public FileMetadataRequest() {
        // jackson
    }

    public FileMetadataRequest(String name, String fileType) {
        this.name = name;
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "FileMetadataRequest{" +
                "name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}