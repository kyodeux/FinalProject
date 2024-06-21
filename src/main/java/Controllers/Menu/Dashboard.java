package Controllers.Menu;

import Controllers.Main;
import Model.Animate.EasingStyle;
import Model.Animate.StatAnimation;
import Model.Product;
import Model.Utils.CircularQueue;
import java.text.NumberFormat;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Dashboard {

    private final Scene root;
    private final Card salesCard;
    private final Card stockCard;
    private final Card usersCard;
    private final Card revenueCard;

    public Dashboard() {
        root = Main.getUI();

        salesCard = new Card(
                (Label) root.lookup("#salesStat"),
                (Line) root.lookup("#salesLine")
        );
        stockCard = new Card(
                (Label) root.lookup("#stockStat"),
                (Line) root.lookup("#stockLine")
        );
        usersCard = new Card(
                (Label) root.lookup("#usersStat"),
                (Line) root.lookup("#usersLine")
        );
        revenueCard = new Card(
                (Label) root.lookup("#revenueStat"),
                (Line) root.lookup("#revenueLine")
        );
        revenueCard.getAnimation().setFormat(NumberFormat.getCurrencyInstance());
    }

    public void update() {
        /*salesCard.setGoal(getSales());
        stockCard.setGoal(getStock());
        usersCard.setGoal(getUserCount());
        revenueCard.setGoal(getRevenue());*/
        
        salesCard.setGoal(999);
        stockCard.setGoal(999);
        usersCard.setGoal(999);
        revenueCard.setGoal(9999999);
        
        salesCard.playAnimation();
        stockCard.playAnimation();
        usersCard.playAnimation();
        revenueCard.playAnimation();
    }

    public int getSales() {
        CircularQueue catalog = Main.catalog;

        int sales = 0;
        for (int i = 0; i < catalog.length(); i++) {
            Product current = (Product) catalog.peekAt(i);
            sales += current.getSales();
        }

        return sales;
    }

    public int getStock(){
        return Main.catalog.length();
    }
    
    public double getRevenue(){
        CircularQueue catalog = Main.catalog;
        
        double revenue = 0;
        for (int i = 0; i < catalog.length(); i++){
            Product current = (Product) catalog.peekAt(i);
            revenue += (double) current.getSales() * current.getCost();
        }
        
        return revenue;
    }
    
    public int getUserCount() {
        return Main.userList.length();
    }

    private static class Card {

        private final Label label;
        private final Line line;
        private final StatAnimation statAnimation;
        private final Timeline lineAnimation;

        public void setGoal(double goal) {
            statAnimation.setGoal(goal);
        }

        public StatAnimation getAnimation() {
            return statAnimation;
        }

        public Card(Label label, Line line) {
            this.label = label;
            this.line = line;

            statAnimation = new StatAnimation(label);
            statAnimation.setStyle(EasingStyle.OutCirc);

            double defaultX = line.getLayoutX();
            lineAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(line.layoutXProperty(), defaultX -80),
                            new KeyValue(line.scaleXProperty(), 0)
                    ),
                    new KeyFrame(Duration.seconds(
                            statAnimation.getDuration()),
                            new KeyValue(line.layoutXProperty(), defaultX, EasingStyle.OutExpo),
                            new KeyValue(line.scaleXProperty(), 1, EasingStyle.OutExpo)
                    )
            );
        }

        public void playAnimation() {
            statAnimation.play();
            lineAnimation.play();
        }
    }
}
