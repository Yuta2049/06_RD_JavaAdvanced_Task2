import DAO.Account;
import service.FileStorageService;
import service.IStorageService;
import service.Service;
import service.TransferService;

import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Starter {

    public static void main(String[] args) {

        Service service = new Service();

        IStorageService storageService = new FileStorageService();

        List<Account> accountList;

        if (Files.exists(FileStorageService.path)) {
            accountList = storageService.readFromStorage();
        } else {
            accountList = service.generateAccounts();
            storageService.writeToStorage(accountList);
        }

        System.out.println("Сумма по всем аккаунтам: " + accountList.stream().mapToLong(x -> x.getBalance()).sum());

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        //ReentrantLock lock = new ReentrantLock();

        //executorService.execute(service.transferMoney(accountList));

        for (int i = 0; i < 20; i++) {
            executorService.execute(new TransferService(accountList));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

        }

        System.out.println();

//        try {
//            Thread.currentThread().sleep(1000000);
//        } catch (InterruptedException e) {
//            System.out.println("Ошибка при переводе главного потока в состояние sleep");
//        }

        accountList.forEach(x -> System.out.println(x));

        //accountList.forEach(x -> x.getBalance());
        System.out.println("Сумма по всем аккаунтам: " + accountList.stream().mapToLong(x -> x.getBalance()).sum());

        //System.out.println(accountList.stream().mapToInt().sum();

    }
}
