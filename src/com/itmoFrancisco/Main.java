package com.itmoFrancisco;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static Hashtable<Long, Dragon> dragonsHashtable = new Hashtable<Long, Dragon>();
    static String fileName;
    static String initialization;


    public static void main(String[] args) {
        fileName = getFileName(args);
        if (fileName == null) return; //program ends  without processing the file
        handleConsoleCommand();
    }

    /**
     * handle the commands given by the user.
     */
    private static void handleConsoleCommand() {
        Scanner keyboard = new Scanner(System.in);
        String currentCommand;
        System.out.println("Enter the command: ");
        while ((currentCommand = keyboard.nextLine()) !="exit") {
            String[] parts = currentCommand.split(" ");
            switch (parts[0]) {
                case "help":
                    help();
                    break;
                case "info":
                    info();
                    break;
                case "show":
                    show();
                    break;
                case "clear":
                    clear();
                case "save":
                    save();
                    break;
                case "print_character":
                    printCharacter();
                    break;
                case "remove_key":
                    if (parts.length < 2) {
                        System.out.println("The command is incomplete, you need to enter the key.");
                        break;
                    }
                    Long key;
                    try {
                        key = Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    remove(key);
                    break;
                case "execute_script":
                    if (parts.length < 2) {
                        System.out.println("The command is incomplete, you need to enter the filename that contain the commands.");
                        break;
                    }
                    executeScript(parts[1]);
                    break;
                case "replace_if_greater":
                    if (parts.length < 3) {
                        System.out.println("The command is incomplete, you need to enter the key and the age.");
                        break;
                    }
                    try {
                        key = Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    Long age;
                    try {
                        age = Long.parseLong(parts[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    replaceIfGreater(key, age);
                    break;
                case "replace_if_lower":
                    if (parts.length < 3) {
                        System.out.println("The command is incomplete, you need to enter the key and the age.");
                        break;
                    }
                    try {
                        key = Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }

                    try {
                        age = Long.parseLong(parts[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    replaceIfLower(key, age);
                    break;
                case "remove_greater_key":
                    if (parts.length < 2) {
                        System.out.println("The command is incomplete, you need to enter the key.");
                        break;
                    }
                    try {
                        key = Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    remove_greater_key(key);
                    break;
                case "count_by_character":
                    if (parts.length < 2) {
                        System.out.println("The command is incomplete, you need to enter the character (EVIL, GOOD, CHAOTIC, FICKLE).");
                        break;
                    }
                    DragonCharacter dragonCharacter = DragonCharacterHelper.parseDragonCharacter(parts[1]);
                    if (dragonCharacter == null) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    count_by_character(dragonCharacter);
                    break;
                case "filter_less_than_killer":
                    if (parts.length < 2) {
                        System.out.println("The command is incomplete, you need to enter the killer weight.");
                        break;
                    }
                    Long weight;
                    try {
                        weight = Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    filterLessThanKiller(weight);

                    break;
                case "insert":
                    insert(keyboard);
                    break;
                case "update":
                    if (parts.length < 2) {
                        System.out.println("The command is incomplete, you need to enter the key.");
                        break;
                    }
                    try {
                        key = Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("The command is invalid.");
                        break;
                    }
                    update(key, keyboard);
                    break;
                default:
                    System.out.println("unknown command");
                    break;
            }
            System.out.println("Enter the new command: ");
        }
        System.out.println("Goodbye.");
    }

    private static void replaceIfGreater(Long id, Long age) {
        if (dragonsHashtable.containsKey(id))
            if (dragonsHashtable.get(id).getAge() < age) {
                dragonsHashtable.get(id).setAge(age);
                System.out.println("\nThe age was replaced.");
                return;
            }
        System.out.println("\nNothing to replace.");
    }

    private static void replaceIfLower(Long id, Long age) {
        if (dragonsHashtable.containsKey(id))
            if (dragonsHashtable.get(id).getAge() > age) {
                dragonsHashtable.get(id).setAge(age);
                System.out.println("\nThe age was replaced.");
                return;
            }
        System.out.println("\nNothing to replace.");
    }

    private static void filterLessThanKiller(Long weight) {
        dragonsHashtable.forEach((k, v) -> {
            if (v.getKiller().compareTo(weight) < 0) {
                System.out.println(v.toXml());
            }
        });
    }

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static void executeScript(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("\nCant read the file.");
            return;
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(ANSI_BLUE + line + ANSI_RESET);
                executeCommand(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\nCant read the file.");
            return;
        }
    }

    private static void executeCommand(String currentCommand) {
        String[] parts = currentCommand.split(" ");
        switch (parts[0]) {
            case "exit":
                System.out.println("Goodbye.");
                break;
            case "help":
                help();
                break;
            case "info":
                info();
                break;
            case "show":
                show();
                break;
            case "clear":
                clear();
            case "save":
                save();
                break;
            case "print_character":
                printCharacter();
                break;
            case "insert":
                System.out.println("The insert command isn't supported in the none-interactive mode.");
                break;
            case "update":
                System.out.println("The update command isn't supported in the none-interactive mode.");
                break;
            case "remove_key":
                if (parts.length != 2) {
                    System.out.println("The remove_key command is incomplete.");
                    break;
                }
                remove(Long.parseLong(parts[1]));
                break;
            case "execute_script":
                if (parts.length != 2) {
                    System.out.println("The execute_script command is incomplete.");
                    break;
                }
                executeScript(parts[1]);

                break;
            case "replace_if_greater":
                if (parts.length < 3) {
                    System.out.println("The command is incomplete");
                    break;
                }
                Long key;
                try {
                    key = Long.parseLong(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("The command is invalid.");
                    break;
                }
                Long age;
                try {
                    age = Long.parseLong(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("The command is invalid.");
                    break;
                }

                replaceIfGreater(key, age);
                break;
            case "replace_if_lower":
                if (parts.length < 3) {
                    System.out.println("The command is incomplete, you need to enter the key and the age.");
                    break;
                }
                try {
                    key = Long.parseLong(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("The command is invalid.");
                    break;
                }

                try {
                    age = Long.parseLong(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("The command is invalid.");
                    break;
                }
                replaceIfLower(key, age);
                break;
            case "remove_greater_key":
                if (parts.length != 2) {
                    System.out.println("The remove_greater_key command is incomplete, you need to enter the key.");
                    break;
                }
                remove_greater_key(Long.parseLong(parts[1]));
                break;
            case "count_by_character":
                if (parts.length != 2) {
                    System.out.println("The count_by_character command is incomplete.");
                    break;
                }
                count_by_character(DragonCharacterHelper.parseDragonCharacter(parts[1]));

                break;
            case "filter_less_than_killer":
                break;
            default:
                System.out.println("unknown command");
                break;
        }
    }

    private static void printCharacter() {
        dragonsHashtable.forEach((k, v) -> {
            System.out.println("\n" + v.getCharacter());
        });
    }

    private static void count_by_character(DragonCharacter dragonCharacter) {
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        dragonsHashtable.forEach((k, v) -> {
            if (v.getCharacter() == dragonCharacter)
                counter.getAndSet(counter.get() + 1);
        });
        System.out.println("\nThere are " + counter.get() + " dragons with this character.");
    }

    private static void update(Long id, Scanner keyboard) {
        if (dragonsHashtable.containsKey(id)) {
            Dragon dragon = createDragon(keyboard);
            dragonsHashtable.replace(id, dragon);
            System.out.println("The dragon was updated.");
        } else
            System.out.println("The dragon don't exists.");
    }

    private static void save() {

        Iterator<Map.Entry<Long, Dragon>> it = dragonsHashtable.entrySet().iterator();
        String dragons = "";
        while (it.hasNext()) {
            Map.Entry<Long, Dragon> currentDragon = it.next();
            dragons += currentDragon.getValue().toXml();
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileContent = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><dragons>%s</dragons>", dragons);
        if (writer != null) {
            try {
                writer.write(fileContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void remove_greater_key(Long id) {
        Iterator<Map.Entry<Long, Dragon>> it = dragonsHashtable.entrySet().iterator();
        Integer deleted = 0;
        while (it.hasNext()) {
            Map.Entry<Long, Dragon> entry = it.next();
            if (entry.getKey() > id) {
                it.remove();
                deleted++;
            }
        }
        System.out.println("Deleted elements: " + deleted);
    }

    private static void remove(Long id) {
        if (dragonsHashtable.containsKey(id)) {
            dragonsHashtable.remove(id);
            System.out.println("The dragon was removed.");
        } else
            System.out.println("The dragon don't exists.");
    }

    private static void insert(Scanner keyboard) {
        Dragon dragon = createDragon(keyboard);
        dragonsHashtable.put(dragon.getId(), dragon);
    }

    public static Long readLong(Scanner keyboard, String message) {
        while (true) {
            String value = keyboard.nextLine();
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                System.out.println(message);
            }
        }
    }

    public static Double readDouble(Scanner keyboard, String message) {
        while (true) {
            String value = keyboard.nextLine();
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.out.println(message);
            }
        }
    }

    public static Float readFloat(Scanner keyboard, String message) {
        while (true) {
            String value = keyboard.nextLine();
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                System.out.println(message);
            }
        }
    }

    private static Dragon createDragon(Scanner keyboard) {
        String name;
        do {
            System.out.println("Enter dragon name: ");
            name = keyboard.nextLine();
            if (name.isEmpty() || name == null) {
                System.out.println("The name is invalid.");
            }
        } while (name.isEmpty() || name == null);

        Long age;
        do {
            System.out.println("Enter dragon age: ");
            age = readLong(keyboard, "Enter the correct value for the age.");
            if (age == 0 || age == null) {
                System.out.println("The age is invalid.");
            }
        } while (age <= 0);

        Double weight;
        do {
            System.out.println("Enter weight: ");
            weight = readDouble(keyboard, "Enter the correct value for the weight.");
            if (weight <= 0) {
                System.out.println("The weight is invalid.");
            }
        } while (weight <= 0);

        Boolean speaking = false;
        System.out.println("Enter speaking (y: yes, otherwise no): ");
        String value = keyboard.nextLine();
        if (value.toLowerCase(Locale.ROOT) == "y") {
            speaking = true;
        }

        Coordinates coordinates = createCoordinates(keyboard);
        DragonCharacter character = createDragonCharacter(keyboard);
        Person killer = createPerson(keyboard);
        return new Dragon(getNewId(), name, coordinates, age, weight, speaking, character, killer);
    }

    private static Long getNewId() {
        Long maxKey = Long.valueOf(0);
        for (Map.Entry<Long, Dragon> entry : dragonsHashtable.entrySet()) {
            if (entry.getKey() > maxKey) {
                maxKey = entry.getKey();
            }
        }
        maxKey++;
        return maxKey;
    }

    private static Coordinates createCoordinates(Scanner keyboard) {
        Double x;
        do {
            System.out.println("Enter coordinate x: ");
            x = readDouble(keyboard, "Enter the correct value for the coordinate.");
            if (x == null) {
                System.out.println("The coordinate is invalid.");
            }
        } while (x == null);

        Float y;
        do {
            System.out.println("Enter coordinate y: ");
            y = readFloat(keyboard, "Enter the correct value for the coordinate.");
            if (y == null) {
                System.out.println("The coordinate is invalid.");
            }
        } while (y == null);

        Coordinates coordinates = new Coordinates();
        coordinates.setX(x);
        coordinates.setY(y);
        return coordinates;
    }

    private static DragonCharacter createDragonCharacter(Scanner keyboard) {
        String value;
        DragonCharacter character = null;
        do {
            System.out.println("Enter Dragon character (EVIL, GOOD, CHAOTIC, FICKLE): ");
            value = keyboard.nextLine();
            switch (value) {
                case "EVIL":
                    character = DragonCharacter.EVIL;
                    break;
                case "GOOD":
                    character = DragonCharacter.GOOD;
                    break;
                case "CHAOTIC":
                    character = DragonCharacter.CHAOTIC;
                    break;
                case " FICKLE":
                    character = DragonCharacter.FICKLE;
                    break;
                default:
                    System.out.println("The character is invalid.");
                    break;
            }
        } while (character == null);
        return character;
    }

    private static Person createPerson(Scanner keyboard) {
        String personName;
        do {
            System.out.println("Enter person name: ");
            personName = keyboard.nextLine();
            if (personName.isEmpty() || personName == null) {
                System.out.println("The person name is invalid.");
            }
        } while (personName.isEmpty() || personName == null);

        Long personWeight;
        do {
            System.out.println("Enter person weight: ");
            personWeight = readLong(keyboard, "Enter the correct value for the weight.");
            if (personWeight <= 0) {
                System.out.println("The person weight is invalid.");
            }
        } while (personWeight <= 0);

        Double height;
        do {
            System.out.println("Enter height: ");
            height = readDouble(keyboard, "Enter the correct value for the height.");
            if (height <= 0) {
                System.out.println("The height is invalid.");
            }
        } while (height <= 0);


        Location location = createLocation(keyboard);

        Person killer = new Person();
        killer.setHeight(height);
        killer.setName(personName);
        killer.setWeight(personWeight);
        killer.setLocation(location);
        return killer;
    }

    private static Location createLocation(Scanner keyboard) {
        String name;
        do {
            System.out.println("Enter location name: ");
            name = keyboard.nextLine();
            if (name.isEmpty() || name == null) {
                System.out.println("The location name is invalid.");
            }
        } while (name.isEmpty() || name == null);

        Double x;
        do {
            System.out.println("Enter location x: ");
            x = readDouble(keyboard, "Enter the correct value for the coordinate.");
            if (x == null) {
                System.out.println("The location is invalid.");
            }
        } while (x == null);

        Double y;
        do {
            System.out.println("Enter location y: ");
            y = readDouble(keyboard, "Enter the correct value for the coordinate.");
            if (y == null) {
                System.out.println("The location is invalid.");
            }
        } while (y == null);

        Float z;
        do {
            System.out.println("Enter coordinate z: ");
            z = readFloat(keyboard, "Enter the correct value for the coordinate.");
            if (z == null) {
                System.out.println("The coordinate is invalid.");
            }
        } while (z == null);

        Location location = new Location();
        location.setName(name);
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        return location;
    }

    private static void clear() {
        dragonsHashtable = new Hashtable<Long, Dragon>();
    }

    private static void show() {
        if (dragonsHashtable.isEmpty()) {
            System.out.println("There are no dragons");
            return;
        }
        dragonsHashtable.forEach((k, v) -> System.out.println("\n" + v.toString()));
    }

    private static void info() {
        System.out.println("type: Hashtable<Long, Dragon>");
        System.out.println("initialization: " + initialization);
        System.out.println("length: " + dragonsHashtable.size());
    }

    private static void help() {
        System.out.println("help : вывести справку по доступным командам");
        System.out.println("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        System.out.println("show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        System.out.println("insert : добавить новый элемент с заданным ключом");
        System.out.println("update id : обновить значение элемента коллекции, id которого равен заданному");
        System.out.println("remove_key id : удалить элемент из коллекции по его ключу");
        System.out.println("clear : очистить коллекцию");
        System.out.println("save : сохранить коллекцию в файл");
        System.out.println("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        System.out.println("exit : завершить программу (без сохранения в файл)");
        System.out.println("replace_if_greater key age : заменить значение по ключу, если новое значение больше старого");
        System.out.println("replace_if_lowe key age : заменить значение по ключу, если новое значение меньше старого");
        System.out.println("remove_greater_key key : удалить из коллекции все элементы, ключ которых превышает заданный");
        System.out.println("count_by_character character : вывести количество элементов, значение поля character которых равно заданному");
        System.out.println("filter_less_than_killer killer : вывести элементы, значение поля killer которых меньше заданного");
        System.out.println("print_unique_character : вывести уникальные значения поля character всех элементов в коллекции");
    }

    /**
     * Get the filename from the arguments or ask the name from the console, if the file doesn't exists create a new one.
     *
     * @param args the first argument must be the filename.
     * @return returns the filename
     */
    private static String getFileName(String[] args) {
        String fileName;
        if (args.length == 0) {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Enter file name: ");
            fileName = keyboard.nextLine();
        } else {
            fileName = args[0];
        }
        File f = new File(fileName);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (!f.exists()) {
            System.out.println("The file doesn't exists.");
            return null;
        } else {
            dragonsHashtable = new XmlReader().read(fileName);
            if (dragonsHashtable == null) {
                System.out.println("There was a problem reading the file.");
                return null;
            }
            initialization = sdf.format(f.lastModified());
        }
        return fileName;
    }

    /**
     * Creates an empty file
     *
     * @param fileName
     * @return true if file was created  otherwise false.
     */
    private static BufferedWriter createFile(String fileName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        /*finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        return writer;
    }
}
