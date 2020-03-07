package sample.store;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

import static sample.store.ElectronicStore.*;

public class ElectronicStoreApp extends Application{
    private Button reset;
    public static ArrayList<Product> stockQuantity = new ArrayList<Product>();



    int stock = 10;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ElectronicStore electronicStore = createStore();
        primaryStage.setTitle("Electronic Store Application - " + electronicStore.getName());
        BorderPane borderPane = getBorderPane(electronicStore);
        Scene scene = new Scene(borderPane, 800, 400);

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BorderPane borderPane = getBorderPane(electronicStore);
                Scene scene = new Scene(borderPane, 800, 400);
                primaryStage.setScene(scene);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private BorderPane getBorderPane(ElectronicStore electronicStore){
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10,10,10,10));

        //Create Lists
        ListView<Product> stockList = new ListView<Product>();
        ListView<Product> cartList = new ListView<Product>();
        ListView<Product> listPop = new ListView<Product>();

         //Create Labels
        //Left
        VBox leftBox = new VBox();
        Text text = new Text("Store Summary");
        leftBox.setAlignment(Pos.TOP_CENTER);
        leftBox.getChildren().add(text);
        GridPane gridPane = new GridPane();

        Label label1 = new Label("# Sale: ");
        gridPane.add(label1, 0,1);
        TextField salesField = new TextField();
        salesField.setPrefWidth(70);
        salesField.setText("0");
        gridPane.add(salesField, 1,1);

        Label label2 = new Label ("Revenue ");
        gridPane.add(label2, 0,2);
        TextField revenueField = new TextField();
        revenueField.setPrefWidth(70);
        revenueField.setText("0.00");
        gridPane.add(revenueField, 1,2);

        Label label3 = new Label("S / Sales");
        gridPane.add(label3, 0, 3);
        TextField saleDollarField = new TextField();
        saleDollarField.setPrefWidth(70);
        saleDollarField.setText("N/A");
        gridPane.add(saleDollarField, 1,3);

        gridPane.setAlignment(Pos.TOP_CENTER);
        leftBox.setMargin(gridPane, new Insets(5,5,5,5));
        leftBox.getChildren().add(gridPane);

        Text text1 = new Text ("Most Popular Items: ");
        leftBox.getChildren().add(text1);

        listPop.setPrefHeight(200);
        Product[] list = electronicStore.getStock();
        Product[] listPopular = new Product[3];
        for (int i=0; i<3; i++){
            listPopular[i] = list[i];
        }
        listPop.setItems(FXCollections.observableArrayList(listPopular));

        leftBox.setMargin(listPop, new Insets(5,5,5,5));
        leftBox.getChildren().add(listPop);

        reset = new Button("Reset Store");
        leftBox.getChildren().add(reset);

        leftBox.setPrefWidth(200);
        borderPane.setLeft(leftBox);
        BorderPane.setMargin(leftBox,new Insets(5,5,5,5));

        leftBox.setPrefWidth(200);
        borderPane.setLeft(leftBox);
        BorderPane.setMargin(leftBox,new Insets(5,5,5,5));

        //Center
        VBox centerBox = new VBox();
        Text storeStockText = new Text("Store Stock:");
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.getChildren().add(storeStockText);
        stockList.setPrefHeight(300);
        for (Product product : electronicStore.stock){
            if (product == null) break;
            stockList.getItems().add(product);
        }

        centerBox.setMargin(stockList, new Insets(5,5,5,5));
        centerBox.getChildren().add(stockList);
        centerBox.setPrefWidth(350);
        Button addButton = new Button("Add to Cart");
        addButton.setDisable(true);
        centerBox.getChildren().add(addButton);

        stockList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Object obj = stockList.getSelectionModel().getSelectedItems();
                if (obj != null){
                    addButton.setDisable(false);
                }else{
                    addButton.setDisable(true);
                }
            }
        });

        borderPane.setCenter(centerBox);
        borderPane.setAlignment(centerBox, Pos.TOP_CENTER);
        BorderPane.setMargin(centerBox, new Insets(5,5,5,5));

        //Right
        VBox rightBox = new VBox();
        Text totalText = new Text("Current Cart ($0.00):");
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.getChildren().add(totalText);

        cartList.setPrefHeight(300);
        rightBox.getChildren().add(cartList);
        centerBox.setMargin(cartList, new Insets(5,5,5,5));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_CENTER);
        Button removeButton = new Button ("Remove from Cart");
        removeButton.setDisable(true);
        hBox.getChildren().add(removeButton);
        Button completeButton = new Button("Complete Sale");
        completeButton.setDisable(true);
        hBox.getChildren().add(completeButton);
        rightBox.getChildren().add(hBox);
        rightBox.setPrefWidth(250);
        borderPane.setRight(rightBox);
        BorderPane.setMargin(rightBox, new Insets(5,5,5,5));

        cartList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Object obj = cartList.getSelectionModel().getSelectedItem();
                if (obj != null) {
                    removeButton.setDisable(false);
                } else {
                    removeButton.setDisable(true);
                }
            }
        });



        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cartList.getItems() == null) {
                    cartList.setItems(FXCollections.observableArrayList(stockList.getSelectionModel().getSelectedItem()));
                } else {
                    cartList.getItems().add(stockList.getSelectionModel().getSelectedItem());
                }
                completeButton.setDisable(false);


                if(stockQuantity.get(stockQuantity.indexOf(stockList.getSelectionModel().getSelectedItem())).getSoldQuantity() <= 0){
                    stockList.getItems().remove(stockList.getSelectionModel().getSelectedItem());
                }

                if (stockList.getItems().size()<=0){
                    stockList.getItems().add(stockList.getSelectionModel().getSelectedItem());
                }else {
                    stockList.getItems().remove(cartList);
                }
                
                String t = totalText.getText();
                double total = Double.parseDouble(t.substring(t.indexOf('$')+1, t.indexOf(')')));
                for (Product p : cartList.getItems()) {
                    total = total + p.getPrice();
                    totalText.setText("Current Cart($" + total + ')');
                    if (stockList.getItems().size()==0);{
                        addButton.setDisable(true);
                    }
                }
            }
        });

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object obj = cartList.getSelectionModel().getSelectedItem();
                if (obj != null) {
                    //cartList.setItems(FXCollections.observableArrayList(stockList.getSelectionModel().getSelectedItem()));
                    cartList.getItems().remove(obj);
                    //stockList.getItems().add((Product)(obj));

                    String t = totalText.getText();
                    double total = 0;
                    totalText.setText("Current Cart($" + total + ')');
                    for (Product p : cartList.getItems()) {
                        total = total + p.getPrice();
                        totalText.setText("Current Cart($" + total + ')');
                        if (stockList.getItems().size() == 0) ;
                        {
                            removeButton.setDisable(true);
                            completeButton.setDisable(true);
                        }
                    }
                }
            }
        });

        return borderPane;

    }
}
