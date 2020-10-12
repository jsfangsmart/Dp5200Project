import com.jfoenix.controls.JFXButton;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.awt.*;
import java.io.File;

/**
 * Description:
 * Auther:smart
 * Date: 2020/9/28 上午8:43
 */
public class MainForm extends Application {

    private TableView<NoteInfo> table = new TableView<NoteInfo>();
    private ObservableList<NoteInfo> data = FXCollections.observableArrayList(
            new NoteInfo("1","DSFOI4895132"),
            new NoteInfo("2","HRGDSG456412"),
            new NoteInfo("3","EGRHRH123456")
    );

    private int id = 1;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Dp5200Tool");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/app.png")));
        stage.setWidth(800);
        stage.setMinWidth(400);
        stage.setHeight(600);
        stage.setHeight(300);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setX(0);
        stage.setY(4);

//        Label label = new Label("Hello MyIdea!");
//        label.setAlignment(Pos.CENTER);
//        label.getStyleClass().add("hello");

//        label.setFont(Font.font(30));
//        label.setTextFill(Color.web("#FFFFFF"));
//        label.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Paint.valueOf("black"), null, null)));
        // 设置设置图标
//        label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("img/app.png"),128,128,false,false)));

//        BorderPane pane = new BorderPane();
//        pane.getStyleClass().add("def_MenuBar");

        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("def_MenuBar");
        Menu menu1 = new Menu("Setup");
        Menu menu2 = new Menu("Update");
        Menu menu3 = new Menu("Operation");
        MenuItem menu1Item1 = new MenuItem("BRU Comm");
        MenuItem menu1Item2 = new MenuItem("CRU Comm");
        menu1.getItems().addAll(menu1Item1,menu1Item2);
        MenuItem menu2Item1 = new MenuItem("BRU Update");
        MenuItem menu2Item2 = new MenuItem("CRU Update");
        menu2.getItems().addAll(menu2Item1,menu2Item2);
        menuBar.getMenus().addAll(menu1,menu2,menu3);

        menu1Item1.setOnAction(
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    // 这个自己看，用处不大
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("弹出的那个1");
                    alert.showAndWait();
                }
            }
        );

//        pane.setTop(menuBar);
//        pane.setCenter(new ScrollPane());

        TableColumn idCol = new TableColumn("ID");
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<Object,Object>("ID"));

        TableColumn serialCol = new TableColumn("SerialNum");
        serialCol.setMinWidth(200);
        serialCol.setCellValueFactory(new PropertyValueFactory<Object,Object>("SerialNum"));

        table.setItems(data);
        table.getColumns().addAll(idCol,serialCol);
        table.setEditable(true);
        table.prefWidthProperty().bind(stage.widthProperty());

        // 这段主要是为了加按钮===========================================================================
        // 既然要添加按钮了，我们就用下Hbox布局（内部元素竖排，类似于HTML的块级元素）吧
        VBox hbox = new VBox();
        Button addButton = new Button("添加");
        addButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                data.add(new NoteInfo(("id" + (++id)), "name"+ (++id)));
            }
        });
        // （这个与本例的中心思想没啥关系，我会在以后的文章中穿插讲一些css的用法）顺便秀一下JFX的按钮
        JFXButton addJFXButton = new JFXButton("添加");
        addJFXButton.getStyleClass().add("button-raised");
        addJFXButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                data.add(new NoteInfo(("id" + (++id)), "name"+ (++id)));
            }
        });
        hbox.getChildren().addAll(menuBar, table, addButton, addJFXButton);
        // 这段主要是为了加按钮===========================================================================

        Scene scence = new Scene(hbox,800,600);

        String strPath = getClass().getResource("css/mainform.css").getFile();
        File file = new File(strPath);
        if (file.exists()) {
            scence.getStylesheets().add(getClass().getResource("css/mainform.css").toExternalForm());
        }
        stage.setScene(scence);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static class NoteInfo{
        private SimpleStringProperty ID;
        private SimpleStringProperty SerialNum;
        private NoteInfo(String strID, String strSerialNum){
            this.ID = new SimpleStringProperty(strID);
            this.SerialNum = new SimpleStringProperty((strSerialNum));

        }

        public String getID() {
            return ID.get();
        }
        public void setID(String strID) {
            ID.set(strID);
        }
        public String getSerialNum(){
            return SerialNum.get();
        }
        public void setSerialNum(String strSerialNum){
            SerialNum.set(strSerialNum);
        }
    }
}
