import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Train {
    int id, seats, fare;
    String name, source, destination;

    Train(int id, String name, String source, String destination, int seats, int fare) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.seats = seats;
        this.fare = fare;
    }

    void show() {
        System.out.println(id + " | " + name + " | " + source + " -> "
                + destination + " | Seats: " + seats + " | Rs." + fare);
    }
}

class Booking {
    int id, age;
    String name, phone;
    Train train;

    Booking(int id, String name, int age, String phone, Train train) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.train = train;
    }

    void show() {
        System.out.println(id + " | " + name + " | " + train.name
                + " | " + train.source + " -> " + train.destination);
    }
}

public class TrainRouteFinder {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Train> trains = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();

    static int bookingId = 1001;

    // GUI components
    static JFrame frame;
    static JTextArea output;

    static void addData() {

        trains.add(new Train(101, "Rajdhani", "Delhi", "Mumbai", 50, 1500));
        trains.add(new Train(102, "Deccan", "Mumbai", "Pune", 40, 500));
        trains.add(new Train(103, "Intercity", "Delhi", "Jaipur", 30, 700));
        trains.add(new Train(104, "Shatabdi", "Delhi", "Agra", 25, 600));
        trains.add(new Train(105, "Chennai Express", "Pune", "Chennai", 35, 1200));

        route("Delhi", "Agra", 200);
        route("Delhi", "Jaipur", 300);
        route("Agra", "Jaipur", 250);
        route("Agra", "Mumbai", 1200);
        route("Jaipur", "Mumbai", 1150);
        route("Mumbai", "Pune", 150);
        route("Pune", "Chennai", 1200);
    }


    static void route(String a, String b, int d) {

        graph.putIfAbsent(a, new HashMap<>());
        graph.putIfAbsent(b, new HashMap<>());

        graph.get(a).put(b, d);
        graph.get(b).put(a, d);
    }


    static Train getTrain(int id) {

        for (Train t : trains)
            if (t.id == id)
                return t;

        return null;
    }

    static void displayTrains() {

        output.setText("");

        output.append(
                "ID\tTrain\t\tRoute\t\tSeats\tFare\n"
        );

        output.append(
                "---------------------------------------------------------------\n"
        );

        for (Train t : trains) {

            output.append(
                    t.id + "\t"
                    + t.name + "\t"
                    + t.source + " -> " + t.destination + "\t"
                    + t.seats + "\tRs." + t.fare + "\n"
            );
        }
    }

    static void searchTrain() {

        String name = JOptionPane.showInputDialog(
                frame,
                "Enter Train Name:"
        );

        if (name == null || name.trim().isEmpty())
            return;

        for (Train t : trains) {

            if (t.name.equalsIgnoreCase(name.trim())) {

                output.setText("");

                output.append("Train Found!\n\n");

                output.append("Train ID       : " + t.id + "\n");
                output.append("Train Name     : " + t.name + "\n");
                output.append("Source         : " + t.source + "\n");
                output.append("Destination    : " + t.destination + "\n");
                output.append("Available Seats: " + t.seats + "\n");
                output.append("Fare           : Rs." + t.fare);

                return;
            }
        }

        JOptionPane.showMessageDialog(
                frame,
                "Train not found!"
        );
    }

    static void bookTicket() {

        displayTrains();

        String idText = JOptionPane.showInputDialog(
                frame,
                "Enter Train ID:"
        );

        if (idText == null)
            return;

        try {

            int id = Integer.parseInt(idText);

            Train t = getTrain(id);

            if (t == null || t.seats == 0) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Train unavailable!"
                );

                return;
            }

            String name = JOptionPane.showInputDialog(
                    frame,
                    "Enter Passenger Name:"
            );

            if (name == null || name.trim().isEmpty())
                return;

            String ageText = JOptionPane.showInputDialog(
                    frame,
                    "Enter Age:"
            );

            if (ageText == null)
                return;

            int age = Integer.parseInt(ageText);

            String phone = JOptionPane.showInputDialog(
                    frame,
                    "Enter Phone Number:"
            );

            if (phone == null)
                return;

            Booking b = new Booking(
                    bookingId++,
                    name,
                    age,
                    phone,
                    t
            );

            bookings.add(b);

            // Reduce available seat
            t.seats--;

