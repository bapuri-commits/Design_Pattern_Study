package visitor;

public class Fruit implements ItemElement{
    private int pricePerKg;
    private int weight;
    private String name;
    
    public Fruit(int priceKg, int weight, String name) {
        this.pricePerKg = priceKg;
        this.weight = weight;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public int getPricePerKg() {
        return pricePerKg;
    }
    
    public int getWeight() {
        return weight;
    }
    
    @Override
    public int accept(ShoppingCartVisitor visitor) {
        return visitor.visit(this);
    }
 
}