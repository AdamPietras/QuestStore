package controllers;

import daoImplementation.CreepDAOImplementation;
import daoImplementation.StudentDAOImplementation;
import models.Mentor;
import view.StaticUi;

import java.util.Scanner;

public class CreepController implements BaseController {


    CreepDAOImplementation creepDAOImplementation = new CreepDAOImplementation();
    Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        creepMenu();
    }


    private void creepMenu(){
        StaticUi.printCreepMenu();
        String option = scanner.next();
        switch (option){
            case "1":
                creepDAOImplementation.showAllMentors();
                break;
            case "2":
                StaticUi.addMentor();
                break;
            case "3":
                //creepDAOImplementation.editMentor();
                break;
            case "4":
                deleteMentorById();
        }
    }

    private void printCreepMenu(){
        System.out.println("Select number to: \n" +
                "1. Show all mentors\n" +
                "2. Add a new mentor\n" +
                "3. Edit mentor by id\n" +
                "4. Remove mentor by id");
    }

    private void addMentor(){
        System.out.println("Insert mentor's login: ");
        String login = scanner.next();
        System.out.println("Insert mentor's password: ");
        String password = scanner.next();
        System.out.println("Insert mentor's name: ");
        String firstName = scanner.next();
        System.out.println("Insert mentor's last name: ");
        String lastName = scanner.next();
        int studentTypeId = 2;
        Mentor mentor = new Mentor(login, password, studentTypeId, firstName, lastName);

        creepDAOImplementation.addMentor(mentor);
        creepDAOImplementation.showAllMentors();
    }


//    private void deleteMentorById(){
//        creepDAOImplementation.showAllMentors();
//        int mentorId = scanner.nextInt();
//        creepDAOImplementation.deleteMentor(mentorId);
//        creepDAOImplementation.showAllMentors();
//    }
//TODO


}
