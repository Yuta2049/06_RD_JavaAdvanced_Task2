package service;

import DAO.Account;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService implements IStorageService {

    public static Path path = Paths.get("/accounts");

    @Override
    public void writeToStorage(List<Account> list) {

        if (Files.exists(path)) {
            // ???

        } else {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                System.out.println("Ошибка при создании каталога: " + path.toString());
            }
        }

            for (Account currentAccount : list) {

                String filename = currentAccount.getName();
                filename = filename.replace(".", "");
                Path p1 = path;
                p1 = p1.resolve(filename);
                File accountFile;
                try {
                    accountFile = Files.createFile(p1).toFile();
                    //Path filePath = Paths.get(path + filename);

                    //try (FileOutputStream fos = new FileOutputStream(new File(filename));
                    try (FileOutputStream fos = new FileOutputStream(accountFile);
                         ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                        oos.writeObject(currentAccount);

                    } catch (FileNotFoundException e) {
                        System.out.println("Запись аккаунта в файл: Ошибка не найден файл");
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Запись аккаунта в файл: Ошибка ввода вывода");
                        System.out.println(e.getMessage());
                    }

                } catch (IOException e) {
                    System.out.println("Запись аккаунта в файл: Ошибка при создании файла");
                }
            }
    }

    @Override
    public List<Account> readFromStorage() {

        List<Account> accountList = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {

            for (Path file: stream) {

                //byte[] bytes = Files.readAllBytes(path);
                byte[] bytes = Files.readAllBytes(file);

                try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                     ObjectInput in = new ObjectInputStream(bis)) {
                    Account account = (Account) in.readObject();
                    accountList.add(account);

                } catch (IOException e) {
                    System.out.println("Запись аккаунта в файл: Ошибка ввода вывода");
                    System.out.println(e.getMessage());
                } catch (ClassNotFoundException e) {
                    System.out.println("Не удалось прочитать тестовые данные из файла");
                    e.getMessage();
                }
            }

        } catch (IOException | DirectoryIteratorException x) {
            // IOException не может броситься во время итерации.
            // В этом куске кода оно может броситься только
            // методом newDirectoryStream.
            System.err.println(x);
        }

        return accountList;
    }
}
