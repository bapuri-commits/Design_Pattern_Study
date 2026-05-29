package decorator;

public class SugarDecorator extends CoffeeDecorator{
    public SugarDecorator(Coffee c){super(c);}
    @Override
    public String getDescription(){
        return coffee.getDescription() + ", Sugar";
    }

    @Override
    public double getCost(){
        return coffee.getCost() + 0.2;
    }
}
