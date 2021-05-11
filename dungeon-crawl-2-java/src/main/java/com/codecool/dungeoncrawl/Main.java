package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.SaveModel;
import com.codecool.dungeoncrawl.savemodule.Save;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();


    Text healthLabel = new Text("Health "+map.getPlayer().getHealth());
    Text inventory = new Text(map.getPlayer().toString());


    GameDatabaseManager dbManager;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 0, 1);
        var pickItem = new Label("Pick-up item");
        ui.add(pickItem, 0,2);
        ui.add(inventory,0,3);
//        ui.add(healthLabel, 1, 0);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
//===========================================================================================
        var layout = new VBox();
        Scene startMenu = new Scene(layout, 800, 640);

        Button loadButton = new Button("Load Game");
        Button playButton = new Button("Play");

        Label title = new Label("Dungeon Crawl");
        title.setId("title");

        playButton.setOnAction(e -> primaryStage.setScene(scene));
        layout.getChildren().addAll(title, playButton, loadButton);
        layout.setAlignment(Pos.CENTER);
        startMenu.getStylesheets().add("stylesheet.css");
        loadButton.setOnAction(e->{
            loadMenu(scene);
//            map = Save.loadState("src/main/saves/yyy.txt");
//            primaryStage.setScene(scene);
            refresh();

        });
//=============================================================================================


        pickItem.setOnMouseClicked(actionEvent ->{
            Player currentPlayer = map.getPlayer();
            int itemNumber = currentPlayer.checkCellType();
            currentPlayer.pickUpItem(itemNumber);
            inventory.setText((map.getPlayer().toString()));
        });

        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
//        pickItem.setOnMouseClicked();

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();

        primaryStage.setScene(startMenu);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }
    //=============================================================================================

    private void loadMenu(Scene scene) {

        Stage primaryStage = new Stage();
        VBox loadLayout = new VBox();
        Scene loadMenu = new Scene(loadLayout, 800, 640);

        Button loadButton2 = new  Button("Load");
        Label label = new  Label("Load Saved Games");



        var savedGame = dbManager.getSavedData();
        ComboBox comboBox = new ComboBox();
        for(SaveModel item : savedGame){
            String data = item.getName()+" "+item.getSaveDate();
            System.out.println(item.getName());
            comboBox.getItems().add(data);

        }


        loadButton2.setOnAction(e->{
            String selectedValue = (String) comboBox.getValue();
            var formattedValue = selectedValue.split(" ")[0];
            map = Save.loadState("src/main/saves/"+formattedValue +".txt");
            primaryStage.setScene(scene);
            refresh();

        });



        loadLayout.getChildren().addAll(label, comboBox, loadButton2);
        primaryStage.setScene(loadMenu);
        primaryStage.show();
    }
    //=============================================================================================
    private void saveMenu(){

        Stage  primaryStage = new Stage();
        VBox saveLayout = new VBox();
        Scene saveMenu = new Scene(saveLayout, 800, 640);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        Label label = new Label("Do you want to save the game?");
        TextField inputBox = new TextField();
        Label nameInput = new Label("Enter your name");
        inputBox.setPromptText("name: ");

        noButton.setOnAction(e -> primaryStage.close());
        yesButton.setOnAction(e->{
            Save.saveState(map, inputBox.getText());
            dbManager.saveData((inputBox.getText()));
            primaryStage.close();
        });

        saveLayout.getChildren().addAll(nameInput, inputBox,  label, yesButton, noButton);
        saveLayout.setAlignment(Pos.CENTER);

        primaryStage.setScene(saveMenu);
        refresh();
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();

    }



    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        KeyCombination saveCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
        if(saveCombination.match(keyEvent)){
            saveMenu();
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1, 0);
                refresh();
                break;
            case S:
                Player player = map.getPlayer();
//                dbManager.savePlayer(player);
                Save.saveState(map, "state");
                break;
            case L:
                map.getPlayer();
                
                Save.loadState("src/main/resources/yyy.txt");
                break;
//
        }
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        inventory.setText((map.getPlayer().toString()));
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
