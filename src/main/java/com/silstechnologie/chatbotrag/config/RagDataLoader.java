package com.silstechnologie.chatbotrag.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
public class RagDataLoader {
    @Value("classpath:/pdfs/cv.pdf")
    private Resource pdfRessource;

    @Value("store-data-v2.json")
    private String storeFile;

    //si une erreur survient à ce niveau il faut aller dans le terminal et faire : ollama run mistral
    //@TODO c'est bien llama3 qui est désigné comme modèle à utiliser mais pourquoi le système ne comprends pas et cherche à utilisé mistral?
    //après avoir fait ce script lorsque tu lance l'application il faut attendre suffisament de temps en fonction de la puissance de ta machine pour voir le résultat

    //utilisation de fichier
    //@Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        String fileStore = Path.of("src", "main", "resources", "store").toAbsolutePath()+"/"+storeFile;
        File file = new File(fileStore);

        //si le fichier n'existe pas il faut le créer et le sauvegarder dans le creer et le sauvegarder puis le sauvegarder en DB
        if(!file.exists()){
            PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdfRessource);
            //recuperer chaque page du fichier pdf dans une liste
            List<Document> documents = pdfDocumentReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            //diviser chaque page en petit bout de fichier
            List<Document> chumks = textSplitter.split(documents);
            //savegarder les informations dans le vectore store
            vectorStore.accept(chumks);

            //sauvegarder les chumks dans le fichier
            vectorStore.save(file);
        }
        else{
            //si les information du pdf son déjà chargé dans la base il faut juste les récupérer
            vectorStore.load(file);
        }
        return vectorStore;
    }

    //utilisation de pg vectore store
    //@PostConstruct
    public void initStore(){

    }
}
