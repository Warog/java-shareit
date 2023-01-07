package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemBookingDto {
   private Integer id;
   private Integer bookerId;
}
