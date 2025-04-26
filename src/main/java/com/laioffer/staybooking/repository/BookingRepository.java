package com.laioffer.staybooking.repository;


import com.laioffer.staybooking.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;


public interface BookingRepository extends JpaRepository<BookingEntity, Long> {


    List<BookingEntity> findAllByGuestId(long guestId);


    List<BookingEntity> findAllByListingId(long listingId);

    // find all bookings that overlap with the given date range; native query enable to use native SQL
    @Query(value = "SELECT * FROM bookings WHERE listing_id = :listingId AND check_in_date < :checkOut AND check_out_date > :checkIn", nativeQuery = true)
    List<BookingEntity> findOverlappedBookings(long listingId, LocalDate checkIn, LocalDate checkOut);

    // given a listing id and a date, check if there is any booking that overlaps with the given date
    boolean existsByListingIdAndCheckOutDateAfter(long listingId, LocalDate date);
}
