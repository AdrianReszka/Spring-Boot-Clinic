package com.example.przychodnia;

import com.example.model.*;
import com.example.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Transactional
@Component
public class DataLoader implements CommandLineRunner {

    private final PacjentRepository pacjentRepo;
    private final LekarzRepository lekarzRepo;
    private final ChorobaRepository chorobaRepo;
    private final ObjawRepository objawRepo;
    private final WizytaRepository wizytaRepo;
    private final PacjentChorobaRepository pcRepo;
    private final PacjentObjawRepository poRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public DataLoader(PacjentRepository pacjentRepo,
                      LekarzRepository lekarzRepo,
                      ChorobaRepository chorobaRepo,
                      ObjawRepository objawRepo,
                      WizytaRepository wizytaRepo,
                      PacjentChorobaRepository pcRepo,
                      PacjentObjawRepository poRepo) {
        this.pacjentRepo = pacjentRepo;
        this.lekarzRepo = lekarzRepo;
        this.chorobaRepo = chorobaRepo;
        this.objawRepo = objawRepo;
        this.wizytaRepo = wizytaRepo;
        this.pcRepo = pcRepo;
        this.poRepo = poRepo;
    }

    @Override
    public void run(String... args) {
        System.out.println(">>> DataLoader startuje – czyszczenie bazy");

        // Usuwanie danych
        poRepo.deleteAll();
        pcRepo.deleteAll();
        wizytaRepo.deleteAll();
        objawRepo.deleteAll();
        chorobaRepo.deleteAll();
        lekarzRepo.deleteAll();
        pacjentRepo.deleteAll();

        // Wymuszenie wykonania delete'ów
        poRepo.flush();
        pcRepo.flush();
        wizytaRepo.flush();
        objawRepo.flush();
        chorobaRepo.flush();
        lekarzRepo.flush();
        pacjentRepo.flush();

        // Reset AUTO_INCREMENT (IDENTITY) dla każdej tabeli
        entityManager.createNativeQuery("ALTER TABLE pacjenci ALTER COLUMN id_pacjenta RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE lekarze ALTER COLUMN id_lekarza RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE choroby ALTER COLUMN id_choroby RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE objawy ALTER COLUMN id_objawu RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE wizyty ALTER COLUMN id_wizyty RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE pacjent_choroba ALTER COLUMN id_pacjent_choroba RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE pacjent_objaw ALTER COLUMN id_pacjent_objaw RESTART WITH 1").executeUpdate();

        System.out.println(">>> Baza została wyczyszczona");

        // Wczytywanie danych – bez zmian
        Pacjent p1 = new Pacjent();
        p1.setImie("Jan");
        p1.setNazwisko("Kowalski");
        p1.setPesel("80010112345");
        p1.setDataUrodzenia(LocalDate.of(1980, 1, 1));
        p1.setAdres("ul. Kwiatowa 1");
        p1.setNumerTelefonu("123456789");
        p1.setEmail("jan.kowalski@example.com");
        pacjentRepo.save(p1);

        Pacjent p2 = new Pacjent();
        p2.setImie("Maria");
        p2.setNazwisko("Wiśniewska");
        p2.setPesel("90020254321");
        p2.setDataUrodzenia(LocalDate.of(1990, 2, 2));
        p2.setAdres("ul. Słoneczna 5");
        p2.setNumerTelefonu("987654321");
        p2.setEmail("maria.wisniewska@example.com");
        pacjentRepo.save(p2);

        Pacjent p3 = new Pacjent();
        p3.setImie("Paweł");
        p3.setNazwisko("Zieliński");
        p3.setPesel("85031567890");
        p3.setDataUrodzenia(LocalDate.of(1985, 3, 15));
        p3.setAdres("ul. Leśna 12");
        p3.setNumerTelefonu("555123456");
        p3.setEmail("pawel.zielinski@example.com");
        pacjentRepo.save(p3);

        Lekarz l1 = new Lekarz();
        l1.setImie("Agnieszka");
        l1.setNazwisko("Nowak");
        l1.setSpecjalizacja("Internista");
        l1.setEmail("agnieszka.nowak@szpital.pl");
        lekarzRepo.save(l1);

        Lekarz l2 = new Lekarz();
        l2.setImie("Tomasz");
        l2.setNazwisko("Lewandowski");
        l2.setSpecjalizacja("Kardiolog");
        l2.setEmail("tomasz.lewandowski@szpital.pl");
        lekarzRepo.save(l2);

        Lekarz l3 = new Lekarz();
        l3.setImie("Ewa");
        l3.setNazwisko("Kaczmarek");
        l3.setSpecjalizacja("Pediatra");
        l3.setEmail("ewa.kaczmarek@przychodnia.pl");
        lekarzRepo.save(l3);

        Choroba c1 = new Choroba();
        c1.setNazwa("Nadciśnienie");
        c1.setOpis("Podwyższone ciśnienie tętnicze");
        chorobaRepo.save(c1);

        Choroba c2 = new Choroba();
        c2.setNazwa("Cukrzyca typu 2");
        c2.setOpis("Zaburzenia metabolizmu glukozy");
        chorobaRepo.save(c2);

        Choroba c3 = new Choroba();
        c3.setNazwa("Astma oskrzelowa");
        c3.setOpis("Przewlekłe zapalenie dróg oddechowych");
        chorobaRepo.save(c3);

        Objaw o1 = new Objaw();
        o1.setNazwa("Ból głowy");
        o1.setOpis("Umiarkowany ból trwający kilka godzin");
        objawRepo.save(o1);

        Objaw o2 = new Objaw();
        o2.setNazwa("Duszność");
        o2.setOpis("Trudności w oddychaniu przy wysiłku");
        objawRepo.save(o2);

        Objaw o3 = new Objaw();
        o3.setNazwa("Ból w klatce piersiowej");
        o3.setOpis("Ostry, kłujący ból przy wysiłku");
        objawRepo.save(o3);

        Wizyta w1 = new Wizyta();
        w1.setPacjent(p1);
        w1.setLekarz(l1);
        w1.setTermin(LocalDateTime.now().plusDays(1));
        wizytaRepo.save(w1);

        Wizyta w2 = new Wizyta();
        w2.setPacjent(p2);
        w2.setLekarz(l2);
        w2.setTermin(LocalDateTime.now().plusDays(2).plusHours(3));
        wizytaRepo.save(w2);

        Wizyta w3 = new Wizyta();
        w3.setPacjent(p3);
        w3.setLekarz(l3);
        w3.setTermin(LocalDateTime.now().plusDays(5).withHour(10).withMinute(0));
        wizytaRepo.save(w3);

        PacjentChoroba pc1 = new PacjentChoroba();
        pc1.setWizyta(w1);
        pc1.setChoroba(c1);
        pcRepo.save(pc1);

        PacjentChoroba pc2 = new PacjentChoroba();
        pc2.setWizyta(w2);
        pc2.setChoroba(c2);
        pcRepo.save(pc2);

        PacjentChoroba pc3 = new PacjentChoroba();
        pc3.setWizyta(w3);
        pc3.setChoroba(c3);
        pcRepo.save(pc3);

        PacjentObjaw po1 = new PacjentObjaw();
        po1.setPacjent(p1);
        po1.setObjaw(o1);
        po1.setDataWystapienia(LocalDate.now());
        poRepo.save(po1);

        PacjentObjaw po2 = new PacjentObjaw();
        po2.setPacjent(p2);
        po2.setObjaw(o3);
        po2.setDataWystapienia(LocalDate.now().minusDays(1));
        poRepo.save(po2);

        PacjentObjaw po3 = new PacjentObjaw();
        po3.setPacjent(p3);
        po3.setObjaw(o2);
        po3.setDataWystapienia(LocalDate.now().minusDays(2));
        poRepo.save(po3);

        System.out.println(">>> Pacjenci zapisani: " + pacjentRepo.count());
    }
}
