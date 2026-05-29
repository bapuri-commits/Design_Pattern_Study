package visitor;

// 곡물과 채소 등도 추가해보자.
public class ShoppingCartClient {
    public static void main(String[] args) {
        ItemElement[] items = new ItemElement[]{
                new Book(20, "What is Justice?"),
                new Book(100, "DesignPattern"),
                new Fruit(10, 2, "Banana"),
                new Fruit(5, 5, "Apple"),
                new Meat(20, 3, "Pork Belly", "Normal"),
                new Meat(30, 2, "Beef Sirloin", "Premium"),
                new Grain(10, 3, "Rice"),
                new Grain(10, 5, "Barley"),
                new Vegetable(8, 2, "Cabbage", false),
                new Vegetable(8, 2, "Spinach", true)};

        int total1 = calculatePrice(items, new ShoppingCartVisitorImpl());
        System.out.println("==========================================");
        System.out.println("Total Cost = " + total1);
        System.out.println("==========================================");
        int total2 = calculatePrice(items, new GrocerVisitor());
        System.out.println("==========================================");
        System.out.println("Total Grocer Cost = " + total2);
        System.out.println("==========================================");
        System.out.println("Total Cost = " + total1);
        System.out.println("Total Grocer Cost = " + total2);
    }

    private static int calculatePrice(ItemElement[] items, ShoppingCartVisitor visitor) {
        int sum = 0;

        for (ItemElement item : items) {
            sum = sum + item.accept(visitor);
        }

        return sum;
    }
}