package com.sirma.model;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class Team implements Comparable<Team>{

    private Employee firstEmployee;
    private Employee secondEmployee;
    private long workingDays;

    public Team(Employee firstEmployee,Employee secondEmployee){
        this.firstEmployee = firstEmployee;
        this.secondEmployee = secondEmployee;
        this.workingDays = 0;
    }

    public long getTeamWorkingDays(){

        long teamWorkingDays = 0;

        try{
            Date firstTeamDate = getTeamStartingDate();
            Date lastTeamDate = getTeamLatestDate();
            teamWorkingDays = TimeUnit.DAYS.convert((lastTeamDate.getTime() - firstTeamDate.getTime()), TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return teamWorkingDays;
    }

    public Date getTeamLatestDate(){

        Date latestTeamWorkFrom = null;

        try{
            Date firstEmployeeLastTeamWork = isStillWorking(firstEmployee);
            Date secondEmployeeLastTeamWork = isStillWorking(secondEmployee);
            latestTeamWorkFrom = calcuTeamLatestDate( firstEmployeeLastTeamWork,secondEmployeeLastTeamWork);
        }catch (Exception e){
            e.printStackTrace();
        }

        return latestTeamWorkFrom;
    }
    public Date  getTeamStartingDate(){

        Date startTeamWorkFrom = null;

        try{
            SimpleDateFormat sdformat = new SimpleDateFormat("dd/mm/yyyy");
            startTeamWorkFrom = calculateTeamStartingDate(sdformat.parse(firstEmployee.getDateFrom()),sdformat.parse(secondEmployee.getDateFrom()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return startTeamWorkFrom;
    }
    private Date calculateTeamStartingDate(Date firstEmployeeStartDate,Date secondEmployeeStartDate){

        if(firstEmployeeStartDate.compareTo(secondEmployeeStartDate) < 0) {
            return firstEmployeeStartDate;
        }
        return secondEmployeeStartDate;
    }
    private Date calcuTeamLatestDate(Date firstEmployeelastDate,Date secondEmployeelastDate){

        if(firstEmployeelastDate.compareTo(secondEmployeelastDate) > 0){
            return secondEmployeelastDate;
        }
        return firstEmployeelastDate;
    }
    private Date isStillWorking(Employee employee){

        Date workingTo = null;
        SimpleDateFormat sdformat = new SimpleDateFormat("dd/mm/yyyy");
        Date today = new Date();
        try {
            workingTo = (employee.getDateTo().equals("NULL")) ? today : sdformat.parse(employee.getDateTo());
        }catch (Exception e){
            e.printStackTrace();
        }
        return workingTo;
    }

    @Override
    public int compareTo(Team team) {
        return (int)(this.workingDays - team.getTeamWorkingDays());
    }
    public Team clone(){
        return new Team(firstEmployee,secondEmployee);
    }
}
