import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        isFile();

    }

    public static void isFile(){
        int count = 0;
        while (true){
            System.out.println("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean isFile = file.isFile();
                if (isFile == true){
                    System.out.println("Путь указан верно");
                    count++;
                    System.out.println("Это файл номер " + count);
                }
                else{
                    System.out.println("Введенный путь не ведет к файлу или файл не существует");
                    continue;
                }
        }
    }
}
