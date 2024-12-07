package com.BankingApplication.BankingApplication;

import com.BankingApplication.BankingApplication.services.BalanceService;
import com.BankingApplication.BankingApplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class BankingApplication implements CommandLineRunner {

	@Autowired
	private  UserService userService;
	@Autowired
	private BalanceService balanceService;
	private String token;

    public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

	public void run(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			if (token == null) {
				showInitialMenu(scanner);
			} else {
				showUserMenu(scanner);
			}
		}
	}

	private void showInitialMenu(Scanner scanner) {
		System.out.println("\nWelcome to the Banking App");
		System.out.println("1. Register");
		System.out.println("2. Login");
		System.out.println("3. Exit");
		System.out.print("Choose an option: ");

		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
			case 1 -> register(scanner);
			case 2 -> login(scanner);
			case 3 -> exitApplication();
			default -> System.out.println("Invalid choice. Please try again.");
		}
	}

	private void showUserMenu(Scanner scanner) {
		System.out.println("\n1. Add Money");
		System.out.println("2. Withdraw Money");
		System.out.println("3. Transfer Money");
		System.out.println("4. Check Balance");
		System.out.println("5. Logout");
		System.out.print("Choose an option: ");

		int choice = scanner.nextInt();
		scanner.nextLine();

		switch (choice) {
			case 1 -> addMoney(scanner);
			case 2 -> withdrawMoney(scanner);
			case 3 -> transferMoney(scanner);
			case 4 -> checkBalance();
			case 5 -> logout();
			default -> System.out.println("Invalid choice. Please try again.");
		}
	}


	private void register(Scanner scanner) {
		System.out.print("Enter your name: ");
		String name = scanner.nextLine();
		System.out.print("Enter your email: ");
		String email = scanner.nextLine();
		System.out.print("Enter a PIN: ");
		String pin = scanner.nextLine();

		try {
			token = userService.registerUser(name, email, pin);
			System.out.println("Registration successful! Your token: " + token);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void login(Scanner scanner) {
		System.out.print("Enter your email: ");
		String email = scanner.nextLine();
		System.out.print("Enter your PIN: ");
		String pin = scanner.nextLine();

		try {
			token = userService.loginUser(email, pin);
			System.out.println("Login successful! Your token: " + token);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void addMoney(Scanner scanner) {
		System.out.print("Enter amount to add: ");
		double amount = scanner.nextDouble();

		try {
			balanceService.addMoney(token, amount);
			System.out.println("Money added successfully!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void withdrawMoney(Scanner scanner) {
		System.out.print("Enter amount to withdraw: ");
		double amount = scanner.nextDouble();

		try {
			balanceService.withdrawMoney(token, amount);
			System.out.println("Money withdrawn successfully!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void transferMoney(Scanner scanner) {
		System.out.print("Enter recipient's email: ");
		String recipientEmail = scanner.nextLine();
		System.out.print("Enter amount to transfer: ");
		double amount = scanner.nextDouble();

		try {
			balanceService.transferMoney(token, recipientEmail, amount);
			System.out.println("Money transferred successfully!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void checkBalance() {
		try {
			balanceService.checkBalance(token);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void logout() {
		try {
			userService.logout(token);
			token = null;
			System.out.println("Logout successful!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void exitApplication() {
		System.out.println("Goodbye!");
		System.exit(0);
	}
}


