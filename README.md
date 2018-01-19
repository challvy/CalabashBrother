# Huluwa

**郑聪尉 151220169@smail.nju.edu.cn**


## 开发环境

* IntelliJ IDEA 2017.2；
* Java8；
* Maven 3.5.2;


## 项目预览

Huluwa 17f-Final 版本是自动化的葫芦娃大战程序，支持存档读档并可回放精彩过程；


![Huluwa-Playing](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa-Playing.gif)

**可用Maven打包后运行Huluwa.jar，按空格键即可立即开始多线程战斗，详见末尾的规则说明**


## 项目详情

### 目录
* /document: 存档与读档文件夹
* /javadoc: javadoc文件夹
* /src: 工程文件夹
* /target: 构建目标文件夹

### 状态机

![Huluwa-Status](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa-Status.png)

### 框架

* Main类: 程序的入口；
* iofile包: 文件操作；
* object包: 二维对象以及各类角色定义；
* storyboard包: 运行逻辑与界面；


## 设计要点

### 设计原则

#### 一、开放封闭原则 The Open-Closed Principle
> absctract class的设计，将可供子类重写的设计为抽象方法，开放扩展；其它方法不可改变，关闭修改；

#### 二、里氏替换原则 The Liskov Substitution Principle
> 所有的Creatures都能被Huluwa, Yeye, Snake, Scorpion或者DemonTroop替换；

#### 三、单一职责原则 The Single-Responsibility Principle
> IOFile类仅实现文件读写；Creature仅实现角色属性与方法；

### 封装与继承

* Object类表示二维对象，可派生出Creature抽象类，也可直接用来定义背景、图标等；
* Creature抽象类表示各色角色，派生出士兵和平民两项抽象类，由其生命特特征，需实现Runnable接口来进行线程编程；
* Creature抽象类有前后遮挡关系，需实现Comparable接口来进行空间排序；
* Soldier抽象类有进攻属性和方法，故需要添加进攻起始时间戳和终止时间戳来控制进攻时长；

![Huluwa-UML](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa-UML.png)

### 集合与泛型

Battle类中的roles和death的使用集合类型Vector<>，泛型的元素类型为Creature；

### 多态

Battle类中的roles和death使用泛型Vector<>，其元素类型Creature可替换为其具体子类Huluwa, Yeye等，体现了多态；

<pre><code>private void initRole(){
    roles = new Vector<Creature>();
    for(Huluwa h: huluwa) {
        roles.add(h);
    }
    roles.add(yeye);
    roles.add(snake);
    roles.add(scorpion);
    for(DemonTroop d: demonTroop){
        roles.add(d);
    }
}
</code></pre>

### 注解

文档使用@author和@see注解，其方法使用@param和@return注解，用来编写javadoc，详情请见javadoc文件夹；

### 输入输出

IOFile类实现：FileReader与BufferedReader用来读取文件，FileWriter与BufferedWriter用来写入文件；

### 线程安全

每个将角色类中都存有Battle类用来与同一个逻辑控制后台交互，故需添加synchronized和volatile关键字；

### 单元测试

角色类以葫芦娃为代表进行测试即可。

* testPosition()用来测试位置信息，被测试的方法有: x(), y()；
* testLive()用来测试生命属性，被测试的方法有: isLive()；
* testRole()用来测试正义或邪恶，被测试的方法有: isRightRole()；
* testImage()用来测试图片属性，被测试的方法有: getImage()；
* testAttack()用来测试攻击属性，被测试的方法有: isAttacking()；


## 总结

* 设计前期经老师提醒，对抽象封装有了更深的体会；
* 中期根据教学进度，陆续添加功能，缝缝补补；
* 后期在实现界面与复盘功能的时候，在原来码得自由自在的情况下，遇到不小的冲击，需要对框架进行比较大幅度改变；
* Battle类用来控制战斗逻辑与界面显示，没能做好MVC设计模式，这种模式在移动互联应用课上深有体会，所以寒假会进一步优化；
* 阵型操作没有单独成一类，需要改进，同样可以考虑采用delegate模式；


## 附录 规则说明

考虑到趣味性，规则有一定修改；

### 前情概要
* 时光倒流，穿山甲又双叒叕钻破了葫芦山，蛇精蝎子精从中逃了出来，变造小喽啰，占据山洞自得其乐，后话不提；
* 另一边七个葫芦娃都成功破壳，个个法力高强，七娃尤其瞩目；
* 在如意的指引下，妖精企图趁葫芦娃们不在抓住爷爷，而爷爷毫无反击能力；
* 好在二娃有火眼金睛，七兄弟及时赶回葫芦山，一场恶斗不可避免；

### 故事开始
* 战斗伊始，双方拉开架势，小喽啰方阵排开，蝎子精蛇精身居其中，七兄弟则鹤翼以待；
* 双方接近并产生碰撞的瞬间, 在非攻击状态下，判站在原地者死；
* 葫芦娃和蝎子精能持续攻击一段时间，发光即为攻击态，判非攻击态者死，若都为攻击态则僵持；
* 葫芦娃HP值初始为2，每次损伤则减1，若死则葫芦藤上对应会重新结上葫芦果，经过七秒钟孕育后复活；
* 蛇精和蝎子精HP值初始为10，只有在被七娃的葫芦收服才判定为死，否则只是被暂时击退，会从山底下继续攻上来；
* 当蛇精和蝎子精都未被收服，则小喽啰将被持续不断地造出来；


### 图文解说
![Huluwa0](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa0.png)
> 战斗伊始，双方拉开架势，小喽啰方阵排开，蝎子精蛇精身居其中，七兄弟则鹤翼以待；

![Huluwa1](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa1.png)
> 来势汹汹！蝎子精带着小喽啰一起冲了过来，但葫芦娃们毫不示弱，不灵不灵和蝎子精进行战斗；

![Huluwa2](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa2.png)
> 葫芦娃们为保护爷爷死伤惨重。不过已经干掉了蝎子精（灰色头像），而七秒钟过后葫芦藤上的葫芦将再次变身Soldiers；

![Huluwa-BadEnding](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa-BadEnding.png)
> 葫芦娃们只攻击离他们最近的敌人，一心恋战却未能保护好爷爷唉；

![Huluwa](https://github.com/challvy/CalabashBrothers/raw/master/prtSc/Huluwa-HappyEnding.png)
> 那谁，别忘了这是谁的地盘；

### 集锦
* 读取document中的文档即可回放查看精彩对局；

## End
