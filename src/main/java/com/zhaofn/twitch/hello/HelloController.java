package com.zhaofn.twitch.hello;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController//告诉spring这是一个rest controller
public class HelloController {//都是由springboot创建，并且call里面的函数
    //get post put patch delete
    @GetMapping("/hello")
    public Person sayHello(@RequestParam(required = false) String locale) {//问号后面的东西就会填到@RequestParam这个参数里， required = false表示这个变量不是必要的
        if (locale == null) {
            locale = "en_US";
        }
        Faker faker = new Faker(new Locale(locale));
        String name = faker.name().fullName();
        String company = faker.company().name();
        String street = faker.address().streetAddress();
        String city = faker.address().city();
        String state = faker.address().state();
        String bookTitle = faker.book().title();
        String bookAuthor = faker.book().author();

        return new Person(name, company, new Address(street, city, state, null), new Book(bookTitle, bookAuthor));
    }

}
