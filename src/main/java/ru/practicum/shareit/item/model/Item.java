package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.Request;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String description;
    Boolean available;
    Integer owner;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "items")
    Set<Request> requests;
}
