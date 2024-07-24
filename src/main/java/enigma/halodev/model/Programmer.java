package enigma.halodev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "programmers")
public class Programmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private Availability availability;

    @Column(nullable = false)
    private Double price;

    // virtual row
    @ManyToMany
    @JoinTable(
            name = "programmers_skills",
            joinColumns = @JoinColumn(name = "programmer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonIgnore
    private List<Skill> skills;

    @OneToMany(mappedBy = "programmer")
    private List<Session> sessions;
}
