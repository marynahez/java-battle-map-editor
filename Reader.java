package rw.util;

import rw.battle.Battle;
import rw.battle.Maximal;
import rw.battle.PredaCon;
import rw.battle.Wall;
import rw.enums.WeaponType;

import java.io.FileNotFoundException;
import java.util.Scanner;


import java.io.File;

/**
 * Class to assist reading in battle file
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public final class Reader {


    /**
     * Load the file, read the file and save all data in battle object
      * @param file that we load and read
     * @return battle object with all saved data about map
     * @author - M.H., T11, 2025.03.31
     */

    public static Battle loadBattle(File file) {
        try {
            Scanner scanner = new Scanner (file);

            int rows = scanner.nextInt();
            int columns = scanner.nextInt();
            scanner.nextLine();


            Battle battle = new Battle(rows, columns);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String [] parts = line.trim().split(",");

                if (parts.length == 2) {
                    continue;
                }
                else if (parts.length == 3) {
                    if (parts[2].equals("WALL")) {
                        int wallRow = Integer.parseInt(parts[0]);
                        int wallColumn = Integer.parseInt(parts[1]);
                        battle.addEntity(wallRow, wallColumn, Wall.getWall());
                    }
                }
                // PREDACON
                else if (parts.length == 7) {

                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    Character symbol = parts[3].charAt(0);
                    String name = parts[4];
                    int health = Integer.parseInt(parts[5]);
                    char weaponType = parts[6].charAt(0);

                    battle.addEntity(r,c, new PredaCon(symbol, name, health, WeaponType.getWeaponType(weaponType)));
                }

                //MAXIMAL

                else if (parts.length == 8) {
                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    char symbol = parts[3].charAt(0);
                    String name = parts[4];
                    int health = Integer.parseInt(parts[5]);
                    int attackStrength = Integer.parseInt(parts[6]);
                    int armourStrength = Integer.parseInt(parts[7]);

                    battle.addEntity(r,c, new Maximal(symbol, name, health, attackStrength, armourStrength));
                }
                else {
                    continue;
                }
                }
            return battle;
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getName());
        }
        catch (Exception e) {
            System.out.println("Error reading file: " + file.getName());
        }
        return null;
    }
}
