package src.main.visualgo.repository;

public class DataRepository {

    /* singleton */
    private static DataRepository dataRepository;
    private static int lastIndex = 0;
    private DataRepository() {
    }

    public static DataRepository getInstance() {
        if (dataRepository == null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }


}
    /* initialization */
//    todo: get problem info from db.dat/ or create database file(.dat)
