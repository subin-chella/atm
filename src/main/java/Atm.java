import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * This class acts as an ATM Created on Dec 30, 2021
 *
 * @author subin subin8899@gmail.com
 */
public class Atm {

	enum Denomination {// A new bill denomination can be added here
		TWENTY(20), TEN(10), FIVE(5), ONE(1);

		private final int value;

		Denomination(final int newValue) {
			value = newValue;
		}

		public int getValue() {
			return value;
		}
	}

	static Scanner sc = new Scanner(System.in);
	static Map<Integer, Integer> billDenominationMap = new LinkedHashMap<>();
	static {
		System.out.println("\nHi! Welcome to our ATM");
		for (Denomination denomination : Denomination.values()) {
			billDenominationMap.put(denomination.value, 0);// Load 0 notes for each denomination
		}
	}

	public static void main(String[] args) {
		System.out.println("\n***********************************");
		System.out.println("Please select an operation(1, 2 or 3)\n 1. Deposit \n 2. Withdraw \n 3. Show balance");
		System.out.println("***********************************");
		switch (sc.next()) {
		case "1":
			deposit();
			break;
		case "2":
			withDraw();
			break;
		case "3":
			System.out.println("Your account balance is: " + findTotal());
			break;
		default:
			System.out.println("Select a valid option..");
		}
		main(args);
	}

	private static void withDraw() {
		System.out.println("Enter the dollar amount to withdraw");
		String amt = sc.next();
		if (!valideWithdrawalAmount(amt)) {
			return;
		}
		int amount = Integer.parseInt(amt);
		if (!dispenseAmount(amount)) {
			System.out.println("Requested withdraw amount is not dispensable");
		}
		printBalance();

	}

	private static void deposit() {
		int totalBills = 0;
		for (Map.Entry<Integer, Integer> entry : billDenominationMap.entrySet()) {
			String input;
			do {
				System.out.println("Input the Number of " + entry.getKey() + " dollar bills to Deposit");
				input = sc.next();
			} while (!validateDepositedBills(input));
			billDenominationMap.put(entry.getKey(), entry.getValue() + Integer.parseInt(input));
			totalBills = totalBills + Integer.parseInt(input);
		}
		if (totalBills == 0) {
			System.out.println("Deposit amount cannot be zero");
			return;
		}
		printBalance();
	}

// Calculate total amount in ATM
	static int findTotal() {
		int total = 0;
		for (Map.Entry<Integer, Integer> entry : billDenominationMap.entrySet()) {
			total = total + (entry.getKey() * entry.getValue());
		}
		return total;
	}

// Print Balance
	static void printBalance() {
		System.out.print("\nBalance:");
		for (Map.Entry<Integer, Integer> entry : billDenominationMap.entrySet()) {
			System.out.print(" "+entry.getKey() + "s=" + entry.getValue() + ",");
		}
		System.out.println("Total=" + findTotal());
	}

// Validation methods
	static boolean validateDepositedBills(String bills) {
		int noOfbills;
		try {
			noOfbills = Integer.parseInt(bills);
			if (noOfbills < 0) {
				System.out.println("Incorrect deposit amount");
				return false;
			}
		} catch (NumberFormatException exp) {
			System.out.println("Invalid number provided");
			return false;
		}

		return true;
	}

	// Validate if withdrawal amount is valid
	static boolean valideWithdrawalAmount(String amountToWithDraw) {
		int amt;
		try {
			amt = Integer.parseInt(amountToWithDraw);
			if (amt <= 0) {
				System.out.println("Incorrect amount");
				return false;
			} else if (amt > findTotal()) {
				System.out.println("Insufficient funds");
				return false;
			}
		} catch (NumberFormatException exp) {
			System.out.println("Invalid number provided");
			return false;
		}
		return true;

	}

	// Method to calculate bills denomination and dispense
	private static boolean dispenseAmount(int amount) {
		boolean isDispensed = false;
		Map<Integer, Integer> billDenominationMapCopy = new LinkedHashMap<>();
		billDenominationMapCopy.putAll(billDenominationMap);
		for (Map.Entry<Integer, Integer> entry : billDenominationMapCopy.entrySet()) {
			int billDenomination = entry.getKey();
			int noOfBills = entry.getValue();
			if (amount >= billDenomination && noOfBills > 0) {
				int calculatedBills = amount / billDenomination;
				int actualWithdrewBills = (calculatedBills > noOfBills) ? noOfBills : calculatedBills;
				if (amount > (billDenomination * actualWithdrewBills) && billDenomination == 1) {
					isDispensed = false;
					break;
				}
				billDenominationMapCopy.put(billDenomination, noOfBills - actualWithdrewBills);
				amount = amount - (billDenomination * actualWithdrewBills);
				isDispensed = true;
			} else {
				continue;
			}
		}
		if (isDispensed) {
			System.out.print("Dispensed: ");
			for (Integer key : billDenominationMap.keySet()) {
				int dispensedBillCount = billDenominationMap.get(key) - billDenominationMapCopy.get(key);
				if (dispensedBillCount > 0) {
					System.out.print(" "+key + "s=" +dispensedBillCount);
				}
			}
			billDenominationMap.putAll(billDenominationMapCopy);
		}
		return isDispensed;
	}

}