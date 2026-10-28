package service;
import model.Account;
import model.Command;
import util.*;
import java.nio.file.Path;
import java.util.Scanner;
record BankInfo(String name, String branch) {}
enum MenuOption { OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER,
                  STATEMENT, LIST_ACCOUNTS, BATCH, REPORT, EXIT }
public class MiniBank {
    static String read(Scanner sc, String label) {
        System.out.print(label + ": "); return sc.nextLine().trim();
    }
    public static void main(String[] args) {
        System.out.println(new BankInfo("MiniBank","Anand"));
        try {
            BankService bank = new BankService(Path.of("data"));
            Scanner sc = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                System.out.println("1. Open account\n2. Deposit\n3. Withdraw\n4. Transfer"
                    + "\n5. Statement\n6. List accounts\n7. Batch deposits\n8. Report\n9. Exit");
                try {
                    int choice = Integer.parseInt(sc.nextLine());
                    if (choice < 1 || choice > 9) { System.out.println("Invalid choice"); continue; }
                    MenuOption option = MenuOption.values()[choice - 1];
                    switch (option) {
                        case OPEN_ACCOUNT -> {
                            String type = read(sc,"Type SAVINGS/CURRENT/FD");
                            String name = read(sc,"Name");
                            String email = read(sc,"Email");
                            String mobile = read(sc,"Mobile");
                            String pan = read(sc,"PAN");
                            String ifsc = read(sc,"IFSC");
                            long opening = Long.parseLong(read(sc,"Opening balance"));
                            Account a = bank.openAccount(type,name,email,mobile,pan,ifsc,opening);
                            System.out.println("Opened: " + a);
                        }
                        case DEPOSIT, WITHDRAW -> {
                            String id = read(sc,"Account");
                            String amount = read(sc,"Amount");
                            Command c = CommandParser.parse(option.name() + " " + id + " " + amount);
                            if (option == MenuOption.DEPOSIT) { bank.deposit(c.accountNumber(),c.amount()); }
                            else { bank.withdraw(c.accountNumber(),c.amount()); }
                            System.out.println("Balance: " + bank.balance(id));
                        }
                        case TRANSFER -> {
                            String from = read(sc,"From account");
                            String to = read(sc,"To account");
                            String amount = read(sc,"Amount");
                            Command c = CommandParser.parse("TRANSFER " + from + " " + amount);
                            bank.transfer(c.accountNumber(),to,c.amount());
                            System.out.println("Transfer completed");
                        }
                        case STATEMENT -> System.out.println(bank.statement(read(sc,"Account")));
                        case LIST_ACCOUNTS -> {
                            for (Account a : bank.listAccounts()) { System.out.println(a); }
                        }
                        case BATCH -> {
                            String id = read(sc,"Account");
                            int tasks = Integer.parseInt(read(sc,"Number of deposits"));
                            long amount = Long.parseLong(read(sc,"Amount per deposit"));
                            bank.batchDeposit(id,tasks,amount);
                            System.out.println("Balance: " + bank.balance(id));
                        }
                        case REPORT -> System.out.println(bank.report());
                        case EXIT -> {
                            bank.save(); exit = true; System.out.println("Saved. Goodbye");
                        }
                    }
                } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
            }
        } catch (Exception e) { System.out.println("Startup failed: " + e.getMessage()); }
    }
}
