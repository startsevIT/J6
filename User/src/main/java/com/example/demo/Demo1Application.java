package com.example.demo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.nio.Buffer;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

@SpringBootApplication
public class Demo1Application {
    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

    @RestController
    public static class RepairRequestController {
        RepairRequestRepository repairRequests = new RepairRequestRepositoryImpl();

        @CrossOrigin
        @PostMapping(value = "/api/repair-requests/create")
        public void addRepairRequest(@RequestBody RepairRequestDTO repairRequestDTO) {
            RepairRequest repairRequest = new RepairRequest(repairRequestDTO.getId(),repairRequestDTO.getOrgTechType(), repairRequestDTO.getOrgTechModel(),
                    repairRequestDTO.getProblemDescryption(), repairRequestDTO.getFio(), repairRequestDTO.getPhoneNumber());
            repairRequests.addRepairRequest(repairRequest);
        }

        @CrossOrigin
        @GetMapping(value = "/api/repair-requests")
        public List<RepairRequest> getAllRepairRequests() {
            return repairRequests.getAll();
        }

        @PutMapping(value = "/api/repair-request/edit/{id}")
        public void editRepairRequest(@PathVariable int id, @RequestBody RepairRequestDTOU repairRequestDTOU) {
            RepairRequest repairRequest = repairRequests.getById(id);
            repairRequest.updateRequestStatus(repairRequestDTOU.getRequestStatus());
            repairRequest.updateProblemDescription(repairRequestDTOU.getProblemDescryption());
            repairRequest.updateMaster(repairRequestDTOU.getMaster());
            repairRequest.updateComment(repairRequestDTOU.getComment());
        }

        @GetMapping(value = "/api/statistics/completed-count")
        public int getCompletedCount() {
            return repairRequests.getCompletedCount();
        }

        @GetMapping(value = "/api/statistics/average-duration")
        public double getAverageDuration() {
            return repairRequests.getAverageDuration();
        }

        @GetMapping(value = "/api/statistics/problem-types")
        public Map<String, Integer> getProblemTypesStatistics() {
            return repairRequests.getProblemDescryptionStatistics();
        }
    }

    public static class RepairRequestDTO {
        @Getter @Setter private int id;
        @Getter @Setter private String orgTechType;
        @Getter @Setter private String orgTechModel;
        @Getter @Setter private String problemDescryption;
        @Getter @Setter private String fio;
        @Getter @Setter private String phoneNumber;
    }

    public static class RepairRequestDTOU {
        @Getter @Setter private String requestStatus;
        @Getter @Setter private String problemDescryption;
        @Getter @Setter private String master;
        @Getter @Setter private String comment;
    }

    public static class RepairRequest {
        @Getter @Setter private int id;
        @Getter @Setter private String orgTechType;
        @Getter @Setter private String orgTechModel;
        @Getter @Setter private String problemDescryption;
        @Getter @Setter private String fio;
        @Getter @Setter private String phoneNumber;
        @Getter @Setter private String requestStatus;
        @Getter @Setter private String master;
        @Getter @Setter private LocalDateTime startDate;
        @Getter @Setter private LocalDateTime completionDate;
        @Getter @Setter private String comment;

        public RepairRequest(int id, String orgTechType, String orgTechModel, String problemDescryption, String fio, String phoneNumber) {
            this.id = id;
            this.orgTechType = orgTechType;
            this.orgTechModel = orgTechModel;
            this.problemDescryption = problemDescryption;
            this.fio = fio;
            this.phoneNumber = phoneNumber;
            this.requestStatus = "Новая заявка";
            this.master = "Не назначен";
            this.startDate = LocalDateTime.now();
            this.completionDate = null;
            this.comment = "  ";
        }

        public void updateRequestStatus(String requestStatus) {
            this.requestStatus = requestStatus;
            if (requestStatus.equals("Завершена")) {
                this.completionDate = LocalDateTime.now();
            }
        }

        public void updateProblemDescription(String problemDescryption) {
            this.problemDescryption = problemDescryption;
        }

        public void updateMaster(String master) {
            this.master = master;
        }

        public void updateComment(String comment) {
            this.comment = comment;
        }

        public RepairRequest(int id, String orgTechType, String orgTechModel, String problemDescryption, String fio, String phoneNumber,String requestStatus, LocalDateTime startDate, String master, LocalDateTime completionDate, String comment) {
            this.id = id;
            this.orgTechType = orgTechType;
            this.orgTechModel = orgTechModel;
            this.problemDescryption = problemDescryption;
            this.fio = fio;
            this.phoneNumber = phoneNumber;
            this.requestStatus = requestStatus;
            this.master = master;
            this.startDate = startDate;
            this.completionDate = completionDate;
            this.comment = comment;
        }
    }

    public interface RepairRequestRepository {
        void addRepairRequest(RepairRequest repairRequest);

        List<RepairRequest> getAll();

        RepairRequest getById(int id);

        int getCompletedCount();

        double getAverageDuration();

        Map<String, Integer> getProblemDescryptionStatistics();
    }

    public static class RepairRequestRepositoryImpl implements RepairRequestRepository {
        private RepairRequest[] buffer = {
                new RepairRequest(1,"Компьютер","DEXP","Не работает","Васили димитров 2", "8589979479","Завершена", LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40), "Dbvjdswd",LocalDateTime.of(2015, Month.JULY, 29, 23, 50, 40), "tretew"),
                new RepairRequest(2,"Компьютер","-","-","- 2--", "8589979479","Завершена", LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40), "Dbvjdswd",LocalDateTime.of(2015, Month.JULY, 29, 23, 50, 40), "tretew"),
        };

        private List<RepairRequest> repairRequestsList = new ArrayList<>(Arrays.stream(buffer).toList());

        @Override
        public void addRepairRequest(RepairRequest repairRequest) {
            repairRequestsList.add(repairRequest);
        }

        @Override
        public List<RepairRequest> getAll() {
            return repairRequestsList;
        }

        @Override
        public RepairRequest getById(int id) {
            for (RepairRequest repairRequest : repairRequestsList) {
                if (repairRequest.getId() == id) {
                    return repairRequest;
                }
            }
            return null;
        }

        @Override
        public int getCompletedCount() {
            int count = 0;
            for (RepairRequest repairRequest : repairRequestsList) {
                if (repairRequest.getRequestStatus().equals("Завершена")) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public double getAverageDuration() {
            double totalDuration = 0;
            int count = 0;
            for (RepairRequest repairRequest : repairRequestsList) {
                if (repairRequest.getCompletionDate() != null) {
                    totalDuration += repairRequest.getStartDate().until(repairRequest.getCompletionDate(), ChronoUnit.HOURS);
                    count++;
                }
            }
            if (count > 0) {
                return totalDuration / count;
            }
            return 0.0;
        }

        @Override
        public Map<String, Integer> getProblemDescryptionStatistics() {
            Map<String, Integer> problemDescryptionStatistics = new HashMap<>();
            for (RepairRequest repairRequest : repairRequestsList) {
                String problemDescryption = repairRequest.getOrgTechType() + "  -----   " + repairRequest.getProblemDescryption();
                problemDescryptionStatistics.put(problemDescryption, problemDescryptionStatistics.getOrDefault(problemDescryption, 0) + 1);
            }
            return problemDescryptionStatistics;
        }
    }

    @Configuration
    @EnableWebMvc
    @RestController
    public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "OPTIONS")
                    .allowedHeaders("*");
        }
    }
}
