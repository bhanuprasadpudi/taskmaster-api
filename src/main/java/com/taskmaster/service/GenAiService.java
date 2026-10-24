package com.taskmaster.service;
import com.taskmaster.dto.GenAiDtos.GenerateRequest;
import com.taskmaster.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.util.Map;
@Service @RequiredArgsConstructor
public class GenAiService {
    private final RestClient.Builder client;
    @Value("${app.genai.base-url:}") private String baseUrl;
    @Value("${app.genai.api-key:}") private String apiKey;
    @Value("${app.genai.model:gpt-4o-mini}") private String model;
    public String generate(GenerateRequest request){
        if(baseUrl==null||baseUrl.isBlank()) return "Objective: "+request.prompt().trim()+". Define the expected outcome, acceptance criteria, dependencies, and a clear completion condition.";
        try{Map<?,?> body=client.baseUrl(baseUrl).defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer "+apiKey).build().post().uri("/chat/completions").contentType(MediaType.APPLICATION_JSON).body(Map.of("model",model,"messages",java.util.List.of(Map.of("role","user","content","Write a concise actionable task description for: "+request.prompt())))).retrieve().body(Map.class);Object choices=body==null?null:body.get("choices");if(!(choices instanceof java.util.List<?> list)||list.isEmpty())throw new ApiException(HttpStatus.BAD_GATEWAY,"GenAI returned no choices");Object message=((Map<?,?>)list.get(0)).get("message");return String.valueOf(((Map<?,?>)message).get("content"));}catch(RestClientException|ClassCastException e){throw new ApiException(HttpStatus.BAD_GATEWAY,"GenAI provider request failed");}
    }
}
