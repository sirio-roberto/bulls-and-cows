package bullscows;

import bullscows.exceptions.GameException;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        char[] allPossibilities = {'0','1','2','3','4','5','6','7','8','9',
                'a', 'b', 'c', 'd','e','f','g','h','i','j','k','l','m','n','o','p','q',
                'r','s','t','u','v','w','x','y','z'};

        try {
            System.out.println("Input the length of the secret code:");
            int n = Integer.parseInt(scan.nextLine());

            System.out.println("Input the number of possible symbols in the code:");
            int numOfSymbols = Integer.parseInt(scan.nextLine());

            if (n > numOfSymbols || n < 1) {
                throw new GameException(String.format(
                        "it's not possible to generate a code with a length of %s with %s unique symbols.%n",
                        n, numOfSymbols));
            }
            if (n > allPossibilities.length) {
                throw new GameException("maximum number of possible symbols in the code is 36 (0-9, a-z).%n");
            }

            String secretCode = getSecretCode(allPossibilities, n, numOfSymbols);
            boolean gameOver = false;
            String range = getRange(allPossibilities, n, numOfSymbols);
            System.out.printf("The secret is prepared: %s.%n", range);
            System.out.println("Okay, let's start a game!");
            int turn = 1;

            while (!gameOver) {
                System.out.printf("Turn %s:%n", turn);
                String userGuess = scan.nextLine();

                gameOver = score(userGuess, secretCode);
                turn++;
            }

            System.out.println("Congratulations! You guessed the secret code.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getRange(char[] allPossibilities, int n, int numOfSymbols) {
        StringBuilder result = new StringBuilder();
        while (n-- > 0) {
            result.append("*");
        }
        result.append(" (");
        result.append(allPossibilities[0]).append("-");
        if (numOfSymbols > 10) {
            result.append("9, a-").append(allPossibilities[numOfSymbols - 1]);
        } else {
            result.append(allPossibilities[numOfSymbols - 1]);
        }
        result.append(")");
        return result.toString();
    }

    private static String getSecretCode(char[] allPossibilities, int n, int numOfSymbols) {
        Random random = new Random();
        Set<Character> uniqueChars = new HashSet<>();
        while (uniqueChars.size() < n) {
            int index = random.nextInt(numOfSymbols);
            uniqueChars.add(allPossibilities[index]);
        }
        StringBuilder sb = new StringBuilder();
        for (char c : uniqueChars) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static boolean score(String userGuess, String secretCode) {
        int cows = 0;
        int bulls = 0;

        for (int i = 0; i < userGuess.length(); i++) {
            if (secretCode.contains(userGuess.substring(i, i + 1))) {
                if (secretCode.charAt(i) == userGuess.charAt(i)) {
                    bulls++;
                } else {
                    cows++;
                }
            }
        }
        StringBuilder sb = new StringBuilder("Grade: ");
        if (bulls > 0) {
            sb.append(bulls).append(" bull");
            if (bulls > 1) {
                sb.append("s");
            }
            if (cows > 0) {
                sb.append(" and ").append(cows).append(" cow");
            }
            if (cows > 1) {
                sb.append("s");
            }
        } else if (cows > 0) {
            sb.append(cows).append(" cow");
            if (cows > 1) {
                sb.append("s");
            }
        } else {
            sb.append("None");
        }
        System.out.println(sb);
        return bulls == secretCode.length();
    }
}
