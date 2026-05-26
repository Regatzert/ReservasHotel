package com.booking.app_booking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.booking.app_booking.entity.Room;
import com.booking.app_booking.enums.RoomType;

public interface RoomRepository  extends JpaRepository<Room, Long> {

    @Query("""
            SELECT r 
                FROM Room r 
                    WHERE r.id NOT IN (
                                        SELECT b.room.id 
                                        FROM Booking b 
                                        WHERE :checkInDate <= b.checkOutDate 
                                        AND :checkOutDate >= b.checkInDate
                                        AND b.bookingStatus IN ('RESERVADO', 'REGISTRO_ENTRADA')
                                    )
                    AND (:roomType IS NULL OR r.type = :roomType)
        """)    
    List<Room> findAvailableRooms(
                                @Param("checkInDate") LocalDate checkInDate, 
                                @Param("checkOutDate") LocalDate checkOutDate, 
                                @Param("roomType") RoomType roomType
                                );


    @Query("""
            SELECT r 
                FROM Room r 
                    WHERE CAST(r.roomNumber AS STRING) LIKE %:searchParam%
                    OR LOWER(r.type) LIKE LOWER(:searchParam)
                    OR CAST(r.pricePerNight AS STRING) LIKE %:searchParam%
                    OR CAST(r.capacity AS STRING) LIKE %:searchParam%
                    OR LOWER(r.description) LIKE LOWER(CONCAT('%', :searchParam, '%'))
        """)
    List<Room> searchRooms(@Param("searchParam") String searchParam);


    @Query("""
            SELECT DISTINCT r.type 
                FROM Room r 
        """)
    List<RoomType> getallRoomTypes();

}
