package com.sirma.service.imp;

import com.sirma.model.Employee;
import com.sirma.model.Team;
import com.sirma.service.interfaces.IAppService;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Service
public class AppService implements IAppService {

    @Override
    public String uploadFile(MultipartFile file, Model model){

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        }
        else{
            try{
                List<Employee> list = new ArrayList<>();
                InputStream inputStream = file.getInputStream();
                CsvParserSettings  settings = new CsvParserSettings();
                settings.setHeaderExtractionEnabled(true);
                CsvParser parser = new CsvParser(settings);
                List<Record> records =  parser.parseAllRecords(inputStream);

                records.forEach( record -> {

                    Employee employee = new Employee(
                            Integer.parseInt(record.getString("EmpID")),
                            Integer.parseInt(record.getString("ProjectID")),
                            record.getString("DateFrom"),
                            record.getString("DateTo")
                    );
                    list.add(employee);
                });

                Hashtable<Integer, Team> teams = new Hashtable<>();

                int allEmployeeCount = list.size();
                int currentEmployeeIndex = 0;

                while(currentEmployeeIndex < allEmployeeCount){
                    for (int i = 0; i < allEmployeeCount ; i++) {
                        createTeam(list.get(currentEmployeeIndex), list.get(i), teams);
                    }
                    currentEmployeeIndex++;
                }

                final Team[] lastTeam = {null};

                teams.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                                .forEach(tmp ->{
                                    lastTeam[0] = tmp.getValue().clone();
                                });

                StringBuilder str = new StringBuilder();
                str.append(" First Employee ID  - "+ lastTeam[0].getFirstEmployee().getId());
                str.append(" Second Employee ID  - "+ lastTeam[0].getSecondEmployee().getId());
                str.append(" WORKING HOURS - "+lastTeam[0].getTeamWorkingDays() );

                model.addAttribute("message", str.toString());
                model.addAttribute("status", true);

            }catch (Exception ex){
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }

        }

        return "file-upload-status";
    }
    private void createTeam(Employee firstEmployee, Employee secondEmployee,Hashtable<Integer,Team> teams){

        if(firstEmployee.getId() == secondEmployee.getId() ||
           firstEmployee.getProjectId() != secondEmployee.getProjectId() ||
           teams.containsKey(firstEmployee.getId()) && teams.get(firstEmployee.getId()).getFirstEmployee().getId() == secondEmployee.getId() ||
           teams.containsKey((int) secondEmployee.getId()) && teams.get((int) secondEmployee.getId()).getSecondEmployee().getId() == firstEmployee.getId())
        {
            return;
        }

        teams.put((int) firstEmployee.getId(),new Team(firstEmployee.clone(),secondEmployee.clone()));
    }

}
