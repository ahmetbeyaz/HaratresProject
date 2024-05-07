import java.util.ArrayList;
import java.util.List;

public class CartService {
    private List<Product> cart = new ArrayList<>();

    public void addToCart(Product product, int stock) {
        cart.add(new Product(product.getName(), product.getPrice(), stock, product.getRating()));
        product.setStock(product.getStock() - stock);
    }

    public void calculateTotal() {
        double total = 0;
        if (cart.isEmpty()) return;


        List<Double> originalPrices = new ArrayList<>();
        for (Product product : cart) {
            originalPrices.add(product.getPrice());
        }


        for (int i = 1; i < cart.size(); i++) {
            double previousPrice = originalPrices.get(i - 1);
            double currentPrice = originalPrices.get(i);

            if (previousPrice > currentPrice) {
                double newPrice = cart.get(i - 1).getPrice() - currentPrice;
                cart.get(i - 1).setPrice(Math.max(newPrice, 0));
            }
        }


        for (Product product : cart) {
            double productTotal = product.getPrice() * product.getStock();
            System.out.println(product.getName() + " - Adet: " + product.getStock() + ", Toplam Fiyat: " + String.format("%.2f", productTotal));
            total += productTotal;
        }

        System.out.println("Sepetin Toplamı: " + String.format("%.2f", total));

    }

    public List<Product> getCart() {
        return cart;
    }
}
