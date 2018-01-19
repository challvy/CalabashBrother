package nju.java.object.creature.soldier;

import nju.java.object.creature.Role;
import nju.java.object.creature.Creature;
import nju.java.storyboard.Battle;

import java.util.Random;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Scorpion类表示蝎子精<br>
 * @see nju.java.object.creature.soldier.Soldier
 */
public class Scorpion extends Soldier {

    public Scorpion (Battle battle){
        super("Scorpion.png",900, 510,250,250, Role.Scorpion, battle,0);
        rightRole = false;
        HP = 10;
        rand = new Random();
    }

    public boolean preMove(){
        int flag=0;
        int aimx=0, aimy=0;
        int minD=9999;
        int i=0;
        for(Creature roleCrt: battle.roles) {
            if (roleCrt.isLive() && roleCrt.isRightRole()){
                flag=1;
                int d = Math.abs(roleCrt.getXInField()-getXInField()) + Math.abs(roleCrt.getYInField()-getYInField());
                if(d<minD){
                    minD = d;
                    aimx = roleCrt.getXInField();
                    aimy = roleCrt.getYInField();
                }
            }
        }
        if(flag==0)
            return false;
        if(aimx >= getXInField()){
            incx = 1;
        } else {
            incx = -1;
        }
        if(aimy >= getYInField()){
            incy = 1;
        } else {
            incy = -1;
        }
        return true;
    }

    public synchronized void move() {
        update();
        for(Creature roleCrt: battle.roles) {
            if(roleCrt.isLive() && willHit(getXInField(),getYInField(), roleCrt.getXInField(),roleCrt.getYInField())) {

                if (roleCrt.isRightRole()) {
                    if (roleCrt.role == Role.Yeye) {
                        roleCrt.setDie();
                    } else if (roleCrt.role == Role.Qiwa) {
                        HP = 0;
                        setX(x() + 1600);
                        setDie();
                        return;
                    } else if (roleCrt.isAttacking()) {
                        HP--;
                        if (isAttacking()) {
                            roleCrt.HP-=2;
                        }
                        if(HP<=0){
                            setX(x() + 1600);
                            setY(rand.nextInt(350)+450);
                            HP=10;
                            return;
                        }
                        return;
                    }
                } else if (roleCrt != this) {
                    incx *= 4;
                    incy *= 3;
                    ;
                    if (roleCrt.getXInField() > getXInField()) {
                        roleCrt.setX(roleCrt.x() + incx);
                    } else if (roleCrt.getXInField() < getXInField()) {
                        roleCrt.setX(roleCrt.x() - incx);
                    }
                    if (roleCrt.getYInField() > getYInField()) {
                        roleCrt.setY(roleCrt.y() + incy);
                    } else if (roleCrt.getYInField() < getYInField()) {
                        roleCrt.setY(roleCrt.y() - incy);
                    }
                }
            }

        }
        int addx = x()+incx;
        int addy = y()+incy;

        if(addy<380)
            addy = 380;
        else if(addy>820)
            addy=820;
        if(addx<0)
            addx = 0;
        else if(addx>1600)
            addx=1600;
        setX(addx);
        setY(addy);
        update();
    }

    public synchronized void attack(){
        if(attack ==false && System.currentTimeMillis()-stopAttack>1000) {
            attack =true;
            startAttack = System.currentTimeMillis();
        } else if (attack ==true && System.currentTimeMillis()-startAttack>2000) {
            attack =false;
            stopAttack = System.currentTimeMillis();;
        }
    }

    public synchronized void run() {
        while (!Thread.interrupted() && runningFlag ==true && isLive()) {
            try {
                if(preMove()) {
                    move();
                    attack();
                }
                Thread.sleep(rand.nextInt(20));
                this.battle.repaint();
            } catch (Exception e) {
            }
        }
    }
}
