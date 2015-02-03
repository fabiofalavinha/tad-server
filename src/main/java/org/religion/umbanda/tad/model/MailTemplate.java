package org.religion.umbanda.tad.model;

public interface MailTemplate<T> {

    String getKey();
    MailMessage createMailMessage(T arg);

}
