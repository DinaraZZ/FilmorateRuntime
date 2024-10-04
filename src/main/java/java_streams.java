import java.util.Comparator;
import java.util.List;

public class java_streams {
    public static void main(String[] args) {
        // 1
        /*List<String> languages = List.of("Java", "Php", "Javascript", "Python");
        List<String> result = languages.stream()
                .filter(language -> language.length() >= 4)
                .map(language -> language.toUpperCase())
                .toList();
        System.out.println(result);*/


        // 2
        /*System.out.print("Фрукты до 1000: ");
        products.stream()
                .filter(product -> product.type.equals(ProductType.FRUIT))
                .filter(product -> product.price < 1000)
                .forEach(product -> System.out.print(product.name + " "));*/

        // 3
        /*System.out.print("\n\nМолочн. изд -10%: ");
        products.stream()
                .filter(product -> product.type.equals(ProductType.DAIRY))
                .map(product -> new Product(product.name, (int)(product.price*0.9), ProductType.DAIRY))
                .forEach(product -> System.out.print(product.name + ", " + product.price + "   "));*/

        // 4
        // Найдите все фрукты, у которых цена меньше 1000 тенге, увеличьте цену каждого из них на 20%,
        // отсортируйте их по возрастанию цены и соберите результаты в новый список продуктов
        List<Product> result = products.stream()
                .filter(product -> product.price < 1000 && product.type.equals(ProductType.FRUIT))
                .map(product -> new Product(product.name, (int) (product.price * 1.2), ProductType.FRUIT))
                .sorted(Comparator.comparingInt(Product::price))
                .toList();
        System.out.println(result);

        // 5
        // Найдите общую стоимость всех мясных продуктов, цена которых после скидки в 10% больше
        // 2000 тенге, отсортируйте их по убыванию цены и выведите названия вместе с их новыми ценами
        products.stream()
                .filter(product -> product.type.equals(ProductType.MEAT))
                .map(product -> new Product(product.name, (int) (product.price * 0.9), ProductType.MEAT))
                .filter(product -> product.price > 2000)
                .sorted((p1, p2) -> p2.price - p1.price)
                .forEach(product -> System.out.print(product.name + ", " + product.price + "\n"));
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
