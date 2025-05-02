import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WordSearchSolver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter puzzle file name:");
        String puzzleFile = scanner.nextLine();
        System.out.println("Enter word file name:");
        String wordFile = scanner.nextLine();
        System.out.println("Enter solution file name:");
        String solutionFile = scanner.nextLine();

        try {
            char[][] puzzle = readPuzzle(puzzleFile);
            List<String> words = readWords(wordFile);

            System.out.println("Original Puzzle:");
            printGrid(puzzle);

            char[][] solution = new char[puzzle.length][puzzle[0].length];
            for (char[] row : solution) Arrays.fill(row, '0'); // Placeholder '0's

            int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

            for (String word : words) {
                String upperWord = word.toUpperCase();
                for (int row = 0; row < puzzle.length; row++) {
                    for (int col = 0; col < puzzle[row].length; col++) {
                        if (puzzle[row][col] == upperWord.charAt(0)) {
                            for (int[] dir : directions) {
                                int dr = dir[0], dc = dir[1];
                                int endRow = row + (upperWord.length() - 1) * dr;
                                int endCol = col + (upperWord.length() - 1) * dc;
                                if (endRow < 0 || endRow >= puzzle.length || endCol < 0 || endCol >= puzzle[0].length) {
                                    continue;
                                }
                                boolean match = true;
                                for (int i = 0; i < upperWord.length(); i++) {
                                    int currRow = row + i * dr;
                                    int currCol = col + i * dc;
                                    if (puzzle[currRow][currCol] != upperWord.charAt(i)) {
                                        match = false;
                                        break;
                                    }
                                }
                                if (match) {
                                    for (int i = 0; i < upperWord.length(); i++) {
                                        int currRow = row + i * dr;
                                        int currCol = col + i * dc;
                                        solution[currRow][currCol] = puzzle[currRow][currCol];
                                    }
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("\nSolution:");
            printGrid(solution);
            writeSolution(solutionFile, solution);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static char[][] readPuzzle(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        char[][] puzzle = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            puzzle[i] = lines.get(i).replaceAll(" ", "").toUpperCase().toCharArray();
        }
        return puzzle;
    }

    public static List<String> readWords(String filename) throws IOException {
        List<String> words = Files.readAllLines(Paths.get(filename));
        words.replaceAll(s -> s.trim().toUpperCase());
        words.removeIf(String::isEmpty);
        return words;
    }

    public static void writeSolution(String filename, char[][] solution) throws IOException {
        List<String> lines = new ArrayList<>();
        for (char[] row : solution) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.length; i++) {
                if (i > 0) sb.append(' ');
                sb.append(row[i]);
            }
            lines.add(sb.toString());
        }
        Files.write(Paths.get(filename), lines);
    }

    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (int i = 0; i < row.length; i++) {
                if (i > 0) System.out.print(' ');
                System.out.print(row[i]);
            }
            System.out.println();
        }
    }
}
