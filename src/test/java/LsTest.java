import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

import org.kohsuke.args4j.CmdLineException;

public class LsTest {

    @Test
    public void test1() throws IOException, CmdLineException {
        String[] command = "-o testOutput1.txt .\\src".split(" ");

        Ls.main(command);

        File file = new File(".\\src\\test\\java\\testOutput\\testOutput1.txt");

        assertEquals(Files.readString(file.toPath()), "main\ntest\n");
    }

    @Test
    public void test2() throws IOException, CmdLineException {
        String[] command = "-l -o testOutput2.txt .\\src".split(" ");

        Ls.main(command);

        File file = new File(".\\src\\test\\java\\testOutput\\testOutput2.txt");

        assertEquals(Files.readString(file.toPath()), """
                111 10/04/2022 19:47 0 main
                111 10/04/2022 22:07 0 test
                """);
    }

    @Test
    public void test3() throws IOException, CmdLineException {
        String[] command = "-l -h -o testOutput1.txt .\\src".split(" ");

        Ls.main(command);

        File file = new File(".\\src\\test\\java\\testOutput\\testOutput1.txt");

        assertEquals(Files.readString(file.toPath()), """
                rwx 10/04/2022 19:47 0B main
                rwx 10/04/2022 22:07 0B test
                """);
    }

    @Test
    //Проверка на исключение, когда указан путь несуществующей директории.
    public void test4() {
        String[] command = ".\\src\\qwerty".split(" ");

        assertThrows(
                IllegalArgumentException.class,
                () -> Ls.main(command)
        );
    }

    @Test
    //Проверка на исколючение, когда используется humanReadable без longFormat.
    public void test5() {
        String[] command = "-h .\\src".split(" ");

        assertThrows(
                IllegalArgumentException.class,
                () -> Ls.main(command)
        );
    }

    @Test
    //Проверка на исколючение, когда неправильный формат ввода.
    public void test6() {
        String[] command = "-f -h .\\src".split(" ");

        assertThrows(
                CmdLineException.class,
                () -> Ls.main(command)
        );
    }
}
