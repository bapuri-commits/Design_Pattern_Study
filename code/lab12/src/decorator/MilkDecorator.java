package decorator;

public class MilkDecorator extends CoffeeDecorator{
    public MilkDecorator(Coffee c){super(c);}
    @Override
    public String getDescription(){
        return coffee.getDescription() + ", Milk";
    }

    @Override
    public double getCost(){
        return coffee.getCost() + 0.5;
    }
}
