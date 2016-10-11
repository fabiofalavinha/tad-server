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
        return new FinancialEntryReceiptMailTemplate(subject, body);
    }
}
