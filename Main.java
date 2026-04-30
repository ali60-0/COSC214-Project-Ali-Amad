import java.util.*;
import java.io.*;

public class Main {

    //  ARRAYLIST Part
    public static void insertRecord(ArrayList<PatientRecord> list, PatientRecord record) {
        list.add(record);
    }

    public static PatientRecord searchRecord(ArrayList<PatientRecord> list, int id) {
        for (PatientRecord p : list) {
            if (p.id == id) return p;
        }
        return null;
    }

    public static boolean deleteRecord(ArrayList<PatientRecord> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }


    // HASHMAP Part
    public static void insertRecord(HashMap<Integer, PatientRecord> map, PatientRecord record) {
        map.put(record.id, record);
    }

    public static PatientRecord searchRecord(HashMap<Integer, PatientRecord> map, int id) {
        return map.get(id);
    }


    //  LINKEDLIST  Part
    public static PatientRecord searchRecord(LinkedList<PatientRecord> list, int id) {
        for (PatientRecord p : list) {
            if (p.id == id) return p;
        }
        return null;
    }


    //  QUEUE  Part
    public static PatientRecord searchRecord(Queue<PatientRecord> queue, int id) {
        for (PatientRecord p : queue) {
            if (p.id == id) return p;
        }
        return null;
    }


    // CSV file loader kaggle
    public static ArrayList<PatientRecord> loadFromCSV(String fileName) {

        ArrayList<PatientRecord> list = new ArrayList<>();
        String line;
        int id = 1;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                try {
                    String name = values[0];     //
                    int age = Integer.parseInt(values[1]);
                    String gender = values[2];
                    String condition = values[4];
                    String hospital = values[7];

                    double billing;
                    try {
                        billing = Double.parseDouble(values[8]);
                    } catch (Exception e) {
                        billing = 0.0;
                    }

                    PatientRecord record = new PatientRecord(
                            id++, name, age, gender,
                            condition, hospital,
                            "Normal", billing
                    );

                    list.add(record);

                } catch (Exception e) {
                    // skip bad row
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    //  BENCHMARK Part
    public static void benchmark(ArrayList<PatientRecord> list) {

        int[] sizes = {100, 1000, 5000, 10000};

        for (int size : sizes) {

            ArrayList<PatientRecord> subList = new ArrayList<>(list.subList(0, size));

            long start = System.nanoTime();
            searchRecord(subList, size / 2);
            long arrayTime = System.nanoTime() - start;

            LinkedList<PatientRecord> linkedList = new LinkedList<>(subList);
            start = System.nanoTime();
            searchRecord(linkedList, size / 2);
            long linkedTime = System.nanoTime() - start;

            HashMap<Integer, PatientRecord> map = new HashMap<>();
            for (PatientRecord p : subList) map.put(p.id, p);

            start = System.nanoTime();
            map.get(size / 2);
            long hashTime = System.nanoTime() - start;

            Queue<PatientRecord> queue = new LinkedList<>(subList);
            start = System.nanoTime();
            searchRecord(queue, size / 2);
            long queueTime = System.nanoTime() - start;

            System.out.println("\nSize: " + size);
            System.out.println("ArrayList: " + arrayTime);
            System.out.println("LinkedList: " + linkedTime);
            System.out.println("HashMap: " + hashTime);
            System.out.println("Queue: " + queueTime);
        }
    }


    //  MAIN PArt
    public static void main(String[] args) {

        ArrayList<PatientRecord> list = loadFromCSV("healthcare_dataset.csv");

        System.out.println("Total Records Loaded: " + list.size());

        for (int i = 0; i < 5; i++) {
            System.out.println(list.get(i));
        }

        // -------- BASIC TESTS --------
        System.out.println("\nSearch ID 5: " + searchRecord(list, 5));

        // -------- BENCHMARK --------
        benchmark(list);

        // -------- INTERACTIVE MENU --------
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Search Patient");
            System.out.println("2. Insert Patient");
            System.out.println("3. Delete Patient");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter ID: ");
                int id = scanner.nextInt();
                System.out.println(searchRecord(list, id));

            } else if (choice == 2) {

                scanner.nextLine();

                System.out.print("Enter Name: ");
                String name = scanner.nextLine();

                System.out.print("Enter Age: ");
                int age = scanner.nextInt();

                PatientRecord p = new PatientRecord(
                        list.size() + 1,
                        name,
                        age,
                        "Unknown",
                        "Unknown",
                        "Unknown",
                        "Normal",
                        0.0
                );

                insertRecord(list, p);
                System.out.println("Added!");

            } else if (choice == 3) {
                System.out.print("Enter ID: ");
                int id = scanner.nextInt();

                boolean removed = deleteRecord(list, id);
                System.out.println(removed ? "Deleted" : "Not found");

            } else {
                break;
            }
        }
    }
}
