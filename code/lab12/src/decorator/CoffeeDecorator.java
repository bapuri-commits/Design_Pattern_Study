package decorator;

abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;
    CoffeeDecorator(Coffee c){ this.coffee = c; }

}