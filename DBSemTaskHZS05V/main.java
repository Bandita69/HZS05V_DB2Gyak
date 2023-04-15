// Author: Timkó András
// Neptun code: HZS05V
// Basic database manager with GUI

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.InputMismatchException;

import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

class JdbcSQLiteConnection extends javax.swing.JFrame {

    static String user;
    static String pass;
    static int wrong = 0;

    static void askForUsernamePassword(int wrong) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Felhasznalonev"));
        JTextField tf = new JTextField();
        panel.add(tf);
        panel.add(new JLabel("Jelszo"));
        JPasswordField pf = new JPasswordField();
        panel.add(pf);
        if (wrong == 1) {
            panel.add(new JLabel("Hibas felhasznalonev vagy jelszo"));
        }
        switch (JOptionPane.showConfirmDialog(null, panel, "Bejelentkezesi modul", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.OK_OPTION:
                user = tf.getText();
                pass = new String(pf.getPassword());
                break;
            case JOptionPane.CANCEL_OPTION:
                System.exit(0);
                break;
            case JOptionPane.CLOSED_OPTION:
                System.exit(0);
                break;
        }

    }

    // world most secure login system lol
    static void login() {
        askForUsernamePassword(wrong);
        if (user.equals("admin") && pass.equals("admin")) {
            wrong = 0;
        } else {
            wrong = 1;
            login();
        }
    }

    // simple window

