package nju.java.object.creature.soldier;


import nju.java.object.creature.Creature;
import nju.java.object.creature.Role;
import nju.java.storyboard.Battle;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Soldier抽象类表示能攻击的生物体<br>
 * @see nju.java.object.creature.Creature
 */
public abstract class Soldier extends Creature {

    protected long startAttack;
    protected long stopAttack;

    protected Soldier(String url, int x, int y, int width, int height, Role roles, Battle battle, int id){
        super(url, x, y, width, height, roles, battle, id);
        attack = false;
        startAttack =  System.currentTimeMillis();;
        stopAttack = System.currentTimeMillis();;
    }
}
