package nju.java.object.creature.civilian;

import nju.java.object.creature.Role;
import nju.java.storyboard.Battle;

import java.util.Random;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Yeye¿‡±Ì æ“Ø“Ø<br>
 * @see nju.java.object.creature.civilian.Civilian
 */
public class Yeye extends Civilian {

    public Yeye(Battle battle){
        super("Yeye.png", 50,550,180,180, Role.Yeye, battle);
        rightRole = true;
        rand = new Random();
    }
}
