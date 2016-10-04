package org.religion.umbanda.tad.model;

public class MailSender {

    private final String general;
    private final String financial;

    public MailSender(String general, String financial) {
        this.general = general;
        this.financial = financial;
    }

    public String getGeneral() {
        return general;
    }

    public String getFinancial() {
        return financial;
    }
}
