package codeit.gatcha.api.security.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name="creation_date", nullable = false)
    private LocalDateTime creationDate;
}
