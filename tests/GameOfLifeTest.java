import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Dependency-free regression tests; throws AssertionError on failure. */
public class GameOfLifeTest {
    private static int failures;
    private static int checks;

    private static void check(String name, String input, boolean[][] expected) throws Exception {
        Path file = Files.createTempFile("game-of-life-test-", ".txt");
        try {
            Files.write(file, input.getBytes(StandardCharsets.UTF_8));
            boolean[][] actual = GameOfLife.readInitialState(file.toString());
            checks++;
            if (!java.util.Arrays.deepEquals(actual, expected)) {
                failures++;
                System.err.println("FAIL: " + name);
            }
        } catch (RuntimeException e) {
            checks++;
            failures++;
            System.err.println("FAIL: " + name + " threw " + e);
        } finally {
            Files.deleteIfExists(file);
        }
    }

    public static void main(String[] args) throws Exception {
        check("valid rectangle", "0 1 0\n1 0 1\n",
                new boolean[][] {{false, true, false}, {true, false, true}});
        check("surrounding whitespace", "  0\t1  \n  1 0 \n",
                new boolean[][] {{false, true}, {true, false}});
        check("empty file", "", null);
        check("blank row", "0 1\n\n", null);
        check("short row", "0 1\n1\n", null);
        check("long row", "0 1\n1 0 1\n", null);
        check("nonbinary number", "0 2\n", null);
        check("negative number", "0 -1\n", null);
        check("nonnumeric value", "0 x\n", null);
        check("single live cell", "1\n", new boolean[][] {{true}});
        System.out.println((checks - failures) + "/" + checks + " checks passed");
        if (failures > 0) {
            throw new AssertionError(failures + " failed checks");
        }
    }
}
