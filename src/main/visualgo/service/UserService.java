package main.visualgo.service;

import main.visualgo.repository.DataRepository;


public class UserService {
    private static UserService userService;
    private static DataRepository dataRepository = DataRepository.getInstance();
    private UserService() {
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }
}
