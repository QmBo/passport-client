package ru.job4j.passport.client.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.passport.client.domain.Passport;
import ru.job4j.passport.client.reposytory.PassportRepository;

import java.util.List;

@Service
public class PassportUnavailableService {
    private final KafkaTemplate<Integer, String> template;
    private final PassportRepository repository;

    public PassportUnavailableService(KafkaTemplate<Integer, String> template, PassportRepository repository) {
        this.template = template;
        this.repository = repository;
    }

    @Scheduled(cron = "0 * * ? * *")
    public void checkUnavailablePassports() {
        List<Passport> unavailable = repository.getUnavailable();
        template.send("unavailable-passports", unavailable.toString());
    }
}
