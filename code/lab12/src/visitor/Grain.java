package visitor;

public class Grain implements ItemElement {
    private int pricePerBag;
    private int bagCount;
    private String name;

    public Grain(int pricePerBag, int bagCount, String name) {
        this.pricePerBag = pricePerBag;
        this.bagCount = bagCount;
        this.name = name;
    }

    public int getPricePerBag() {
        return pricePerBag;
    }

    public int getBagCount() {
        return bagCount;
    }

    public String getName() {
        return name;
    }

    @Override
    public int accept(ShoppingCartVisitor visitor) {
        return visitor.visit(this);
    }
}