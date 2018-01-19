package nju.java.object.creature.civilian;

import nju.java.object.creature.Creature;
import nju.java.object.creature.Role;
import nju.java.storyboard.Battle;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Civilian抽象类表示平民，没有攻击性<br>
 * @see nju.java.object.creature.soldier.Soldier
 */
public abstract class Civilian extends Creature {

    protected Civilian(String url, int x, int y, int width, int height, Role role, Battle battle){
        super(url, x, y, width, height, role, battle,0);
    }

    public boolean preMove(){
        int flag=0;
        int aimx=0, aimy=0;
        int minD=9999;
        int i=0;
        for(; i< battle.roles.size(); i++) {
            if (battle.roles.get(i).isRightRole()){
                flag=1;;
                int d = Math.abs(battle.roles.get(i).getXInField()-getXInField()) + Math.abs(battle.roles.get(i).getYInField()-getYInField());
                if(d<minD){
                    minD = d;
                    aimx = battle.roles.get(i).getXInField();
                    aimy = battle.roles.get(i).getYInField();
                }
            }
        }
        if(flag==0)
            return false;

        incx = rand.nextInt(3)-1;
        incy = rand.nextInt(3)-1;
        return true;
    }
    public synchronized void move() {
        update();
        for(Creature roleCrt: battle.roles) {
            if(willHit(getXInField(), getYInField(), roleCrt.getXInField(),roleCrt.getYInField())){

                if(!roleCrt.isRightRole()){
                    setDie();
                    return;
                } else if(roleCrt != this) {
                    incx *= 4;
                    incy *= 3;;
                    if (roleCrt.getXInField() > getXInField()) {
                        roleCrt.setX(roleCrt.x() + incx);
                    } else if (roleCrt.getXInField() < getXInField()) {
                        roleCrt.setX(roleCrt.x() - incx);
                    }
                    if (roleCrt.getYInField() > getYInField()) {
                        roleCrt.setY(roleCrt.y() + incy);;
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
        else if(addy>820)
            addy=820;
        if(addx<0)
            addx = 0;
        else if(addx>1510)
            addx=1510;
        setX(addx);
        setY(addy);
        update();
    }
    public synchronized void run() {
        while (!Thread.interrupted() && isLive()  && runningFlag) {
            try {
                if(preMove()){
                    move();
                }
                Thread.sleep(rand.nextInt(40));
                this.battle.repaint();
            } catch (Exception e) {
                ;;;;
            }
        }
    }
}
