package nju.java.storyboard;


import nju.java.iofile.IOFile;
import nju.java.object.Object;
import nju.java.object.creature.Creature;
import nju.java.object.creature.Role;
import nju.java.object.creature.civilian.Yeye;
import nju.java.object.creature.soldier.DemonTroop;
import nju.java.object.creature.soldier.Huluwa;
import nju.java.object.creature.soldier.Scorpion;
import nju.java.object.creature.soldier.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Battle类继承自JPanel，被Ground类调用<br>
 * 实现绘图功能与战斗逻辑，各类角色在此声明并定义<br>
 * @see nju.java.storyboard.Ground
 * @see nju.java.Main
 */
public class Battle extends JPanel {

    // 长宽
    private int width;
    private int height;

    // 运行模式状态，用来做界面逻辑跳转(虽然也没几个跳转界面)
    private RunningMode runningMode;

    // 计时器以及事件监听器
    private Timer timer ;
    private ActionListener timerTask ;

    // 各色角色
    private Huluwa[] huluwa;
    private DemonTroop[] demonTroop;
    private int demonTroopNum;
    private Yeye yeye;
    private Scorpion scorpion;
    private Snake snake;

    // 存放死去的角色
    private Vector<Creature> death;

    // 若snake和scorp分别被收服，则不再放入death容器中
    private int snakeDieFlag=0;
    private int scorpionDieFlag=0;

    // flushGround是大背景，对应background, badEndingGround, happyEndingGround三种情况
    private Object flushGround;
    private Object background;
    private Object badEndingGround;
    private Object happyEndingGround;

    // 七个复活葫芦娃的葫芦图标，iconLife为各方生命标志
    private Object[] hulu;
    private Object[] iconLife;

    /**
     * 若蛇精和蝎子精都被收服，则小喽啰不再复活
     */
    public boolean flagMainDemonDie;

    /**
     * roles存放当前活着的角色
     */
    public Vector<Creature> roles;

    /**
     * 是否开始（播放或回放）
     */
    protected boolean startFlag;

    /**
     * 结局：endingType值为1则为badEnding，值为2则为happyEnding
     */
    protected int endingType;

