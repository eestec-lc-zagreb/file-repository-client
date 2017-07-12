package hr.eestec_zg.file.repository.client.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileMetadataResource {

    private String id;
    private String name;
    private String type;
    private String location;
    private ZonedDateTime createdAt;

    public FileMetadataResource() {
        // for mapping
    }

    public FileMetadataResource(String id, String name, String type, String location, ZonedDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileMetadataResource that = (FileMetadataResource) o;

        return (id != null ? id.equals(that.id) : that.id == null) &&
                (name != null ? name.equals(that.name) : that.name == null) &&
                (type != null ? type.equals(that.type) : that.type == null) &&
                (location != null ? location.equals(that.location) : that.location == null) &&
                (createdAt != null ? createdAt.equals(that.createdAt) : that.createdAt == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FileMetadataResource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
