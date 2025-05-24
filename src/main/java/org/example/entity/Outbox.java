package org.example.entity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.TypesOfEvent;

@Entity
@Getter
@Table(name = "outbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Embedded
    private String value;

    public Outbox(Long id, TypesOfEvent event) {
        if (id == null || event == null) {
            throw new IllegalArgumentException("Неправльные значения, необоходимы реальные id и action");
        }
        this.value = (id + " ") + event.name();
    }
}
