package com.example.demo.controller;

import com.example.demo.model.Question;
import com.example.demo.model.Quiz;
import com.example.demo.repo.QuestionRepository;
import com.example.demo.repo.QuizRepository;
import com.example.demo.service.EmbeddingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final QuizRepository quizzes;
    private final QuestionRepository questions;
    private final EmbeddingService embeddingService;


    @GetMapping
    public String admin(Model m) { m.addAttribute("quizzes", quizzes.findAll()); return "admin"; }


    // Простая форма: имя и список Q/A в формате: ВОПРОС\nОТВЕТ\n--- (разделитель)
    @PostMapping("/upload")
    @Transactional
    public String upload(@RequestParam("name") String name, @RequestParam("payload") String payload) {
        Quiz quiz = new Quiz();
        quiz.setName(name);
        quiz = quizzes.save(quiz);
        String[] blocks = payload.split("[\r\n]+---[\n\r]+");
        for (String b : blocks) {
            String[] qa = b.trim().split("[\n\r]+", 2);
            if (qa.length<2) continue;
            Question q = new Question();
            q.setQuiz(quiz); q.setQuestionText(qa[0].trim()); q.setAnswerText(qa[1].trim());
            q = questions.save(q);
            embeddingService.computeAndStoreForQuestion(q);
        }
        return "redirect:/admin";
    }
}