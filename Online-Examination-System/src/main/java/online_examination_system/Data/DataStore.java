package online_examination_system.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import online_examination_system.exams.Answer;
import online_examination_system.exams.Exam;
import online_examination_system.exams.Result;
import online_examination_system.questions.DescriptiveQuestion;
import online_examination_system.questions.MCQQuestion;
import online_examination_system.questions.Question;
import online_examination_system.users.Admin;
import online_examination_system.users.Faculty;
import online_examination_system.users.Student;
import online_examination_system.users.Users;

/**
 * JSON persistence facade for users, exams and results.
 */
public class DataStore {

    private static final Path DATA_DIR = resolveDataDirectory();
    private static final Path USERS_FILE = DATA_DIR.resolve("users.json");
    private static final Path EXAMS_FILE = DATA_DIR.resolve("exams.json");
    private static final Path RESULTS_FILE = DATA_DIR.resolve("results.json");

    private DataStore() {
    }

    public static Users findUserByCredentials(String email, String password, String role) {
        for (Users user : loadUsers()) {
            if (user.getEmail().equalsIgnoreCase(safe(email))
                    && user.getPassword().equals(safe(password))
                    && user.getRole().equalsIgnoreCase(safe(role))) {
                return user;
            }
        }
        return null;
    }

    private static Path resolveDataDirectory() {
        Path[] candidates = {
            Path.of("src", "main", "java", "online_examination_system", "Data"),
            Path.of("main", "java", "online_examination_system", "Data"),
            Path.of("online_examination_system", "Data")
        };

        for (Path candidate : candidates) {
            if (Files.exists(candidate.resolve("users.json"))) {
                return candidate;
            }
        }

        return candidates[0];
    }

    public static List<Users> loadUsers() {
        List<Users> users = new ArrayList<>();
        for (Object item : readArray(USERS_FILE)) {
            Map<String, Object> row = asMap(item);
            String id = string(row.get("id"));
            String role = safe(string(row.get("role"))).toLowerCase();
            String name = string(row.get("name"));
            String email = string(row.get("email"));
            String password = string(row.get("password"));

            if ("student".equals(role)) {
                Student student = new Student(id, name, email, password, id);
                student.setEnrolledExams(stringList(row.get("enrolledExamIds")));
                student.setResults(resultsForStudent(id));
                users.add(student);
            } else if ("faculty".equals(role)) {
                users.add(new Faculty(id, name, email, password, id, stringOr(row.get("subject"), "General")));
            } else if ("admin".equals(role)) {
                users.add(new Admin(id, name, email, password, id));
            }
        }
        return users;
    }

    public static List<Exam> loadExams() {
        List<Exam> exams = new ArrayList<>();
        for (Object item : readArray(EXAMS_FILE)) {
            Map<String, Object> row = asMap(item);
            Exam exam = new Exam(
                string(row.get("examId")),
                string(row.get("title")),
                integer(row.get("durationMinutes")),
                stringOr(row.get("scheduledTime"), "Not scheduled")
            );
            for (Object qItem : asList(row.get("questions"))) {
                Question question = toQuestion(asMap(qItem));
                if (question != null) {
                    exam.addQuestion(question);
                }
            }
            exams.add(exam);
        }
        return exams;
    }

    public static Exam findExam(String examId) {
        for (Exam exam : loadExams()) {
            if (exam.getExamId().equals(safe(examId))) {
                return exam;
            }
        }
        return null;
    }

    public static List<Result> loadResults() {
        List<Result> results = new ArrayList<>();
        for (Object item : readArray(RESULTS_FILE)) {
            Map<String, Object> row = asMap(item);
            results.add(new Result(
                string(row.get("resultId")),
                string(row.get("studentId")),
                integer(row.get("score")),
                integer(row.get("totalMarks"))
            ));
        }
        return results;
    }

    public static List<Result> resultsForStudent(String studentId) {
        List<Result> results = new ArrayList<>();
        for (Object item : readArray(RESULTS_FILE)) {
            Map<String, Object> row = asMap(item);
            if (string(row.get("studentId")).equals(safe(studentId))) {
                results.add(new Result(
                    string(row.get("resultId")),
                    string(row.get("studentId")),
                    integer(row.get("score")),
                    integer(row.get("totalMarks"))
                ));
            }
        }
        return results;
    }

