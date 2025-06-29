package com.lawlink.repository;

import com.lawlink.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Find all appointments for a specific client
     */
    List<Appointment> findByClientIdOrderByAppointmentDateTimeDesc(Long clientId);

    /**
     * Find all appointments for a specific lawyer
     */
    List<Appointment> findByLawyerIdOrderByAppointmentDateTimeDesc(Long lawyerId);

    /**
     * Find appointments by status
     */
    List<Appointment> findByStatusOrderByAppointmentDateTimeAsc(Appointment.AppointmentStatus status);

    /**
     * Find upcoming appointments for a client
     */
    @Query("SELECT a FROM Appointment a WHERE a.client.id = :clientId " +
           "AND a.appointmentDateTime > :now " +
           "AND a.status IN ('PENDING', 'CONFIRMED') " +
           "ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findUpcomingAppointmentsByClientId(@Param("clientId") Long clientId, 
                                                        @Param("now") LocalDateTime now);

    /**
     * Find upcoming appointments for a lawyer
     */
    @Query("SELECT a FROM Appointment a WHERE a.lawyer.id = :lawyerId " +
           "AND a.appointmentDateTime > :now " +
           "AND a.status IN ('PENDING', 'CONFIRMED') " +
           "ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findUpcomingAppointmentsByLawyerId(@Param("lawyerId") Long lawyerId, 
                                                        @Param("now") LocalDateTime now);

    /**
     * Find appointments within a date range for a lawyer (for availability checking)
     */
    @Query("SELECT a FROM Appointment a WHERE a.lawyer.id = :lawyerId " +
           "AND a.appointmentDateTime < :endTime " +
           "AND (a.appointmentDateTime + FUNCTION('MINUTE', a.durationMinutes)) > :startTime " +
           "AND a.status IN ('PENDING', 'CONFIRMED')")
    List<Appointment> findConflictingAppointments(@Param("lawyerId") Long lawyerId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * Find appointments for a specific date for a lawyer
     */
    @Query("SELECT a FROM Appointment a WHERE a.lawyer.id = :lawyerId " +
           "AND DATE(a.appointmentDateTime) = DATE(:date) " +
           "AND a.status IN ('PENDING', 'CONFIRMED') " +
           "ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findAppointmentsByLawyerAndDate(@Param("lawyerId") Long lawyerId,
                                                     @Param("date") LocalDateTime date);

    /**
     * Find past appointments for rating purposes
     */
    @Query("SELECT a FROM Appointment a WHERE a.client.id = :clientId " +
           "AND a.lawyer.id = :lawyerId " +
           "AND a.appointmentDateTime < :now " +
           "AND a.status = 'COMPLETED'")
    List<Appointment> findCompletedAppointmentsBetweenClientAndLawyer(@Param("clientId") Long clientId,
                                                                     @Param("lawyerId") Long lawyerId,
                                                                     @Param("now") LocalDateTime now);

    /**
     * Count appointments by status for a lawyer
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.lawyer.id = :lawyerId AND a.status = :status")
    Long countAppointmentsByLawyerAndStatus(@Param("lawyerId") Long lawyerId, 
                                           @Param("status") Appointment.AppointmentStatus status);

    /**
     * Find appointments that need to be marked as completed (past appointments with CONFIRMED status)
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime < :cutoffTime " +
           "AND a.status = 'CONFIRMED'")
    List<Appointment> findAppointmentsToMarkCompleted(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Find appointments that need to be marked as no-show
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime < :cutoffTime " +
           "AND a.status = 'PENDING'")
    List<Appointment> findAppointmentsToMarkNoShow(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Check if a client has any upcoming appointments with a lawyer
     */
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.client.id = :clientId " +
           "AND a.lawyer.id = :lawyerId " +
           "AND a.appointmentDateTime > :now " +
           "AND a.status IN ('PENDING', 'CONFIRMED')")
    boolean hasUpcomingAppointment(@Param("clientId") Long clientId, 
                                  @Param("lawyerId") Long lawyerId, 
                                  @Param("now") LocalDateTime now);

    /**
     * Find appointments within a specific time range
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startTime AND :endTime " +
           "ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findAppointmentsBetween(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}