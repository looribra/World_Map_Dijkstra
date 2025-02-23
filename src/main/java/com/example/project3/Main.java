package com.example.project3;


import com.example.project3.graph.Edge;
import com.example.project3.graph.Graph;
import com.example.project3.graph.Vertex;
import javafx.animation.*;
import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main extends Application {

    private Pane mapPane;
    private TableView<TableRow> pathTable;
    private TextField totalDistanceField;
    private TextField timeField;
    private TextField costField;
    private ImageView mapImageView;


    HashMap<String, Country> countries = new HashMap<>();
    HashMap<String,Vertex<Country>> vertices = new HashMap<>();

    Graph<Country> graph = new Graph<>(true, true);  //weighted and directed graph
    Graph<Country> graphCost = new Graph<>(true, true);  //weighted and directed graph
    Graph<Country> graphTime = new Graph<>(true, true);  //weighted and directed graph

    @Override
    @SuppressWarnings("unchecked")
    public void start(Stage stage){

        BorderPane mainPane = new BorderPane();
        Scene mainPageScene = new Scene(mainPane, 800, 600);
        mainPane.setPadding(new Insets(20));

        //style the scene
        mainPageScene.getStylesheets().add("style.css");

        //right side
        VBox rightSide = new VBox();
        rightSide.setSpacing(30);





        Label fromLabel = new Label("From:");
        fromLabel.setStyle("-fx-font-weight: bold;-fx-background-color: #07589D; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-size: 16px;");
        fromLabel.setMaxWidth(80);
        fromLabel.setMinWidth(80);
        fromLabel.setPrefWidth(80);
        fromLabel.setAlignment(Pos.CENTER);
        ComboBox<String> sourceCountry = new ComboBox<>();
        sourceCountry.setPrefHeight(30);
        sourceCountry.setPrefWidth(150);
        AutoCompleteComboBoxListener<String> autoCompleteComboBoxListener = new AutoCompleteComboBoxListener<>(sourceCountry);
        sourceCountry.setStyle("-fx-font-size: 14px;-fx-border-radius: 10px; -fx-background-radius: 10px;");


        HBox fromBox = new HBox();
        fromBox.setSpacing(10);
        fromBox.setAlignment(Pos.CENTER);
        fromBox.getChildren().addAll(fromLabel, sourceCountry);

        Label toLabel = new Label("To:");
        toLabel.setStyle("-fx-font-weight: bold;-fx-background-color: #07589D; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-size: 16px;");
        toLabel.setMaxWidth(80);
        toLabel.setMinWidth(80);
        toLabel.setPrefWidth(80);
        toLabel.setAlignment(Pos.CENTER);

        ComboBox<String> destinationCountry = new ComboBox<>();
        destinationCountry.setPrefHeight(30);
        destinationCountry.setPrefWidth(150);
        destinationCountry.setStyle("-fx-font-size: 14px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        AutoCompleteComboBoxListener<String> autoCompleteComboBoxListener2 = new AutoCompleteComboBoxListener<>(destinationCountry);

        HBox toBox = new HBox();
        toBox.setSpacing(10);
        toBox.setAlignment(Pos.CENTER);
        toBox.getChildren().addAll(toLabel, destinationCountry);



        Label filterLabel = new Label("Filter:");
        filterLabel.setStyle("-fx-font-weight: bold;-fx-background-color: #07589D; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-size: 16px;");
        filterLabel.setMaxWidth(80);
        filterLabel.setMinWidth(80);
        filterLabel.setPrefWidth(80);
        filterLabel.setAlignment(Pos.CENTER);
        ComboBox<String> filter = new ComboBox<>();
        filter.setPrefHeight(30);
        filter.setPrefWidth(150);
        filter.setStyle("-fx-font-size: 14px;-fx-border-radius: 10px; -fx-background-radius: 10px;");
        //fill the combo box with the Shortest Path and the Fastest Path and the lowest cost
        filter.getItems().addAll("Shortest Path", "Fastest Path", "Lowest Cost");

        HBox filterBox = new HBox();
        filterBox.setSpacing(10);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.getChildren().addAll(filterLabel, filter);

        VBox srcDestBox = new VBox();
        srcDestBox.setSpacing(10);
        srcDestBox.setAlignment(Pos.CENTER);
        srcDestBox.getChildren().addAll(fromBox, toBox, filterBox);

        VBox buttons = new VBox();
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        Button findPath = new Button("Find Path");
        findPath.setPrefSize(150, 35);
        findPath.setMaxSize(150, 35);
        findPath.setMinSize(150, 35);
        findPath.setStyle("-fx-background-color: #F9F7F2;-fx-border-color: #07589D; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-font-size: 16px; -fx-text-fill: #07589D;");
        findPath.setOnMouseEntered(e -> findPath.setStyle("-fx-background-color: #07589D;-fx-border-color: #07589D; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-font-size: 16px; -fx-text-fill: white;"));
        findPath.setOnMouseExited(e -> findPath.setStyle("-fx-background-color: #F9F7F2;-fx-border-color: #07589D; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-font-size: 16px; -fx-text-fill: #07589D;"));
        Button clearPath = new Button("Clear");
        clearPath.setPrefSize(150, 35);
        clearPath.setMaxSize(150, 35);
        clearPath.setMinSize(150, 35);
        clearPath.setStyle("-fx-background-color: #F9F7F2;-fx-border-color: #07589D; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-font-size: 16px;-fx-text-fill: #07589D;");
        clearPath.setOnMouseEntered(e -> clearPath.setStyle("-fx-background-color: #07589D;-fx-border-color: #07589D; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-font-size: 16px; -fx-text-fill: white;"));
        clearPath.setOnMouseExited(e -> clearPath.setStyle("-fx-background-color: #F9F7F2;-fx-border-color: #07589D; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-font-size: 16px;-fx-text-fill: #07589D;"));
        buttons.getChildren().addAll(findPath, clearPath);


        Label pathLabel = new Label("  Path Details  ");
        pathLabel.setStyle("-fx-font-weight: bold;-fx-background-color: #07589D; -fx-border-radius: 10px; -fx-background-radius: 15px; -fx-text-fill: white; -fx-font-size: 20px;");

        pathTable = new TableView<>();
        pathTable.setPrefWidth(300);
        pathTable.setPrefHeight(300);

        TableColumn<TableRow, String> fromColumn = new TableColumn<>("From");
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        fromColumn.setPrefWidth(100);
        TableColumn<TableRow, String> toColumn = new TableColumn<>("To");
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        toColumn.setPrefWidth(100);
        TableColumn<TableRow, String> distanceColumn = new TableColumn<>("Distance");
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        distanceColumn.setPrefWidth(100);

        pathTable.getColumns().addAll(fromColumn, toColumn, distanceColumn);

        Label totalDistanceLabel = new Label(" Total Distance: ");
        totalDistanceLabel.setStyle("-fx-font-weight: bold;-fx-background-color: #07589D; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-size: 16px;");
        totalDistanceField = new TextField();
        totalDistanceField.setPrefWidth(100);
        totalDistanceField.setEditable(false);
        HBox totalDistanceBox = new HBox();
        totalDistanceBox.setSpacing(10);
        totalDistanceBox.setAlignment(Pos.CENTER);
        totalDistanceBox.getChildren().addAll(totalDistanceLabel, totalDistanceField);


        Label costLabel = new Label("Cost: ");
        costLabel.setStyle("-fx-font-weight: bold;-fx-background-color: #07589D; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-size: 16px;");
        costField = new TextField();
        costField.setEditable(false);

        HBox costBox = new HBox();
        costBox.setSpacing(10);
        costBox.setAlignment(Pos.CENTER);
        costBox.getChildren().addAll(costLabel, costField);


        Label timeLabel = new Label("Time: ");
        timeLabel.setStyle("-fx-font-weight: bold;-fx-background-color: #07589D; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-size: 16px;");

        TextField timeField = new TextField();
        timeField.setEditable(false);

        HBox timeBox = new HBox();
        timeBox.setSpacing(10);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.getChildren().addAll(timeLabel, timeField);

        VBox pathBox = new VBox();
        pathBox.setSpacing(10);
        pathBox.setAlignment(Pos.CENTER);
        pathBox.getChildren().addAll(pathLabel, pathTable, totalDistanceBox, costBox, timeBox);


        rightSide.setAlignment(Pos.TOP_CENTER);
        rightSide.getChildren().addAll(srcDestBox,buttons,pathBox );
        mainPane.setRight(rightSide);



        Image mapImage = new Image("world-map.jpg");

        mapImageView = new ImageView(mapImage);
        mapImageView.setPreserveRatio(true);
        mapImageView.setFitWidth(1000  );
        mapImageView.setFitHeight( 595.83 );
        mapPane = new Pane();
        mapPane.getChildren().add(mapImageView);
        mapPane.setMinSize(1000 ,  595.83);
        mapPane.setMaxSize(1000 ,  595.83 );
        mapPane.setPrefSize(1000 ,  595.83 );
        mapPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        mainPane.setLeft(mapPane);
        BorderPane.setAlignment(mapPane, Pos.CENTER);


        //Title Label
        Image logoImage = new Image("logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(550);
        logoImageView.setPreserveRatio(true);
        mainPane.setTop(logoImageView);
        BorderPane.setAlignment(logoImageView, Pos.CENTER_LEFT);

        //read countries from file
        readData();

        //add countries to choice boxes
        String[] countryNames = getCountryNames();
        sourceCountry.getItems().addAll(countryNames);
        destinationCountry.getItems().addAll(countryNames);


        //mapping
        for(String countryName : countries.keySet()){
            Circle circle = new Circle(4);
            double x = lonToX(countries.get(countryName).getLon(), mapImageView) - 33;
            circle.setCenterX(x);
            double y = latToY(countries.get(countryName).getLat(), mapImageView) + 90;
            circle.setCenterY(y);
            circle.setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 1px;");
            circle.setOnMousePressed(e -> {
                //handle left click and right click
                if(e.getButton() == MouseButton.PRIMARY){
                    sourceCountry.setValue(countryName);
                }else if(e.getButton() == MouseButton.SECONDARY){
                    destinationCountry.setValue(countryName);
                }
            });
            Tooltip tooltip = new Tooltip(countryName);
            Tooltip.install(circle, tooltip);
            tooltip.setShowDelay(Duration.millis(0));


            mapPane.getChildren().add(circle);
        }


        //find path button
        findPath.setOnAction(e -> {

            String from = sourceCountry.getValue();
            String to = destinationCountry.getValue();
            String filterValue = filter.getValue();

            if(from == null || to == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please select a country");
                alert.showAndWait();
            }else if (!vertices.containsKey(from)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Source Country not found");
                alert.showAndWait();
            } else if (!vertices.containsKey(to)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Destination Country not found");
                alert.showAndWait();
            } else if (filterValue == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please select a filter");
                alert.showAndWait();

            } else{
                Dijkstra<Country> dijkstra = new Dijkstra<>(graph);
                LinkedList<Vertex<Country>> path;
                switch (filterValue) {
                    case "Shortest Path":
                        dijkstra = new Dijkstra<>(graph);
                        path = dijkstra.getShortestPath(vertices.get(from), vertices.get(to));
                        break;
                    case "Fastest Path":
                        dijkstra = new Dijkstra<>(graphTime);
                        path = dijkstra.getShortestPathTime(vertices.get(from), vertices.get(to));
                        break;
                    case "Lowest Cost":
                        dijkstra = new Dijkstra<>(graphCost);
                        path = dijkstra.getShortestPathTime(vertices.get(from), vertices.get(to));
                        break;
                    default:
                        path = dijkstra.getShortestPath(vertices.get(from), vertices.get(to));
                        break;
                }

                if (path != null) {
                    displayPath(path, filterValue, from, to);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No path found");
                    alert.showAndWait();
                }

            }
        });

        //clear path button
        clearPath.setOnAction(e -> {
            pathTable.getItems().clear();
            mapPane.getChildren().removeIf(node -> node instanceof Line);
            totalDistanceField.setText("");
            mapPane.getChildren().removeIf(node -> node instanceof ImageView && node.getId() != null && node.getId().equals("plane"));
            sourceCountry.setValue(null);
            destinationCountry.setValue(null);

        });

        stage.setScene(mainPageScene);
        stage.setMaximized(true);
        stage.show();

    }

    //to calculate the angel of the line so the plane can rotate
    private double calcAngle(double startX, double startY, double endX, double endY) {
        // Calculate the slope to get the angle
        double slope = (endY - startY) / (endX - startX);
        double angleRad = Math.atan(slope);
        double angleDeg = Math.toDegrees(angleRad);

        // Rotate the line by the angle
        if (endX < startX) {
            angleDeg += 180;
        }
        return angleDeg;
    }



    private void readData() {
        try {
            File file = new File("data.txt");
            Scanner scanner = new Scanner(file);

            // Read number of countries and paths
            int numCountries = scanner.nextInt();
            int numPaths = scanner.nextInt();
            scanner.nextLine(); // Consume the remaining newline

            // Debug: Confirm file read
            System.out.println("Number of countries: " + numCountries);
            System.out.println("Number of paths: " + numPaths);

            // Read countries
            for (int i = 0; i < numCountries; i++) {
                String line = scanner.nextLine().trim();
                System.out.println("Reading country line: " + line); // Debug

                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    if (parts.length == 3) {
                        String name = parts[0];
                        double lat = Double.parseDouble(parts[1]);
                        double lon = Double.parseDouble(parts[2]);

                        // Create and add country
                        Country country = new Country(name, lat, lon);
                        countries.put(name, country);

                        // Add vertex to graph
                        Vertex<Country> vertex = graph.addVertex(country);
                        vertices.put(name, vertex);
                    } else {
                        System.err.println("Invalid country line: " + line);
                    }
                }
            }

            // Debug: Confirm countries loaded
            System.out.println("Countries loaded: " + countries.size());

            // Read paths
            for (int i = 0; i < numPaths; i++) {
                String line = scanner.nextLine().trim();
                System.out.println("Reading path line: " + line); // Debug

                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    if (parts.length == 4) {
                        String from = parts[0];
                        String to = parts[1];
                        double cost = Double.parseDouble(parts[2].replace("$", ""));
                        double time = Double.parseDouble(parts[3].replace("min", ""));

                        if (countries.containsKey(from) && countries.containsKey(to)) {
                            double distance = calculateDistance(from, to);

                            // Add edges to appropriate graphs
                            graph.addEdge(vertices.get(from), vertices.get(to), distance);
                            graphCost.addEdge(vertices.get(from), vertices.get(to), cost);
                            graphTime.addEdge(vertices.get(from), vertices.get(to), time);
                        } else {
                            System.err.println("Invalid path: " + line);
                        }
                    } else {
                        System.err.println("Invalid path line: " + line);
                    }
                }
            }

            // Debug: Confirm paths loaded
            System.out.println("Paths loaded successfully.");

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File not found");
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
    }


    //The haversine formula
    //Δ lat = lat2 − lat1 (in radians)
    //Δ long = long2 − long1 (in radians)
    //a = sin²(Δ lat/2) + cos(lat1).cos(lat2).sin²(Δ long/2)
    //c = 2.atan2(√a, √(1−a))
    //d = R.c
    private double calculateDistance(String from, String to) {

        double lat1 = countries.get(from).getLat();
        double lon1 = countries.get(from).getLon();
        double lat2 = countries.get(to).getLat();
        double lon2 = countries.get(to).getLon();

        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c;
    }



    private double deg2rad(double lat1) {
        return (lat1 * Math.PI / 180.0);
    }

    private String[] getCountryNames() {
        String[] countryNames = new String[countries.size()];
        //get the names of the countries
        int i = 0;
        for(String key : countries.keySet()){
            countryNames[i] = key;
            i++;
        }
        return countryNames;
    }




    public static void main(String[] args) {
        launch();
    }


    private double lonToX(double lon, ImageView mapImage){
        double minLon = -180;
        double maxLon = 180;
        double mapWidth = mapImage.getFitWidth();
        double bboxWidth = maxLon - minLon;
        double widthPct = (lon - minLon) / bboxWidth;
        return Math.floor(mapWidth * widthPct);
    }

    private double latToY(double lat, ImageView mapImage){
        double minLat = -79;
        double maxLat = 88;
        double mapHeight = mapImage.getFitHeight();
        double bboxHeight = maxLat - minLat;
        double heightPct = (lat - minLat) / bboxHeight;
        return Math.floor(mapHeight * (1 - heightPct));

    }

    private void displayPath(LinkedList<Vertex<Country>> path, String filterValue, String from, String to) {
        Image image = new Image("plane.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);
        imageView.setRotate(45);
        imageView.setId("plane");

        pathTable.getItems().clear();
        mapPane.getChildren().removeIf(node -> node instanceof Line);
        mapPane.getChildren().removeIf(node -> node instanceof ImageView && node.getId() != null && node.getId().equals("plane"));

        double totalDistance = 0;
        double totalTime = 0;
        double totalCost = 0;

        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < path.size() - 1; i++) {
            Vertex<Country> fromVertex = path.get(i);
            Vertex<Country> toVertex = path.get(i + 1);
            String fromName = fromVertex.getData().getName();
            String toName = toVertex.getData().getName();

            // Draw the path on the map
            Line line = new Line();
            line.setStartX(lonToX(countries.get(fromName).getLon(), mapImageView) - 33);
            line.setStartY(latToY(countries.get(fromName).getLat(), mapImageView) + 90);
            line.setEndX(lonToX(countries.get(toName).getLon(), mapImageView) - 33);
            line.setEndY(latToY(countries.get(toName).getLat(), mapImageView) + 90);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(800), line);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1300), imageView);
            translateTransition.setFromX(line.getStartX());
            translateTransition.setFromY(line.getStartY());
            translateTransition.setToX(line.getEndX());
            translateTransition.setToY(line.getEndY());
            translateTransition.setCycleCount(1);
            translateTransition.setAutoReverse(false);

            RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), imageView);
            rotateTransition.setFromAngle(45);
            rotateTransition.setByAngle(calcAngle(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
            rotateTransition.setCycleCount(1);
            rotateTransition.setAutoReverse(false);

            sequentialTransition.getChildren().addAll(rotateTransition, fadeTransition, translateTransition);
            mapPane.getChildren().add(line);

            // Calculate values
            totalDistance += calculateDistance(fromName, toName);

            // Get weights based on filter
            Edge<Country> edge = fromVertex.getEdges().stream()
                    .filter(e -> e.getTo().equals(toVertex))
                    .findFirst()
                    .orElse(null);

            if (edge != null) {
                if (filterValue.equals("Fastest Path")) {
                    totalTime += edge.getWeight();
                } else if (filterValue.equals("Lowest Cost")) {
                    totalCost += edge.getWeight();
                }
            }

            TableRow row = new TableRow(fromName, toName, String.format("%.2f", totalDistance) + " km");
            pathTable.getItems().add(row);
        }

        mapPane.getChildren().add(imageView);
        sequentialTransition.play();

        // Update UI fields
        totalDistanceField.setText(String.format("%.2f", totalDistance) + " km");
        timeField.setText(filterValue.equals("Fastest Path") ? String.format("%.2f", totalTime) + " min" : "");
        costField.setText(filterValue.equals("Lowest Cost") ? String.format("%.2f", totalCost) + " $" : "");
    }


}