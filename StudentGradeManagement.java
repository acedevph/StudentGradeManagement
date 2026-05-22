import java.util.*;

public class StudentGradeManagement {

    // ─────────────────────────────────────────────
    // Student class
    // ─────────────────────────────────────────────
    static class Student implements Comparable<Student> {
        private String name;
        private Map<String, Double> grades; // subject → grade

        public Student(String name) {
            this.name = name;
            this.grades = new LinkedHashMap<>();
        }

        public String getName() { return name; }

        public void addGrade(String subject, double grade) {
            grades.put(subject, grade);
        }

        public double getAverage() {
            if (grades.isEmpty()) return 0;
            double sum = 0;
            for (double g : grades.values()) sum += g;
            return sum / grades.size();
        }

        public double getHighest() {
            return grades.values().stream().mapToDouble(d -> d).max().orElse(0);
        }

        public double getLowest() {
            return grades.values().stream().mapToDouble(d -> d).min().orElse(0);
        }

        public Map<String, Double> getGrades() { return grades; }

        @Override
        public int compareTo(Student other) {
            return Double.compare(other.getAverage(), this.getAverage()); // descending
        }

        @Override
        public String toString() {
            return String.format("%-20s | Avg: %6.2f | High: %6.2f | Low: %6.2f",
                    name, getAverage(), getHighest(), getLowest());
        }
    }

    // ─────────────────────────────────────────────
    // GradeManager class
    // ─────────────────────────────────────────────
    static class GradeManager {
        private ArrayList<Student> students = new ArrayList<>();

        public void addStudent(String name) {
            for (Student s : students) {
                if (s.getName().equalsIgnoreCase(name)) {
                    System.out.println("  [!] Student \"" + name + "\" already exists.");
                    return;
                }
            }
            students.add(new Student(name));
            System.out.println("  [+] Student \"" + name + "\" added.");
        }

        public Student findStudent(String name) {
            for (Student s : students) {
                if (s.getName().equalsIgnoreCase(name)) return s;
            }
            return null;
        }

        public void inputGrade(String studentName, String subject, double grade) {
            if (grade < 0 || grade > 100) {
                System.out.println("  [!] Grade must be between 0 and 100.");
                return;
            }
            Student s = findStudent(studentName);
            if (s == null) {
                System.out.println("  [!] Student not found.");
                return;
            }
            s.addGrade(subject, grade);
            System.out.printf("  [+] %s: %s = %.2f added.%n", studentName, subject, grade);
        }

        public void displayAllStudents() {
            if (students.isEmpty()) {
                System.out.println("  No students on record.");
                return;
            }
            System.out.println("\n  ── All Students ──────────────────────────────────────");
            System.out.printf("  %-20s | %-8s | %-8s | %-8s%n",
                    "Name", "Average", "Highest", "Lowest");
            System.out.println("  " + "─".repeat(55));
            for (Student s : students) {
                System.out.println("  " + s);
                for (Map.Entry<String, Double> e : s.getGrades().entrySet()) {
                    System.out.printf("      %-18s: %.2f%n", e.getKey(), e.getValue());
                }
            }
            System.out.println("  " + "─".repeat(55));
        }

        public void displayRanking() {
            if (students.isEmpty()) {
                System.out.println("  No students to rank.");
                return;
            }
            ArrayList<Student> sorted = new ArrayList<>(students);
            Collections.sort(sorted);
            System.out.println("\n  ── Student Ranking (by Average) ──────────────────────");
            System.out.printf("  %-6s %-20s %-10s %-12s%n",
                    "Rank", "Name", "Average", "Remarks");
            System.out.println("  " + "─".repeat(55));
            int rank = 1;
            for (Student s : sorted) {
                System.out.printf("  %-6d %-20s %-10.2f %-12s%n",
                        rank++, s.getName(), s.getAverage(), getRemarks(s.getAverage()));
            }
            System.out.println("  " + "─".repeat(55));
        }

        private String getRemarks(double avg) {
            if (avg >= 90) return "Excellent";
            if (avg >= 80) return "Very Good";
            if (avg >= 70) return "Good";
            if (avg >= 60) return "Passing";
            return "Failed";
        }
    }

    // ─────────────────────────────────────────────
    // Main
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GradeManager gm = new GradeManager();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     STUDENT GRADE MANAGEMENT SYSTEM      ║");
        System.out.println("╚══════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println("\n  [1] Add Student");
            System.out.println("  [2] Input Grade");
            System.out.println("  [3] Display All Students");
            System.out.println("  [4] Display Ranking");
            System.out.println("  [5] Exit");
            System.out.print("  Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("  Student Name: ");
                    gm.addStudent(sc.nextLine().trim());
                    break;
                case "2":
                    System.out.print("  Student Name: ");
                    String sName = sc.nextLine().trim();
                    System.out.print("  Subject: ");
                    String subj = sc.nextLine().trim();
                    double grade = -1;
                    while (grade < 0 || grade > 100) {
                        System.out.print("  Grade (0-100): ");
                        try { grade = Double.parseDouble(sc.nextLine().trim()); }
                        catch (NumberFormatException e) { grade = -1; }
                    }
                    gm.inputGrade(sName, subj, grade);
                    break;
                case "3":
                    gm.displayAllStudents();
                    break;
                case "4":
                    gm.displayRanking();
                    break;
                case "5":
                    running = false;
                    System.out.println("  Goodbye!");
                    break;
                default:
                    System.out.println("  [!] Invalid choice.");
            }
        }
        sc.close();
    }
}