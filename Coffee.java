import java.util.ArrayList;
import java.util.List;

public class Coffee {
    private String name;
    private int price;
    private List<CoffeeDecorator> decorators;

    private Coffee(CoffeeBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.decorators = new ArrayList<>(builder.decorators);  // Инициализируем декораторы из билдера
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<CoffeeDecorator> getDecorators() {
        return decorators;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", decorators=" + decorators +
                '}';
    }

    public static class CoffeeBuilder {
        private String name;
        private int price;
        private List<CoffeeDecorator> decorators = new ArrayList<>();

        public CoffeeBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CoffeeBuilder setPrice(int price) {
            this.price = price;
            return this;
        }

        public CoffeeBuilder addDecorator(CoffeeDecorator decorator) {
            this.decorators.add(decorator);
            return this;
        }

        public Coffee build() {
            return new Coffee(this);
        }
    }
}
