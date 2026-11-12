import java.util.*;

// Represents a registered student.
class User {
    private final String studentId;
    private final String password;

    public User(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

// Represents an election candidate.
class Candidate {
    private final String candidateName;

    public Candidate(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateName() {
        return candidateName;
    }
}

// Handles registration, login, voting, and election results.
class VotingSystem {
    private final Scanner sc;
    private final HashMap<String, User> users = new HashMap<>();

    // Required assignment maps:
    private final Map<String, Boolean> voted = new HashMap<>();
    private final Map<String, Integer> candidateVotes = new LinkedHashMap<>();

    private final List<Candidate> candidates = new ArrayList<>();

    public VotingSystem(Scanner sc) {
        this.sc = sc;

        candidates.add(new Candidate("Candidate A"));
        candidates.add(new Candidate("Candidate B"));
        candidates.add(new Candidate("Candidate C"));

        for (Candidate candidate : candidates) {
            candidateVotes.put(candidate.getCandidateName(), 0);
        }
    }

    public void register() {
        System.out.print("Enter Student ID: ");
        String id = sc.nextLine().trim();

        if (id.isEmpty() || users.containsKey(id)) {
            System.out.println("Invalid or already registered ID!");
            return;
        }

        System.out.print("Create Password: ");
        String password = sc.nextLine();

        if (password.trim().isEmpty()) {
            System.out.println("Password cannot be empty!");
            return;
        }

        users.put(id, new User(id, password));
        voted.put(id, false);
        System.out.println("Registration successful!");
    }

    public void loginAndVote() {
        System.out.print("Enter Student ID: ");
        String id = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = users.get(id);
        if (user == null || !user.checkPassword(password)) {
            System.out.println("Invalid student ID or password!");
            return;
        }

        if (Boolean.TRUE.equals(voted.get(id))) {
            System.out.println("You have already voted!");
            return;
        }

        System.out.println("\n===== CANDIDATE LIST =====");
        for (int i = 0; i < candidates.size(); i++) {
            System.out.println((i + 1) + ". "
                    + candidates.get(i).getCandidateName());
        }

        System.out.print("Choose candidate number: ");
        try {
            int choice = Integer.parseInt(sc.nextLine().trim());

            if (choice < 1 || choice > candidates.size()) {
                System.out.println("Invalid candidate choice!");
                return;
            }

            String candidateName =
                    candidates.get(choice - 1).getCandidateName();

            candidateVotes.put(candidateName,
                    candidateVotes.get(candidateName) + 1);
            voted.put(id, true);

            System.out.println("Vote cast successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid candidate number!");
        }
    }

    public void viewTotalVotes() {
        System.out.println("\n===== TOTAL VOTES =====");
        int total = 0;

        for (Map.Entry<String, Integer> entry : candidateVotes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
            total += entry.getValue();
        }

        System.out.println("-----------------------");
        System.out.println("Total votes cast: " + total);
    }

    public void displayResults() {
        System.out.println("\n===== ELECTION RESULTS =====");

        int total = 0;
        for (int count : candidateVotes.values()) {
            total += count;
        }

        if (total == 0) {
            System.out.println("No votes have been cast yet!");
            return;
        }

        List<Map.Entry<String, Integer>> results =
                new ArrayList<>(candidateVotes.entrySet());

        results.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        int highestVotes = results.get(0).getValue();
        System.out.println("\nWINNER / TOP FINALIST:");
        for (Map.Entry<String, Integer> entry : results) {
            if (entry.getValue() == highestVotes) {
                System.out.println(entry.getKey() + " - "
                        + entry.getValue() + " vote(s)");
            }
        }

        // Runner-up means the next distinct vote total.
        int secondHighest = -1;
        for (Map.Entry<String, Integer> entry : results) {
            if (entry.getValue() < highestVotes) {
                secondHighest = entry.getValue();
                break;
            }
        }

        if (secondHighest == -1) {
            System.out.println("\nNo runner-up: all candidates are tied.");
        } else {
            System.out.println("\nRUNNER-UP:");
            for (Map.Entry<String, Integer> entry : results) {
                if (entry.getValue() == secondHighest) {
                    System.out.println(entry.getKey() + " - "
                            + entry.getValue() + " vote(s)");
                }
            }
        }
    }
}

// Main class. Save this source file as Main.java.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VotingSystem system = new VotingSystem(sc);

        while (true) {
            System.out.println("\n===== ONLINE VOTING SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login & Vote");
            System.out.println("3. View Total Votes");
            System.out.println("4. Display Results");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1:
                        system.register();
                        break;
                    case 2:
                        system.loginAndVote();
                        break;
                    case 3:
                        system.viewTotalVotes();
                        break;
                    case 4:
                        system.displayResults();
                        break;
                    case 5:
                        System.out.println(
                                "Thank you for using the Online Voting System!");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid option! Please choose 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid menu number!");
            }
        }
    }
}
