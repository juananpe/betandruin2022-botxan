package ui;

import businessLogic.BlFacade;
import exceptions.UserNotFoundException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.kordamp.bootstrapfx.BootstrapFX;
import uicontrollers.*;
import uicontrollers.admin.*;
import uicontrollers.user.*;
import utils.History;
import utils.Window;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUI {

    private BorderPane mainWrapper;

    public Window navBarLag, welcomeLag, loginLag, registerLag, browseEventsLag,
            userMenuLag, userOverviewLag, profileLag, betsLag, movementsLag,
            adminMenuLag,  createEventLag, createQuestionLag, createForecastLag, adminOverviewLag, eventsLag, questionsLag, forecastsLag;
    private BlFacade businessLogic;
    private Stage stage;
    private Scene scene;

    // Default scene resolution
    public static final int NAVBAR_HEIGHT = 90;
    public static final int SCENE_WIDTH = 1280;
    public static final int SCENE_HEIGHT = 720-NAVBAR_HEIGHT;
    private double xOffset = 0;
    private double yOffset = 0;

    // The history
    private History history;

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    public void setBusinessLogic(BlFacade afi) {
        businessLogic = afi;
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Constructor for the main GUI class.
     * Sets the business logic, loads all the windows and
     * displays the main GUI in the scene.
     * @param bl the business logic.
     */
    public MainGUI(BlFacade bl) {
        Platform.startup(() -> {
            try {
                setBusinessLogic(bl);
                history = new History();
                init(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Creates a new window and assigns its corresponding value
     * UI and Controller
     * @param fxmlfile the name of the fxml file
     * @return the new window
     * @throws IOException in case de load.loader() fails.
     */
    private Window load(String fxmlfile, String title, int width, int height) throws IOException {
        Window window = new Window(title, width, height);
        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxmlfile), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == NavBarController.class) {
                return new NavBarController(businessLogic);
            } else if (controllerClass == BrowseEventsController.class) {
                return new BrowseEventsController(businessLogic);
            } else if (controllerClass == CreateQuestionController.class) {
                return new CreateQuestionController(businessLogic);
            } else if (controllerClass == LoginController.class) {
                return new LoginController(businessLogic);
            } else if (controllerClass == RegisterController.class) {
                return new RegisterController(businessLogic);
            } else if (controllerClass == WelcomeController.class) {
                return new WelcomeController(businessLogic);
            } else if (controllerClass == UserMenuController.class) {
                return new UserMenuController(businessLogic);
            }else if (controllerClass == CreateForecastController.class) {
                return new CreateForecastController(businessLogic);
            } else if (controllerClass == CreateEventController.class) {
                return new CreateEventController(businessLogic);
            }else if (controllerClass == AdminMenuController.class) {
                return new AdminMenuController(businessLogic);
            } else if (controllerClass == UserOverviewController.class) {
                return new UserOverviewController(businessLogic);
            } else if (controllerClass == ProfileController.class) {
                return new ProfileController(businessLogic);
            } else if (controllerClass == BetsController.class) {
                return new BetsController(businessLogic);
            } else if (controllerClass == MovementsController.class) {
                return new MovementsController(businessLogic);
            }  else if (controllerClass == AdminOverviewController.class) {
                return new AdminOverviewController(businessLogic);
            } else if (controllerClass == EventsController.class) {
                return new EventsController(businessLogic);
            } else if (controllerClass == QuestionsController.class) {
                return new QuestionsController(businessLogic);
            } else if (controllerClass == ForecastsController.class) {
                return new ForecastsController(businessLogic);
            } else {
                // default behavior for controllerFactory:
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc); // fatal, just bail...
                }
            }
        });
        window.setUi(loader.load());
        ((Controller) loader.getController()).setMainApp(this);
        window.setController(loader.getController());
        return window;
    }

    /**
     * Initializes all the windows that do not require user authentication.
     * @param stage the stage of the project
     * @throws IOException thrown if any fxml file is not found
     */
    public void init(Stage stage) throws IOException {
        this.stage = stage;
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/favicon.png")));

        // TODO Not supported in JavaFX, will test it in 3rd iteration
        // set icon for mac os (and other systems which do support this method)
        // final Taskbar taskbar = Taskbar.getTaskbar();
        // BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/icon/favicon.png"));
        // taskbar.setIconImage(img);

        navBarLag = load("/NavBarGUI.fxml", "NavBar",  SCENE_WIDTH, SCENE_HEIGHT);
        welcomeLag = load("/WelcomeGUI.fxml", "Welcome", 350, 500);
        loginLag = load("/Login.fxml", "Login", 700, 500);
        registerLag = load("/RegisterGUI.fxml", "Register", 900, 600);
        browseEventsLag = load("/BrowseEvents.fxml", "BrowseEvents", SCENE_WIDTH, SCENE_HEIGHT);
        createEventLag = load("/CreateEvent.fxml", "CreateEvent", SCENE_WIDTH, SCENE_HEIGHT);

        //Update user money everytime a scene is shown.
        stage.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                    navBarLag.getController().redraw();
            }
        });

        setupScene();
        ResizeHelper.addResizeListener(this.stage);

        // FIXME (testing admin menu) Change to welcomeLag after testing and remove loadLoggedWindows()
        try {
            businessLogic.setCurrentUser(businessLogic.getUserByUsername("admin1"));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        loadLoggedWindows();
        history.setCurrentWindow(adminMenuLag);
        showScene(adminMenuLag);
    }

    /**
     * Initializes windows that require user authentication. This allows us to have
     * current user's information available already when the window is loaded.
     * @throws IOException thrown if any fxml file is not found
     */
    public void loadLoggedWindows() throws IOException {
        // Admin windows
        adminOverviewLag = load("/admin/AdminOverview.fxml", "AdminOverview", SCENE_WIDTH, SCENE_HEIGHT);
        eventsLag = load("/admin/Events.fxml", "Events", SCENE_WIDTH, SCENE_HEIGHT);
        questionsLag = load("/admin/Questions.fxml", "Questions", SCENE_WIDTH, SCENE_HEIGHT);
        forecastsLag = load("/admin/Forecasts.fxml", "Forecasts", SCENE_WIDTH, SCENE_HEIGHT);
        createQuestionLag = load("/CreateQuestion.fxml", "CreateQuestion", SCENE_WIDTH, SCENE_HEIGHT);
        createForecastLag = load("/CreateForecast.fxml", "CreateForecast", SCENE_WIDTH, SCENE_HEIGHT);

        // User windows
        adminMenuLag = load("/admin/AdminMenu.fxml", "AdminMenu", SCENE_WIDTH, SCENE_HEIGHT);
        userMenuLag = load("/user/UserMenuGUI.fxml", "UserMenu", SCENE_WIDTH, SCENE_HEIGHT);
        userOverviewLag = load("/user/UserOverview.fxml", "UserOverview", SCENE_WIDTH, SCENE_HEIGHT);
        profileLag = load("/user/Profile.fxml", "Profile", SCENE_WIDTH, SCENE_HEIGHT);
        betsLag = load("/user/Bets.fxml", "Bets", SCENE_WIDTH, SCENE_HEIGHT);
        movementsLag = load("/user/Movements.fxml", "Movements", SCENE_WIDTH, SCENE_HEIGHT);
    }

    /**
     * Prepares the window with the navigation bar so that it is possible
     * to navigate between windows without removing the navigation bar.
     */
    private void setupScene() {
        // Initialize the wrapper for the navbar and the content
        mainWrapper = new BorderPane();

        // Add the navbar to the wrapper
        mainWrapper.setTop(navBarLag.getUi());

        // Initialize the scene
        scene = new Scene(mainWrapper, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        // Import Roboto fonts
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap");
        // Global font css
        scene.getStylesheets().add(getClass().getResource("/css/fonts.css").toExternalForm());
        // Global color css
        scene.getStylesheets().add(getClass().getResource("/css/colors.css").toExternalForm());

        // Add the wrapper of the navbar and the content to the scene
        scene.setRoot(mainWrapper);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        // Dragging window with mouse:
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (history.getCurrentWindow().getController().getClass().getSimpleName().equals("WelcomeController")
                        || history.getCurrentWindow().getController().getClass().getSimpleName().equals("LoginController")
                        || history.getCurrentWindow().getController().getClass().getSimpleName().equals("RegisterController")) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        });

        // Add the scene to the root
        stage.setScene(scene);
    }

    /**
     * Displays the given window in the scene.
     * @param window the window.
     */
    private void showScene(Window window) {
        stage.setTitle(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString(window.getTitle()));

        // Do not show navbar in Welcome, Login, Register
        if (Arrays.asList("Welcome", "Login", "Register").contains(window.getTitle()))
            mainWrapper.setTop(null);
        else {
            mainWrapper.setTop(navBarLag.getUi());
            if (Arrays.asList("UserMenu", "AdminMenu").contains(window.getTitle())) {
                ((NavBarController) navBarLag.getController()).getUserBar().setVisible(false);
                ((NavBarController) navBarLag.getController()).getUserBar().setManaged(false);
            } else {
                ((NavBarController) navBarLag.getController()).getUserBar().setVisible(true);
                ((NavBarController) navBarLag.getController()).getUserBar().setManaged(true);
            }
        }

        mainWrapper.setCenter(window.getUi());

        stage.setWidth(window.getWidth());
        stage.setHeight(window.getHeight());
        stage.centerOnScreen();

        ((NavBarController)navBarLag.getController()).updateNav();
        window.getController().redraw();
        stage.show();
    }

    /**
     * Changes the stage to the last visited window.
     */
    public void goBack() {
        Window previousWindow = history.moveToPrevious();
        ((NavBarController) navBarLag.getController()).enableHistoryBtns();
        if (previousWindow != null) showScene(previousWindow);
    }

    /**
     * Stores the current window in the history and displays
     * the next window
     */
    public void goForward() {
        Window nextWindow = history.moveToNext();
        ((NavBarController) navBarLag.getController()).enableHistoryBtns();
        if (nextWindow != null) showScene(nextWindow);
    }
    /**
     * Stores the current window in the history and displays
     * the window with the given title.
     * @param title the title of the window.
     */
    public void goForward(String title) {
        // Get the new window
        Window newWindow = getWindow(title);
        // Move to the requested window and store the old one
        history.moveToWindow(newWindow);

        ((NavBarController) navBarLag.getController()).enableHistoryBtns();
        showScene(newWindow);
    }

    /**
     * Returns the window with the given title
     * @param title the title of the window.
     */
    public Window getWindow(String title) {
        return switch(title) {
            case "Login":
                yield loginLag;
            case "Register":
                yield registerLag;
            case "BrowseEvents":
                yield browseEventsLag;
            case "CreateQuestion":
                yield createQuestionLag;
            case "Welcome":
                yield welcomeLag;
            case "UserMenu":
                yield userMenuLag;
            case "CreateForecast":
                yield createForecastLag;
            case "CreateEvent":
                yield createEventLag;
            case "AdminMenu":
                yield adminMenuLag;
            default: // get the welcome window
                yield welcomeLag;
        };
    }

    /**
     * Returns the history.
     * @return the history.
     */
    public History getHistory() {
        return history;
    }

    /**
     * Returns the stage of the javaFX UI.
     * @return The stage of the javaFX UI.
     */
    public Stage getStage()
    {
        return this.stage;
    }
}