    public static boolean enrollStudent(String studentId, String examId) {
        List<Object> users = readArray(USERS_FILE);
        boolean changed = false;
        for (Object item : users) {
            Map<String, Object> row = asMap(item);
            if (string(row.get("id")).equals(safe(studentId)) && "student".equalsIgnoreCase(string(row.get("role")))) {
                List<Object> enrolled = asList(row.get("enrolledExamIds"));
                if (!containsString(enrolled, examId)) {
                    enrolled.add(toStoredNumberOrString(examId));
                    row.put("enrolledExamIds", enrolled);
                    changed = true;
                }
            }
        }
        if (changed) writeArray(USERS_FILE, users);
        return changed;
    }

    public static Users addUser(String name, String email, String password, String role) {
        List<Object> users = readArray(USERS_FILE);
        int nextId = nextId(users, "id");
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", nextId);
        row.put("role", role);
        row.put("name", name);
        row.put("email", email);
        row.put("password", password);
        if ("student".equalsIgnoreCase(role)) {
            row.put("enrolledExamIds", new ArrayList<>());
            row.put("results", new ArrayList<>());
        } else if ("faculty".equalsIgnoreCase(role)) {
            row.put("questionBankIds", new ArrayList<>());
            row.put("managedExamIds", new ArrayList<>());
        }
        users.add(row);
        writeArray(USERS_FILE, users);
        return "student".equalsIgnoreCase(role)
            ? new Student(String.valueOf(nextId), name, email, password, String.valueOf(nextId))
            : new Faculty(String.valueOf(nextId), name, email, password, String.valueOf(nextId), "General");
    }

    public static Exam addExam(String title, int duration, String scheduledTime, String facultyId) {
        List<Object> exams = readArray(EXAMS_FILE);
        int nextId = nextId(exams, "examId");
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("examId", nextId);
        row.put("title", title);
        row.put("subject", "General");
        row.put("durationMinutes", duration);
        row.put("scheduledTime", scheduledTime);
        row.put("createdByFacultyId", toStoredNumberOrString(facultyId));
        row.put("questions", new ArrayList<>());
        exams.add(row);
        writeArray(EXAMS_FILE, exams);
        return new Exam(String.valueOf(nextId), title, duration, scheduledTime);
    }

    public static void saveExam(Exam exam, String facultyId) {
        List<Object> exams = readArray(EXAMS_FILE);
        boolean updated = false;
        for (int i = 0; i < exams.size(); i++) {
            Map<String, Object> row = asMap(exams.get(i));
            if (string(row.get("examId")).equals(exam.getExamId())) {
                exams.set(i, examToMap(exam, facultyId));
                updated = true;
                break;
            }
        }
        if (!updated) {
            exams.add(examToMap(exam, facultyId));
        }
        writeArray(EXAMS_FILE, exams);
    }

    public static void saveResult(Result result, Student student, Exam exam, List<Answer> answers) {
        List<Object> results = readArray(RESULTS_FILE);
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("resultId", toStoredNumberOrString(result.getResultId()));
        row.put("studentId", toStoredNumberOrString(result.getStudentId()));
        row.put("studentName", student.getName());
        row.put("examId", toStoredNumberOrString(exam.getExamId()));
        row.put("examTitle", exam.getTitle());
        row.put("score", result.getScore());
        row.put("totalMarks", result.getTotalMarks());
        row.put("percentage", result.getTotalMarks() == 0 ? 0 : (result.getScore() * 100.0) / result.getTotalMarks());
        row.put("submittedAt", LocalDateTime.now().toString());
        List<Object> answerRows = new ArrayList<>();
        for (Answer answer : answers) {
            Map<String, Object> answerRow = new LinkedHashMap<>();
            answerRow.put("questionId", toStoredNumberOrString(answer.getQuestionId()));
            answerRow.put("studentResponse", answer.getStudentResponse());
            answerRows.add(answerRow);
        }
        row.put("answers", answerRows);
        results.add(row);
        writeArray(RESULTS_FILE, results);
    }

    public static String nextResultId() {
        return String.valueOf(nextId(readArray(RESULTS_FILE), "resultId"));
    }

