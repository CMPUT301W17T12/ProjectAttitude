package com.projectattitude.projectattitude.Objects;

/**
 * Created by henry on 3/26/2017.
 */

public class FollowRequest {
    private String requester; //User ID that initialized request
    private String requestee; //User ID that receives request
    private String ID; //Request's ID assigned by elastic search

    public FollowRequest(String requester, String requestee) {
        this.requester = requester;
        this.requestee = requestee;
    }

    public String getRequester() {
        return requester;
    }

    public String getRequestee() {
        return requestee;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
