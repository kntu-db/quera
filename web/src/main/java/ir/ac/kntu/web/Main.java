package ir.ac.kntu.web;

import ir.ac.kntu.orm.repo.RepositoryFactory;

import javax.sql.DataSource;

public class Main {

    DataSource dataSource;

    public static void main(String[] args) {
        Person person = new Person();
        person.setId(1);
        person.setName("Ali");
        person.setAge(20);
        System.out.println(person);

        RepositoryFactory factory = new RepositoryFactory();

//        PersonRepository repository = factory.createRepository(PersonRepository.class);




    }
}
