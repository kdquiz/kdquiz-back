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
    private int time=10; //시간

    @Column
    private Boolean useHint=false; //힌트여부

    @Column
    private int hintTime=0; //힌트 시간

    @Column
    private String hintContent; //힌트질문

    @Column
    private Boolean useAiFeedback=false; //Ai여부

    @Column
    private String aiQuestion;//ai질문

    @Column
    private String commentary; //해설

    @Column
    private int score=0; //점수

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;
}
