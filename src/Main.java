import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) {
        isFile();
    }

    public static void isFile(){
        int fileCount = 0;
        int yandexBotCount = 0;
        int googleBotCount = 0;
        int totalRequests = 0;
        Statistics statistics = new Statistics();
        while (true){
            System.out.println("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean isFile = file.isFile();
                if (isFile){
                    System.out.println("Путь указан верно");
                    fileCount++;
                    System.out.println("Это файл номер " + fileCount);

                    int linesNum = 0;
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
                            totalRequests++;

                            LogEntry logEntry = new LogEntry(line);
                            statistics.addEntry(logEntry);

                            String userAgent = extractUserAgent(line);
                            if (userAgent.contains("YandexBot")) yandexBotCount++;
                            else if (userAgent.contains("Googlebot")) {
                                googleBotCount++;

                            }

                        }
                        System.out.println("Всего строк: "+linesNum);
                        System.out.println("Доля запросов от YandexBot: "+ (double) yandexBotCount/totalRequests);
                        System.out.println("Доля запросов от GoogleBot: "+ (double) googleBotCount/totalRequests);
                        System.out.println("Средняя скорость трафика за час: " + statistics.getTrafficRate()+ " байт/час");

                        Set<String> existingPages = statistics.getExistingPages();
                        System.out.println("Существующие страницы:");
                        existingPages.forEach(System.out::println);

                        Map<String, Double> osFrequency = statistics.getOsStatistics();
                        System.out.println("Статистика операционных систем:");
                        osFrequency.forEach((os, count) -> System.out.println(os + ": " + count));

                        Set<String> notExistingPages = statistics.getNotExistingPages();
                        System.out.println("Несуществующие страницы:");
                        notExistingPages.forEach(System.out::println);

                        Map<String, Double> browserFrequency = statistics.getBrowserStatistics();
                        System.out.println("Статистика браузеров:");
                        browserFrequency.forEach((browser, percentage) -> System.out.println(browser + ": " + percentage));

                        System.out.println("Средняя посещаемость за час: "+statistics.getAverageVisitsPerHour());
                        System.out.println("Средняя посещаемость одним пользователем: "+statistics.getAverageVisitsPerUser());
                        System.out.println("Средняя частота ошибочных запросов за час: "+statistics.getAverageErrorsPerHour());

                        System.out.println("Пиковая посещаемость сайта в секунду: "+statistics.getPeakVisitsPerSec());
                        System.out.println("Список сайтов, со страниц которых есть ссылки на текущий сайт: "+statistics.getRefererDomains());
                        System.out.println("Максимальная посещаемость одним пользователем: "+statistics.getMaxVisitsPerUser());
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

    private static String extractUserAgent(String logEntry){
        Pattern pattern = Pattern.compile(".*\"([^\"]*)\"$");
        Matcher matcher = pattern.matcher(logEntry);

        if (matcher.find()){
            String userAgent = matcher.group(1);
            return processUserAgent(userAgent);
        }
        return "";
    }

    private static String processUserAgent(String userAgent){
        int start = userAgent.indexOf('(');
        int end = userAgent.indexOf(')', start);

        if (start!=-1 && end!=-1){
            String firstBrackets = userAgent.substring(start+1, end);
            String[] parts = firstBrackets.split(";");

            if (parts.length>=2){
                String fragments = parts[1].trim();
                int slashIndex = fragments.indexOf('/');
                if (slashIndex!=-1) return fragments.substring(0, slashIndex).trim();
            }
        }
        return "";
    }


}

