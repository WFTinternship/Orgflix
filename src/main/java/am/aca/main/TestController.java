package am.aca.main;

import am.aca.DAO.*;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by David on 5/27/2017.
 */
public class TestController {

    public static void main(String[] args) throws SQLException {
        Controller controller = new Controller();
        Scanner scanner = new Scanner(System.in);

        System.out.println("To login press \"l\", to register press \"r\", for guest view press \"g\": ");
        final String choice = scanner.nextLine();

        if(choice.equals("r")){
            User user = new User();
            System.out.println("Please provide nick-name: ");
            user.setNick( scanner.nextLine() );
            System.out.println("Please provide email: ");
            user.setEmail( scanner.nextLine() );
            System.out.println("Please provide new password: ");
            user.setPass( scanner.nextLine() );
            System.out.println("Please provide your name: ");
            user.setUserName( scanner.nextLine() );
            controller.getDao().getUserDAO().addUser(user);
        }

        if(choice.equals("l")){
            System.out.println("Please provide email: ");
            String email = scanner.nextLine();
            System.out.println("Please provide pass: ");
            String pass = scanner.nextLine();
            controller.getDao().authentication(email,pass);

            boolean terminate = false;
            while(!terminate){
                System.out.print("Wish list(\"v\") | Wached list(\"w\") | Add film(\"a\") | List directors(\"l\")" +
                        " | Add director (\"d\") | Remove from list(\"r\") | Exit (\"e\") .\nPlease choose the option: " );
                try {
                    switch (scanner.nextLine()){
                        case "a":
                            Film film = new Film();
                            System.out.println("Input film title");
                            film.setTitle( scanner.nextLine() );
                            System.out.println("Input year");
                            film.setProdYear( scanner.nextInt() );
                            System.out.println("Does film has Oscar y/n");
                            film.setHasOscar( scanner.nextLine().equals("y"));
                            System.out.println(controller.getDao().getDirectorDao().listDirectors());
                            System.out.println("Is director listed? Input Id number or -1 to add new one");
                            int directId = scanner.nextInt();
                            if(directId != -1) film.setDirector("A director");
                            System.out.println("Input Director Name");
                            film.setDirector( scanner.nextLine() );
                            //    if( Controller.addContact() ) System.out.println("New contact is added \n");
                            //    else System.out.println("Contact is NOT added \n");;
                            break;
                        case "v":
                            //      Controller.getContactsList();
                            break;
                        case "e":
                            terminate = true;
                            System.out.println("Exiting prgram \n");
                        default:
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

    }
}
