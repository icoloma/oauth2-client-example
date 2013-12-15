package com.acme.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * We are going to create a new event to check that everything is ok
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private String name;
    private String description;
    private String uuid;
    private Photo photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo {

        private String url;

        public Photo() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
