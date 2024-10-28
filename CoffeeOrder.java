package CoffeeShop;

import java.util.Arrays;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

class CoffeeOrder extends Observable{
    private String customerName;
    private String coffeeType;
    private CoffeeDecorator coffee;
    private OrderState currentState;

    public CoffeeOrder(String customerName, String coffeeType, CoffeeDecorator coffee){
        this.customerName = customerName;
        this.coffeeType=coffeeType;
        this.coffee=coffee;
        this.currentState=new WaitingState();
    }

    public void printOrder(){
        System.out.println("Customer Name:" + customerName);
        System.out.println("Coffee Type: "+coffeeType);
        System.out.println("Extras: "+coffee.getDescription());
        System.out.println("Cost: "+coffee.getCost());
        System.out.println("Order State: "+currentState.getClass().getSimpleName());
    }

    public String getOrderDetails() {
        return "<html>Customer Name: " + customerName +
                "<br>Coffee Type: " + coffeeType +
                "<br>Extras: " + coffee.getDescription() +
                "<br>Cost: " + coffee.getCost() +
                "</html>";
    }



    public void setState(OrderState state){
        currentState=state;
    }

    public void processOrder(){
        currentState.handle(this);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentState instanceof WaitingState){
                    transitionToReadyState();
                } else if (currentState instanceof ReadyState) {
                    transitionToDeliveredState();
                }
                setChanged();
                notifyObservers();
                timer.cancel();
            }
        }, currentState instanceof WaitingState ? 30000 : 60000);
    }

    public String getCoffeeType(){
        return coffeeType;
    }

    private void transitionToReadyState(){
        currentState = new ReadyState();
    }

    private void transitionToDeliveredState(){
        currentState = new DeliveredState();
    }

    public OrderState getState() {
        return currentState;
    }

    public CoffeeDecorator getCoffee() {
        return coffee;
    }

    public void setCoffee(CoffeeDecorator coffee) {
        this.coffee = coffee;
    }

    public char[] getCustomerName() {
        return customerName.toCharArray();
    }

    public void setCustomerName(char[] customerName) {
        this.customerName = Arrays.toString(customerName);
    }
}