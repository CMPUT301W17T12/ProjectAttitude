package com.projectattitude.projectattitude.Objects;

/**
 * Created by henry on 3/26/2017.
 */

/**
 * The class that acts as the instance of a request object made when a user wishes to follow another
 * user. This object is instantiated in the profile page and is managed by the ElasticSearchRequestController.
 * @see com.projectattitude.projectattitude.Controllers.ElasticSearchRequestController
 */
public class FollowRequest {
    private String requester; //User ID that initialized request
    private String requestee; //User ID that receives request
    private String ID; //Request's ID assigned by elastic search

    /**
     * Creating the request object.
     * @param requester The user creating the request.
     * @param requestee The user to recieve the request.
     */
    public FollowRequest(String requester, String requestee) {
        this.requester = requester;
        this.requestee = requestee;
    }

    /**
     * Returns the requester.
     * @return String requester
     */
    public String getRequester() {
        return requester;
    }

    /**
     * Returns the requestee.
     * @return String requestee
     */
    public String getRequestee() {
        return requestee;
    }

    /**
     * Returns the ID.
     * @return String ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets the ID.
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * This is what the list view shows
     * @return a string currently containing the emotionState
     */
    @Override
    public String toString(){
        return(requester);
    }
}
