package nju.java.object.creature;

import nju.java.object.Object;
import nju.java.storyboard.Battle;

import java.util.Random;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Creature抽象类表示各色角色
 * <br>
 * @see nju.java.object.Object
 */
public abstract class Creature extends Object implements Comparable, Runnable{

    // 角色编号与正反派
    public Role role;
    private int ID;
    public boolean rightRole;

    // 物体中心点
    protected int xInField;
    protected int yInField;

    // 根据透视原理，物体大小会根据坐标(x, y)进行缩放
    protected int widthInField;
    protected int heightInField;

    // 角色步进参数
    protected int incx;
    protected int incy;

    // 攻击状态
    protected boolean attack;

    // 生命标志与血条值
    public boolean live;
    public int HP;

    // 进程控制标志
    public boolean runningFlag;

    // 需要与战役控制后台进行交互
    protected Battle battle;

    protected Random rand;

    protected Creature(String url, int x, int y, int width, int height, Role role, Battle battle, int id) {
        super(url, x, y, width, height);
        widthInField = width;
        heightInField = height;
        this.role = role;
        live = true;
        runningFlag = true;
        this.battle = battle;
        this.ID = id;
        update();
    }

    /**
     * 根据透视定理进行空间排序
     */
    @Override
    public int compareTo(java.lang.Object o) {
        Creature obj = (Creature) o;
        return Integer.compare(this.y()+this.getHeightInField(), obj.y()+obj.getHeightInField());
    }

    public abstract void run();

    public synchronized void update(){
        if(role == Role.Scorpion || role == Role.Snake) {
            widthInField = (int) (getWidth() * (((double) y() + 600) / 1400.0));
            heightInField = (int) (getHeight() * (((double) y() + 600) / 1400.0));
        } else {
            widthInField = (int) (getWidth() * (((double) y() + 800) / 1600.0));
            heightInField = (int) (getHeight() * (((double) y() + 800) / 1600.0));
        }
        xInField = this.x() + (int) (getWidthInField() /2.0);
        yInField = this.y() + (int) (getHeightInField() * 9/10.0);
    }

    protected boolean willHit(int x1, int y1, int x2, int y2){
        return Math.abs(y1-y2)<6 && Math.abs(x1-x2)<12;
    }

    public int getXInField(){
        return xInField;
    }

    public int getYInField(){
        return yInField;
    }

    public int getWidthInField() {
        return widthInField;
    }

    public int getHeightInField() {
        return heightInField;
    }

    public boolean isRightRole() {
        return rightRole;
    }

    public boolean isLive(){
        return live;
    }

    public boolean isAttacking(){
        return attack;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }

    public void setLive(){
        live = true;
        runningFlag = true;
    }

    public void setDie(){
        live = false;
        runningFlag = false;
    }

    public int getID() {
        return ID;
    }

}

