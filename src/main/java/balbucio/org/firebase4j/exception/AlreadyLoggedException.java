package balbucio.org.firebase4j.exception;

import balbucio.org.firebase4j.model.User;
import lombok.Getter;

public class AlreadyLoggedException extends Exception{

    @Getter
    private User user;

    public AlreadyLoggedException(User user) {
        super("This instance already has a user logged in!");
        this.user = user;
    }
}
