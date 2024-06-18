package com.forohubjr.Forohub.controller;

import com.forohubjr.Forohub.domain.topic.*;
import com.forohubjr.Forohub.domain.user.User;
import com.forohubjr.Forohub.domain.user.UserRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/topics")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;

    private List<Topic> topics;

    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static
    class MyConfiguration {
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AnswerTopicData> saveTopic(@RequestBody @Valid SaveTopicData saveTopicData) {

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User actualUser = userRepository.getReferenceById(authUser.getId());

        Topic newTopic = new Topic(saveTopicData);
        newTopic.setCreation(String.valueOf(LocalDateTime.now()));

        List<Topic> newTopics = actualUser.getTopics();
        newTopics.add(newTopic);
        actualUser.setTopics(newTopics);

        userRepository.save(actualUser);
        System.out.println(actualUser);
        AnswerTopicData answerTopicData = new AnswerTopicData(newTopic.getId(), newTopic.getTitle(), newTopic.getMessage(), newTopic.getCourse());

        return ResponseEntity.ok().body(answerTopicData);
    }

    @GetMapping
    public ResponseEntity<Page<TopicsListData>> topicsList(@PageableDefault(size = 2) Pageable pageable) {
        return ResponseEntity.ok(topicRepository.findAll(pageable).map(TopicsListData::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerTopicData> getTopicData(@PathVariable Long id) {
        Topic topic = topicRepository.getReferenceById(id);
        var topicData = new AnswerTopicData(topic.getId(), topic.getTitle(), topic.getMessage(),
                topic.getCourse());
        return ResponseEntity.ok(topicData);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateTopic(@PathVariable Long id, @RequestBody @Valid UpdateTopicData updateTopicData) {
        if (topicRepository.existsById(id)) {
            Topic topic = topicRepository.getReferenceById(id);
            topic.updateTopic(updateTopicData);
            AnswerTopicData answerTopicData = new AnswerTopicData(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getCourse());

            return ResponseEntity.ok(answerTopicData);
        } else {
            return ResponseEntity.badRequest().body("Id doesn't exist");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTopicData(@PathVariable Long id) {
        if (topicRepository.existsById(id)) {
            topicRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Id doesn't exist");
        }

    }

}
