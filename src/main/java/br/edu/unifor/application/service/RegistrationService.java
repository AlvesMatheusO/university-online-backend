package br.edu.unifor.application.service;

import java.time.Year;
import java.util.Random;
import java.util.function.Predicate;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationService {

    private final Random random = new Random();

    /**
     * Gera uma matrícula única baseada no ano atual + 5 dígitos.
     */
    public String generateUnique(Predicate<String> existsCheck) {

        String yearPrefix = String.valueOf(Year.now().getValue()).substring(2);
        String registration;

        do {
            int randomPart = random.nextInt(90000) + 10000; // Gera um número aleatório de 5 dígitos
            registration = yearPrefix + randomPart;
        } while (existsCheck.test(registration));
        return registration;
    }

}
