package service;

import DAO.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DAO.Account.class);

    public void showDemo() {

        IStorageService storageService = new FileStorageService();

        List<Account> accountList;

        if (Files.exists(Constants.ACCOUNT_PATH)) {
            accountList = storageService.readFromStorage();
        } else {
            accountList = generateAccounts();
            storageService.writeToStorage(accountList);
        }

        accountList.forEach(System.out::println);
        System.out.println("Сумма по всем аккаунтам: " + accountList.stream().mapToLong(x -> x.getBalance()).sum());

        ExecutorService executorService = Executors.newFixedThreadPool(Constants.QUANTITY_OF_THREADS);

        for (int i = 0; i < Constants.QUANTITY_OF_THREADS; i++) {
            executorService.execute(new TransferService(accountList));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            logger.error("Ошибка при awaitTermination: ");
        }

        System.out.println();
        accountList.forEach(System.out::println);
        System.out.println("Сумма по всем аккаунтам: " + accountList.stream().mapToLong(x -> x.getBalance()).sum());
    }

    private List<Account> generateAccounts() {

        List<Account> accountList = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < Constants.QUANTITY_OF_ACCOUNT_FOR_GENERATION; i++) {
            Account account = new Account();
            account.setName(String.valueOf(random.nextInt(Integer.MAX_VALUE)));
            account.setBalance(Constants.INITIAL_ACCOUNT_BALANCE);
            accountList.add(account);
        }
        return accountList;
    }
}
