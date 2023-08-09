
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class warsztat {

    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] workshops;
    private static Object NumberUtils;

    public static void printOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
    }

    public static void main(String[] args) {
        workshops = loadDataToTab(FILE_NAME);
        printOptions(OPTIONS);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            switch (input) {
                case "exit":
                    saveTabToFile(FILE_NAME, workshops);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                    addWorkshop();
                    break;
                case "remove":
                    removeWorkshop(workshops, getTheNumber());
                    System.out.println("Value was successfully deleted.");
                    break;
                case "list":
                    printTab(workshops);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }

            printOptions(OPTIONS);
        }
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n, NumberUtils)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            n = scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void removeWorkshop(String[][] tab, int index) {
        if (index < 0 || index >= tab.length) {
            System.out.println("Invalid index.");
            return;
        }

        String[][] newTab = new String[tab.length - 1][tab[0].length];
        for (int i = 0, newRow = 0; i < tab.length; i++) {
            if (i != index) {
                newTab[newRow] = tab[i];
                newRow++;
            }
        }

        workshops = newTab;
    }

    public static boolean isNumberGreaterEqualZero(String input, Object NumberUtils) {
        if (NumberUtils.equals(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void addWorkshop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add workshop description");
        String description = scanner.nextLine();
        System.out.println("Please add workshop due date");
        String dueDate = scanner.nextLine();
        System.out.println("Is your workshop important: true/false");
        String isImportant = scanner.nextLine();
        workshops = Arrays.copyOf(workshops, workshops.length + 1);
        workshops[workshops.length - 1] = new String[3];
        workshops[workshops.length - 1][0] = description;
        workshops[workshops.length - 1][1] = dueDate;
        workshops[workshops.length - 1][2] = isImportant;
    }

    public static String[][] loadDataToTab(String fileName) {
        Path dir = Paths.get(fileName);
        if (!Files.exists(dir)) {
            System.out.println("File not exist.");
            System.exit(0);
        }

        String[][] tab = null;
        try {
            List<String> strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);

        String[] lines = new String[workshops.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }

        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static class ConsoleColors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String BLUE = "\u001B[34m";
    }
}