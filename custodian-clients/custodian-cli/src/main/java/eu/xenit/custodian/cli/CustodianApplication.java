package eu.xenit.custodian.cli;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustodianApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CustodianApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CustodianApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String strArgs = Arrays.stream(args).collect(Collectors.joining(", "));
        logger.info("Application started with arguments:" + strArgs);
    }
}
