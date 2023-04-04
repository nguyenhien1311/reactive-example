package com.example.reactive.repositories;

import com.example.reactive.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

    Person sam = Person.builder().id(1).firstName("Rachel").lastName("Bruno").build();
    Person luisa = Person.builder().id(2).firstName("Rachel").lastName("Luisa").build();
    Person bruno = Person.builder().id(3).firstName("Rachel").lastName("Sam").build();
    Person isabella = Person.builder().id(4).firstName("Rachel").lastName("Isabella").build();

    @Override
    public Mono<Person> findById(Integer id) {
        return findAll().filter(person -> person.getId() == id).next().switchIfEmpty(Mono.error(() -> {
            throw new RuntimeException();
        }));
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(sam, luisa, bruno, isabella);
    }
}
