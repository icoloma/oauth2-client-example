package com.acme.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * We are going to create a new event to check that everything is ok
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private String name;
    private String uuid;

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
}
