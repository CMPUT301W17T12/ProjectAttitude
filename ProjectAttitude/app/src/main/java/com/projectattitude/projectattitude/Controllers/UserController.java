package com.projectattitude.projectattitude.Controllers;

import com.projectattitude.projectattitude.Objects.User;

/**
 * Created by Vuk on 3/10/2017.
 */

public class UserController {

    private static UserController instance = new UserController();
    private ElasticSearchUserController ES = ElasticSearchUserController.getInstance();

    private User activeUser;


    public static UserController getInstance() {
        return instance;
    }

    private UserController() {
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
        //saveUser();
    }


}
