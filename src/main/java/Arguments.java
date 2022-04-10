import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;


public class Arguments {
    @Option(name = "-l", usage = "long format")
    boolean longFormat;

    @Option(name = "-h", usage = "human-readable")
    boolean humanReadable;

    @Option(name = "-r", usage = "reverse")
    boolean reverse;

    @Option(name = "-o", usage = "output")
    String output;

    @Argument(metaVar = "directory", usage = "Input path", required = true)
    String directory;

    Arguments(String[] args) throws IOException, CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.print("java -jar ls.jar [-l][-h][-r] [-o output.file] directory_or_file");
            System.err.println(e.getMessage());
            throw e;
        }
    }
}
