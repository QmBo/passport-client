package ru.job4j.passport.client.reposytory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.job4j.passport.client.domain.Passport;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

/**
 * PassportRepository class.
 *
 * @author Victor Egorov (qrioflat@gmail.com).
 * @version 0.1
 * @since 26.11.2021
 */
@Repository
public class PassportRepository {
    private final String url;
    private final RestTemplate client;

    /**
     * Instantiates a new Passport repository.
     *
     * @param url    the url
     * @param client the client
     */
    public PassportRepository(@Value("${ru.job4j.api.url}") String url, RestTemplate client) {
        this.url = url;
        this.client = client;
    }

    /**
     * Save passport.
     *
     * @param passport the passport
     * @return the passport
     */
    public Passport save(Passport passport) {
        return client.postForEntity(
                format("%s/save", url),
                passport,
                Passport.class
        ).getBody();
    }

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean delete(int id) {
        return client.exchange(
                format("%s/delete?id=%s", url, id),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    /**
     * Find all Passports.
     *
     * @return the list
     */
    public List<Passport> findAll() {
        return getList(
                format("%s/find", url)
        );
    }

    /**
     * Find by serial Passport.
     *
     * @param serial the serial
     * @return the list
     */
    public Passport findBySerial(long serial) {
        return getList(
                format("%s/find?serial=%s", url, serial)
        ).stream().findFirst().orElse(null);
    }

    /**
     * Update passport.
     *
     * @param id          the id
     * @param newPassport the new passport
     * @return the passport
     */
    public Passport update(Integer id, Passport newPassport) {
        return client.exchange(
                format("%s/update?id=%s", url, id),
                HttpMethod.PUT,
                new HttpEntity<>(newPassport),
                Passport.class
        ).getBody();
    }

    /**
     * Gets unavailable Passports.
     *
     * @return the unavailable
     */
    public List<Passport> getUnavailable() {
        return getList(format("%s/unavailable", url));
    }

    private List<Passport> getList(String url) {
        List<Passport> body = client.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Passport>>() {
                }
        ).getBody();
        return body == null ? Collections.emptyList() : body;
    }

    /**
     * Gets replaceable Passports.
     *
     * @return the replaceable
     */
    public List<Passport> getReplaceable() {
        return getList(format("%s/find-replaceable", url));
    }
}
