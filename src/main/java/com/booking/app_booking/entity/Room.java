package com.booking.app_booking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.booking.app_booking.enums.RoomType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Número de habitación requerido")
    @Column(unique = true)
    private Integer roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @DecimalMin(value = "0.1", message = "Precio por noche requerido")
    private BigDecimal pricePerNight;

    @Min(value = 1, message = "Capacidad de habitación requerida")
    private Integer roomCapacity;

    private String description;
    private String imageUrl;


    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

}
