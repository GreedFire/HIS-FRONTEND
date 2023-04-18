package com.hisfrontend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDto {
    private long id;
    private String firstname;
    private String surname;
    private String pesel;
    private String sex;
    private String status;
    private LocalDateTime registrationDate;
    private LocalDateTime registeredOnDate;

}
