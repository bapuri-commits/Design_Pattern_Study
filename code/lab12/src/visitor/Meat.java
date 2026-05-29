package visitor;

public class Meat implements ItemElement{
    private int pricePerKg;
    private int weight;
    private String name;
    private String grade;

    public Meat(int pricePerKg, int weight, String name, String grade){
        this.pricePerKg = pricePerKg;
        this.weight = weight;
        this.name = name;
        this.grade = grade;
    }

    public int getPricePerKg() {
        return pricePerKg;
    }

    public int getWeight() {
        return weight;
    }

    public String getGrade() {
        return grade;
    }

    public String getName(){
        return name;
    }

    @Override
    public int accept(ShoppingCartVisitor visitor){
        return visitor.visit(this);
    }
}
/*
* public enum Grade {
    NORMAL, PREMIUM //이렇게 Enum을 이용할수도 있음
}*/