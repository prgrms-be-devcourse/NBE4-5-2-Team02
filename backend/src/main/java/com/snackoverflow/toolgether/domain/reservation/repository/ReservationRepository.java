package com.snackoverflow.toolgether.domain.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackoverflow.toolgether.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByRenter_Id(Long renterId);
	List<Reservation> findByOwner_Id(Long ownerId);
}
