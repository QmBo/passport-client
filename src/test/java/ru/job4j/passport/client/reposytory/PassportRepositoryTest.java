package ru.job4j.passport.client.reposytory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.job4j.passport.client.domain.Passport;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

class PassportRepositoryTest {
    private final List<Integer> toDelete = new ArrayList<>(100);
    private final PassportRepository repository = new PassportRepository(
            "http://localhost:8080/api/passport/",
            new RestTemplateBuilder().build()
    );

    @AfterEach
    public void deletePassports() {
        toDelete.forEach(repository::delete);
    }

    @Test
    public void whenFindAllTheListPassports() {
        List<Passport> all = repository.findAll();
        assertThat(all.size(), not(0));
    }

    @Test
    public void whenFindSerialThePassport() {
        Passport bySerial = repository.findBySerial(2);
        assertThat(bySerial.getSerial(), is(2L));
        assertThat(bySerial.getId(), is(not(0)));
    }

    @Test
    public void whenAddPassportThenPassportAdded() {
        List<Passport> all = repository.findAll();
        Passport save = repository.save(new Passport().setSerial(987654321L).setEndDate(new Date()));
        Integer id = save.getId();
        List<Passport> allAfter = repository.findAll();
        assertThat(allAfter, not(all));
        assertThat(id, not(0));
        boolean delete = repository.delete(id);
        assertThat(delete, is(true));
    }

    @Test
    public void whenUpdatePassportThenPassportUpdate() {
        Date endDate = new Date();
        Date newEndDate = new Date(System.currentTimeMillis() + 1000000000L);
        Passport before = repository.save(new Passport().setSerial(987654321L).setEndDate(endDate));
        Integer id = before.getId();
        toDelete.add(id);
        Passport after = repository.update(id, new Passport().setSerial(987654321L).setEndDate(newEndDate));
        assertThat(before.equals(after), is(false));
    }

    @Test
    public void whenGetUnavailablePassportThenPassports() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, -3);
        Date endDate = calendar.getTime();
        List<Passport> before = repository.getUnavailable();
        Passport passport = repository.save(new Passport().setSerial(987654321L).setEndDate(endDate));
        Integer id = passport.getId();
        toDelete.add(id);
        List<Passport> after = repository.getUnavailable();
        assertThat(before.size(), not(after.size()));
        assertThat(before.stream().anyMatch(pass -> pass.getId().equals(id)), is(false));
        assertThat(after.stream().anyMatch(pass -> pass.getId().equals(id)), is(true));
    }

    @Test
    public void whenGetReplaceablePassportThenPassports() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 2);
        Date endDate = calendar.getTime();
        List<Passport> before = repository.getReplaceable();
        Passport passport = repository.save(new Passport().setSerial(987654321L).setEndDate(endDate));
        Integer id = passport.getId();
        toDelete.add(id);
        List<Passport> after = repository.getReplaceable();
        assertThat(before.size(), not(after.size()));
        assertThat(before.stream().anyMatch(pass -> pass.getId().equals(id)), is(false));
        assertThat(after.stream().anyMatch(pass -> pass.getId().equals(id)), is(true));
    }
}