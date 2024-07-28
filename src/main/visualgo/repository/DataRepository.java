package main.visualgo.repository;

public class DataRepository {

    /* singleton */
    private static DataRepository dataRepository;

    private DataRepository() {}

    public static DataRepository getInstance() {
        if (dataRepository == null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    /* initialization */
//    todo: get test cases in data/testcases and convert to File instances
//    todo: get solutions in data/solutions and convert to File instances
}
