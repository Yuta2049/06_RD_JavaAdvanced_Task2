package service;

import DAO.Account;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public interface IStorageService {

    Path path = Paths.get("accounts");

    void writeToStorage(List<Account> list);

    List<Account> readFromStorage();

}
