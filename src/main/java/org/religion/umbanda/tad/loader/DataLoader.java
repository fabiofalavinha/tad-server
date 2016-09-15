package org.religion.umbanda.tad.loader;

public interface DataLoader {

    boolean accept(String key);

    void load(String data);

}
