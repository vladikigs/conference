package com.waveaccess.conference.repository;


import com.waveaccess.conference.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByAudience(String audience);
}
