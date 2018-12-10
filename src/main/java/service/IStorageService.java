package service;

import DAO.Account;

import java.util.List;

public interface IStorageService {
    void writeToStorage(List<Account> list);

    List<Account> readFromStorage();
}
