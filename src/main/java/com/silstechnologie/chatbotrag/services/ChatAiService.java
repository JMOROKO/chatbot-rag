package com.silstechnologie.chatbotrag.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@BrowserCallable
@AnonymousAllowed //permet de ne pas passer par l'authentification pour utiliser les méthodes de cette class dans mes composant vaadin(react)
public class ChatAiService {
    //permet d'interagir avec un LLM en utilisant du texte
    private ChatClient chatClient;
    private VectorStore vectorStore;
    @Value("classpath:prompts/prompt-template.st")
    private Resource resource;

    public ChatAiService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public String ragChat(String question){
        //faire une recherche sémentique dans le vecteur store généré
        List<Document> documents = vectorStore.similaritySearch(question);
        //recherche generation du contexte
        List<String> context = documents.stream().map(Document::getContent).toList();
        //creation du prompt template
        PromptTemplate promptTemplate = new PromptTemplate(resource);

        //remplacer les paramètres du fichier par les bonnes informations
        Prompt prompt = promptTemplate.create(Map.of("context", context, "question", question));
        return chatClient.prompt(prompt)
                //.user(question)
                .call()
                .content();
    }
}
