import org.kohsuke.args4j.CmdLineException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Ls {
    public static void main(String[] args) throws IOException, CmdLineException {
        Arguments flags = new Arguments(args);

        boolean l = flags.longFormat;
        boolean h = flags.humanReadable;
        boolean r = flags.reverse;
        String outputName = flags.output;
        File directory = new File(flags.directory);

        if (!l && h) throw new IllegalArgumentException();
        if (!directory.exists()) throw new IllegalArgumentException();

        ArrayList<String> list = getDirectoryInfo(directory, l, h, r);

        write(list, outputName);
    }

    private static String getRWX(File file, boolean h) {
        StringBuilder rwx = new StringBuilder();
        if (h) {
            if (file.canRead()) rwx.append('r');
            else rwx.append('-');
            if (file.canWrite()) rwx.append('w');
            else rwx.append('-');
            if (file.canExecute()) rwx.append('x');
            else rwx.append('-');
        } else {
            if (file.canRead()) rwx.append(1);
            else rwx.append(0);
            if (file.canWrite()) rwx.append(1);
            else rwx.append(0);
            if (file.canExecute()) rwx.append(1);
            else rwx.append(0);
        }
        return rwx.toString();
    }

    private static String getDate(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/y H:mm");
        return sdf.format(file.lastModified());
    }

    private static String getSize(File file, boolean h) {
        StringBuilder size = new StringBuilder();
        long length = file.length();
        if (h) {
            int i = 0;
            while (length >= 1024) {
                length /= 1024;
                i++;
            }
            size.append(length);
            String type = switch (i) {
                case 1 -> "KB";
                case 2 -> "MB";
                case 3 -> "GB";
                case 4 -> "TB";
                default -> "B";
            };
            size.append(type);
        } else size.append(length);
        return size.toString();
    }

    private static String getInfo(File file, boolean l, boolean h) {
        StringBuilder info = new StringBuilder();
        if (l) {
            info.append(getRWX(file, h)).append(" ");
            info.append(getDate(file)).append(" ");
            info.append(getSize(file, h)).append(" ");
        }
        info.append(file.getName());
        return info.toString();
    }

    private static ArrayList<String> getDirectoryInfo(File directory, boolean l, boolean h, boolean r) {
        ArrayList<String> dirInfo = new ArrayList<>();
        File[] listOfFiles = directory.listFiles();
        for (File file : listOfFiles != null ? listOfFiles : new File[0]) dirInfo.add(getInfo(file, l, h));
        if (r) Collections.reverse(dirInfo);
        return dirInfo;
    }

    private static void write(ArrayList<String> dirInfo, String outputName) {
        if (outputName != null) {
            File outputFile = new File("./src//test//java//testOutput//" + outputName);
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile.toURI()))) {
                for (String info : dirInfo) {
                    writer.write(info + "\n");
                }
            } catch (IOException e) {
                System.out.println("Writing error");
            }
            System.out.println("Finish");
        } else {
            for (String fileInfo : dirInfo) System.out.println(fileInfo);
        }
    }
}
