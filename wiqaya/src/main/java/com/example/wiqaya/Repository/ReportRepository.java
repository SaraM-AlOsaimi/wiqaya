package com.example.wiqaya.Repository;

import com.example.wiqaya.Model.Report;
import com.example.wiqaya.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Integer> {

    Report findReportById(Integer id);

    @Query("select r from Report r where r.isPublished=true and r.house.city=?1")
    List<Report> findAllByIsPublishedAndCity(String city);


    // query to get the reports that done by one eng and its checked
    @Query("select r from Report r where r.engineer.id=?1 and r.house.status='checked'")
    List<Report> findAllByEngineerId(Integer engineerId);

    @Query("select r from Report r where r.house.user.id=?1")
    List<Report> findReportsForTheSameUser(Integer userId);
}
