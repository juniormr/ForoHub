package com.forohubjr.Forohub.domain.topic;

public record TopicsListData(
        Long id,
        String title,
        String message,
        String course
) {
    public TopicsListData(Topic topic){
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getCourse());
    }

}
