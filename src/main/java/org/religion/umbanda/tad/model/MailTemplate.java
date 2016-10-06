package org.religion.umbanda.tad.model;

public interface MailTemplate<T> {

    MailMessage createMailMessage(T arg);

}
