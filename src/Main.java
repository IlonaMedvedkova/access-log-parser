import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Курсовой проект синтаксис языка и базовые типы данных
        System.out.println("Введите первое число: ");
        int number1 = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число: ");
        int number2 = new Scanner(System.in).nextInt();
        sum(number1, number2);
        System.out.println("-------------");
        sub(number1, number2);
        System.out.println("-------------");
        mul(number1, number2);
        System.out.println("-------------");
        div(number1, number2);

    }
    //метод подсчета суммы
    public static void sum (int x, int y) {
        int z = x+y;
        System.out.println("Сумма чисел: " + z);
    }

    //метод подсчета разности
    public static void sub (int x, int y){
        int z=x-y;
        System.out.println("Разность чисел: " + z);
    }

    //метод подсчета произведения
    public static void mul (int x, int y){
        int z=x*y;
        System.out.println("Произведение чисел: " + z);
    }

    //метод подсчета частного
    public static void div (int x, int y){
        double z= (double) x /y;
        System.out.println("Частное чисел: " + z);

    }
}
