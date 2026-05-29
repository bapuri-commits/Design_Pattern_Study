package decorator2;

public class RedShapeDecorator extends ShapeDecorator {
    public RedShapeDecorator(Shape decoratedShape) { super(decoratedShape); }
    @Override public void draw() {
        decoratedShape.draw();
        setRedBorder(decoratedShape);
    }
    private void setRedBorder(Shape s) { System.out.println("Border Color: Red"); }
}
