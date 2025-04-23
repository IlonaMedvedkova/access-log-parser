import java.io.*;
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
                if (isFile){
                    System.out.println("Путь указан верно");
                    count++;
                    System.out.println("Это файл номер " + count);

                    int linesNum = 0;
                    int maxLength = Integer.MIN_VALUE;
                    int minLength = Integer.MAX_VALUE;
                    try {
                        FileReader fileReader = new FileReader(path);
                        BufferedReader reader = new BufferedReader(fileReader);

                        String line;
                        while ((line = reader.readLine())!=null){
                            int length = line.length();

                            if (length>1024){
                                throw new TooLongLineException("Строка слишком длинная - длиннее 1024 символов");
                            }

                            linesNum++;
                            maxLength=Math.max(maxLength, length);
                            minLength=Math.min(minLength, length);

                        }
                        System.out.println("Всего строк: "+linesNum);
                        System.out.println("Максимальная строка: "+maxLength);
                        System.out.println("Минимальная строка: "+minLength);
                    }catch (FileNotFoundException fnf){
                        System.err.println("Файл не найден: "+fnf.getMessage());
                        fnf.printStackTrace();
                    }catch (IOException ioe){
                        System.err.println("Ошибка при чтении файла: "+ioe.getMessage());
                        ioe.printStackTrace();
                    }
                }
                else{
                    System.out.println("Введенный путь не ведет к файлу или файл не существует");
                    continue;
                }
        }
    }


}

