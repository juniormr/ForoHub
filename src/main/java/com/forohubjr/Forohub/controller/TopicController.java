package com.forohubjr.Forohub.controller;

import com.forohubjr.Forohub.domain.topic.*;
import com.forohubjr.Forohub.infra.errors.IntegrityValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/topics")
@SecurityRequirement(name = "bearer-key")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CreateTopicService createService;

    private List<Topic> topics;

    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static
    class MyConfiguration {
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AnswerTopicData> saveTopic(@RequestBody @Valid SaveTopicData saveTopicData,
                                                     UriComponentsBuilder uriComponentsBuilder) throws IntegrityValidation {
        var response = createService.createTopic(saveTopicData);
        URI url = uriComponentsBuilder.path("/topics/{id}").buildAndExpand(response.Id()).toUri();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TopicsListData>> topicsList(@PageableDefault(size = 2) Pageable pageable) {
        return ResponseEntity.ok(topicRepository.findAll(pageable).map(TopicsListData::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerTopicData> getTopicData(@PathVariable Long id) {
        Topic topic = topicRepository.getReferenceById(id);
        if (topicRepository.existsById(id)) {
            var topicData = new AnswerTopicData(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getStatus(), topic.getCourse());
            return ResponseEntity.ok(topicData);
        } else {
            throw new IntegrityValidation("Topic with this Id doesn't exist");
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<AnswerTopicData> updateTopic(@PathVariable Long id, @RequestBody @Valid UpdateTopicData updateTopicData) {
        if (topicRepository.existsById(id)) {
            Topic topic = topicRepository.getReferenceById(id);
            topic.updateTopic(updateTopicData);
            AnswerTopicData answerTopicData = new AnswerTopicData(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getStatus(), topic.getCourse());
            return ResponseEntity.ok(answerTopicData);
        } else {
            throw new IntegrityValidation("Topic with this Id doesn't exist");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteTopicData(@PathVariable Long id) {
        if (topicRepository.existsById(id)) {
            System.out.println("deleting");
            topicRepository.deleteTopic(id);
            return ResponseEntity.ok("Deleted topic sucessfully");
        } else {
            throw new IntegrityValidation("Topic with this Id doesn't exist");
        }

    }

}
