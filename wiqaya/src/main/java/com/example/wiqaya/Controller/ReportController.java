package com.example.wiqaya.Controller;

import com.example.wiqaya.ApiResponse.ApiResponse;
import com.example.wiqaya.DTO.IN.ReportDTOIN;
import com.example.wiqaya.Repository.ReportRepository;
import com.example.wiqaya.Service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wiqaya/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(200).body( reportService.getAll());
    }

    @PostMapping("/create-report/engineerid/{engineerId}/requestinspectionid/{RequestInspectionId}")
    public ResponseEntity CreateReport( @PathVariable Integer engineerId,@PathVariable Integer RequestInspectionId,@RequestBody @Valid ReportDTOIN reportDTOIN){
        reportService.CreateReport(engineerId,RequestInspectionId,reportDTOIN);
        return ResponseEntity.status(200).body(new ApiResponse("report created successfully"));
    }


    @DeleteMapping ("/delete/report-id/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        reportService.delete(id);
        return ResponseEntity.status(200).body(new ApiResponse("report deleted"));
    }

    @PutMapping("/publish-report/user/{user}/report/{report}")
    public ResponseEntity publishReport(@PathVariable Integer user, @PathVariable Integer report){
        reportService.publishReport(user, report);
        return ResponseEntity.status(200).body(new ApiResponse("report published"));
    }

    @GetMapping("/my-reports/{userId}")
    public ResponseEntity<?> getMyReports(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(reportService.getMyReports(userId));
    }

}
