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
 * DemonTroop类表示小喽啰<br>
 * @see nju.java.object.creature.soldier.Soldier
 */
public class DemonTroop extends Soldier {

    public DemonTroop(String url, int x, int y, Battle battle, int id){
        super(url, x, y, 160, 160, Role.DemonTroop, battle, id);
        rightRole = false;
        rand = new Random();
    }

    /**
     * @return 小喽啰的步进策略：若爷爷在200步之内则冲向爷爷，否则往前且随机上下移动
     */
    private boolean preMove(){
        int flag=0;
        int aimx=0, aimy=0;
        int i=0;
        for(Creature roleCrt: battle.roles) {
            if (roleCrt.isLive() && roleCrt.rightRole){
                flag=1;
                if(roleCrt.role== Role.Yeye &&
                        ( (Math.abs(roleCrt.getXInField()-getXInField())+Math.abs(roleCrt.getYInField()-getYInField())<200) ||  getXInField()< roleCrt.getXInField() ) ){
                    aimx = roleCrt.getXInField();
                    aimy = roleCrt.getYInField();
                    if(aimx >= getXInField()){
                        incx = rand.nextInt(2);
                    } else {
                        incx = rand.nextInt(2)-1;
                    }
                    if(aimy >= getYInField()){
                        incy = rand.nextInt(2);
                    } else {
                        incy = rand.nextInt(2)-1;
                    }
                    return true;
                }
            }
        }
        if(flag==0)
            return false;

        incx = rand.nextInt(2)-1;
        incy = rand.nextInt(3)-1;
        return true;
    }

    private synchronized void move() {
        update();
        for(Creature roleCrt: battle.roles) {
            if(roleCrt.isLive() && willHit(getXInField(),getYInField(), roleCrt.getXInField(),roleCrt.getYInField()) ) {

                if (roleCrt.isRightRole()) {
                    if (roleCrt.role == Role.Yeye) {
                        roleCrt.setDie();
                    } else {
                        roleCrt.HP--;
                        setX(x() + 1600);
                        if (battle.flagMainDemonDie) {
                            setDie();
                            return;
                        }
                    }

                } else if (roleCrt != this) {
                    incx *= 4;
                    incy *= 3;
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

        if(addy<450)
            addy = 450;
        else if(addy>760)
            addy=760;
        if(addx<-100)
            addx = -100;
        else if(addx>1600)
            addx=1600;
        setX(addx);
        setY(addy);
        update();
    }

    public synchronized void run() {
        while (!Thread.interrupted() && runningFlag) {
            try {
                if(preMove()) {
                    move();;
                }
                Thread.sleep(rand.nextInt(16));
                this.battle.repaint();
            } catch (Exception e) {
                ;
            }
        }
    }

}
