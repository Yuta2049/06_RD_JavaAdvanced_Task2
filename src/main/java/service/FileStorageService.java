package service;

import DAO.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService implements IStorageService {

    private static final Logger logger = LoggerFactory.getLogger(service.FileStorageService.class);

    @Override
    public void writeToStorage(List<Account> list) {

        if (!Files.exists(Constants.ACCOUNT_PATH)) {
            try {
                Files.createDirectory(Constants.ACCOUNT_PATH);
            } catch (IOException e) {
                logger.error("Ошибка при создании каталога: " + Constants.ACCOUNT_PATH.toString());
            }
        }

        for (Account currentAccount : list) {

            Path p1 = Constants.ACCOUNT_PATH;
            p1 = p1.resolve(currentAccount.getName());
            File accountFile;

            try {
                accountFile = Files.createFile(p1).toFile();

                try (FileOutputStream fos = new FileOutputStream(accountFile);
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(currentAccount);
                }

            } catch (IOException e) {
                logger.error("Ошибка ввода-вывода при записи аккаунта в файл");
            }
        }
    }

    @Override
    public List<Account> readFromStorage() {

        List<Account> accountList = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Constants.ACCOUNT_PATH)) {

            for (Path file : stream) {

                byte[] bytes = Files.readAllBytes(file);

                try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                     ObjectInput in = new ObjectInputStream(bis)) {
                    Account account = (Account) in.readObject();
                    accountList.add(account);

                } catch (ClassNotFoundException e) {
                    logger.error("Чтение аккаунта из файла: Класс не найден");
                    e.getMessage();
                }
            }
        } catch (IOException e) {
            logger.error("Чтение аккаунта из файла: Ошибка ввода вывода");
            System.out.println(e.getMessage());
        }
        return accountList;
    }
}
