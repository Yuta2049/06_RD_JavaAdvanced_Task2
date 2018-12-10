package service;

import DAO.Account;
import Exceptions.InsufficientAmountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class TransferService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(service.TransferService.class);

    private List<Account> accountList;
    public static int transactionCount = 0;

    public TransferService(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public void run() {

        while (transactionCount < Constants.MAX_TRANSACTION_COUNT) {

            Random random = new Random();
            int ac1 = random.nextInt(accountList.size());

            int ac2;
            do {
                ac2 = random.nextInt(accountList.size());
            } while (ac1 == ac2);

            Account account1 = accountList.get(ac1);
            Account account2 = accountList.get(ac2);

            int summ = new Random().nextInt(Integer.MAX_VALUE) / 3;

            boolean checkFirst = false;

            if (ac1 < ac2) {
                account1 = accountList.get(ac1);
                account2 = accountList.get(ac2);
                checkFirst = true;
            } else {
                account1 = accountList.get(ac2);
                account2 = accountList.get(ac1);
                summ = -summ;
            }

            boolean done = false;

            while (!done) {

                if (account1.lock.tryLock()) {

                    try {

                        account2.lock.lock();

                        try {

                            if (checkFirst) {
                                if (account1.getBalance() < Math.abs(summ)) {
                                    throw new InsufficientAmountException("Недостаточная сумма на счете для перевода");
                                }
                            } else if (account2.getBalance() < Math.abs(summ)) {
                                throw new InsufficientAmountException("Недостаточная сумма на счете для перевода");
                            }

                            account1.setBalance(account1.getBalance() - summ);
                            account2.setBalance(account2.getBalance() + summ);

                            logger.info("Провели транзацию. Счет 1: " + account1 + ", счет 2: " + account2 + ". Сумма перевода: " + summ);

                            transactionCount++;

                        } finally {
                            account2.lock.unlock();
                        }

                        done = true;

                    } catch (InsufficientAmountException exception) {
                        logger.warn("Недостаточная сумма на аккаунте: " + account1.toString() + ". Сумма перевода: " + summ);
                        done = true;
                    } finally {
                        account1.lock.unlock();
                    }
                }
            }
        }
    }
}
