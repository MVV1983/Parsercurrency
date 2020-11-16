import org.jsoup.Jsoup; //импорт бибилиотеки для парсинга HTML
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class Main {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws IOException {

        TrSearcher();//допиливаем таблицу на нашу страницу
    }

    public static String[] readDataFromHtml() {
        String title = null;
        Document doc = null;
        try {
            doc = (Document) Jsoup.connect("https://yandex.ru/").get();
            title = doc.title();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Element usd = doc.select("span.inline-stocks__value_inner").first();// таким образом заберем определенный элемент - первый здесь
        //System.out.println("USD: " +usd.text());

        Element eur = doc.select("span.inline-stocks__value_inner").get(1);
        //System.out.println("EUR: " +eur.text());

        Element oil = doc.select("span.inline-stocks__value_inner").get(2);
        //System.out.println("OIL: " +oil.text());

        String[] currancy = new String[3];
        currancy[0] = usd.text();
        currancy[1] = eur.text();
        currancy[2] = oil.text();
        return currancy;//USSD;
    }


    public static void TrSearcher() throws FileNotFoundException {
        Date date = new Date();
        //String par1 = readDataFromHtml();
        //инициализируем массив и заталкиваем в него массив данных из readDataFromHtml
        String[] sendArray = readDataFromHtml();
        final String searchString = "</table>";

        //в аррэйлисте будем хранить ячейки таблицы для достройки в html
        ArrayList<String> parsedFile = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileInputStream("Currency.html"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(searchString)) {
                    parsedFile.add("\t<tr>");
                    parsedFile.add("\t<td>" + date);
                    parsedFile.add("\t<td>" + "USD: " + sendArray[0] + "</td>");
                    parsedFile.add("\t<td>" + "EUR: " + sendArray[1] + "</td>");
                    parsedFile.add("\t<td>" + "OIL: " + sendArray[2] + "</td>");
                    parsedFile.add("\t</tr>");
                }
                parsedFile.add(line);
            }
        }
        catch (FileNotFoundException e){
            System.out.print("Файл HTML не найден");
        }

        try (PrintWriter writer = new PrintWriter("Currency.html")) {
            for (String line : parsedFile) {
                writer.println(line);
            }
        }
    }
}