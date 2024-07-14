package com.silstechnologie.chatbotrag;

import com.silstechnologie.chatbotrag.entities.Person;
import com.silstechnologie.chatbotrag.repositories.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class ChatbotRagApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbotRagApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(PersonRepository personRepository){
		return args -> {
			for(int i=0; i<=100; i++){
				Person person = Person.builder()
						.nom(UUID.randomUUID().toString().substring(0,8))
						.email(UUID.randomUUID().toString().substring(0,8)+"@gmail.com")
						.build();
				personRepository.save(person);
			}
		};
	}
}
