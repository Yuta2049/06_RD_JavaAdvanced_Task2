package service;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

    public static final long INITIAL_ACCOUNT_BALANCE = 1_000_000_000;

    public static final Path ACCOUNT_PATH = Paths.get("accounts");

    public static final int QUANTITY_OF_ACCOUNT_FOR_GENERATION = 4;

    public static final int QUANTITY_OF_THREADS = 20;

    public static final int MAX_TRANSACTION_COUNT = 1000;


}
