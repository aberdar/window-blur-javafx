package example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    // переменные для перемещения
    private double offsetX;
    private double offsetY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.TRANSPARENT); // убираем рамки
        primaryStage.setScene(new Scene(createContent(primaryStage), null)); // не заполняем сцену цветом
        primaryStage.show();
    }

    private Parent createContent(Stage stage) {
        var root = new Pane();
        root.setPrefSize(800, 600);

        Robot robot = new Robot();
        // делаем снимок экрана
        WritableImage image = robot.getScreenCapture(null, new Rectangle2D(0, 0, 1920, 1080));
        final ImageView view = new ImageView(image);
        // эффект размытия
        view.setEffect(new GaussianBlur());

        // перетаскивание мзображение
        view.setOnMousePressed(event -> {
            offsetX = event.getScreenX() - stage.getX();
            offsetY = event.getScreenY() - stage.getY();
        });

        view.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX);
            stage.setY(event.getScreenY() - offsetY);
        });

        // Добаление таймера, отслеживание позиции
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!stage.isFocused()) {
                    var image2 = robot.getScreenCapture(null, new Rectangle2D(0, 0, 1920, 1080));
                    view.setImage(image2);
                }

                view.setViewport(new Rectangle2D(stage.getX(), stage.getY(), 800, 600));
            }
        };
        timer.start();

        // кнопка для закрытия окна
        Button btn = new Button("X");
        btn.setOnAction(event -> stage.close());

        root.getChildren().addAll(view, btn);


        return root;
    }

    public static void main(String[] args) {
        launch();
    }
}
