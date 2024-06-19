package com.forohubjr.Forohub.domain.topic;

import com.forohubjr.Forohub.domain.user.User;
import com.forohubjr.Forohub.domain.user.UserRepository;
import com.forohubjr.Forohub.infra.errors.IntegrityValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CreateTopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;

    public AnswerTopicData createTopic(SaveTopicData data) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User actualUser = userRepository.getReferenceById(authUser.getId());
        Optional<Topic> titleExist = topicRepository.findAll().stream()
                .filter(t -> t.getTitle().equals(data.title()))
                .filter(t -> t.getMessage().equals(data.message()))
                .findAny();
        if (titleExist.isPresent()) {
            throw new IntegrityValidation("This topic and message already exist");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
            LocalDateTime dateTime = LocalDateTime.now();
            String formattedDateTime = dateTime.format(formatter);

            Topic newTopic = new Topic(data);
            newTopic.setCreation(formattedDateTime);
            newTopic.setStatus("active");

            List<Topic> newTopics = actualUser.getTopics();
            newTopics.add(newTopic);
            actualUser.setTopics(newTopics);

            userRepository.save(actualUser);
            Long actualId = actualUser.getTopics().stream().filter(t -> t.getTitle().equals(newTopic.getTitle())).findFirst().get().getId();

            return new AnswerTopicData(actualId, newTopic.getTitle(), newTopic.getMessage(), newTopic.getStatus(), newTopic.getCourse());
        }
    }
}
