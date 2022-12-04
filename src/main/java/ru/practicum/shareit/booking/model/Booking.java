package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class Booking {
    Integer id;
    // дата и время начала бронирования
    LocalDate start;
    // дата и время конца бронирования
    LocalDate end;
    // вещь, которую пользователь бронирует
    Item item;
    // пользователь, который осуществляет бронирование
    User booker;
    /*
    статус бронирования.

    Может принимать одно из следующих
    значений:
    WAITING — новое бронирование, ожидает одобрения,
    APPROVED — бронирование подтверждено владельцем,
    REJECTED — бронирование отклонено владельцем,
    CANCELED — бронирование отменено создателем.
    */
    Status status;
}


enum Status {
    // новое бронирование, ожидает одобрения
    WAITING,
    // бронирование подтверждено владельцем
    APPROVED,
    // бронирование отклонено владельцем
    REJECTED,
    // бронирование отменено создателем
    CANCELED
}