    private static Question toQuestion(Map<String, Object> row) {
        String id = string(row.get("questionId"));
        String type = string(row.get("type"));
        String text = string(row.get("question"));
        int marks = integer(row.get("marks"));
        if ("mcq".equalsIgnoreCase(type)) {
            List<Object> options = asList(row.get("options"));
            return new MCQQuestion(id, text, marks,
                option(options, 0), option(options, 1), option(options, 2), option(options, 3),
                indexToOption(integer(row.get("correctAnswer")))
            );
        }
        if ("descriptive".equalsIgnoreCase(type)) {
            return new DescriptiveQuestion(id, text, marks, string(row.get("sampleAnswer")));
        }
        return null;
    }

    private static Map<String, Object> examToMap(Exam exam, String facultyId) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("examId", toStoredNumberOrString(exam.getExamId()));
        row.put("title", exam.getTitle());
        row.put("subject", "General");
        row.put("durationMinutes", exam.getDuration());
        row.put("scheduledTime", exam.getScheduledTime());
        row.put("createdByFacultyId", toStoredNumberOrString(facultyId));
        List<Object> questions = new ArrayList<>();
        for (Question question : exam.getQuestions()) {
            Map<String, Object> q = new LinkedHashMap<>();
            q.put("questionId", toStoredNumberOrString(question.getQuestionID()));
            q.put("question", question.getQuestion());
            q.put("marks", question.getMarks());
            if (question instanceof MCQQuestion) {
                MCQQuestion mcq = (MCQQuestion) question;
                q.put("type", "mcq");
                List<Object> options = new ArrayList<>();
                options.add(mcq.getOptionA());
                options.add(mcq.getOptionB());
                options.add(mcq.getOptionC());
                options.add(mcq.getOptionD());
                q.put("options", options);
                q.put("correctAnswer", optionToIndex(mcq.getCorrectOption()));
            } else if (question instanceof DescriptiveQuestion) {
                q.put("type", "descriptive");
                q.put("sampleAnswer", ((DescriptiveQuestion) question).getExpectedAnswer());
            }
            questions.add(q);
        }
        row.put("questions", questions);
        return row;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> readArray(Path path) {
        try {
            ensureFile(path);
            Object parsed = SimpleJson.parse(Files.readString(path, StandardCharsets.UTF_8));
            if (parsed instanceof List) {
                return (List<Object>) parsed;
            }
        } catch (Exception ex) {
            System.out.println("  [DATA] Unable to read " + path.getFileName() + ": " + ex.getMessage());
        }
        return new ArrayList<>();
    }

    private static void writeArray(Path path, List<Object> rows) {
        try {
            ensureFile(path);
            Files.writeString(path, SimpleJson.stringify(rows), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("  [DATA] Unable to save " + path.getFileName() + ": " + ex.getMessage());
        }
    }

    private static void ensureFile(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        if (!Files.exists(path)) {
            Files.writeString(path, "[]", StandardCharsets.UTF_8);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        return value instanceof Map ? (Map<String, Object>) value : new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    private static List<Object> asList(Object value) {
        return value instanceof List ? (List<Object>) value : new ArrayList<>();
    }

    private static List<String> stringList(Object value) {
        List<String> strings = new ArrayList<>();
        for (Object item : asList(value)) {
            strings.add(string(item));
        }
        return strings;
    }

    private static int integer(Object value) {
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(string(value));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static String string(Object value) {
        return value == null ? "" : String.valueOf(value).replaceAll("\\.0$", "");
    }

    private static String stringOr(Object value, String fallback) {
        String text = string(value);
        return text.isEmpty() ? fallback : text;
    }

    private static String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private static String option(List<Object> options, int index) {
        return index < options.size() ? string(options.get(index)) : "";
    }

    private static String indexToOption(int index) {
        return switch (index) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "C";
            case 3 -> "D";
            default -> String.valueOf(index);
        };
    }

    private static int optionToIndex(String option) {
        return switch (safe(option).toUpperCase()) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> 0;
        };
    }

    private static boolean containsString(List<Object> values, String expected) {
        for (Object value : values) {
            if (string(value).equals(safe(expected))) return true;
        }
        return false;
    }

    private static Object toStoredNumberOrString(String value) {
        try {
            return Integer.parseInt(safe(value));
        } catch (NumberFormatException ex) {
            return safe(value);
        }
    }

    private static int nextId(List<Object> rows, String field) {
        int max = 0;
        for (Object item : rows) {
            max = Math.max(max, integer(asMap(item).get(field)));
        }
        return max + 1;
    }
}
