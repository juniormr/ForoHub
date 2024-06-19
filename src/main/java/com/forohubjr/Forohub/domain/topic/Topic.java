package com.forohubjr.Forohub.domain.topic;

//import com.forohubjr.Forohub.reply.Reply;
//import com.forohubjr.Forohub.user.User;
import com.forohubjr.Forohub.domain.user.User;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

@Table(name = "topics")
@Entity(name = "Topic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "Id")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    private String message;
    private String status;
    private String course;
    private String creation;

//    private String status;
//
    @ManyToOne
    private User user;


    public Topic(SaveTopicData saveTopicData) {
        this.title = saveTopicData.title();
        this.message= saveTopicData.message();
        this.course = saveTopicData.course();
    }

    public void updateTopic(UpdateTopicData updateTopicData){
        if(updateTopicData.title() != null){
            this.title = updateTopicData.title();
        }
        if(updateTopicData.message() != null){
            this.message = updateTopicData.message();
        }
        if(updateTopicData.status() != null){
            this.status = updateTopicData.status();
        }
        if(updateTopicData.course() != null){
            this.course = updateTopicData.course();
        }
    }

    @Override
    public String toString() {
        return "Topic" +
                " Id=" + Id +
                ", title='" + title +
                ", message='" + message  +
                ", course='" + course  +
                ", creation='" + creation ;
    }
}
