package kdquiz.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Embeddable
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuizImg {
    @Id
    private String fileName;

    @Column
    private Long questionId;

}
