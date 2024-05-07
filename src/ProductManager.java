import java.util.Comparator;
import java.util.Scanner;

public class ProductManager {
    private ProductService productService;
    private CartService cartService;
    private Scanner scanner = new Scanner(System.in);

    public ProductManager(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    public void run() {
        getProductInfo();
        if (!productService.getProducts().isEmpty()) {
            System.out.println("Ürünler sıralanıyor...");
            sortProducts();
            addToCart();
            cartService.calculateTotal();
        } else {
            System.out.println("Hiç ürün girilmediği için işlem yapılamıyor.");
        }
    }

    private void getProductInfo() {

        int productCount;
        do {
            System.out.println("Kaç farklı ürün gireceksiniz? (en az 2)");
            productCount = scanner.nextInt();
            scanner.nextLine();
            if (productCount < 2) {
                System.out.println("En az 2 ürün girmelisiniz.");
            }
        } while (productCount < 2);

        for (int i = 0; i < productCount; i++) {
            System.out.println("Ürün " + (i + 1) + ":");
            System.out.println("Ürün adı:");
            String name ;
            while (true) {
                name = scanner.nextLine();
                if (name.length() <= 20) break;
                System.out.println("Lütfen 20 karakterden az ürün ismi giriniz !!!");
            }

            double price;
            while (true) {
                System.out.print("Fiyat (1-100 arası): ");
                price = scanner.nextDouble();
                scanner.nextLine();
                if (price >= 1 && price <= 100) break;
                System.out.println("Fiyat 1 ile 100 arasında olmalıdır.");
            }

            int stock;
            while (true) {
                System.out.print("Stok Miktarı (en az 1): ");
                stock = scanner.nextInt();
                scanner.nextLine();
                if (stock >= 1) break;
                System.out.println("Stok miktarı en az 1 olmalıdır.");
            }

            double rating;
            while (true) {
                System.out.print("Değerlendirme Puanı (0-5 arası): ");
                rating = scanner.nextDouble();
                scanner.nextLine();
                if (rating >= 0 && rating <= 5) break;
                System.out.println("Değerlendirme puanı 0-5 arasında olmalıdır.");
            }
            productService.addProduct(new Product(name, price, stock, rating));
        }
    }

    private void sortProducts() {
        Comparator<Product> comparator = chooseSorting();
        productService.sortProducts(comparator);
        System.out.println("Sıralanmış Ürünler:");
        productService.getProducts().forEach(System.out::println);
    }

    private Comparator<Product> chooseSorting() {
        Comparator<Product> comparator = null;
        while (comparator == null) {
            System.out.println("Ürünleri hangi kritere göre sıralamak istersiniz? (name/price/stock/rating)");
            String criterion = scanner.nextLine();
            System.out.println("Artan mı azalan mı? (Artan/Azalan)");
            String order = scanner.nextLine();

            switch (criterion) {
                case "name":
                    comparator = Comparator.comparing(Product::getName);
                    break;
                case "price":
                    comparator = Comparator.comparing(Product::getPrice);
                    break;
                case "stock":
                    comparator = Comparator.comparing(Product::getStock);
                    break;
                case "rating":
                    comparator = Comparator.comparing(Product::getRating);
                    break;
                default:
                    System.out.println("Geçersiz kriter, lütfen 'name', 'price', 'stock', veya 'rating' kullanın.");
                    continue;
            }
            if (order.equalsIgnoreCase("Artan")) {
                comparator = comparator.reversed();
            } else if (!order.equalsIgnoreCase("Azalan")) {
                System.out.println("Lütfen 'Artan' veya 'Azalan' olarak yanıt veriniz.");
                comparator = null;
            }
        }
        return comparator;
    }

    private void addToCart() {
        while (true) {
            System.out.print("Sepete ürün eklemek ister misiniz? (Evet/Hayır): ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("Hayır")) {
                break;
            } else if (!answer.equalsIgnoreCase("Evet")) {
                System.out.println("Lütfen 'Evet' veya 'Hayır' olarak yanıt veriniz.");
                continue;
            }

            System.out.print("Eklemek istediğiniz ürünün adı: ");
            String productName = scanner.nextLine();

            Product product = productService.getProducts().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(productName))
                    .findFirst()
                    .orElse(null);

            if (product == null) {
                System.out.println("Geçerli bir ürün adı giriniz. Mevcut ürünler:");
                productService.getProducts().forEach(p -> System.out.println(p.getName()));
                continue;
            }

            System.out.println("Eklemek istediğiniz adet (Mevcut stok: " + product.getStock() + "): ");
            int stock = scanner.nextInt();
            scanner.nextLine();

            if (stock > product.getStock()) {
                System.out.println("Stokta yeterli ürün bulunmamaktadır. Mevcut stok: " + product.getStock());
                continue;
            }

            cartService.addToCart(product, stock);
            System.out.println(product.getName() + " sepetinize eklendi.");
        }
    }
}
