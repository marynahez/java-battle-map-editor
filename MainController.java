package rw.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import rw.battle.*;
import rw.enums.WeaponType;
import rw.util.Reader;
import rw.util.Writer;
import javafx.scene.paint.Color;
import java.io.File;

public class MainController {


    //Store the data of editor
    private Battle battle;
    private Button[][] buttons;

    @FXML private TextField fxRows;
    @FXML private TextField fxColumns;

    @FXML private GridPane fxGridPaneWorld;

    @FXML private TextField fxSymbolPredacon;
    @FXML private TextField fxNamePredacon;
    @FXML private TextField fxHealthPredacon;
    @FXML private ComboBox <String> fxWeaponPredacon;
    @FXML private Label fxLeftStatusLabel;
    @FXML private Label fxRightStatusLabel;

    //Maximal variables
    @FXML private TextField fxSymbolMaximal;
    @FXML private TextField fxNameMaximal;
    @FXML private TextField fxHealthMaximal;
    @FXML private TextField fxWeaponMaximal;
    @FXML private TextField fxArmorMaximal;

    @FXML private RadioButton fxRadioButton;
    @FXML private RadioButton fxRadioButton1;
    @FXML private ToggleButton fxDeletePredacon;
    @FXML private ToggleButton fxDeleteMaximal;
    @FXML private ToggleButton fxAddWall;
    @FXML private ToggleButton fxDeleteWall;
    @FXML private TextArea fxTextFieldDetails;

    int rows; //Valid rows
    int cols; //Valid columns
    private java.io.File File;

    /**
     * Set up default values in the GUI
     *  @author - M.H., T11, 2025.04.01
     */
    @FXML
    public void initialize() {
        fxWeaponPredacon.getItems().addAll("CLAWS (1)", "LASER (2)", "TEETH (3)");
        fxWeaponPredacon.setValue("CLAWS (1)");
    }

    /**
     * Open fileChooser and load the needed file with all data about map
     * Be able to see the loaded world in 2D grid
     * @author - M.H., T11, 2025.04.09
     */
    @FXML
    public void handleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            battle = Reader.loadBattle(file);
            rows = battle.getRows();
            cols = battle.getColumns();
            fxRows.setText(String.valueOf(rows));
            fxColumns.setText(String.valueOf(cols));
            fxGridPaneWorld.getChildren().clear();

            int totalRows = rows + 2;
            int totalCols = cols + 2;
            buttons = new Button[totalRows][totalCols];

