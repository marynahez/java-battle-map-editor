package rw.util;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import rw.battle.*;

import java.io.File;
import java.io.PrintWriter;


public class Writer {
    /**
     * Save all information about Battle including map in a specified file that user chooses
     * @param file in which the user want to save all map information
     * @param battle the battle object including all map's details
     * @return file that was saved
     * @author - M.H., T11, 2025.04.01
     */
    public static File saveFile(File file, Battle battle) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
            //Getting rows and columns from battle
            int rows = battle.getRows();
            int columns = battle.getColumns();
            writer.println(rows); //1st line
            writer.println(columns); //2nd line

            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    Entity entity = battle.getEntity(row, column);
                    if (entity == null) {
                        writer.println(row + "," + column);
                    }
                    else {
                        if (entity instanceof Wall) {
                            writer.println(row + "," + column + ",WALL");
                        }
                        else if (entity instanceof PredaCon) {
                            PredaCon pcon = (PredaCon) entity;

                            String predLine = row + "," + column + ",PREDACON" + "," + pcon.getSymbol() + "," + pcon.getName() + "," + pcon.getHealth() + "," + pcon.getWeaponType();
                            predLine = predLine.replace("CLAWS", "C").replace("LASER", "L").replace("TEETH", "T");
                            writer.println(predLine);
                        }
                        else if (entity instanceof Maximal) {
                            Maximal m = (Maximal) entity;
                            writer.println(row + "," + column + ",MAXIMAL" + "," + m.getSymbol() + "," + m.getName() + "," + m.getHealth() + "," + m.weaponStrength() + "," + m.armorStrength());
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error while saving file");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while saving file");
            alert.showAndWait();
        }
        finally {
            writer.close();
        }
        return file;
    }
}

