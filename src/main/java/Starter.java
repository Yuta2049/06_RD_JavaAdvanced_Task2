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

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        ReentrantLock lock = new ReentrantLock();

        //executorService.execute(service.transferMoney(accountList));

        for(int i = 0; i < 20; i++) {
            executorService.execute(new TransferService(accountList));
        }

        executorService.shutdown();

        /*executorService.execute(new Runnable() {

            Object call() throws Exception {
            println("In thread")
            return "From thread"

            Future future = pool.submit(new Callable() {
            }
        })
        println("From main")
        println(future.get())

        try {
            pool.submit(new Callable() {
                Object call() throws Exception {
                    throw new IllegalStateException()
                }
            }).get()
        } catch (ExecutionException e) {println("Got it: ${e.cause}")}

        pool.shutdown()*/

        /*executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });


        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }*/

        //for (int i = 0; i < 10; i++) {
        //    service.transferMoney(accountList);
        //}

        System.out.println();
        //accountList.forEach(x -> System.out.println(x));
        accountList.forEach(x -> System.out.println(x));

    }
}
