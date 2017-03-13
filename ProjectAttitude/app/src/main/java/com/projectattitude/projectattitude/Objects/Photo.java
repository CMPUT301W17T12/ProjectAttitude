package com.projectattitude.projectattitude.Objects;

import java.io.Serializable;

/**
 * Created by Vuk on 3/12/2017.
 */

public class Photo implements Serializable{

    private String photo;

    public Photo(){
    }

    public String getPhoto(){
        return photo;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

}
