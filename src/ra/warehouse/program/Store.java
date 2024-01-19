package ra.warehouse.program;

import ra.warehouse.entity.Category;
import ra.warehouse.entity.Product;

import java.util.*;
import java.util.stream.Collectors;

public class Store {
    public static List<Category> categories = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Store store = new Store();
        store.menu(scanner);
    }

    public void menu(Scanner scanner) {
        do {
            System.out.println("===== QUẢN LÝ KHO =====");
            System.out.println("1. Quản lý danh mục");
            System.out.println("2. Quản lý sản phẩm");
            System.out.println("3. Thoát");
            System.out.print("Chọn: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        Store.categoryManagement(scanner);
                        break;
                    case 2:
                        Store.productManagement(scanner);
                        break;
                    case 3:
                        System.exit(0);
                        System.out.println("Kết thúc chương trình.");
                        break;
                    default:
                        System.out.println("Chọn không hợp lệ. Vui lòng chọn lại.");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Chọn phải là số nguyên, vui lòng nhập lại!");
            }
        } while (true);
    }

    public static void categoryManagement(Scanner scanner) {
        boolean isExist = true;
        if (Category.readFromFile() != null) {
            categories = Category.readFromFile();
        }
        do {
            System.out.println("===== QUẢN LÝ DANH MỤC =====");
            System.out.println("1. Thêm mới danh mục");
            System.out.println("2. Cập nhật danh mục");
            System.out.println("3. Xóa danh mục");
            System.out.println("4. Tìm kiếm danh mục theo tên danh mục");
            System.out.println("5. Thống kê số lượng sp đang có trong danh mục");
            System.out.println("6. Quay lại");
            System.out.print("Chọn: ");
            int categoryChoice = Integer.parseInt(scanner.nextLine());
            switch (categoryChoice) {
                case 1:
                    Store.addNewCategory(scanner);
                    break;
                case 2:
                    Store.updateCategory(scanner);
                    break;
                case 3:
                    Store.deleteCategory(scanner);
                    break;
                case 4:
                    Store.searchCategoryByName(scanner);
                    break;
                case 5:
                    Store.displayProductCountInCategory(scanner);
                    break;
                case 6:
                    isExist = false;
                    System.out.println("Quay lại menu chính.");
                    break;
                default:
                    System.out.println("Chọn không hợp lệ. Vui lòng chọn lại.");
            }

        } while (isExist);
    }

    public static void addNewCategory(Scanner scanner) {
        System.out.println("Nhập số danh mục cần nhập thông tin:");
        int numberOfCategory = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numberOfCategory; i++) {
            Category newCategory = new Category();
            newCategory.inputData(scanner);
            categories.add(newCategory);
            System.out.println("Thêm mới danh mục thành công!");
        }
        Category.writeToFile(categories);
    }

    public static void updateCategory(Scanner scanner) {
        System.out.println("Nhập vào mã danh mục cần cập nhật thông tin:");
        int categoryIdUpdate = Integer.parseInt(scanner.nextLine());
        int indexUpdate = getIndexByCategoryId(categoryIdUpdate);
        if (indexUpdate >= 0) {
            // Tìm thấy danh mục cần cập nhật --> Tiến hành cập nhật
            Category updateCategory = new Category();
            updateCategory.setId(categoryIdUpdate);
            updateCategory.inputCategoryName(scanner);
            updateCategory.inputDescription(scanner);
            updateCategory.inputStatus(scanner);
            categories.set(indexUpdate, updateCategory);
            System.out.println("Cập nhật danh mục thành công!");
        } else {
            System.out.printf("Danh mục %s không tìm thấy, vui lòng nhập lại\n", categoryIdUpdate);
        }
        Category.writeToFile(categories);
    }

    public static int getIndexByCategoryId(int categoryId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == categoryId) {
                return i;
            }
        }
        // Không tìm thấy mã danh mục trong categories
        return -1;
    }

    private static Category getCategoryById(int categoryId) {
        for (Category category : Store.categories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    public static void deleteCategory(Scanner scanner) {
        System.out.println("Nhập vào mã danh mục cần xoá:");
        int categoryIdDelete = Integer.parseInt(scanner.nextLine());
        int indexDelete = getIndexByCategoryId(categoryIdDelete);
        if (indexDelete >= 0) {
            // Tìm thấy danh mục cần xoá --> Tiến hành xoá
            if (checkProductsInCategory(categoryIdDelete)) {
                System.out.println("Danh mục đang có sản phẩm tham chiếu. Không thể xóa.");
            } else {
                categories.remove(indexDelete);
                System.out.println("Đã xóa danh mục có mã " + categoryIdDelete);
            }
        } else {
            System.out.printf("Danh mục %s không tìm thấy, vui lòng nhập lại\n", categoryIdDelete);
        }
        Category.writeToFile(categories);
    }

    public static boolean checkProductsInCategory(int categoryId) {
        for (Product product : products) {
            if (product.getCategoryId() == categoryId) {
                return true; // Có sản phẩm tham chiếu
            }
        }
        return false; // Không có sản phẩm tham chiếu
    }

    public static void searchCategoryByName(Scanner scanner) {
        System.out.println("Nhập tên danh mục cần tìm kiếm:");
        String categoryName = scanner.nextLine();
        boolean found = false;
        for (Category category : categories) {
            if (category.getName().toLowerCase().contains(categoryName.toLowerCase())) {
                category.displayData();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Không tìm thấy danh mục nào có tên chứa \"" + categoryName + "\".");
        }
    }

    public static void displayProductCountInCategory(Scanner scanner) {
        System.out.println("Nhập vào mã danh mục cần thống kê số lượng sản phẩm:");
        int categoryIdStatistic = Integer.parseInt(scanner.nextLine());
        int indexStatistic = getIndexByCategoryId(categoryIdStatistic);
        if (indexStatistic >= 0) {
            int productCount = getProductCountInCategory(categoryIdStatistic);
            System.out.println("Số lượng sản phẩm trong danh mục '" + categories.get(indexStatistic).getName() + "': " + productCount);
        } else {
            System.out.printf("Danh mục %s không tìm thấy, vui lòng nhập lại\n", categoryIdStatistic);
        }
    }

    private static int getProductCountInCategory(int categoryId) {
        int count = 0;
        for (Product product : products) {
            if (product.getCategoryId() == categoryId) {
                count++;
            }
        }
        return count;
    }

    public static void productManagement(Scanner scanner) {
        boolean isExist = true;
        if (Product.readFromFile() != null) {
            products = Product.readFromFile();
        }
        do {
            System.out.println("===== QUẢN LÝ SẢN PHẨM =====");
            System.out.println("1. Thêm mới sản phẩm");
            System.out.println("2. Cập nhật sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Hiển thị sản phẩm theo tên A-Z");
            System.out.println("5. Hiển thị sản phẩm theo lợi nhuận từ cao-thấp");
            System.out.println("6. Tìm kiếm sản phẩm");
            System.out.println("7. Quay lại");
            System.out.print("Chọn: ");
            int productChoice = Integer.parseInt(scanner.nextLine());
            switch (productChoice) {
                case 1:
                    Store.addNewProduct(scanner);
                    break;
                case 2:
                    Store.updateProduct(scanner);
                    break;
                case 3:
                    Store.deleteProduct(scanner);
                    break;
                case 4:
                    Store.displayProductsByNameAZ();
                    break;
                case 5:
                    Store.displayProductsByProfit();
                    break;
                case 6:
                    Store.searchProduct(scanner);
                    break;
                case 7:
                    isExist = false;
                    System.out.println("Quay lại menu chính.");
                    break;
                default:
                    System.out.println("Chọn không hợp lệ. Vui lòng chọn lại.");
            }
        } while (isExist);
    }

    public static void addNewProduct(Scanner scanner) {
        System.out.println("Nhập số sản phẩm cần nhập thông tin:");
        int numberOfProducts = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numberOfProducts; i++) {
            Product newProduct = new Product();
            newProduct.inputData(scanner);
            products.add(newProduct);
            System.out.println("Thêm mới sản phẩm thành công!");
        }
        Product.writeToFile(products);
    }

    public static void updateProduct(Scanner scanner) {
        System.out.println("Nhập vào mã sản phẩm cần cập nhật thông tin:");
        String productIdUpdate = scanner.nextLine();
        int indexUpdate = getIndexByProductId(productIdUpdate);
        if (indexUpdate >= 0) {
            // Tìm thấy sản phẩm cần cập nhật --> Tiến hành cập nhật
            Product updatedProduct = new Product();
            updatedProduct.setId(productIdUpdate);
            updatedProduct.inputProductName(scanner);
            updatedProduct.inputImportPrice(scanner);
            updatedProduct.inputExportPrice(scanner);
            updatedProduct.inputDescription(scanner);
            updatedProduct.inputStatus(scanner);
            updatedProduct.inputCategoryId(scanner);
            //Lưu thông tin cập nhật vào danh sách sản phẩm
            products.set(indexUpdate, updatedProduct);
            System.out.println("Cập nhật sản phẩm thành công!");
            Product.writeToFile(products);
        } else {
            System.out.printf("Sản phẩm có mã %s không tìm thấy, vui lòng nhập lại\n", productIdUpdate);
        }
    }

    private static int getIndexByProductId(String productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(productId)) {
                return i;
            }
        }
        // Không tìm thấy mã sản phẩm trong products
        return -1;
    }

    private static Product findProductById(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public static void deleteProduct(Scanner scanner) {
        System.out.print("Nhập mã sản phẩm cần xóa: ");
        String productId = scanner.nextLine();

        // Kiểm tra xem sản phẩm có tồn tại hay không
        Product productToDelete = findProductById(productId);
        if (productToDelete == null) {
            System.out.println("Không tìm thấy sản phẩm có mã " + productId);
            return;
        }

        // Hiển thị thông tin sản phẩm trước khi xóa
        System.out.println("Thông tin sản phẩm:");
        productToDelete.displayData();

        // Xóa sản phẩm và lưu lại vào file
        products.remove(productToDelete);
        Product.writeToFile(products);
        System.out.println("Đã xóa thành công sản phẩm có mã " + productId);
    }

    public static void displayProductsByNameAZ() {
        if (products == null || products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống.");
            return;
        }
        Map<Integer, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategoryId));

        productsByCategory.forEach((categoryId, categoryProducts) -> {
            Category category = getCategoryById(categoryId);
            System.out.println("Danh mục: " + category.getName());

            categoryProducts.stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .forEach(Product::displayData);
        });
    }
    public static void displayProductsByProfit() {
        if (products == null || products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống.");
            return;
        }
        products.sort(Comparator.comparing(Product::getProfit).reversed());

        // Hiển thị thông tin các sản phẩm
        System.out.println("Danh sách sản phẩm theo lợi nhuận từ cao đến thấp:");
        for (Product product : products) {
            product.displayData();
        }
    }

    public static void searchProduct(Scanner scanner) {
        System.out.print("Nhập từ khóa tìm kiếm: ");
        String keyword = scanner.nextLine().toLowerCase();
        boolean found = false;
        System.out.println("Kết quả tìm kiếm sản phẩm:");
        for (Product product : products) {
            if (productContainsKeyword(product, keyword)) {
                product.displayData();
                found = true;
            }
        }
        if (!found && products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống.");
        } else if (!found) {
            System.out.println("Không tìm thấy sản phẩm nào thỏa mãn điều kiện tìm kiếm.");
        }
    }

    private static boolean productContainsKeyword(Product product, String keyword) {
        // Kiểm tra xem sản phẩm có chứa từ khóa trong tên, giá nhập hoặc giá xuất không
        return product.getName().toLowerCase().contains(keyword) ||
                String.valueOf(product.getImportPrice()).toLowerCase().contains(keyword) ||
                String.valueOf(product.getExportPrice()).toLowerCase().contains(keyword);
    }

}
