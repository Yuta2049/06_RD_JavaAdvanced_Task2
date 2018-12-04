import DAO.Account;
import service.Service;

import java.util.ArrayList;
import java.util.List;

public class Starter {

    public static void main(String[] args) {
        Service service = new Service();
        List<Account> accountList = service.generateAccounts();


        for (int i = 0; i < 10; i++) {

            service.transferMoney(accountList);

        }

        System.out.println();
        accountList.forEach(x -> System.out.println(x));

    }
}
