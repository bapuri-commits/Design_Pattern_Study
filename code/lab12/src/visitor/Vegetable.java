package visitor;

public class Vegetable implements ItemElement {
    private int pricePerBundle;
    private int bundleCount;
    private String name;
    private boolean isOrganic;

    public Vegetable(int pricePerBundle, int bundleCount, String name, boolean isOrganic) {
        this.pricePerBundle = pricePerBundle;
        this.bundleCount = bundleCount;
        this.name = name;
        this.isOrganic = isOrganic;
    }

    public int getPricePerBundle() {
        return pricePerBundle;
    }

    public int getBundleCount() {
        return bundleCount;
    }

    public String getName() {
        return name;
    }

    public boolean isOrganic() {
        return isOrganic;
    }

    @Override
    public int accept(ShoppingCartVisitor visitor) {
        return visitor.visit(this);
    }
}