            for (int i = 0; i < totalRows; i++) {
                for (int j = 0; j < totalCols; j++) {
                    Button button = new Button();
                    fxGridPaneWorld.getColumnConstraints().remove(0, fxGridPaneWorld.getColumnConstraints().size());
                    fxGridPaneWorld.getRowConstraints().remove(0, fxGridPaneWorld.getRowConstraints().size());
                    button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setHgrow(button, Priority.ALWAYS);
                    GridPane.setVgrow(button, Priority.ALWAYS);

                    if (i == 0 || j == 0 || i == totalRows - 1 || j == totalCols - 1) {
                        button.setText("#");
                    } else {
                        button.setText(".");
                    }

                    fxGridPaneWorld.add(button, j, i);
                    buttons[i][j] = button;
                }
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Entity entity = battle.getEntity(i, j);
                    if (entity != null) {
                        buttons[i + 1][j + 1].setText(String.valueOf(entity.getSymbol()));
                    }
                }
            }

            fxLeftStatusLabel.setText("Map loaded: " + file.getName());
            fxLeftStatusLabel.setTextFill(Color.GRAY);
        }
    }

    /**
     * Save all data about map in chosen file though "Save as..." though FileChooser
     * @author - M.H., T11, 2025.03.31
     */
    @FXML
    public void handleSaveAs() {
        FileChooser fileChooser = new FileChooser(); //create FileChooser
        fileChooser.setTitle("Save Map File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Map File", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(null);


        if (selectedFile != null) {
            Writer.saveFile(selectedFile, battle);
            fxLeftStatusLabel.setText("Saved Map to " + selectedFile.getName());
            fxLeftStatusLabel.setTextFill(Color.GRAY);
        }

    }

    /**
     * Exit the programme if choose "Quit"
     * @author - M.H., T11, 2025.04.02
     */
    @FXML
    public void handleQuit() {
        System.out.println("Quit");
        System.exit(0);
    }

    /**
     * The function to show pop up information when user chooses "About"
     * @author - M.H., T11, 2025.04.02
     */
    @FXML
    public void handleHelpAbout() {
        System.out.println("About RW MAP Editor");
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("About");
        alert.setHeaderText("Message");
        alert.setContentText("""
                Author: Maryna Hez
                Email: maryna.hez@ucalgary.ca
                Version: v1.0
                This is a Battle editor for PredaCons versus Maximals
                
                """);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Validates row and column input
     * Gives suggestions to user what inputs are valid and not
     * Creates the battle world
     * Updates status labels accordingly
     * @author - M.H., T11, 2025.04.03
     */

    @FXML
    public void handleCreateWorld() {
        try {
            rows = Integer.parseInt(fxRows.getText());
            cols = Integer.parseInt(fxColumns.getText());

            if (rows == 0 || cols == 0 || fxRows.getText() == null || fxColumns.getText() == null) {
                battle = null;
                fxGridPaneWorld.getChildren().clear();
                fxLeftStatusLabel.setText("");
                fxRightStatusLabel.setText("");

            }

            if (rows <= 0) {
                fxLeftStatusLabel.setText("The number of rows must be greater than 0");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return;
            }
            if (cols <= 0) {
                fxLeftStatusLabel.setText("The number of columns must be greater than 0");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return;
            }

            battle = new Battle(rows, cols);
            fxLeftStatusLabel.setText("World created.");
            fxLeftStatusLabel.setTextFill(Color.GRAY);
            fxRightStatusLabel.setText("Battle drawn!");
            fxRightStatusLabel.setTextFill(Color.GRAY);
            handleCreateGridPane();

        }
        catch (NumberFormatException e) {
            fxLeftStatusLabel.setText("Please enter a digital number for rows and columns.");
            fxLeftStatusLabel.setTextFill(Color.RED);
            return;
        }
    }

    /**
     * Created object Predacon adding all data for this player
     * Gives suggestions to user what inputs are valid and not
     * Updates status labels accordingly
     * @return Predacon object
     * @author M.H. T11 2025.04.08
     */


    @FXML
    public PredaCon addPredacon() {
        String predaconSymbol = fxSymbolPredacon.getText();
        String predaconName = fxNamePredacon.getText();
        String predaconHealth = fxHealthPredacon.getText();
        String predaconWeapon = fxWeaponPredacon.getValue();
        int validHealthPred;

        //FIRST PART. ALL CHECKS

        if (this.battle == null) {
            fxLeftStatusLabel.setText("Please create a battle first.");
            fxLeftStatusLabel.setTextFill(Color.RED);
            return null;
        }


        if (predaconSymbol.isEmpty() || predaconName.isEmpty() || predaconHealth.isEmpty()) {
            fxLeftStatusLabel.setText("Please fill in all Predacon fields.");
            fxLeftStatusLabel.setTextFill(Color.RED);
            return null; //No need to have other checks if fields are empty
        }

        //Check of valid Symbol
        if (predaconSymbol.length() != 1 || !Character.isLetter(predaconSymbol.charAt(0))) {
            fxLeftStatusLabel.setText("Predacon symbol needs to be 1 character.");
            fxLeftStatusLabel.setTextFill(Color.RED);
            return null;
        }
        //Check of valid Name
        if (!predaconName.matches("[a-zA-Z ]+")) {
            fxLeftStatusLabel.setText("Predacon name needs to be alphanumeric.");
            fxLeftStatusLabel.setTextFill(Color.RED);
            return null;
        }
        //Check of valid health
        try {
            validHealthPred = Integer.parseInt(predaconHealth);
            if (validHealthPred < 0) {
                fxLeftStatusLabel.setText("Predacon health needs to be greater than 0 or if 0 the Maximal is dead.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            } else {
                fxLeftStatusLabel.setText("");
            }
        } catch (NumberFormatException e) {
            fxLeftStatusLabel.setText("Please enter a valid number for the predacon health.");
            fxLeftStatusLabel.setTextFill(Color.RED);
            return null;
        }

        WeaponType weaponType = null;
        if (predaconWeapon.contains("CLAWS")) {
            weaponType = WeaponType.CLAWS; //Refer to enum
        } else if (predaconWeapon.contains("LASER")) {
            weaponType = WeaponType.LASER;
        } else if (predaconWeapon.contains("TEETH")) {
            weaponType = WeaponType.TEETH;
        }

        //Do not allow user to enter strength such as 01, 02, 03... which does not make any sense
        if (predaconHealth.startsWith("0") && predaconHealth.length() > 1) {
            fxLeftStatusLabel.setText("Do not start Predacon Health with 0.");
            fxLeftStatusLabel.setTextFill(Color.RED);
            return null;
            }

            //SECOND PART. CREATING PREDACON

            PredaCon predaCon = new PredaCon(predaconSymbol.charAt(0), predaconName, validHealthPred, weaponType);
            fxLeftStatusLabel.setText("Added a new PredaCon!");
            fxLeftStatusLabel.setTextFill(Color.GRAY);
            fxTextFieldDetails.setText(
                    "Type: PredaCon" + "\n" +
                    "Symbol: " + predaCon.getSymbol() + "\n" +
                    "Name: " + predaCon.getName() + "\n" +
                    "Health: " + predaCon.getHealth() + "\n" +
                    "Weapon: " + predaCon.getWeaponType().toString() + "\n"
                    );
        return predaCon;
    }

    /**
     * Created object Maximal adding all data for this player
     * Gives suggestions to user what inputs are valid and not
     * Updates status labels accordingly
     * @return Maximal object
     * @author M.H. T11 2025.04.08
     */

        @FXML
        public Maximal addMaximal () {
            String maximalSymbol = fxSymbolMaximal.getText();
            String maximalName = fxNameMaximal.getText();
            String maximalHealth = fxHealthMaximal.getText();
            String maximalWeapon = fxWeaponMaximal.getText();
            String maximalArmor = fxArmorMaximal.getText();

            int validHealthMaximal;
            int validMaximalWeapon;
            int validMaximalArmor;


            //FIRST PART. ALL CHECKS

            if (this.battle == null) {
                fxLeftStatusLabel.setText("Please create a battle first.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }


            if (maximalSymbol.isEmpty() || maximalName.isEmpty() || maximalHealth.isEmpty() ||
                    maximalWeapon.isEmpty() || maximalArmor.isEmpty()) {
                fxLeftStatusLabel.setText("Please fill in all Maximal fields.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null; //No need to have other checks if fields are empty
            }

            //Check of valid Symbol
            if (maximalSymbol.length() != 1 || !Character.isLetter(maximalSymbol.charAt(0))) {
                fxLeftStatusLabel.setText("Maximal symbol needs to be 1 character.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }
            //Check of valid Name
            if (!maximalName.matches("[a-zA-Z ]+")) {
                fxLeftStatusLabel.setText("Maximal name needs to be alphanumeric.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }
            //Check of valid health
            try {
                validHealthMaximal = Integer.parseInt(maximalHealth);
                if (validHealthMaximal < 0) {
                    fxLeftStatusLabel.setText("Maximal health needs to be greater than 0 or if 0 the Maximal is dead.");
                    fxLeftStatusLabel.setTextFill(Color.RED);
                    return null;
                } else {
                    fxLeftStatusLabel.setText("");
                }
            } catch (NumberFormatException e) {
                fxLeftStatusLabel.setText("Please enter a valid number for the maximal health.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }

            //Maximal Weapon check (valid number or not)
            try {
                validMaximalWeapon = Integer.parseInt(maximalWeapon);
                if (validMaximalWeapon <= 0) {
                    fxLeftStatusLabel.setText("Maximal weapon strength needs to be greater than 0.");
                    fxLeftStatusLabel.setTextFill(Color.RED);
                    return null;
                }
            } catch (NumberFormatException e) {
                fxLeftStatusLabel.setText("Please enter a positive number for Maximal weapon strength.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }

            //Maximal Armor strength check (valid number or not)
            try {
                validMaximalArmor = Integer.parseInt(maximalArmor);
                if (validMaximalArmor <= 0) {
                    fxLeftStatusLabel.setText("Maximal armor strength needs to be greater than 0.");
                    fxLeftStatusLabel.setTextFill(Color.RED);
                    return null;
                }
            } catch (NumberFormatException e) {
                fxLeftStatusLabel.setText("Please enter a positive number for Maximal armor strength.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }

            //Do not allow user to enter strength such as 01, 02, 03... which does not make any sense
            if (maximalWeapon.startsWith("0") && maximalWeapon.length() > 1) {
                fxLeftStatusLabel.setText("Do not start Maximal Weapon with 0.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }
            //Do not allow user to enter strength such as 01, 02, 03... which does not make any sense
            if (maximalArmor.startsWith("0") && maximalArmor.length() > 1) {
                fxLeftStatusLabel.setText("Do not start Maximal Armor with 0.");
                fxLeftStatusLabel.setTextFill(Color.RED);
                return null;
            }

            //SECOND PART. CREATING MAXIMAL
            Maximal maximal = new Maximal(maximalSymbol.charAt(0), maximalName, validHealthMaximal, validMaximalWeapon, validMaximalArmor);
            fxLeftStatusLabel.setText("Added a new Maximal!");
            fxLeftStatusLabel.setTextFill(Color.GRAY);
            fxTextFieldDetails.setText(
                    "Type: Maximal" + "\n" +
                            "Symbol: " + maximal.getSymbol() + "\n" +
                            "Name: " + maximal.getName() + "\n" +
                            "Health: " + maximal.getHealth() + "\n" +
                            "Weapon: " + maximal.weaponStrength() + "\n" +
                            "Weapon Strength: " + maximal.armorStrength()
            );
            return maximal;
        }

    /**
     * Create view of the world with adding all walls and Robots
     * Create details about Maximals or Predacons in the text field "Details"
     * Updates status labels accordingly
     * @author M.H. 2025.04.08
     */


    @FXML
        public void handleCreateGridPane(){
            fxGridPaneWorld.getChildren().clear();
            // 2 extra cells for making later walls around the world
            int totalRows = rows + 2;
            int totalColumns = cols + 2;

            for (int i = 0; i < totalRows; i++) {
                for (int j = 0; j < totalColumns; j++) {
                    Button button = new Button();
                    fxGridPaneWorld.getColumnConstraints().remove(0, fxGridPaneWorld.getColumnConstraints().size());
                    fxGridPaneWorld.getRowConstraints().remove(0, fxGridPaneWorld.getRowConstraints().size());
                    button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setHgrow(button, Priority.ALWAYS);
                    GridPane.setVgrow(button, Priority.ALWAYS);

                    button.setStyle("""
                           
                            -fx-border-color: grey;
                           -fx-background-color: white;
                           """);
                    fxGridPaneWorld.add(button, j,
                    i);
                        if (i == 0 || j == 0 || i == totalRows - 1 || j == totalColumns - 1) {
                            button.setText("#") ;
                        }
                    else {
                            button.setText(".");
                        }

                    int realRows = i - 1;
                    int realCols = j - 1;

                    button.setOnAction(event -> {
                        if (battle == null) {
                            return;
                        }
                        if (realRows < 0 || realCols < 0 || realRows >= rows || realCols >= cols) {
                            return;
                        }

                        //Add Predacon to the view
                        if (fxRadioButton.isSelected()) {
                            Entity entity = battle.getEntity(realRows, realCols);
                            if (entity != null && !(entity instanceof Wall)) {
                                fxLeftStatusLabel.setText("The cell is occupied");
                                fxLeftStatusLabel.setTextFill(Color.RED);
                                return;
                            }

                            PredaCon predaCon = addPredacon();
                            if (predaCon != null) {
                                if (!button.getText().equals("#")) {
                                battle.addEntity(realRows, realCols, predaCon);
                                button.setText(String.valueOf(predaCon.getSymbol())); //Add Predacon symbol where he is now
                            }
                            }
                            return;
                        }
                        //Add Maximal to the view

                        if (fxRadioButton1.isSelected()) {
                            Entity entity = battle.getEntity(realRows, realCols);
                            if (entity != null && !(entity instanceof Wall)) {
                                fxLeftStatusLabel.setText("The cell is occupied");
                                fxLeftStatusLabel.setTextFill(Color.RED);
                                return;
                            }

                            Maximal maximal = addMaximal();
                            if (maximal != null) {
                                if (!button.getText().equals("#")) {
                                    battle.addEntity(realRows, realCols, maximal);
                                    button.setText(String.valueOf(maximal.getSymbol())); //Add Maximal symbol where he is now
                                }
                            }
                            return;
                        }
                        //Delete Predacon from the view
                        if (fxDeletePredacon.isSelected()) {
                            Entity entity = battle.getEntity(realRows, realCols);
                            if (entity instanceof PredaCon) {
                                battle.removeRobot(entity);
                                button.setText(".");
                                fxTextFieldDetails.setText("");
                                fxLeftStatusLabel.setText("PredaCon is deleted!");
                            }
                            return;
                        }

                        //Delete Maximal from the view
                        if (fxDeleteMaximal.isSelected()) {
                            Entity entity = battle.getEntity(realRows, realCols);
                            if (entity instanceof Maximal) {
                                battle.removeRobot(entity);
                                button.setText(".");
                                fxTextFieldDetails.setText("");
                                fxLeftStatusLabel.setText("Maximal is deleted!");
                            }
                            return;
                        }

                        //Add or remove Wall from the view
                        if (fxAddWall.isSelected()) {
                            Entity entity = battle.getEntity(realRows, realCols);
                            if (entity instanceof Maximal || entity instanceof PredaCon || entity instanceof Wall) {
                                fxLeftStatusLabel.setText("The cell is occupied");
                                fxLeftStatusLabel.setTextFill(Color.RED);
                                return;
                            } else {
                               battle.addEntity(realRows, realCols, Wall.getWall());
                               fxLeftStatusLabel.setText("The wall is added!");
                               fxLeftStatusLabel.setTextFill(Color.GRAY);
                               button.setText(String.valueOf(Wall.getWall().getSymbol()));
                                return;
                            }
                        }
                        if (fxDeleteWall.isSelected()) {
                                if (battle.getEntity(realRows, realCols) instanceof Wall) {
                                    battle.addEntity(realRows, realCols, null);
                                    fxLeftStatusLabel.setText("The wall is removed!");
                                    fxLeftStatusLabel.setTextFill(Color.GRAY);
                                    button.setText(".");
                                } else {
                                    fxLeftStatusLabel.setText("There is no wall to remove here.");
                                    fxLeftStatusLabel.setTextFill(Color.RED);
                                }
                                return;
                            }

                    });
                }
            }
        }
}