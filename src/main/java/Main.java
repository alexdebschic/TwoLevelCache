import TwoLevelCache.TwoLevelCache;
import java.util.Scanner;

public class Main {
    public static void main(String[] args ) {
        System.out.println("Two level cache");
        int MemorySize = 2;
        int FileSystemSize = 2;
        int StrategyFlag = 1;
        if (args.length == 3) {
            MemorySize = Integer.parseInt(args[0]);
            FileSystemSize = Integer.parseInt(args[1]);
            StrategyFlag = Integer.parseInt(args[2]);
        }
        TwoLevelCache cache = new TwoLevelCache<Long, String>(MemorySize,FileSystemSize,StrategyFlag);

        String scHelpMsg = "Доступные команды: \n"
                + "p - Добавить объект\n"
                + "g - Просмотреть значение объекта по ключу\n"
                + "r - Удалить объект по ключу\n"
                + "s - Размер кеша\n"
                + "c - Очистить кеш\n"
                + "f - Проверка свободного места\n"
                + "h - Подсказка\n"
                + "q - Выход\n";
        System.out.println(scHelpMsg);
        Scanner sc = new Scanner(System.in);
        long key = 1;
        while (true){
            System.out.print(">");
            String command;
            String GetKey;
            command = sc.nextLine().toLowerCase();


            switch (command) {
                case "p":
                    cache.put(key, Long.toString(key));
                    key++;
                    break;
                case "g":
                    System.out.println("Введите ключ");
                    GetKey = sc.nextLine();
                    System.out.println(cache.get(new Long(GetKey)));
                    break;
                case "r":
                    System.out.println("Введите ключ");
                    GetKey = sc.nextLine();
                    cache.remove(new Long(GetKey));
                    break;
                case "s":
                    System.out.println(cache.size());
                    break;
                case "c":
                    cache.clear();
                    break;
                case "f":
                    System.out.println(cache.IsFreeSpace());
                    break;
                case "h":
                    System.out.println(scHelpMsg);
                    break;
                case "q":
                    System.exit(0);
                    break;
            }
        }
    }
}
