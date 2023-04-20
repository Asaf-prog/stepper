import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {
        System.out.println("---Stepper UI---");
        Scanner input = new Scanner(System.in);
        int choice = 0;
        while(choice != 0)//(int)MenuItems.EXIT)//will change to exit
        {
            choice=input.nextInt();
            switch (choice){
                case 1:
                    System.out.println("1");
                    break;
                case 2:
                    System.out.println("2");
                    break;
                case 3:
                    System.out.println("3");
                    break;
                case 4:
                    System.out.println("4");
                    break;
                case 5:
                    System.out.println("5");
                    break;
                case 6:
                    System.out.println("6");
                    break;
            }

        }






    }
}