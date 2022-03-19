package ece651.riskgame.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger logger = new Logger();
    private PrintWriter writer;
    private ArrayList<String> buffer;
    private SimpleDateFormat sdf;

    private Logger() {
        try {
            String logFile = "log.txt";
            FileWriter fileWriter = new FileWriter(logFile);
            writer = new PrintWriter(fileWriter, true);
            buffer = new ArrayList<>();
            sdf = new SimpleDateFormat();
            sdf.applyPattern("MM-dd HH:mm:ss");
        } catch (IOException ignored) {
        }
    }

    public static Logger getInstance() {
        return logger;
    }

    public void writeLog(String log) {
        Date date = new Date();
        buffer.add(sdf.format(date) + "  |  " + log);
    }

    public Iterable<String> getBuffer() {
        return buffer;
    }

    public void flushBuffer() {
        for (String s : buffer) {
            writer.println(s);
        }
        buffer.clear();
    }

}
