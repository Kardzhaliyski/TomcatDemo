import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    public static void main(String[] args) {
        Logger log = LogManager.getLogger("fileLog");
        System.out.println("start");
        log.error("Message error main");
        System.out.println("done");
    }
}
