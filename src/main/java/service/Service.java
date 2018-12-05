package service;

import DAO.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Service {

    public List<Account> generateAccounts() {

        List<Account> accountList = new ArrayList<Account>();

        for (int i = 0; i < 10; i++) {

            Account account = new Account();
            account.setName(String.valueOf(Math.random()));
            //account.setBalance((int) Math.random().);
            account.setBalance(1_000_000);

            accountList.add(account);
        }

        accountList.forEach(x -> System.out.println(x));

        return accountList;
    }

    public void thread() {


    }

    //public void TransferMoney(Account account1, Account account2, double sum) {
    public void transferMoney(List<Account> accountList) {

        int ac1 = (int) (Math.random() * 10);

        int ac2;
        do {
            //ac2 = (int) (Math.random() * 10);
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

        if (account1.getBalance() > 0) {
            int summ = new Random().nextInt((int) account1.getBalance());

            account1.setBalance(account1.getBalance() + summ);
            account2.setBalance(account2.getBalance() - summ);
        }

    }

}

