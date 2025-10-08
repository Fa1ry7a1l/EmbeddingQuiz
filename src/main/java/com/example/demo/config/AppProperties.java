package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app")
@Component
@Getter
@Setter
public class AppProperties {
    @Getter
    @Setter
    public static class Ollama {
        private String baseUrl;
        private String embedModel;
    }
    @Getter
    @Setter
    public static class Similarity {
        private double threshold = 0.75; // по косинусу (similarity)
    }
    private Ollama ollama = new Ollama();
    private Similarity similarity = new Similarity();
}
