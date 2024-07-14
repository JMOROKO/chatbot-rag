package com.silstechnologie.chatbotrag.services;

import com.silstechnologie.chatbotrag.entities.Person;
import com.silstechnologie.chatbotrag.repositories.PersonRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;

@BrowserCallable
@AnonymousAllowed
public class PersonService extends CrudRepositoryService<Person, Long, PersonRepository> {
}
