import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//by Nir Tuttnauer 14.3.2024
// Game of Life
// The Game of Life is a cellular automaton devised by the British mathematician John Horton Conway in 1970.
// The game is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input.
// One interacts with the Game of Life by creating an initial configuration and observing how it evolves.

//this was made for 'around' and adiel gur was the one who gave me this task

public class GameOfLife {

    public static void main(String[] args) {
        // Create a scanner object to read input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file name (without the extension):");
        String filePath = "src/" + scanner.nextLine() + ".txt";
        // Read the initial state from the file

        boolean[][] initialState = readInitialState(filePath);
        if (initialState == null) {
            System.out.println("Error reading initial state.");
            return;
        }
        // Print the initial state
        printState(initialState);
        //get the width and height of the grid
        int width = initialState[0].length;
        int height = initialState.length;
        if(!checkInput(width, height)){
            System.out.println("Invalid input.");
            return;
        }
        // Prompt the user for the number of generations
        int numGenerations = promptForPositiveInteger(scanner, "Enter number of generations: ");
        //close the scanner
        scanner.close();
        // Run the game of life
        runGameOfLife(initialState, numGenerations);


    }

    public static boolean[][] readInitialState(String filePath) {


        try {
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            // Use a list to store lines as we don't know the height yet
            List<String> lines = new ArrayList<>();
            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
            // Close the scanner
            fileScanner.close();
            // Get the height and width of the grid
            int height = lines.size();
            int width = 0;
            if (height > 0) {
                width = lines.get(0).split("\\s+").length; // Detect width from the first line
            }

            // Now create the array with known dimensions
            boolean[][] initialState = new boolean[height][width];
            // Parse the lines and populate the array
            for (int i = 0; i < height; i++) {
                String[] values = lines.get(i).split("\\s+");  // Split by whitespace
                for (int j = 0; j < width; j++) {
                    initialState[i][j] = Integer.parseInt(values[j]) == 1;
                }
            }
            System.out.println("Initial state loaded from " + filePath);
            System.out.println("Width: " + width + ", Height: " + height);
            return initialState;
        // Catch exceptions
            // FileNotFoundException: file not found
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            return null;
            // NumberFormatException: invalid data format
        } catch (NumberFormatException e) {
            System.err.println("Invalid data format in the file. Expecting 0 or 1.");
            return null;
        }
    }

    public static int countNeighbors(boolean[][] grid, int row, int col) {
        // Count the number of live neighbors for the specified cell
        int count = 0;
        // Get the height and width of the grid
        int height = grid.length;
        int width = grid[0].length;
        // Check the 8 neighboring cells
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < height && j >= 0 && j < width && !(i == row && j == col)) {
                    if (grid[i][j]) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static void runGameOfLife(boolean[][] initialState, int numGenerations) {
        // Get the height and width of the grid
        int height = initialState.length;
        int width = initialState[0].length;
        // Create a copy of the initial state
        boolean[][] currentGeneration = initialState.clone();

        // Run the game of life for the specified number of generations
        for (int gen = 1; gen <= numGenerations; gen++) {
            // Print the current generation
            System.out.println("Generation " + gen + ":");
            printState(currentGeneration);
            // Create a new 2D array to store the next generation
            boolean[][] nextGeneration = new boolean[height][width];
            // Compute the next generation based on the current generation
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int neighbors = countNeighbors(currentGeneration, i, j);
                    nextGeneration[i][j] = (neighbors == 3) || (currentGeneration[i][j] && neighbors == 2);
                }
            }
            // Update the current generation
            currentGeneration = nextGeneration;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printState(boolean[][] grid) {
        // Clear the console
        clearConsole();
        // Print the grid
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                System.out.print(cell ? "1 " : "0 ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void clearConsole() {
        // Clear the console
        try {
            // Check if the operating system is Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Clear the console using ANSI escape codes
                System.out.print("\033[H\033[2J");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkInput(int width, int height) {
        // Check if the width and height are positive integers
        if (width <= 0 || height <= 0) {
            System.out.println("Width and height must be positive integers.");
            return false;
        }
        return true;
    }

    public static int promptForInteger(Scanner scanner, String prompt) {
        // Prompt the user for an integer
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    public static int promptForPositiveInteger(Scanner scanner, String prompt) {
        // Prompt the user for a positive integer
        while (true) {
            int value = promptForInteger(scanner, prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a positive integer.");
        }
    }
}
