package com.example.demo.service;

import com.example.demo.config.AppProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class OllamaClient {
    private final RestClient rest;
    private final String model;


    public OllamaClient(AppProperties props) {
        this.rest = RestClient.create(props.getOllama().getBaseUrl());
        this.model = props.getOllama().getEmbedModel();
    }


    public float[] embed(String text) {
        record EmbReq(String model, String prompt) {}
        record EmbRes(List<Double> embedding) {}




        EmbRes res = rest.post()
                .uri("/api/embeddings")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new EmbReq(model, text))
                .retrieve()
                .body(EmbRes.class);


        float[] v = new float[res.embedding().size()];
        for (int i=0;i<v.length;i++) v[i] = res.embedding().get(i).floatValue();
        return v;
    }
}