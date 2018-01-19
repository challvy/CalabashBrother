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
 * HuluwaÀà±íÊ¾ºùÂ«ÍÞ<br>
 * @see nju.java.object.creature.soldier.Soldier
 */
public class Huluwa extends Soldier{

    protected int hulux;
    protected int huluy;
    private Battle battle;

    public Huluwa(String url, int x, int y, Role role, int hulux, int huluy, Battle battle, int id){
        super(url,x, y, 110,110,role, battle, id);
        rightRole = true;
        rand = new Random();
        this.hulux = hulux;
        this.huluy = huluy;
        HP = 2;
        this.battle = battle;
    }

    private boolean preMove(){
        int flag=0;
        int aimx=0, aimy=0;
        int minD=9999;
        int i=0;
        for(Creature roleCrt: battle.roles) {
            if (!roleCrt.isRightRole()){
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
            incy = rand.nextInt(2);
        } else {
            incy = -1;
        }
        return true;
    }

    private synchronized void move() throws InterruptedException {
        update();

        for(Creature roleCrt: battle.roles) {
            if(willHit(getXInField(), getYInField(), roleCrt.getXInField(),roleCrt.getYInField())) {

                if (!roleCrt.isRightRole()) {
                    if (isAttacking()) {
                        if (roleCrt.role == Role.Scorpion || roleCrt.role == Role.Snake) {
                            roleCrt.HP--;
                        } else if (battle.flagMainDemonDie) {
                            roleCrt.setX(roleCrt.x() + 1600);
                            roleCrt.setY(450 + rand.nextInt(350));
                            roleCrt.HP = 0;
                            roleCrt.setDie();
                        }

                    } else {
                        if (role == Role.Qiwa && (roleCrt.role == Role.Scorpion || roleCrt.role == Role.Snake)) {
                            roleCrt.HP = 0;
                            roleCrt.setDie();
                        } else {
                            HP--;
                            if(HP>0){
                                setX(-120);
                                setY(rand.nextInt(350)+450);
                            } else {
                                setX(hulux);
                                setY(huluy);
                                setDie();
                                Thread.sleep(7000);
                                HP=2;
                                setLive();
                            }
                        }
                    }
                    return;

                } else if (roleCrt != this) {
                    incx *= 4;;
                    incy *= 3;;
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
        if(addy<450) {
            addy += 3;
            setY(addy);
            incx = incy = 0;
            update();
            return;
        }
        else if(addy>800)
            addy=800;
        if(addx<-150)
            addx = -150;
        else if(addx>1510)
            addx=1510;
        setX(addx);
        setY(addy);
        update();
    }

    private synchronized void attack(){
        if(role== Role.Qiwa) return;
        if(!isAttacking() && System.currentTimeMillis()-stopAttack>3000) {
            attack = true;
            startAttack = System.currentTimeMillis();
        } else if (attack && System.currentTimeMillis()-startAttack>2000) {
            attack = false;
            stopAttack = System.currentTimeMillis();
        }
    }

    public synchronized void run() {
        while (!Thread.interrupted() && runningFlag) {
            try {
                if(preMove()){
                    move();
                    attack();
                }
                Thread.sleep(rand.nextInt(20));
                this.battle.repaint();
            } catch (Exception e) {
                ;
            }
        }
    }
}
