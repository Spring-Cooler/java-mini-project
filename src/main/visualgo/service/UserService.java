package src.main.visualgo.service;

import src.main.visualgo.repository.DataRepository;

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
//    todo: show problems in db(problems.dat)
//    todo: parse AST()
//    todo: run AST()
//    todo: visualize results()
}
