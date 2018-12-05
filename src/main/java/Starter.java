import DAO.Account;
import service.FileStorageService;
import service.IStorageService;
import service.Service;

import java.nio.file.Files;
import java.util.List;

public class Starter {

    public static void main(String[] args) {

        Service service = new Service();

        IStorageService storageService = new FileStorageService();

        List<Account> accountList;

//        if (Files.exists(FileStorageService.path)) {
        if (Files.exists(FileStorageService.path)) {
            accountList = storageService.readFromStorage();
        } else {
            accountList = service.generateAccounts();
            storageService.writeToStorage(accountList);
        }

        for (int i = 0; i < 10; i++) {

            service.transferMoney(accountList);

        }

        System.out.println();
        //accountList.forEach(x -> System.out.println(x));
        accountList.forEach(x -> System.out.println(x));

    }
}
