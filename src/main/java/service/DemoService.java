package service;

import DAO.Account;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DemoService {

    public void showDemo() {

        IStorageService storageService = new FileStorageService();

        List<Account> accountList;

        if (Files.exists(Constants.ACCOUNT_PATH)) {
            accountList = storageService.readFromStorage();
        } else {
            accountList = generateAccounts();
            storageService.writeToStorage(accountList);
        }

        accountList.forEach(x -> System.out.println(x));

        System.out.println("Сумма по всем аккаунтам: " + accountList.stream().mapToLong(x -> x.getBalance()).sum());

        ExecutorService executorService = Executors.newFixedThreadPool(Constants.QUANTITY_OF_THREADS);

        for (int i = 0; i < Constants.QUANTITY_OF_THREADS; i++) {
            executorService.execute(new TransferService(accountList));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // ТУТ ПИСАТЬ ЧЕГО=НИБУДЬ В ЛОГ
        }

        System.out.println();

        accountList.forEach(x -> System.out.println(x));

        //accountList.forEach(x -> x.getBalance());
        System.out.println("Сумма по всем аккаунтам: " + accountList.stream().mapToLong(x -> x.getBalance()).sum());

    }

    public List<Account> generateAccounts() {

        List<Account> accountList = new ArrayList<Account>();

        for (int i = 0; i < 4; i++) {
            Account account = new Account();
            Random random = new Random();
            account.setName(String.valueOf(random.nextInt(Integer.MAX_VALUE)));
            account.setBalance(Constants.INITIAL_ACCOUNT_BALANCE);
            accountList.add(account);
        }
        return accountList;
    }
}
