public class PatientRecord {

    int id;
    String name;
    int age;
    String gender;
    String condition;
    String hospital;
    String admissionType;
    String insurance;
    double billing;

    public PatientRecord(int id, String name, int age, String gender,
                         String condition, String hospital,
                         String admissionType, String insurance, double billing) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.condition = condition;
        this.hospital = hospital;
        this.admissionType = admissionType;
        this.insurance = insurance;
        this.billing = billing;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Name: " + name +
                ", Age: " + age +
                ", Condition: " + condition +
                ", Insurance: " + insurance;
    }
}
