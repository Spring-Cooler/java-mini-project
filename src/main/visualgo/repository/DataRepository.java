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
}
