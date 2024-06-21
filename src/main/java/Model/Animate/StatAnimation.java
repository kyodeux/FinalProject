package Model.Animate;

import java.text.NumberFormat;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class StatAnimation {
    private final Label label;
    private Interpolator style;
    private Timeline animation;
    private String flag;
    private double duration;
    private final double iterations = 10;
    private double goal;
    private NumberFormat format;
    
    public void setStyle(Interpolator style){
        this.style = style;
    }
    public void setFlag(String flag){
        this.flag = flag;
    }
    public void setDuration(double duration){
        this.duration = duration;
    }
    public void setGoal(double goal){
        this.goal = goal;
        compute();
    }
    public void setFormat(NumberFormat format){
        this.format = format;
    }
    
    public double getDuration(){
        return duration;
    }
    
    public void compute(){
        animation = new Timeline();
        double step = 1 / iterations;
        double delay = duration * step;
        
        for (double i = 0; i <= iterations; i += step){
            long value = Math.round(style.interpolate(0, goal, step * i));
            animation.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(delay * i),
                    event -> {
                        label.setText(flag + 
                                (format != null? format.format(value):
                                value));
                    }
            ));
            
        }
    }
    
    public void play(){
        if(animation == null){
            try{
                compute();
            }catch(Exception e){}
            
            if (animation == null){return;}
        }
        
        animation.play();
    }
    
    public StatAnimation(Label label){
        this.label = label;
        //default values
        style = EasingStyle.OutSine;
        goal = 5;
        duration = 1.5;
        flag = "";
    }
}
