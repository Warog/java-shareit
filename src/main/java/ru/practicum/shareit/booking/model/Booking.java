package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "START_DATE")
    LocalDateTime start;
    @Column(name = "END_DATE")
    LocalDateTime end;
    @Column(name = "ITEM_ID")
    Integer itemId;
    @Column(name = "BOOKER_ID")
    Integer bookerId;
    @Enumerated(EnumType.STRING)
    BookingStatus.Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}


