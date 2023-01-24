package ru.practicum.shareit.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    // ткст запроса, содержащий описание требуемой вещи
    String description;
    // пользователь, создавший запрос
    Integer requestor;
    // дата и время создания запроса
    LocalDateTime created;

    @ManyToMany
    @JoinTable(name = "request_item",
        joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns =  @JoinColumn(name = "item_id")
    )
    Set<Item> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        return id != null && id.equals(((Request) o).getId());
    }

}
