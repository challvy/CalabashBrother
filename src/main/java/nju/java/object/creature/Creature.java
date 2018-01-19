package nju.java.object.creature;

import nju.java.object.Object;
import nju.java.storyboard.Battle;

import java.util.Random;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Creature�������ʾ��ɫ��ɫ
 * <br>
 * @see nju.java.object.Object
 */
public abstract class Creature extends Object implements Comparable, Runnable{

    // ��ɫ�����������
    public Role role;
    private int ID;
    public boolean rightRole;

    // �������ĵ�
    protected int xInField;
    protected int yInField;

    // ����͸��ԭ�������С���������(x, y)��������
    protected int widthInField;
    protected int heightInField;

    // ��ɫ��������
    protected int incx;
    protected int incy;

    // ����״̬
    protected boolean attack;

    // ������־��Ѫ��ֵ
    public boolean live;
    public int HP;

    // ���̿��Ʊ�־
    public boolean runningFlag;

    // ��Ҫ��ս�ۿ��ƺ�̨���н���
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
     * ����͸�Ӷ�����пռ�����
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

