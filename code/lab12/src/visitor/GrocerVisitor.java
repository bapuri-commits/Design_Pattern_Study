package visitor;

public class GrocerVisitor implements ShoppingCartVisitor{
    @Override
    public int visit(Book book){
        return 0;
    }
    @Override
    public int visit(Fruit fruit){
        int cost = fruit.getPricePerKg() * fruit.getWeight() * 7 / 10;
        System.out.println(fruit.getName() + "Grocer cost = " + cost);
        return cost;
    }
    @Override
    public int visit(Meat meat){
        int cost = meat.getPricePerKg() * meat.getWeight() * 8 / 10;
        System.out.println(meat.getName() + "Grocer cost = " + cost);
        return cost;
    }
    @Override
    public int visit(Grain grain){
        int cost = grain.getBagCount() * grain.getPricePerBag() * 6 / 10;
        System.out.println(grain.getName() + "Grocer cost = " + cost);
        return cost;
    }
    @Override
    public int visit(Vegetable vegetable){
        int cost = vegetable.getBundleCount()* vegetable.getPricePerBundle() * 5 / 10;
        System.out.println(vegetable.getName() + "Grocer cost = " + cost);
        return cost;
    }
}
