package org.religion.umbanda.tad.model.financial;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail-template.properties")
public class FinancialEntryReceiptMailTemplateConfiguration {

    @Value("${mail.template.financialEntry.receipt.subject}")
    private String subject;

    @Value("${mail.template.financialEntry.receipt.body}")
    private String body;

    @Bean
    public FinancialEntryReceiptMailTemplate financialEntryReceiptMailTemplate() {
        body =
            "Prezado %s,\n\n" +
            "Obrigado pela sua colaboração, abaixo segue o detalhamento de sua contribuição.\n\n" +
            "Recibo No.: %s\n\n" +
            "Data Pagto: %s\n\n" +
            "Referente: %s\n\n" +
            "Valor: %s\n\n" +
            "Obs.: %s\n\n" +
            "Secretaria TAD\n";
        return new FinancialEntryReceiptMailTemplate(subject, body);
    }
}
