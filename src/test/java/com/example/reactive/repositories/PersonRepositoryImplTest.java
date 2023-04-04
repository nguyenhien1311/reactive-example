package com.example.reactive.repositories;

import com.example.reactive.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

class PersonRepositoryImplTest {

    PersonRepositoryImpl repository = new PersonRepositoryImpl();

    @Test
    void findByIdBlocking() {
        System.out.println("findByIdBlocking");
        repository.findById(11).subscribe(System.out::println, throwable -> System.out.println("No such person"));
    }

    @Test
    void findByIdNonBlocking() {
        System.out.println("findByIdNonBlocking");
        repository.findById(1).subscribe(System.out::println, throwable -> System.out.println("No such person"));
    }

    @Test
    void findByIdOperation() {
        System.out.println("findByIdOperation");
        Mono<Person> personMono = repository.findById(1);
        personMono.map(Person::getLastName).subscribe(System.out::println);
    }

    @Test
    void findAllBlocking() {
        System.out.println("findAllBlocking");
        Flux<Person> personFlux = repository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person);
    }

    @Test
    void findAllNonBlocking() {
        System.out.println("findAllNonBlocking");
        Flux<Person> personFlux = repository.findAll();
        personFlux.subscribe(person -> System.out.println(person.getLastName()));
    }

    @Test
    void findAllOperation() {
        System.out.println("findAllOperation");
        Flux<Person> personFlux = repository.findAll();
        personFlux.map(Person::getLastName).subscribe(System.out::println);
    }

    @Test
    void findAllFluxToMono() {
        System.out.println("findAllFluxToMono");
        Flux<Person> personFlux = repository.findAll();
        Mono<List<Person>> listMono = personFlux.collectList();
        listMono.subscribe(people -> people.forEach(System.out::println));
    }

    @Test
    void findAllFluxFilter() {
        System.out.println("findAllFluxFilter");
        repository.findAll().filter(person -> person.getLastName().equals("Sam")).subscribe(person -> System.out.println(person.getLastName()));
    }

    @Test
    void findAllFluxFilter2() {
        System.out.println("findAllFluxFilter2");
        Mono<Person> personMono = repository.findAll().filter(person -> person.getFirstName().equals("Rachel")).next();
        personMono.subscribe(System.out::println);
    }

    @Test
    void findByIdNotFound() {
        System.out.println("findByIdNotFound");
        Flux<Person> flux = repository.findAll();
        int id = 9;

        Mono<Person> mono = flux.filter(person -> person.getId() == id).single()
                .doOnError(throwable -> {
                    System.out.println("Loi trong flux");
                    System.out.println(throwable.getMessage());
                });

        mono.subscribe(person -> System.out.println(person.getLastName()), throwable -> {
            System.out.println("Loi trong mono");
            System.out.println(throwable.getMessage());
        });
    }

}