    public void simpleWindow(Connection conn) {
        JFrame frame = new JFrame("Database Manager Pro 9000");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        frame.setLayout(new GridLayout(6, 2));

        // button
        JButton button = new JButton("Select movies, directors and studios");
        frame.add(button);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                String[] table = JS.viewTable(conn);
                JS.JScrollablePanelTest(table);
            }
        });

        // button2
        JButton button2 = new JButton("Connection info");
        frame.add(button2);

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                String[] table = new String[100];
                try {

                    DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                    table[0] = dm.getDriverName();
                    table[1] = dm.getDriverVersion();
                    table[2] = dm.getDatabaseProductName();
                    table[3] = dm.getDatabaseProductVersion();

                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                JS.JScrollablePanelTest(table);
            }
        });

        // button3
        JButton button3 = new JButton("Delete all data from tables");
        frame.add(button3);

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.deleteTable(conn);
            }
        });

        // button4
        JButton button4 = new JButton("Fill tables with data");
        frame.add(button4);

        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.deleteTable(conn);
                JS.batchInsert(conn);
            }
        });

        // button5
        JButton button5 = new JButton("Query");
        frame.add(button5);

        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.queryPanel(conn);
            }
        });

        // button6
        JButton button6 = new JButton("Insert new movie");
        frame.add(button6);

        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.insertMoviePanel(conn);
            }
        });

        // button7
        JButton button7 = new JButton("Delete movie");
        frame.add(button7);

        button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.deleteMoviePanel(conn);
            }
        });

        // button8
        JButton button8 = new JButton("Insert review");
        frame.add(button8);

        button8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.insertReviewPanel(conn);
            }
        });

        // button9
        JButton button9 = new JButton("View review");
        frame.add(button9);

        button9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.viewReviewPanel(conn);
            }
        });

        // button10
        JButton button10 = new JButton("Insert actor");
        frame.add(button10);

        button10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.insertActorPanel(conn);
            }
        });

        // button11
        JButton button11 = new JButton("View movies with actors");
        frame.add(button11);

        button11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                String[] table = JS.viewMoviesWithActors(conn);
                JS.JScrollablePanelTest(table);
            }
        });

        // button12
        JButton button12 = new JButton("Free text query");
        frame.add(button12);

        button12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.freeTextQueryPanel(conn);
            }
        });

        frame.setVisible(true);

    }

    // query and close
    public void JScrollablePanelTest(String[] table) {
        setTitle("Result");
        setLayout(new BorderLayout());
        JPanel panel = createPanel(table);
        add(panel, BorderLayout.CENTER);

        JButton myButton = new JButton("Close");
        myButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();

            }
        });

        // this makes a txt file with the data
        JButton myButton2 = new JButton("Print");
        myButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                JS.print(table);

            }
        });

        panel.add(myButton);
        panel.add(myButton2);
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static JPanel createPanel(String[] table) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(15, 1, 2, 2));
        int i = 0;
        while (table[i] != null) {
            JLabel label = new JLabel(table[i] + "\n");
            label.setFont(new Font("Arial", Font.PLAIN, 15));

            panel.add(label);
            i++;
        }

        return panel;
    }

    public void queryPanel(Connection conn) {

        String[] query = new String[100];
        JPanel panel = new JPanel(new GridLayout(9, 3));
        panel.add(new JLabel("Rendezo"));
        JTextField tf = new JTextField();
        panel.add(tf);
        panel.add(new JLabel("Szuletesi ev"));
        JTextField tf2 = new JTextField();
        panel.add(tf2);
        panel.add(new JLabel("Szuletesi neme"));
        JTextField tf3 = new JTextField();
        panel.add(tf3);
        panel.add(new JLabel("Szuletesi hely"));
        JTextField tf4 = new JTextField();
        panel.add(tf4);
        panel.add(new JLabel("Film Cime"));
        JTextField tf5 = new JTextField();
        panel.add(tf5);
        panel.add(new JLabel("Film megjelenes eve"));
        JTextField tf6 = new JTextField();
        panel.add(tf6);
        panel.add(new JLabel("Film zsanraja"));
        JTextField tf7 = new JTextField();
        panel.add(tf7);
        panel.add(new JLabel("Film pontszama"));
        JTextField tf8 = new JTextField();
        panel.add(tf8);

        panel.add(new JLabel("Film studioja"));
        JTextField tf10 = new JTextField();
        panel.add(tf10);

        switch (JOptionPane.showConfirmDialog(null, panel, "Enter the search details:", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.OK_OPTION:
                query[0] = tf.getText();
                int born = 0;
                // parse int from string
                query[1] = tf2.getText();
                if (!(query[1].equals(""))) {
                    born = Integer.parseInt(query[1]);
                }

                query[2] = tf3.getText();
                query[3] = tf4.getText();
                query[4] = tf5.getText();
                int year = 0;
                // parse int from string
                query[5] = tf6.getText();
                if (!(query[5].equals(""))) {
                    year = Integer.parseInt(query[5]);
                }

                query[6] = tf7.getText();
                double score = 0;
                // parse double from string
                query[7] = tf8.getText();
                if (!(query[7].equals(""))) {
                    score = Double.parseDouble(query[7]);
                }

                query[8] = tf10.getText();

                JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
                String[] table = JS.queryDire(conn, query[0], born, query[2], query[3], query[4], year, query[6],
                        score, query[8]);
                JS.JScrollablePanelTest(table);

                break;
            case JOptionPane.CANCEL_OPTION:

                break;
            case JOptionPane.CLOSED_OPTION:

                break;
        }

    }

    public void insertMoviePanel(Connection conn) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Film cime"));
        JTextField tf = new JTextField();
        panel.add(tf);
        panel.add(new JLabel("Film megjelenes eve"));
        JTextField tf2 = new JTextField();
        panel.add(tf2);
        panel.add(new JLabel("Film zsanraja"));
        JTextField tf3 = new JTextField();
        panel.add(tf3);
        panel.add(new JLabel("Film pontszama"));
        JTextField tf4 = new JTextField();
        panel.add(tf4);

        JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
        String[] idName = JS.queryDirectors(conn);
        String[] choices = new String[idName.length];
        String[] choices2 = new String[idName.length];
        int j = 0;
        for (int i = 0; i < idName.length; i++) {
            if (i % 2 == 1) {
                choices[j] = idName[i];
                j++;
            }

            if (idName[i] == null) {
                break;
            } else {
                choices2[j] = idName[i];
            }
        }
        String[] directorNames = new String[j];
        for (int i = 0; i < j; i++) {
            directorNames[i] = choices[i];
        }
        int[] directorIds = new int[j];
        for (int i = 0; i < j; i++) {
            directorIds[i] = Integer.parseInt(choices2[i]);
        }

        final JComboBox<String> cb = new JComboBox<String>(directorNames);

        cb.setMaximumSize(cb.getPreferredSize());
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(cb);

        switch (JOptionPane.showConfirmDialog(null, panel, "Enter the movie details:", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.CANCEL_OPTION:

                break;
            case JOptionPane.CLOSED_OPTION:

                break;
            case JOptionPane.OK_OPTION:
                String title = tf.getText();

                int year = 0;
                // parse int from string
                String yearGT = tf2.getText();
                if (!(yearGT.equals(""))) {
                    year = Integer.parseInt(yearGT);
                }

                String genre = tf3.getText();

                double score = 0;
                // parse double from string
                String scoreGT = tf4.getText();
                if (!(scoreGT.equals(""))) {
                    score = Double.parseDouble(scoreGT);
                }
                int director = directorIds[cb.getSelectedIndex()];

                if (score > 10 || score < 0) {
                    JOptionPane.showMessageDialog(null, "Pontszam 0 es 10 kozott lehet!");
                    insertMoviePanel(conn);
                    break;

                }
                if (title.equals("")) {
                    JOptionPane.showMessageDialog(null, "Cim nem lehet ures!");
                    insertMoviePanel(conn);
                    break;

                }
                if (genre.equals("")) {
                    JOptionPane.showMessageDialog(null, "Zsanra nem lehet ures!");
                    insertMoviePanel(conn);
                    break;

                }
                if (year > 2022 || year < 1900) {
                    JOptionPane.showMessageDialog(null, "Ev 1900 es 2022 kozott lehet!");
                    insertMoviePanel(conn);
                    break;

                } else {
                    JdbcSQLiteConnection JS2 = new JdbcSQLiteConnection();
                    JS2.insertMovie(conn, title, year, genre, score, director);
                }

                break;

        }

    }

    // delete movie panel
    public void deleteMoviePanel(Connection conn) {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Film cime"));

        JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
        String[] idName = JS.queryMovies(conn);
        String[] choices = new String[idName.length];
        String[] choices2 = new String[idName.length];
        int j = 0;
        for (int i = 0; i < idName.length; i++) {
            if (i % 2 == 1) {
                choices[j] = idName[i];
                j++;
            }

            if (idName[i] == null) {
                break;
            } else {
                choices2[j] = idName[i];
            }
        }
        String[] movieNames = new String[j];
        for (int i = 0; i < j; i++) {
            movieNames[i] = choices[i];
        }
        int[] movieIds = new int[j];
        for (int i = 0; i < j; i++) {
            movieIds[i] = Integer.parseInt(choices2[i]);
        }

        final JComboBox<String> cb = new JComboBox<String>(movieNames);

        cb.setMaximumSize(cb.getPreferredSize());
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(cb);

        switch (JOptionPane.showConfirmDialog(null, panel, "Choose a moive:", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.CANCEL_OPTION:

                break;
            case JOptionPane.CLOSED_OPTION:

                break;
            case JOptionPane.OK_OPTION:

                int id = movieIds[cb.getSelectedIndex()];

                JdbcSQLiteConnection JS2 = new JdbcSQLiteConnection();
                JS2.deleteMovie(conn, id);

                break;

        }

    }

    // insert review panel
    public void insertReviewPanel(Connection conn) {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        panel.add(new JLabel("Pontszama"));
        JTextField tf3 = new JTextField();
        panel.add(tf3);
        panel.add(new JLabel("Szovege"));
        JTextField tf4 = new JTextField();
        panel.add(tf4);
        panel.add(new JLabel("Cime"));

        JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
        String[] idName = JS.queryMovies(conn);
        String[] choices = new String[idName.length];
        String[] choices2 = new String[idName.length];
        int j = 0;
        for (int i = 0; i < idName.length; i++) {
            if (i % 2 == 1) {
                choices[j] = idName[i];
                j++;
            }

            if (idName[i] == null) {
                break;
            } else {
                choices2[j] = idName[i];
            }
        }
        String[] movieNames = new String[j];
        for (int i = 0; i < j; i++) {
            movieNames[i] = choices[i];
        }
        int[] movieIds = new int[j];
        for (int i = 0; i < j; i++) {
            movieIds[i] = Integer.parseInt(choices2[i]);
        }

        final JComboBox<String> cb = new JComboBox<String>(movieNames);

        cb.setMaximumSize(cb.getPreferredSize());
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(cb);

        switch (JOptionPane.showConfirmDialog(null, panel, "Choose a moive:", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.CANCEL_OPTION:

                break;
            case JOptionPane.CLOSED_OPTION:

                break;
            case JOptionPane.OK_OPTION:

                double score = 0;
                // parse double from string
                String scoreGT = tf3.getText();
                if (!(scoreGT.equals(""))) {
                    score = Double.parseDouble(scoreGT);
                }
                int movie = movieIds[cb.getSelectedIndex()];
                String text = tf4.getText();

                if (score > 10 || score < 0) {
                    JOptionPane.showMessageDialog(null, "Pontszam 0 es 10 kozott lehet!");
                    insertReviewPanel(conn);
                    break;

                } else {
                    JdbcSQLiteConnection JS2 = new JdbcSQLiteConnection();
                    JS2.insertReview(conn, movie, text, score);
                }

                break;

        }

    }

    // view review panel
    public void viewReviewPanel(Connection conn) {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Film cime"));

        JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
        String[] idName = JS.queryMovies(conn);
        String[] choices = new String[idName.length];
        String[] choices2 = new String[idName.length];
        int j = 0;
        for (int i = 0; i < idName.length; i++) {
            if (i % 2 == 1) {
                choices[j] = idName[i];
                j++;
            }

            if (idName[i] == null) {
                break;
            } else {
                choices2[j] = idName[i];
            }
        }
        String[] movieNames = new String[j];
        for (int i = 0; i < j; i++) {
            movieNames[i] = choices[i];
        }
        int[] movieIds = new int[j];
        for (int i = 0; i < j; i++) {
            movieIds[i] = Integer.parseInt(choices2[i]);
        }

        final JComboBox<String> cb = new JComboBox<String>(movieNames);

        cb.setMaximumSize(cb.getPreferredSize());
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(cb);

        switch (JOptionPane.showConfirmDialog(null, panel, "Choose a moive:", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.CANCEL_OPTION:

                break;
            case JOptionPane.CLOSED_OPTION:

                break;
            case JOptionPane.OK_OPTION:

                int movie_id = movieIds[cb.getSelectedIndex()];

                JdbcSQLiteConnection JS2 = new JdbcSQLiteConnection();
                String[] reviews = JS2.queryReviews(conn, movie_id);
                JS.JScrollablePanelTest(reviews);

                break;

        }

    }

    // insert actor panel
    public void insertActorPanel(Connection conn) {
        JPanel panel = new JPanel(new GridLayout(6, 1));

        panel.add(new JLabel("Film cime"));

        JdbcSQLiteConnection JS = new JdbcSQLiteConnection();
        String[] idName = JS.queryMovies(conn);
        String[] choices = new String[idName.length];
        String[] choices2 = new String[idName.length];
        int j = 0;
        for (int i = 0; i < idName.length; i++) {
            if (i % 2 == 1) {
                choices[j] = idName[i];
                j++;
            }

            if (idName[i] == null) {
                break;
            } else {
                choices2[j] = idName[i];
            }
        }
        String[] movieNames = new String[j];
        for (int i = 0; i < j; i++) {
            movieNames[i] = choices[i];
        }
        int[] movieIds = new int[j];
        for (int i = 0; i < j; i++) {
            movieIds[i] = Integer.parseInt(choices2[i]);
        }

        final JComboBox<String> cb = new JComboBox<String>(movieNames);

        cb.setMaximumSize(cb.getPreferredSize());
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(cb);

        panel.add(new JLabel("Nev"));
        JTextField tf1 = new JTextField();
        panel.add(tf1);
        panel.add(new JLabel("Szuletesi ev"));
        JTextField tf2 = new JTextField();
        panel.add(tf2);
        panel.add(new JLabel("Orszag"));
        JTextField tf3 = new JTextField();
        panel.add(tf3);
        panel.add(new JLabel("Nem"));
        JTextField tf4 = new JTextField();
        panel.add(tf4);

        switch (JOptionPane.showConfirmDialog(null, panel, "Add an actor:", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.CANCEL_OPTION:

                break;
            case JOptionPane.CLOSED_OPTION:

                break;
            case JOptionPane.OK_OPTION:

                String name = tf1.getText();
                int birth_year = 0;
                // parse int from string
                String birth_yearGT = tf2.getText();
                if (!(birth_yearGT.equals(""))) {
                    birth_year = Integer.parseInt(birth_yearGT);
                }
                String country = tf3.getText();
                String gender = tf4.getText();

                int movie_id = movieIds[cb.getSelectedIndex()];
                JdbcSQLiteConnection JS2 = new JdbcSQLiteConnection();

                JS2.insertActor(conn, movie_id, birth_year, name, country, gender);

                break;

        }

    }

    // free text query panel
    public void freeTextQueryPanel(Connection conn) {
        JPanel panel = new JPanel(new GridLayout(1, 1));

        panel.add(new JLabel("Kerdes:"));

        JTextField tf1 = new JTextField();
        panel.add(tf1);

        switch (JOptionPane.showConfirmDialog(null, panel, "Free text query:", JOptionPane.OK_CANCEL_OPTION)) {
            case JOptionPane.CANCEL_OPTION:

                break;
            case JOptionPane.CLOSED_OPTION:

                break;
            case JOptionPane.OK_OPTION:

                String query = tf1.getText();

                JdbcSQLiteConnection JS2 = new JdbcSQLiteConnection();
                String[] result = JS2.freeTextQuery(conn, query);
                JS2.JScrollablePanelTest(result);

                break;

        }

    }

    public void print(String[] table) {

        File file = new File("G:/EGYETEM/2022_2023_2/adat2/beadando", "Query.txt");
        if (!(file.exists())) {
            try {

                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try (FileWriter myWriter = new FileWriter(
                "G:/EGYETEM/2022_2023_2/adat2/beadando/Query.txt", true);) {

            int i = 0;
            while (table[i] != null) {
                myWriter.write(table[i] + "\n");
                i++;
            }

        } catch (IOException e) {
            System.out.println("Bajvan");
            e.printStackTrace();
        }

    }

    // ez a függvény csinálja a kapcsolatot a db-el
    static Connection konnekt() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");

            String dbURL = "jdbc:sqlite:G:/EGYETEM/2022_2023_2/adat2/sqlite-tools-win32-x86-3410100/beadando";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");

            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;

    }

    // ez a függvény azt csinálja hogy lezárja a kapcsolatot a db-el
    static void klose(Connection conn) {

        try {

            conn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // query directors
    public String[] queryDirectors(Connection conn) {
        String query[] = new String[100];
        int i = 0;

        String sql = "SELECT id, name FROM directors";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                query[i] = Integer.toString(id);
                i++;
                query[i] = name;
                i++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return query;
    }

    // query movies
    public String[] queryMovies(Connection conn) {
        String query[] = new String[100];
        int i = 0;

        String sql = "SELECT id, title FROM movies";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                query[i] = Integer.toString(id);
                i++;
                query[i] = title;
                i++;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return query;
    }

    // query reviews
    public String[] queryReviews(Connection conn, int movie_id) {
        String query[] = new String[100];
        int i = 0;

        String sql = "SELECT reviews.id AS rid, review, reviews.rating AS riv, date FROM reviews INNER JOIN movies ON movies.id = reviews.movie_id WHERE movies.id = "
                + movie_id;

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("rid");
                String review = rs.getString("review");
                double raiting = rs.getDouble("riv");
                String date = rs.getString("date");
                query[i] = Integer.toString(id);
                i++;
                query[i] = review;
                i++;
                query[i] = Double.toString(raiting);
                i++;
                query[i] = date;
                i++;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return query;
    }

    // free text query
    public String[] freeTextQuery(Connection conn, String query) {
        String result[] = new String[100];
        String row;
        int i = 0;

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                row = "";

                for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
                    row += rs.getString(j);
                    row += " | ";

                }
                result[i] = row;
                i++;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    // select movies, directors and studios
    public String[] viewTable(Connection conn) {
        String query[] = new String[100];
        int i = 0;

        String sql = "select title, year, genre, rating, directors.name AS din, born, gender, directors.country AS dic,"
                +
                "studios.name AS stn, founded, studios.country AS stc from movies inner join directors on" +
                " movies.director = directors.id inner join studios on movies.studio = studios.id;";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String title = rs.getString("title");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");
                float rating = rs.getFloat("rating");
                String director = rs.getString("din");
                String born = rs.getString("born");
                String gender = rs.getString("gender");
                String country = rs.getString("dic");
                String studio = rs.getString("stn");
                String founded = rs.getString("founded");
                String scountry = rs.getString("stc");

                query[i] = String.format("%30s |%8d |%10s |%8.1f |%30s |%8s |%8s |%10s |%30s |%8s |%10s \n",
                        title, year, genre, rating, director, born, gender, country, studio, founded, scountry);
                i++;

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return query;

    }

    // select movies with actors
    public String[] viewMoviesWithActors(Connection conn) {
        String query[] = new String[100];
        int i = 0;

        String sql = "SELECT name, born, gender, country, title, year, genre FROM actors INNER JOIN movieActors ON actors.id = movieActors.actor_id INNER JOIN movies ON movieActors.movie_id = movies.id;";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                int born = rs.getInt("born");
                String gender = rs.getString("gender");
                String country = rs.getString("country");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");

                query[i] = String.format("%30s |%8d |%10s |%10s |%30s |%8d |%8s \n",
                        name, born, gender, country, title, year, genre);
                i++;

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return query;
    }

    // rendezőt ad meg # végül nem használtam
    static void insertRowDirectors(Connection conn, String name, int born, String gender, String country) {
        String sql = "INSERT INTO directors(name, born, gender, country) VALUES(\"" + name + "\"," + born + ","
                + ",\"" + gender + "\"," + "\"" + country + "\"," + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ez a függvény azt csinálja hogy több sort is beilleszt a táblába batch módban
    // egy fileból
    public void batchInsert(Connection conn) {

        // biztonság kedvéért
        deleteTable(conn);

        try (Statement stmt = conn.createStatement()) {

            try {
                File file = new File("G:/EGYETEM/2022_2023_2/adat2/beadando", "Inserts.txt");
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                // if file is empty
                if (file.length() != 0) {
                    stmt.addBatch(" UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'movies';");
                    stmt.addBatch(" UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'directors';");
                    stmt.addBatch(" UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'reviews';");
                    stmt.addBatch(" UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'studios';");
                    stmt.addBatch(" UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'actors';");
                    stmt.addBatch(" UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = 'movieActors';");

                }

                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    stmt.addBatch(line);
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ez a függvény azt csinálja hogy frissít egy sort a táblában, ahol a szam
    // oszlop értéke egyenlő az id-val, de nemtudom hogy kell # végül nem használtam
    static void updateRow(Connection conn, int id, String name) {

        String sql = "UPDATE table1 SET name = " + " \"" + name + " \"" + " WHERE szam = " + id + ";";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ez a függvény minden tábla tartalmát törli
    public void deleteTable(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.addBatch("DELETE FROM movies;");
            stmt.addBatch("DELETE FROM directors;");
            stmt.addBatch("DELETE FROM reviews;");
            stmt.addBatch("DELETE FROM studios;");
            stmt.addBatch("DELETE FROM actors;");
            stmt.addBatch("DELETE FROM movieActors;");
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ez a függvény azt csinálja hogy lekérdezi a táblából az összes sort TODO:
    // rendezo es studio
    public String[] queryDire(Connection conn, String name, int born, String gender, String country, String title,
            int year,
            String genre, double rating, String studio) {
        String[] query = new String[100];
        int i = 0;
        int first = 0;
        String sql = "SELECT title, year, genre, rating," +
                "directors.name AS din, born, gender, directors.country AS dic, studios.name AS stn, founded, studios.country AS stc FROM movies INNER JOIN directors ON movies.director = directors.id INNER JOIN studios ON movies.studio = studios.id";

        if (!(name.equals("")) || born != 0 || !(gender.equals("")) || !(country.equals("")) || !(title.equals(""))
                || year != 0 || !(genre.equals("")) || rating != 0 || !(studio.equals(""))) {
            sql += " WHERE";
        }

        if (!(name.equals(""))) {
            if (first == 0) {
                sql += " name = " + "\"" + name + "\"";
                first = 1;
            } else {
                sql += " AND name = " + "\"" + name + "\"";
            }
        }
        if (born != 0) {
            if (first == 0) {
                sql += " born = " + born;
                first = 1;
            } else {
                sql += " AND born = " + born;
            }
        }
        if (!(gender.equals(""))) {
            if (first == 0) {
                sql += " gender = " + "\"" + gender + "\"";
                first = 1;
            } else {
                sql += " AND gender = " + "\"" + gender + "\"";
            }

        }
        if (!(country.equals(""))) {
            if (first == 0) {
                sql += " country = " + "\"" + country + "\"";
                first = 1;
            } else {
                sql += " AND country = " + "\"" + country + "\"";
            }
        }
        if (!(title.equals(""))) {
            if (first == 0) {
                sql += " title = " + "\"" + title + "\"";
                first = 1;
            } else {
                sql += " AND title = " + "\"" + title + "\"";
            }
        }
        if (year != 0) {
            if (first == 0) {
                sql += " year = " + year;
                first = 1;
            } else {
                sql += " AND year = " + year;
            }
        }
        if (!(genre.equals(""))) {
            if (first == 0) {
                sql += " genre = " + "\"" + genre + "\"";
                first = 1;
            } else {
                sql += " AND genre = " + "\"" + genre + "\"";
            }
        }
        if (rating != 0) {
            if (first == 0) {
                sql += " rating = " + rating;
                first = 1;
            } else {
                sql += " AND rating = " + rating;
            }
        }
        if (!(studio.equals(""))) {
            if (first == 0) {
                sql += " stn = " + "\"" + studio + "\"";
                first = 1;
            } else {
                sql += " AND stn = " + "\"" + studio + "\"";
            }
        }

        sql += ";";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {

                title = rs.getString("title");
                year = rs.getInt("year");
                genre = rs.getString("genre");
                rating = rs.getDouble("rating");

                name = rs.getString("din");
                born = rs.getInt("born");
                gender = rs.getString("gender");
                country = rs.getString("dic");

                studio = rs.getString("stn");
                int founded = rs.getInt("founded");
                String studioCountry = rs.getString("stc");

                query[i] = String.format("%30s |%8d |%10s |%8.1f |%20s |%8d |%5s |%8s |%20s |%8d |%10s \n",
                        title, year, genre, rating, name, born, gender, country, studio, founded, studioCountry);
                i++;

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return query;
    }

    // ez a függvény a megadott paraméterek alapján egy új filmet ad hozzá a
    // táblához
    public void insertMovie(Connection conn, String title, int year, String genre, double rating, int director) {
        String sql = "INSERT INTO movies (title, year, director, genre, rating) VALUES (\"" + title + "\", " + year
                + ", " + director + ", \"" + genre + "\", " + rating + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // delete movie
    public void deleteMovie(Connection conn, int id) {
        String sql = "DELETE FROM movies WHERE id = " + id + ";";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // insertReview INSERT INTO reviews (movie_id, review, rating, date) VALUES (1,
    // 'Great movie!', 9, '2019-01-01');
    public void insertReview(Connection conn, int movie_id, String review, double rating) {
        String date = LocalDate.now().toString();
        String sql = "INSERT INTO reviews (movie_id, review, rating, date) VALUES (" + movie_id + ", \"" + review
                + "\", " + rating + ", \"" + date + "\");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // insertActor INSERT INTO actors (name, born, gender, country) VALUES ('Tim
    // Robbins', 1958, 'male', 'USA');
    public void insertActor(Connection conn, int movie_id, int birth_year, String name, String country, String gender) {

        String sql = "INSERT INTO actors (name, born, gender ,country) VALUES (\"" + name + "\", " + birth_year + ", \""
                + gender + "\", \""
                + country + "\");";

        String sql2 = "SELECT id FROM actors WHERE name = \"" + name + "\" AND born = " + birth_year + ";";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        int actor_id = 0;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql2);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                actor_id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql3 = "INSERT INTO movieActors (movie_id, actor_id) VALUES (" + movie_id + ", " + actor_id + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // int ellenörzés # végül nem használtam
    static boolean validInt(String sc) {

        try {
            Integer.parseInt(sc);
            return true;
        } catch (InputMismatchException e) {

            return false;
        }

    }

    // név ellenőrzés # végül nem használtam
    static boolean nameCheck(String nam) {
        String name = nam.trim();
        if (!Pattern.matches("^[\\p{L}\\p{M}' \\.\\-]+$", name)) {

            return false;
        } else {

            return true;
        }
    }

    public static void main(String[] args) {

        login();

        Connection conn = konnekt();

        JdbcSQLiteConnection js = new JdbcSQLiteConnection();

        js.simpleWindow(conn);

    }

}
