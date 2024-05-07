public class Main {
    public static void main(String[] args) {


        ProductService productService = new ProductService();
        CartService cartService = new CartService();


        ProductManager productManager = new ProductManager(productService, cartService);


        productManager.run();




    }
}