    /**
     * 添加键盘监听器并初始化参数
     */
    public Battle() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        init();
        setRestartParameter();
    }

    /**
     * 重载paint函数，调用了painting函数
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        painting(g);
    }

    /**
     * 在不同模式下都可调用来绘制图像
     */
    private void painting(Graphics g){
        if(runningMode==RunningMode.HOME){
            g.drawImage(flushGround.getImage(),0,0,this);
            drawRoles(g);
        } else if(runningMode==RunningMode.PLAYING) {
            freshEndingType();
            setAttackImage();
            freshRole();
            drawRoles(g);
        } else if(runningMode==RunningMode.PLAYBACK) {
            g.drawImage(flushGround.getImage(),0,0,this);
            drawRoles(g);
        } else if(runningMode==RunningMode.OVER){
            g.drawImage(flushGround.getImage(),0,0,this);
        }
        repaint();
    }

    private void drawRoles(Graphics g) {
        // Draw Background
        g.drawImage(background.getImage(),0,0,this);

        // Draw Icons
        if(huluwaIsLive()) {
            g.drawImage(iconLife[0].getImage(), iconLife[0].x(), iconLife[0].y(), iconLife[0].getWidth(), iconLife[0].getHeight(), this);
        } else {
            g.drawImage(iconLife[4].getImage(), iconLife[4].x(), iconLife[4].y(), iconLife[4].getWidth(), iconLife[4].getHeight(), this);
        }
        if(yeye.isLive()) {
            g.drawImage(iconLife[1].getImage(), iconLife[1].x(), iconLife[1].y(), iconLife[1].getWidth(), iconLife[1].getHeight(), this);
        } else {
            g.drawImage(iconLife[5].getImage(), iconLife[5].x(), iconLife[5].y(), iconLife[5].getWidth(), iconLife[5].getHeight(), this);
        }
        if(snake.isLive()) {
            g.drawImage(iconLife[2].getImage(), iconLife[2].x(), iconLife[2].y(), iconLife[2].getWidth(), iconLife[2].getHeight(), this);
        } else {
            g.drawImage(iconLife[6].getImage(), iconLife[6].x(), iconLife[6].y(), iconLife[6].getWidth(), iconLife[6].getHeight(), this);
        }
        if(scorpion.isLive()) {
            g.drawImage(iconLife[3].getImage(), iconLife[3].x(), iconLife[3].y(), iconLife[3].getWidth(), iconLife[3].getHeight(), this);
        } else {
            g.drawImage(iconLife[7].getImage(), iconLife[7].x(), iconLife[7].y(), iconLife[7].getWidth(), iconLife[7].getHeight(), this);
        }

        // Draw Hulu
        for(int i=0;i<7;i++){
            if(!huluwa[i].isLive()) {
                g.drawImage(hulu[i].getImage(),hulu[i].x(),hulu[i].y(),hulu[i].getWidth(),hulu[i].getHeight(),this);
            }
        }

        // Draw Creatures
        Collections.sort(roles);
        for (Creature role : roles) {
            if (role.isLive()) {
                g.drawImage(role.getImage(), role.x(), role.y(), role.getWidthInField(), role.getHeightInField(), this);
            }
        }
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE && runningMode==RunningMode.HOME){
                runningMode = RunningMode.PLAYING;
                startFlag=true;
                startThread();
                initTimer(100);
            } else if (key == KeyEvent.VK_L && (runningMode==RunningMode.HOME || runningMode==RunningMode.OVER)) {
                runningMode = RunningMode.PLAYBACK;
                int flag = 1;
                JFileChooser jFileChooser = null;
                jFileChooser = new JFileChooser(new File("document"));
                jFileChooser.setDialogTitle("Choose Files");
                flag = jFileChooser.showDialog(null, null);
                IOFile.setReadFile(jFileChooser.getSelectedFile());
                initTimer(10);
                /*
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("D:\\Documents\\IdeaProjects\\HuluwaFinal2"));
                int returnVal = chooser.showOpenDialog(new JPanel());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("打开的文件是: " + chooser.getSelectedFile().getName());
                    try {
                        load(chooser.getSelectedFile().getName());;
                    } catch (IOException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
                */

            } else if (key == KeyEvent.VK_R && (runningMode==RunningMode.PLAYING||runningMode==RunningMode.OVER)) {
                for(Creature c: roles){
                    c.runningFlag = false;
                }
                setRestartParameter();
                roles.clear();
                setCreature();
                freshRole();
            } else if(key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
    }

    private void initTimer(int time){
        timerTask = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setAttackImage();
                freshEndingType();
                LoadOrSave();
                repaint();
            }
        };
        timer = new Timer(time,timerTask);
        timer.start();
    }

    private synchronized void LoadOrSave(){
        if(runningMode==RunningMode.HOME){

        } else if(runningMode==RunningMode.PLAYING) {
            if(!snake.isLive()){
                snakeDieFlag=1;
            }
            if(!scorpion.isLive()){
                scorpionDieFlag=1;
            }
            try {
                IOFile.writeFile(roles);
                IOFile.writeFile(death);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(runningMode==RunningMode.PLAYBACK) {
            String str = null;
            if (IOFile.getReadFile() == null) {
                return;
            }
            str = IOFile.getNextString();
            if (str != null) {
                playback(str);
            } else {
                runningMode = RunningMode.OVER;
            }

        } else if(runningMode==RunningMode.OVER){

        }
    }

    private void playback(String str){
        String name = null;
        int id=0;
        int x = -1, y = -1;
        boolean isAlive = false;
        boolean attack = false;

        String[] readFileLine = str.split(" ");
        if(readFileLine.length != 6)
            return;
        name = readFileLine[0];
        id = Integer.parseInt(readFileLine[1]);
        x = Integer.parseInt(readFileLine[2]);
        y = Integer.parseInt(readFileLine[3]);
        isAlive = (readFileLine[4].equals("true")) ? true:false;
        attack = (readFileLine[5].equals("true")) ? true:false;

        if (name.equals("Yeye") ) {
            yeye.setX(x);
            yeye.setY(y);
            yeye.update();
        } else if (name.equals( "Scorpion")) {
            scorpion.setX(x);
            scorpion.setY(y);
            scorpion.update();
            scorpion.setAttack(attack);
            if(!isAlive)
                scorpion.setDie();
        } else if (name.equals("Snake")) {
            snake.setX(x);
            snake.setY(y);
            snake.update();
            if(!isAlive)
                snake.setDie();
        } else if(name.equals("Huluwa")) {
            huluwa[id-1].setX(x);
            huluwa[id-1].setY(y);
            huluwa[id-1].update();
            huluwa[id-1].setAttack(attack);
            if(!isAlive){
                huluwa[id-1].setDie();
            } else {
                huluwa[id-1].setLive();
            }
        } else if(name.equals("DemonTroop")){
            demonTroop[id-1].setX(x);
            demonTroop[id-1].setY(y);
            demonTroop[id-1].update();
        }
        repaint();
    }

    /**
     * @return 得到窗口宽度
     */
    public int getBoardWidth() {
        return width;
    }

    /**
     * @return 得到窗口高度
     */
    public int getBoardHeight() {
        return height;
    }
    private void init(){
        setCreature();
        initIcon();
        initRole();
    }
    private void setRestartParameter(){
        endingType=0;
        runningMode=RunningMode.HOME;
        flagMainDemonDie = false;
        startFlag=true;
        flushGround.setImage(background.getImage());
    }
    private void initIcon() {
        flushGround = new Object("Hulugu.png",0,0,1600,900);
        background = new Object("Hulugu.png",0,0,1600,900);
        badEndingGround = new Object("BadEnding.png",0,0,1600,900);
        happyEndingGround = new Object("HappyEnding.png",0,0,1600,900);
        width = background.getImage().getWidth(this);
        height = background.getImage().getHeight(this);
        hulu = new Object[7];
        hulu[0] = new Object("Hulu1.png",550,310,120,120);
        hulu[1] = new Object("Hulu2.png",600,300,120,120);
        hulu[2] = new Object("Hulu3.png",670,240,120,120);
        hulu[3] = new Object("Hulu4.png",730,300,120,120);
        hulu[4] = new Object("Hulu5.png",770,220,120,120);
        hulu[5] = new Object("Hulu6.png",840,200,120,120);
        hulu[6] = new Object("Hulu7.png",880,250,120,120);

        iconLife = new Object[8];
        iconLife[0] = new Object("HuluwaIconLive.png",50,50,100,100);
        iconLife[1] = new Object("YeyeIconLive.png",200,50,100,100);
        iconLife[2] = new Object("SnakeIconLive.png",1300,50,100,100);
        iconLife[3] = new Object("ScorpionIconLive.png",1450,50,100,100);
        iconLife[4] = new Object("HuluwaIconDie.png",50,50,100,100);
        iconLife[5] = new Object("YeyeIconDie.png",200,50,100,100);
        iconLife[6] = new Object("SnakeIconDie.png",1300,50,100,100);
        iconLife[7] = new Object("ScorpionIconDie.png",1450,50,100,100);
    }
    private void initRole(){
        // 将各色角色加入roles容器中
        roles = new Vector<Creature>();
        // death初始不为空
        death = new Vector<Creature>();
        for(Huluwa h: huluwa) {
            roles.add(h);
            death.add(h);
        }
        roles.add(yeye);
        roles.add(snake);
        roles.add(scorpion);
        death.add(yeye);
        death.add(snake);
        death.add(scorpion);
        for(DemonTroop d: demonTroop){
            roles.add(d);
            death.add(d);
        }
    }
    private void setCreature(){
        // 葫芦娃为鹤翼阵型
        huluwa = new Huluwa[7];
        huluwa[0] = new Huluwa("Dawa.png",520, 560, Role.Dawa,560, 360,this,1);
        huluwa[1] = new Huluwa("Erwa.png",400, 650, Role.Erwa,620,330,this,2);
        huluwa[2] = new Huluwa("Sanwa.png",160, 440, Role.Sanwa,685,270,this,3);
        huluwa[3] = new Huluwa("Siwa.png", 400, 530, Role.Siwa,740,330,this,4);
        huluwa[4] = new Huluwa("Wuwa.png", 220, 780, Role.Wuwa,790,240,this,5);
        huluwa[5] = new Huluwa("Liuwa.png", 300, 720, Role.Liuwa,855,220,this,6);
        huluwa[6] = new Huluwa("Qiwa.png",300, 460, Role.Qiwa,905,295,this,7);

        // 喽啰为方阵，共计6种种角色
        demonTroopNum = 16;
        demonTroop = new DemonTroop[demonTroopNum];
        demonTroop[0] = new DemonTroop("DemonTroop1.png",1000,450,this,1);
        demonTroop[1] = new DemonTroop("DemonTroop1.png",1000,420+100,this,2);
        demonTroop[2] = new DemonTroop("DemonTroop1.png",1000,420+100*2,this,3);
        demonTroop[3] = new DemonTroop("DemonTroop1.png",1000,420+100*3,this,4);
        demonTroop[4] = new DemonTroop("DemonTroop2.png",1150,450,this,5);
        demonTroop[5] = new DemonTroop("DemonTroop3.png",1150,420+100,this,6);
        demonTroop[6] = new DemonTroop("DemonTroop2.png",1150,420+2*100,this,7);
        demonTroop[7] = new DemonTroop("DemonTroop3.png",1150,420+3*100,this,8);
        demonTroop[8] = new DemonTroop("DemonTroop4.png",1150+150,450,this,9);
        demonTroop[9] = new DemonTroop("DemonTroop5.png",1150+150,420+100,this,10);
        demonTroop[10] = new DemonTroop("DemonTroop4.png",1150+150,420+2*100,this,11);
        demonTroop[11] = new DemonTroop("DemonTroop5.png",1150+150,420+3*100,this,12);
        demonTroop[12] = new DemonTroop("DemonTroop6.png", 1450, 450,this,13);
        demonTroop[13] = new DemonTroop("DemonTroop6.png", 1450, 420+100,this,14);
        demonTroop[14] = new DemonTroop("DemonTroop6.png", 1450, 420+100*2,this,15);
        demonTroop[15] = new DemonTroop("DemonTroop6.png", 1450, 420+100*3,this,16);

        // 爷爷、蛇精、蝎子精分别只有一个，其参数内设即可
        scorpion = new Scorpion(this);
        snake = new Snake(this);
        yeye = new Yeye(this);
    }
    private void setAttackImage(){
        if(huluwa[0].isAttacking()){
            huluwa[0].setImageByUrl("DawaAttack.png");
        } else {
            huluwa[0].setImageByUrl("Dawa.png");
        }
        if(huluwa[1].isAttacking()){
            huluwa[1].setImageByUrl("ErwaAttack.png");
        } else {
            huluwa[1].setImageByUrl("Erwa.png");
        }
        if(huluwa[2].isAttacking()){
            huluwa[2].setImageByUrl("SanwaAttack.png");
        } else {
            huluwa[2].setImageByUrl("Sanwa.png");
        }
        if(huluwa[3].isAttacking()){
            huluwa[3].setImageByUrl("SiwaAttack.png");
        } else {
            huluwa[3].setImageByUrl("Siwa.png");
        }
        if(huluwa[4].isAttacking()){
            huluwa[4].setImageByUrl("WuwaAttack.png");
        } else {
            huluwa[4].setImageByUrl("Wuwa.png");
        }
        if(huluwa[5].isAttacking()){
            huluwa[5].setImageByUrl("LiuwaAttack.png");
        } else {
            huluwa[5].setImageByUrl("Liuwa.png");
        }
        if(huluwa[6].isAttacking()){
            huluwa[6].setImageByUrl("QiwaAttack.png");
        } else {
            huluwa[6].setImageByUrl("Qiwa.png");
        }
        if(scorpion.isAttacking()) {
            scorpion.setImageByUrl("ScorpionAttack.png");
        } else {
            scorpion.setImageByUrl("Scorpion.png");
        }
    }
    private void startThread(){
        new Thread(yeye).start();
        new Thread(snake).start();
        new Thread(scorpion).start();
        demonTroopNum=16;
        for(int i=0;i<demonTroopNum;i++) {
            new Thread(demonTroop[i]).start();
        }
        for(int i=0;i<7;i++){
            new Thread(huluwa[i]).start();
        }
    }
    private boolean huluwaIsLive(){
        for(int i=0;i<7;i++)
            if(huluwa[i].isLive())
                return true;
        return false;
    }
    private void freshEndingType(){
        if (!yeye.isLive()) {
            for (Creature c : roles) {
                c.runningFlag = false;
            }
            endingType = 1;
        }
        if (!scorpion.isLive() && !snake.isLive()) {
            flagMainDemonDie = true;
        }
        int flag = 0;
        for (Creature role : roles) {
            if (!role.isRightRole()) {
                flag = 1;
            }
        }
        if (flag == 0) {
            for (Creature c : roles) {
                c.runningFlag = false;
            }
            endingType = 2;
        }

        if(endingType==1 && (runningMode==RunningMode.PLAYING || runningMode==RunningMode.PLAYBACK)){
            flushGround.setImage(badEndingGround.getImage());
            runningMode=RunningMode.OVER;
        } else if(endingType==2 && (runningMode==RunningMode.PLAYING || runningMode==RunningMode.PLAYBACK)){
            flushGround.setImage(happyEndingGround.getImage());
            runningMode=RunningMode.OVER;
        }
    }
    private void freshRole(){
        Iterator<Creature> iterator = roles.iterator();
        while(iterator.hasNext()){
            Creature c = iterator.next();
            if (!c.isLive()) {
                iterator.remove();
            }
        }

        Iterator<Creature> iteratorDeath = death.iterator();
        while(iteratorDeath.hasNext()){
            Creature c = iteratorDeath.next();
            if (c.isLive()) {
                iteratorDeath.remove();
            }
        }

        /*
        //错误：调用vector.remove(index)方法导致modCount和expectedModCount的值不一致,需要调用Itr类的remove方法
        for (Creature r : roles) {
            if (!r.isLive()) {
                roles.remove(r);
            }
        }
        for(Creature d: death){
            if(d.isLive()) {
                death.remove(d);
            }
        }
        */

        for(Huluwa h: huluwa) {
            if(h.isLive() && !roles.contains(h)) {
                roles.add(h);
            } else if(!h.isLive() && !death.contains(h)){
                death.add(h);
            }
        }
        if(yeye.isLive() && !roles.contains(yeye)) {
            roles.add(yeye);
        } else {
            death.add(yeye);
        }
        if(snake.isLive() && !roles.contains(snake)) {
            roles.add(snake);
        } else if(!snake.isLive() && !death.contains(snake) && snakeDieFlag==0){
            death.add(snake);
        }
        if(scorpion.isLive() && !roles.contains(scorpion)) {
            roles.add(scorpion);
        } else if(!scorpion.isLive() && !death.contains(scorpion) && scorpionDieFlag==0){
            death.add(scorpion);
        }
        for(DemonTroop d: demonTroop){
            if(d.isLive() && !roles.contains(d)) {
                roles.add(d);
            } else if(!d.isLive() && !death.contains(d)){
                death.add(d);
            }
        }
    }

}
