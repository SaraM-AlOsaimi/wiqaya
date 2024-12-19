package com.example.wiqaya.Repository;

import com.example.wiqaya.Model.Offer;
import com.example.wiqaya.Model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

    Offer findOfferById(Integer id);


    @Query("SELECT o FROM Offer o WHERE o.report = :report")
    List<Offer> findOffersByReport(Report report);

    List<Offer> findOffersByReportId(Integer id);

    @Query("select o from Offer o where o.report.house.user.id=?1")
    List<Offer> findOffersByUserId(Integer userId);

    @Query("select o from Offer o where o.report.house.user.id=?1 and o.serviceProvider.id=?2")
    List<Offer> findCompletedOffersByUserAndProvider(Integer userId,Integer providerId);

 List<Offer> findOffersByServiceProviderId(Integer serviceProviderId);
}


