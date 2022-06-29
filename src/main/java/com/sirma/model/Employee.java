package com.sirma.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @CsvBindByName(column = "EmpID")
    private long id;
    @CsvBindByName(column = "ProjectID")
    private long projectId;
    @CsvBindByName(column = "DateFrom")
    private String dateFrom;
    @CsvBindByName(column = "DateTo")
    private String dateTo;

    public Employee clone(){
        return new Employee(this.id,this.projectId,this.dateFrom,this.dateTo);
    }
}
