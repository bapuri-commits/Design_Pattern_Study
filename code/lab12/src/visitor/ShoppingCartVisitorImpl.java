package visitor;

public class ShoppingCartVisitorImpl implements ShoppingCartVisitor{
 
    @Override
    public int visit(Book book) {
        int cost = 0;
        
        if(book.getPrice() > 50) cost = book.getPrice()-5;
        else cost = book.getPrice();
        
        System.out.println("Book ISBN : " + book.getIsbnNumber() + " cost = " + cost);
        return cost;
    }
 
    @Override
    public int visit(Fruit fruit) {
        int cost = fruit.getPricePerKg() * fruit.getWeight();
        System.out.println(fruit.getName() + " cost = " + cost);
        return cost;
    }

    @Override
    public int visit(Meat meat) {
        int cost = meat.getPricePerKg() * meat.getWeight();
        if (meat.getGrade().equals("Normal")) {//enum 사용했더라면 meat.getGrade() == meat.Normal로 처리
            System.out.println(meat.getName() + " cost = " + cost);
            return cost;
        } else {
            System.out.println(meat.getName() + " cost = " + cost * 2);
            return cost * 2;//int라서 2배로 함, 만약 20%같은 처리가 필요하면 double로 캐스팅하거나 정수 *12/10을 통해 처리 가능
        }
    }

    @Override
    public int visit(Grain grain){
        int cost = grain.getBagCount() * grain.getPricePerBag();
        if(grain.getBagCount() >= 5){
            cost = cost * 9 / 10;
        }
        System.out.println(grain.getName() + " cost = " + cost);
        return cost;
    }

    @Override
    public int visit(Vegetable vegetable){
        int cost = vegetable.getBundleCount()* vegetable.getPricePerBundle();
        if(vegetable.isOrganic()){
            cost =  (int)(cost * 1.5);
        }
        System.out.println(vegetable.getName() + " cost = " + cost);
        return cost;
    }
 
}
