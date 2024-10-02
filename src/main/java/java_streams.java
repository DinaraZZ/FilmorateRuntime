import java.util.List;

public class java_streams {
    public static void main(String[] args) {
        /*List<String> languages = List.of("Java", "Php", "Javascript", "Python");
        List<String> result = languages.stream()
                .filter(language -> language.length() >= 4)
                .map(language -> language.toUpperCase())
                .toList();
        System.out.println(result);*/

        /////////////////////////////////
        //.foreach(str->sout)
        System.out.print("Фрукты до 1000: ");
        products.stream()
                .filter(product -> product.type.equals(ProductType.FRUIT))
                .filter(product -> product.price < 1000)
                .forEach(product -> System.out.print(product.name + " "));

        System.out.print("\n\nМолочн. изд -10%: ");
        products.stream()
                .filter(product -> product.type.equals(ProductType.DAIRY))
                .map(product -> new Product(product.name, (int)(product.price*0.9), ProductType.DAIRY))
                .forEach(product -> System.out.print(product.name + ", " + product.price + "   "));
    }


    static List<Product> products = List.of(
            new Product("Apple", 450, ProductType.FRUIT),
            new Product("Banana", 700, ProductType.FRUIT),
            new Product("Carrot", 300, ProductType.VEGETABLE),
            new Product("Tomato", 600, ProductType.VEGETABLE),
            new Product("Orange", 800, ProductType.FRUIT),
            new Product("Cucumber", 400, ProductType.VEGETABLE),
            new Product("Grapes", 1200, ProductType.FRUIT),
            new Product("Potato", 200, ProductType.VEGETABLE),
            new Product("Beef", 4500, ProductType.MEAT),
            new Product("Chicken", 2000, ProductType.MEAT),
            new Product("Pork", 3000, ProductType.MEAT),
            new Product("Milk", 500, ProductType.DAIRY),
            new Product("Cheese", 1500, ProductType.DAIRY),
            new Product("Yogurt", 800, ProductType.DAIRY)
    );

    record Product(String name, int price, ProductType type) {
    }

    enum ProductType {FRUIT, VEGETABLE, MEAT, DAIRY}
}
