package com.example.demo.controller;

import com.example.demo.repo.QuestionRepository;
import com.example.demo.repo.QuizRepository;
import com.example.demo.service.SimilarityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {
    private final QuizRepository quizzes;
    private final QuestionRepository questions;
    private final SimilarityService similarityService;


    @GetMapping("/{quizId}")
    public String start(@PathVariable("quizId") Long quizId, Model m) {
        var qs = questions.findByQuizId(quizId);
        m.addAttribute("quiz", quizzes.findById(quizId).orElseThrow());
        m.addAttribute("questions", qs);
        return "quiz";
    }


    @PostMapping("/{questionId}/answer")
    public String answer(@PathVariable("questionId") Long questionId, @RequestParam("userAnswer") String userAnswer, Model m) {
        boolean ok = similarityService.matches(questionId, userAnswer);
        m.addAttribute("ok", ok);
        m.addAttribute("userAnswer", userAnswer);
        m.addAttribute("q", questions.findById(questionId).orElseThrow());
        return "result";
    }
}