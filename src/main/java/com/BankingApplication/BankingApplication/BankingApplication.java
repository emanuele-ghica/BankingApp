package com.BankingApplication.BankingApplication;

import com.BankingApplication.BankingApplication.models.User;
import com.BankingApplication.BankingApplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class BankingApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	private User loggedInUser = null;

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

	public void run(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while(true) {
			if(loggedInUser == null) {
				System.out.println("Welcome to the Banking App");
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
					case 1 -> register(scanner);
					case 2 -> login(scanner);
					case 3 -> {
						System.out.println("Goodbye!");
						return;
					}
					default -> System.out.println("Invalid choice.");
				}
			} else {
				System.out.println("Welcome, " + loggedInUser.getName());
				System.out.println("1. Add Money");
				System.out.println("2. Withdraw Money");
				System.out.println("3. Transfer Money");
				System.out.println("4. Check Balance");
				System.out.println("5. Logout");
				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
					case 1 -> addMoney(scanner);
					case 2 -> withdrawMoney(scanner);
					case 3 -> transferMoney(scanner);
					case 4 -> checkBalance();
					case 5 -> loggedInUser = null;
					default -> System.out.println("Invalid choice.");
				}
			}
		}
	}

	private void register(Scanner scanner) {
		System.out.println("Enter your name:");
		String name = scanner.nextLine();
		System.out.println("Enter your email:");
		String email = scanner.nextLine();
		System.out.println("Enter a PIN:");
		String pin = scanner.nextLine();

		try {
			loggedInUser = userService.registerUser(name, email, pin);
			System.out.println("Registration successful!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void login(Scanner scanner) {
		System.out.println("Enter your email:");
		String email = scanner.nextLine();
		System.out.println("Enter your PIN:");
		String pin = scanner.nextLine();

		try {
			loggedInUser = userService.loginUser(email, pin);
			System.out.println("Login successful!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void addMoney(Scanner scanner) {
		System.out.println("Enter amount to add:");
		double amount = scanner.nextDouble();
		userService.addMoney(loggedInUser, amount);
		System.out.println("Money added successfully!");
	}

	private void withdrawMoney(Scanner scanner) {
		System.out.println("Enter amount to withdraw:");
		double amount = scanner.nextDouble();

		try {
			userService.withdrawMoney(loggedInUser, amount);
			System.out.println("Money withdrawn successfully!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void transferMoney(Scanner scanner) {
		System.out.println("Enter recipient's email:");
		String recipientEmail = scanner.nextLine();
		System.out.println("Enter amount to transfer:");
		double amount = scanner.nextDouble();

		try {
			userService.transferMoney(loggedInUser, recipientEmail, amount);
			System.out.println("Money transferred successfully!");
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void checkBalance() {
		try {
			userService.checkBalance(loggedInUser);
		} catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
