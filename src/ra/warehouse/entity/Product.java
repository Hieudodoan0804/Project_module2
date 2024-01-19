package ra.warehouse.entity;

import ra.warehouse.design.IProduct;
import ra.warehouse.program.Store;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Product implements IProduct, Serializable {
    private String id;
    private String name;
    private double importPrice;
    private double exportPrice;
    private double profit;
    private String description;
    private boolean status;
    private int categoryId;

    public Product() {
    }

    public Product(String id, String name, double importPrice, double exportPrice, double profit, String description, boolean status, int categoryId) {
        this.id = id;
        this.name = name;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
        this.profit = profit;
        this.description = description;
        this.status = status;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(double exportPrice) {
        this.exportPrice = exportPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void inputData(Scanner scanner) {
        inputCategoryId(scanner);
        inputProductId(scanner);
        inputProductName(scanner);
        inputImportPrice(scanner);
        inputExportPrice(scanner);
        inputDescription(scanner);
        inputStatus(scanner);
    }

    public void inputProductId(Scanner scanner) {
        System.out.println("Nhập mã sản phẩm:");
        do {
            this.id = scanner.nextLine();
            if (this.id.matches("^P.{3}$")) {
                boolean isUnique = true;
                if (Store.products != null) {
                    for (Product product : Store.products) {
                        if (product.getId().toLowerCase().equalsIgnoreCase(this.id)) {
                            isUnique = false;
                            System.out.println("Mã sản phẩm bị trùng , vui lòng nhập lại!");
                            break;
                        }
                    }
                }
                if (isUnique) {
                    break;
                }
            } else {
                System.out.println("Mã sản phẩm phải gồm 4 ký tự , bắt đầu là P, vui lòng nhập lại!");
            }
        } while (true);
    }

    public void inputProductName(Scanner scanner) {
        System.out.println("Nhập tên sản phẩm:");
        do {
            this.name = scanner.nextLine();
            if (this.name.length() >= 6 && this.name.length() <= 30) {
                boolean isUnique = true;
                if (Store.products != null) {
                    for (Product product : Store.products) {
                        if (product.getName().toLowerCase().equalsIgnoreCase(this.name)) {
                            isUnique = false;
                            System.out.println("Tên sản phẩm bị trùng, vui lòng nhập lại!");
                            break;
                        }
                    }
                }
                if (isUnique) {
                    break;
                }
            } else {
                System.out.println("Tên sản phẩm phải từ 6-30 ký tự, vui lòng nhập lại!");
            }
        } while (true);
    }

    public void inputImportPrice(Scanner scanner) {
        System.out.println("Nhập giá sản phẩm nhập:");
        do {
            try {
                this.importPrice = Double.parseDouble(scanner.nextLine());
                if (this.importPrice > 0) {
                    break;
                } else {
                    System.out.println("Giá sản phẩm phải lớn hơn 0!");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Giá nhập phải có định dạng số thực, vui lòng nhập lại!");
            }
        } while (true);
    }

    public void inputExportPrice(Scanner scanner) {
        System.out.println("Nhập giá bán sản phẩm:");
        do {
            try {
                double exportPrice = Double.parseDouble(scanner.nextLine());
                double minExportPrice = this.importPrice * MIN_INTEREST_RATE;
                if (exportPrice > 0 && exportPrice >= minExportPrice + this.importPrice) {
                    this.exportPrice = exportPrice;
                    break;
                } else {
                    System.out.println("Giá bán phải lớn hơn 0 và ít nhất là " + MIN_INTEREST_RATE + " lần giá nhập (" + minExportPrice + ")");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Giá bán phải có định dạng số thực, vui lòng nhập lại!");
            }
        } while (true);
    }

    public void inputDescription(Scanner scanner) {
        System.out.println("Nhập mô tả sản phẩm:");
        do {
            this.description = scanner.nextLine().trim();
            if (!this.description.isEmpty()) {
                break;
            } else {
                System.out.println("Mô tả danh mục không được bỏ trống!");
            }
        } while (true);
    }

    public void inputStatus(Scanner scanner) {
        System.out.println("Nhập trạng thái sản phẩm:");
        do {
            String input = scanner.nextLine().toLowerCase();
            if ("true".equals(input) || "false".equals(input)) {
                this.status = Boolean.parseBoolean(input);
                break;
            } else {
                System.out.println("Vui lòng chỉ nhập 'true' hoặc 'false'!");
            }
        } while (true);
    }

    public void inputCategoryId(Scanner scanner) {
        System.out.println("Nhập mã danh mục:");
        do {
            try {
                this.categoryId = Integer.parseInt(scanner.nextLine());
                if (isValidCategoryId()) {
                    break;
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Mã danh mục phải là số nguyên!");
            }
        } while (true);
    }

    private boolean isValidCategoryId() {
        if (Store.categories != null) {
            for (Category category : Store.categories) {
                if (category.getId() == this.categoryId) {
                    System.out.println("Đã chọn danh mục: " + category.getName());
                    return true;
                }
            }
            System.out.println("Mã danh mục không tồn tại!");
        }
        return false;
    }

    @Override
    public void displayData() {
        System.out.printf("Danh mục: %s | Mã sản phẩm: %s | Tên sản phẩm: %s | Giá nhập sản phẩm: %.2f | Giá bán sản phẩm: %.2f | Lợi nhuận: %.2f | Mô tả: %s | Trạng thái: %s\n",
                getCategoryName(), this.id, this.name, this.importPrice, this.exportPrice, calProfit(), this.description, getStatusString());
    }

    private String getStatusString() {
        return this.status ? "Còn hàng" : "Ngừng kinh doanh";
    }

    private String getCategoryName() {
        for (Category category : Store.categories) {
            if (category.getId() == this.categoryId) {
                return category.getName();
            }
        }
        return "Không xác định";
    }

    @Override
    public Double calProfit() {
        return this.profit = this.exportPrice - this.importPrice;
    }

    public static List<Product> readFromFile() {
        List<Product> products = null;
        File file = new File("Products.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            products = (List<Product>) ois.readObject();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return products;
    }

    public static void writeToFile(List<Product> products) {
        File file = new File("Products.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(products);
            oos.flush();
            oos.close();
            fos.close();
        } catch (FileNotFoundException | NotSerializableException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
