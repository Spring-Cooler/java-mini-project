package main.visualgo.service;

import main.visualgo.repository.DataRepository;


public class UserService {
    /* singleton */
    private static UserService userService;
    private static DataRepository dataRepository = DataRepository.getInstance();

    private UserService() {}

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    /* initialization */
//    todo : check Solution codes and convert to Ast analysis code
//    todo : get TestCases and
}
