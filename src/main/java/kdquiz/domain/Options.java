package kdquiz.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private int time=30; //시간

    @Column
    private Boolean useHint=true; //힌트여부

    @Column
    private int hintTime=30; //힌트 시간

    @Column
    private String hintContent="힌트"; //힌트질문

    @Column
    private Boolean useAiFeedback=true; //Ai여부

    @Column
    private String aiQuestion="ai 피드백";//ai질문

    @Column
    private String commentary="코멘트 피드백"; //해설

    @Column
    private int score=20; //점수

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
