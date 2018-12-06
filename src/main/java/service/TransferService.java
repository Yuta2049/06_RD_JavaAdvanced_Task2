package service;

import DAO.Account;
import Exceptions.InsufficientAmountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(service.TransferService.class);

    private List<Account> accountList;
    public static int transactionCount = 0;
    public static final int MAXTRANSACTIONCOUNT = 500;

    public TransferService(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+". Зашли в run");

        logger.info("Hello from run.");

        while (transactionCount < MAXTRANSACTIONCOUNT) {

            //System.out.println(Thread.currentThread().getName()+". Транзакция");

            int ac1 = (int) (Math.random() * 10);

            int ac2;
            do {
                ac2 = (int) (Math.random() * 10);
            } while (ac1 == ac2);

            Account account1;
            Account account2;

            if (ac1 < ac2) {
                account1 = accountList.get(ac1);
                account2 = accountList.get(ac2);
            } else {
                account1 = accountList.get(ac2);
                account2 = accountList.get(ac1);
            }

            Lock l = new ReentrantLock();
            try {

                int summ = new Random().nextInt(Integer.MAX_VALUE);

                if (account1.getBalance() < summ) {
//                    System.out.println("Недосотаточная сумма на аккаунте: "+account1.getName());
//                    System.out.println("Аккаунт: "+account1.getName()+". Баланс: "+account1.getBalance()+". Сумма перевода: "+summ);
                    throw new InsufficientAmountException("Недостаточная сумма на счете для перевода");
                }

                transferMoney(account1, account2, summ);

//                System.out.println("Сумма на аккаунте достаточная: "+account1.getName());
//                System.out.println("Аккаунт: "+account1.getName()+". Баланс: "+account1.getBalance()+". Сумма перевода: "+summ);

                transactionCount++;

            } catch (InsufficientAmountException exception) {
                System.out.println(exception.getMessage());
            }

            finally {

                // снимаем локи
            }

        }

    }


    public void transferMoney(Account account1, Account account2, long summ) {

        account1.setBalance(account1.getBalance() + summ);
        account2.setBalance(account2.getBalance() - summ);

    }

}
