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

                    PatientRecord p = new PatientRecord(
                            id++,
                            name,
                            age,
                            gender,
                            condition,
                            hospital,
                            admissionType,
                            insurance,
                            billing
                    );

                    list.add(p);

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
            if (p.id == id) {
                return p;
            }
        }

        return null;
    }

    // ================= INSERT =================
    public static void insertRecord(ArrayList<PatientRecord> list, PatientRecord p) {
        list.add(p);
    }

    // ================= DELETE =================
    public static boolean deleteRecord(ArrayList<PatientRecord> list, int id) {

        Iterator<PatientRecord> iterator = list.iterator();

        while (iterator.hasNext()) {

            PatientRecord p = iterator.next();

            if (p.id == id) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    // ================= BENCHMARK =================
    public static void benchmark(ArrayList<PatientRecord> mainList, int run) {

        int[] sizes = {100, 1000, 5000, 10000};

        System.out.println("\n================ PERFORMANCE TABLE (RUN " + run + ") ================");

        System.out.printf(
                "%-8s %-12s %-12s %-12s %-12s %-12s %-12s %-12s %-12s%n",
                "Size",
                "Structure",
                "Insert",
                "Search",
                "Delete",
                "Traverse",
                "Total",
                "Complexity",
                "Best Use"
        );

        System.out.println("---------------------------------------------------------------------------------------------------------------");

        for (int size : sizes) {

           if (mainList.size() < size) {
    continue;
}

ArrayList<PatientRecord> subList =
        new ArrayList<>(mainList.subList(0, size));

            // ================= ARRAYLIST =================
            ArrayList<PatientRecord> arrayList =
                    new ArrayList<>(subList);

            long startInsert = System.nanoTime();

            arrayList.add(
                    new PatientRecord(
                            999999,
                            "Test",
                            20,
                            "Male",
                            "Test",
                            "Test",
                            "Emergency",
                            "Aetna",
                            1000
                    )
            );

            long insertTime = System.nanoTime() - startInsert;

            long startSearch = System.nanoTime();

            searchArrayList(arrayList, size / 2);

            long searchTime = System.nanoTime() - startSearch;

            long startDelete = System.nanoTime();

            deleteRecord(arrayList, size / 2);

            long deleteTime = System.nanoTime() - startDelete;

            long startTraverse = System.nanoTime();

            for (PatientRecord p : arrayList) {
                p.id += 0;
            }

            long traverseTime = System.nanoTime() - startTraverse;

            long total =
                    insertTime + searchTime + deleteTime + traverseTime;

            System.out.printf(
                    "%-8d %-12s %-12d %-12d %-12d %-12d %-12d %-12s %-12s%n",
                    size,
                    "ArrayList",
                    insertTime,
                    searchTime,
                    deleteTime,
                    traverseTime,
                    total,
                    "O(n)",
                    "Traversal"
            );

            // ================= LINKEDLIST =================
            LinkedList<PatientRecord> linkedList =
                    new LinkedList<>(subList);

            startInsert = System.nanoTime();

            linkedList.add(
                    new PatientRecord(
                            999999,
                            "Test",
                            20,
                            "Male",
                            "Test",
                            "Test",
                            "Emergency",
                            "Aetna",
                            1000
                    )
            );

            insertTime = System.nanoTime() - startInsert;

            startSearch = System.nanoTime();

            for (PatientRecord p : linkedList) {
                if (p.id == size / 2) break;
            }

            searchTime = System.nanoTime() - startSearch;

            startDelete = System.nanoTime();

            linkedList.removeIf(p -> p.id == size / 2);

            deleteTime = System.nanoTime() - startDelete;

            startTraverse = System.nanoTime();

            for (PatientRecord p : linkedList) {
                p.id += 0;
            }

            traverseTime = System.nanoTime() - startTraverse;

            total =
                    insertTime + searchTime + deleteTime + traverseTime;

            System.out.printf(
                    "%-8d %-12s %-12d %-12d %-12d %-12d %-12d %-12s %-12s%n",
                    size,
                    "LinkedList",
                    insertTime,
                    searchTime,
                    deleteTime,
                    traverseTime,
                    total,
                    "O(n)",
                    "Insert/Delete"
            );

            // ================= HASHMAP =================
            HashMap<Integer, PatientRecord> map =
                    new HashMap<>();

            for (PatientRecord p : subList) {
                map.put(p.id, p);
            }

            startInsert = System.nanoTime();

            map.put(
                    999999,
                    new PatientRecord(
                            999999,
                            "Test",
                            20,
                            "Male",
                            "Test",
                            "Test",
                            "Emergency",
                            "Aetna",
                            1000
                    )
            );

            insertTime = System.nanoTime() - startInsert;

            startSearch = System.nanoTime();

            map.get(size / 2);

            searchTime = System.nanoTime() - startSearch;

            startDelete = System.nanoTime();

            map.remove(size / 2);

            deleteTime = System.nanoTime() - startDelete;

            startTraverse = System.nanoTime();

            for (PatientRecord p : map.values()) {
                p.id += 0;
            }

            traverseTime = System.nanoTime() - startTraverse;

            total =
                    insertTime + searchTime + deleteTime + traverseTime;

            System.out.printf(
                    "%-8d %-12s %-12d %-12d %-12d %-12d %-12d %-12s %-12s%n",
                    size,
                    "HashMap",
                    insertTime,
                    searchTime,
                    deleteTime,
                    traverseTime,
                    total,
                    "O(1)",
                    "Fast Search"
            );

            // ================= QUEUE =================
            Queue<PatientRecord> queue =
                    new LinkedList<>(subList);

            startInsert = System.nanoTime();

            queue.add(
                    new PatientRecord(
                            999999,
                            "Test",
                            20,
                            "Male",
                            "Test",
                            "Test",
                            "Emergency",
                            "Aetna",
                            1000
                    )
            );

            insertTime = System.nanoTime() - startInsert;

            startSearch = System.nanoTime();

            for (PatientRecord p : queue) {
                if (p.id == size / 2) break;
            }

            searchTime = System.nanoTime() - startSearch;

            startDelete = System.nanoTime();

            queue.poll();

            deleteTime = System.nanoTime() - startDelete;

            startTraverse = System.nanoTime();

            for (PatientRecord p : queue) {
                p.id += 0;
            }

            traverseTime = System.nanoTime() - startTraverse;

            total =
                    insertTime + searchTime + deleteTime + traverseTime;

            System.out.printf(
                    "%-8d %-12s %-12d %-12d %-12d %-12d %-12d %-12s %-12s%n",
                    size,
                    "Queue",
                    insertTime,
                    searchTime,
                    deleteTime,
                    traverseTime,
                    total,
                    "O(n)",
                    "FIFO"
            );

            System.out.println("---------------------------------------------------------------------------------------------------------------");
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        ArrayList<PatientRecord> list =
                loadFromCSV("healthcare_dataset.csv");

        System.out.println("Total Records Loaded: " + list.size());

        // preview first 5 records
        for (int i = 0; i < Math.min(5, list.size()); i++) {
            System.out.println(list.get(i));
        }

        // ================= 4 BENCHMARK RUNS =================
        benchmark(list, 1);
        benchmark(list, 2);
        benchmark(list, 3);
        benchmark(list, 4);

        // ================= INTERACTIVE MENU =================
Scanner scanner = new Scanner(System.in);

while (true) {

    int choice;

    // ================= MENU FAIL SAFE =================
    while (true) {

        System.out.println("\n--- MENU ---");
        System.out.println("1. Search Patient");
        System.out.println("2. Insert Patient");
        System.out.println("3. Delete Patient");
        System.out.println("4. View Doctors");
        System.out.println("5. Exit");

        System.out.print("Enter choice: ");

        if (scanner.hasNextInt()) {

            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice >= 1 && choice <= 5) {
                break;
            } else {
                System.out.println("Invalid option. Please enter 1-5.");
            }

        } else {

            System.out.println("Invalid input. Please enter a whole number.");
            scanner.nextLine();
        }
    }

    // ================= SEARCH =================
    if (choice == 1) {

        System.out.print("Enter ID: ");

        int id = scanner.nextInt();

        PatientRecord p =
                searchArrayList(list, id);

        if (p != null) {
            System.out.println("\nFOUND:");
            System.out.println(p);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // ================= INSERT =================
    else if (choice == 2) {

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

        PatientRecord p =
                new PatientRecord(
                        list.size() + 1,
                        name,
                        age,
                        gender,
                        condition,
                        hospital,
                        admission,
                        insurance,
                        billing
                );

        insertRecord(list, p);

        System.out.println("\nPatient Added Successfully!");
        System.out.println("New Patient ID: " + p.id);
    }

    // ================= DELETE =================
    else if (choice == 3) {

        System.out.print("Enter ID to delete: ");

        int id = scanner.nextInt();

        boolean removed =
                deleteRecord(list, id);

        if (removed) {
            System.out.println("Patient deleted.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    // ================= VIEW DOCTORS =================
    else if (choice == 4) {

        System.out.println("\n===== DOCTOR LIST =====");

        for (PatientRecord p : list) {

            System.out.println(
                    "Patient ID: " + p.id +
                    " | Name: " + p.name +
                    " | Doctor: " + p.doctor
            );
        }
    }

    // ================= EXIT =================
    else if (choice == 5) {

        System.out.println("Exiting program...");
        break;
    }

    else {
        System.out.println("Invalid choice.");
    }
}
}
