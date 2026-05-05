import java.io.*;
import java.util.*;

public class Main {

    // ================= LOAD CSV =================
    public static ArrayList<PatientRecord> loadFromCSV(String fileName) {
        ArrayList<PatientRecord> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            br.readLine(); // skip header
            int id = 1;

            while ((line = br.readLine()) != null) {

                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                try {
                    String name = values[0].replace("\"", "");
                    int age = Integer.parseInt(values[1]);
                    String gender = values[2];

                    String condition = values[4];
                    String admissionType = values[5];
                    String insurance = values[6];
                    String hospital = values[7];

                    double billing;
                    try {
                        billing = Double.parseDouble(values[8]);
                    } catch (Exception e) {
                        billing = 0.0;
                    }

                    list.add(new PatientRecord(id++, name, age, gender,
                            condition, hospital, admissionType, insurance, billing));

                } catch (Exception e) {
                    // skip bad rows
                }
            }

        } catch (Exception e) {
            System.out.println("File not found!");
        }

        return list;
    }

    // ================= SEARCH =================
    public static PatientRecord searchArrayList(ArrayList<PatientRecord> list, int id) {
        for (PatientRecord p : list) {
            if (p.id == id) return p;
        }
        return null;
    }

    public static PatientRecord searchLinkedList(LinkedList<PatientRecord> list, int id) {
        for (PatientRecord p : list) {
            if (p.id == id) return p;
        }
        return null;
    }

    public static PatientRecord searchHashMap(HashMap<Integer, PatientRecord> map, int id) {
        return map.get(id);
    }

    public static PatientRecord searchQueue(Queue<PatientRecord> queue, int id) {
        for (PatientRecord p : queue) {
            if (p.id == id) return p;
        }
        return null;
    }

    // ================= PERFORMANCE =================
    public static long testArrayList(ArrayList<PatientRecord> list, int id) {
        long start = System.nanoTime();
        searchArrayList(list, id);
        return System.nanoTime() - start;
    }

    public static long testLinkedList(LinkedList<PatientRecord> list, int id) {
        long start = System.nanoTime();
        searchLinkedList(list, id);
        return System.nanoTime() - start;
    }

    public static long testHashMap(HashMap<Integer, PatientRecord> map, int id) {
        long start = System.nanoTime();
        searchHashMap(map, id);
        return System.nanoTime() - start;
    }

    public static long testQueue(Queue<PatientRecord> queue, int id) {
        long start = System.nanoTime();
        searchQueue(queue, id);
        return System.nanoTime() - start;
    }

    // ================= TABLE =================
    public static void printTable(ArrayList<PatientRecord> data, int run) {

        int[] sizes = {100, 1000, 5000, 10000};

        System.out.println("\n=========== PERFORMANCE TABLE (RUN " + run + ") ===========");
        System.out.printf("%-8s %-12s %-12s %-12s %-12s %-12s%n",
                "Size", "ArrayList", "LinkedList", "HashMap", "Queue", "Complexity");
        System.out.println("---------------------------------------------------------------");

        for (int size : sizes) {

            ArrayList<PatientRecord> sub = new ArrayList<>(data.subList(0, size));
            LinkedList<PatientRecord> linked = new LinkedList<>(sub);
            HashMap<Integer, PatientRecord> map = new HashMap<>();
            Queue<PatientRecord> queue = new LinkedList<>(sub);

            for (PatientRecord p : sub) {
                map.put(p.id, p);
            }

            long t1 = testArrayList(sub, size / 2);
            long t2 = testLinkedList(linked, size / 2);
            long t3 = testHashMap(map, size / 2);
            long t4 = testQueue(queue, size / 2);

            System.out.printf("%-8d %-12d %-12d %-12d %-12d %-12s%n",
                    size, t1, t2, t3, t4, "O(n) / O(1)");
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        ArrayList<PatientRecord> list = loadFromCSV("healthcare_dataset.csv");

        System.out.println("Total Records Loaded: " + list.size());

        for (int i = 0; i < Math.min(5, list.size()); i++) {
            System.out.println(list.get(i));
        }

        // ===== 4 RUNS =====
        for (int i = 1; i <= 4; i++) {
            printTable(list, i);
        }

        // ===== INTERACTIVE MENU =====
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

                PatientRecord result = searchArrayList(list, id);
                System.out.println(result != null ? result : "Not Found");

            } else if (choice == 2) {

                scanner.nextLine();

                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Age: ");
                int age = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Gender: ");
                String gender = scanner.nextLine();

                System.out.print("Condition: ");
                String condition = scanner.nextLine();

                System.out.print("Hospital: ");
                String hospital = scanner.nextLine();

                System.out.print("Admission Type: ");
                String admission = scanner.nextLine();

                System.out.print("Insurance: ");
                String insurance = scanner.nextLine();

                System.out.print("Billing: ");
                double billing = scanner.nextDouble();

                list.add(new PatientRecord(list.size() + 1, name, age, gender,
                        condition, hospital, admission, insurance, billing));

                System.out.println("Added!");

            } else if (choice == 3) {

                System.out.print("Enter ID to delete: ");
                int id = scanner.nextInt();

                list.removeIf(p -> p.id == id);
                System.out.println("Deleted (if existed)");

            } else {
                break;
            }
        }
    }
}
