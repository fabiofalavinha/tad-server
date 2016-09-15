package org.religion.umbanda.tad;

import org.religion.umbanda.tad.loader.DataLoader;
import org.religion.umbanda.tad.log.Log;
import org.religion.umbanda.tad.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ApplicationDataLoader implements CommandLineRunner {

    private static final Log log = LogFactory.createLog(ApplicationDataLoader.class);

    private final List<DataLoader> dataLoaders;

    @Autowired
    public ApplicationDataLoader(List<DataLoader> dataLoaders) {
        this.dataLoaders = dataLoaders;
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("Loading data from command line [arguments=%d]...", strings.length);
        for (String argumentAsString : strings) {
            log.info("Parsing argument [%s]...", argumentAsString);
            final Argument argument = parseArgument(argumentAsString);
            final Optional<DataLoader> dataLoaderOptional = findDataLoader(argument);
            dataLoaderOptional.ifPresent(d -> {
                log.info("Loading data [%s=%s] using [%s]...", argument.getKey(), argument.getValue(), d.getClass().getName());
                d.load(argument.getValue());
            });
        }
    }

    private Argument parseArgument(String argumentAsString) {
        argumentAsString = argumentAsString.replace("--", "");
        String[] argumentTokens = argumentAsString.split("=");
        return new Argument(argumentTokens[0], argumentTokens[1]);
    }

    private Optional<DataLoader> findDataLoader(Argument argument) {
        return dataLoaders.stream().filter(d -> d.accept(argument.getKey())).findFirst();
    }

    private class Argument {

        private final String key;
        private final String value;

        Argument(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
