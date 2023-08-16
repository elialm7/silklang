package shdlang.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        log.info("Main has been called");
        System.out.println("Hello world!");
    }
}