package bsugoldirevlaba6;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

public class BouncingBall implements Runnable {
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    private static final int MAX_Radius=40;
    private boolean click=false;
    private static boolean trenie=false;
    private static final int MIN_Radius=15;
    private static final int MAX_SPEED=20;
    private static  int tren=0;
    private Field field;
    private int radius ;
    private Color color;
    private  double x;
    private double y;
    private int speed;
    private double speedX;
    private double speedY;


    public BouncingBall(Field field){
        this.field=field;
        radius = new Double(Math.random()*(MAX_Radius - MIN_Radius)).intValue() + MIN_Radius;
        speed = new Double(Math.round(5*MAX_SPEED / radius)).intValue();
        if (speed>MAX_SPEED) {
            speed = MAX_SPEED;
        }
        double angle = Math.random()*2*Math.PI;
        speedX = 3*Math.cos(angle);
        speedY = 3*Math.sin(angle);
        color = new Color((float)Math.random(), (float)Math.random(),
                (float)Math.random());
// Начальное положение мяча случайно
        x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
// Создаѐм новый экземпляр потока, передавая аргументом
// ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
// Запускаем поток
        thisThread.start();
    }
    public BouncingBall(int x){
        tren=tren-x;
        trenie=true;
    }


    // Метод run() исполняется внутри потока. Когда он завершает работу,
// то завершается и поток

    public void run() {
        balls=field.getter();

        try {
// Крутим бесконечный цикл, т.е. пока нас не прервут,
// мы не намерены завершаться
            if(!click || speed>0){
            while(true) {
// Синхронизация потоков на самом объекте поля
// Если движение разрешено - управление будет
// возвращено в метод
// В противном случае - активный поток заснѐт
                if(trenie){
                    speed=speed+tren;
                    trenie=false;
                }
                field.canMove(this);
                if (x + speedX <= radius) {
// Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
// Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x=new Double(field.getWidth()-radius).intValue();
                } else
                if (y + speedY <= radius) {
// Достигли верхней стенки
                    speedY = -speedY;
                    y = radius;
                } else
                if (y + speedY >= field.getHeight() - radius) {
// Достигли нижней стенки
                    speedY = -speedY;
                    y=new Double(field.getHeight()-radius).intValue();
                } else {
// Просто смещаемся
                    x += speedX;
                    y += speedY;
                }

// Засыпаем на X миллисекунд, где X определяется
// исходя из скорости
// Скорость = 1 (медленно), засыпаем на 15 мс.
// Скорость = 15 (быстро), засыпаем на 1 мс.

                if(speed>=16){
                    speed=15;
                    Thread.sleep(16-speed);
                }
                else {
                    Thread.sleep(16 - speed);
                }
            }}
            else{
                speedY=0;
                speedX=0;
            }
        } catch (InterruptedException ex) {
// Если нас прервали, то ничего не делаем
// и просто выходим (завершаемся)
        }
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius,
                2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }



}