            JOptionPane.showMessageDialog(
                    frame,
                    "Booking Successful!\n\n"
                    + "Booking ID: " + b.id
                    + "\nPassenger: " + b.name
                    + "\nTrain: " + t.name
                    + "\nRoute: " + t.source + " -> " + t.destination
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter valid numbers!"
            );
        }
    }

    static void cancelBooking() {

        String idText = JOptionPane.showInputDialog(
                frame,
                "Enter Booking ID:"
        );

        if (idText == null)
            return;

        try {

            int id = Integer.parseInt(idText);

            for (int i = 0; i < bookings.size(); i++) {

                if (bookings.get(i).id == id) {

                    bookings.get(i).train.seats++;

                    bookings.remove(i);

                    JOptionPane.showMessageDialog(
                            frame,
                            "Booking Cancelled Successfully!"
                    );

                    return;
                }
            }

            JOptionPane.showMessageDialog(
                    frame,
                    "Booking not found!"
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Enter a valid Booking ID!"
            );
        }
    }

    static void displayBookings() {

        output.setText("");

        if (bookings.isEmpty()) {

            output.append("No bookings available.");

            return;
        }

        output.append(
                "Booking ID\tPassenger\tAge\tPhone\tTrain\tRoute\n"
        );

        output.append(
                "--------------------------------------------------------------------------\n"
        );

        for (Booking b : bookings) {

            output.append(
                    b.id + "\t"
                    + b.name + "\t"
                    + b.age + "\t"
                    + b.phone + "\t"
                    + b.train.name + "\t"
                    + b.train.source + " -> "
                    + b.train.destination + "\n"
            );
        }
    }


    static void shortestPath() {

        String start = JOptionPane.showInputDialog(
                frame,
                "Enter Starting City:"
        );

        if (start == null)
            return;

        String end = JOptionPane.showInputDialog(
                frame,
                "Enter Destination City:"
        );

        if (end == null)
            return;

        start = start.trim();
        end = end.trim();

        if (!graph.containsKey(start)
                || !graph.containsKey(end)) {

            JOptionPane.showMessageDialog(
                    frame,
                    "City not found!"
            );

            return;
        }

        HashMap<String, Integer> dist = new HashMap<>();
        HashMap<String, String> prev = new HashMap<>();

        for (String city : graph.keySet())
            dist.put(city, Integer.MAX_VALUE);

        dist.put(start, 0);

        PriorityQueue<String> pq =
                new PriorityQueue<>(
                        Comparator.comparingInt(dist::get)
                );

        pq.add(start);

        while (!pq.isEmpty()) {

            String u = pq.poll();

            for (String v : graph.get(u).keySet()) {

                int d =
                        dist.get(u)
                        + graph.get(u).get(v);

                if (d < dist.get(v)) {

                    dist.put(v, d);

                    prev.put(v, u);

                    pq.remove(v);

                    pq.add(v);
                }
            }
        }

        ArrayList<String> path = new ArrayList<>();

        String city = end;

        while (city != null) {

            path.add(city);

            city = prev.get(city);
        }

        Collections.reverse(path);

        output.setText("");

        output.append(
                "SHORTEST ROUTE\n\n"
        );

        output.append(
                "From      : " + start + "\n"
        );

        output.append(
                "To        : " + end + "\n\n"
        );

        output.append(
                "Route     : "
                + String.join(" -> ", path)
                + "\n"
        );

        output.append(
                "Distance  : "
                + dist.get(end)
                + " km"
        );
    }

    static void createGUI() {

        frame = new JFrame("Train Route Finder");

        frame.setSize(900, 600);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // Title
        JLabel title = new JLabel(
                "TRAIN ROUTE FINDER",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // Output area
        output = new JTextArea();

        output.setFont(
                new Font("Monospaced", Font.PLAIN, 14)
        );

        output.setEditable(false);

        output.setMargin(
                new Insets(10, 10, 10, 10)
        );

        JScrollPane scrollPane =
                new JScrollPane(output);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // Button panel
        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(2, 4, 10, 10)
                );

        JButton trainButton =
                new JButton("View Trains");

        JButton searchButton =
                new JButton("Search Train");

        JButton bookButton =
                new JButton("Book Ticket");

        JButton cancelButton =
                new JButton("Cancel Booking");

        JButton bookingButton =
                new JButton("View Bookings");

        JButton routeButton =
                new JButton("Shortest Route");

        JButton clearButton =
                new JButton("Clear");

        JButton exitButton =
                new JButton("Exit");

        buttonPanel.add(trainButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);

        buttonPanel.add(bookingButton);
        buttonPanel.add(routeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        

        trainButton.addActionListener(e ->
                displayTrains()
        );

        searchButton.addActionListener(e ->
                searchTrain()
        );

        bookButton.addActionListener(e ->
                bookTicket()
        );
        cancelButton.addActionListener(e ->
                cancelBooking()
        );
        bookingButton.addActionListener(e ->
                displayBookings()
        );
        routeButton.addActionListener(e ->
                shortestPath()
        );
        clearButton.addActionListener(e ->
                output.setText("")
        );
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        addData();
        SwingUtilities.invokeLater(
                () -> createGUI()
        );
    }
}
