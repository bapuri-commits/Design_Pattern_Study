package visitor;

public interface ShoppingCartVisitor {
    int visit(Book book);
    int visit(Fruit fruit);
    int visit(Meat meat);
    int visit(Grain grain);
    int visit(Vegetable vegetable);
}