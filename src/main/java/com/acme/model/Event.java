package com.acme.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * We are going to create a new event to check that everything is ok
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private String name;
    private String uuid;
    private Background bg;

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

    public Background getBg() {
        return bg;
    }

    public void setBg(Background bg) {
        this.bg = bg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Background {

        private String original;
        private String thumbnail;

        public Background() {
        }

        public Background(String original) {
            this.original = original;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}
