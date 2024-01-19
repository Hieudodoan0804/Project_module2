package ra.warehouse.entity;

import ra.warehouse.design.ICategory;
import ra.warehouse.program.Store;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Category implements ICategory,Serializable {
    private int id;
    private String name;
    private String description;
    private boolean status;

    public Category() {
    }

    public Category(int id, String name, String description, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public void inputData(Scanner scanner) {
        inputCategoryId(scanner);
        inputCategoryName(scanner);
        inputDescription(scanner);
        inputStatus(scanner);

    }

    public void inputCategoryId(Scanner scanner) {
        System.out.println("Nhập mã danh mục:");
        do {
            try {
                this.id = Integer.parseInt(scanner.nextLine());

                if (this.id > 0) {
                    boolean isUnique = true;
                    if (Store.categories != null) {
                        for (Category category : Store.categories) {
                            if (category.getId() == this.id) {
                                isUnique = false;
                                System.out.println("Mã danh mục bị trùng, vui lòng nhập lại!");
                                break;
                            }
                        }
                    }
                    if (isUnique) {
                        break;
                    }
                } else {
                    System.out.println("Mã danh mục phải lớn hơn 0.");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Mã danh mục phải là số nguyên!");
            }
        } while (true);
    }

    public void inputCategoryName(Scanner scanner) {
        System.out.println("Nhập tên danh mục:");
        do {
            this.name = scanner.nextLine();
            if (isCategoryNameValid(this.name)) {
                boolean isUnique = true;

                if (Store.categories != null) {
                    for (Category category : Store.categories) {
                        if (category.getName().equalsIgnoreCase(this.name)) {
                            isUnique = false;
                            System.out.println("Tên danh mục bị trùng, vui lòng nhập lại!");
                            break;
                        }
                    }
                }

                if (isUnique) {
                    break;
                }
            }
        } while (true);
    }
    private static boolean  isCategoryNameValid(String name) {
        if (name.length() >= 6 && name.length() <= 30) {
            return true;
        } else {
            System.out.println("Tên danh mục phải từ 6-30 ký tự, vui lòng nhập lại!");
            return false;
        }
    }

    public void inputDescription(Scanner scanner) {
        System.out.println("Nhập mô tả danh mục:");
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
        System.out.println("Nhập trạng thái danh mục:");
        do {
            String input = scanner.nextLine().toLowerCase();
            if ("true".equals(input) || "false".equals(input)) {
                this.status = Boolean.parseBoolean(input);
                break;
            } else {
                System.out.println("Vui lòng chỉ nhập 'true' hoặc 'false'");
            }
        } while (true);
    }

    @Override
    public void displayData() {
        System.out.printf("Mã danh mục: %d | Tên danh mục: %s | Mô tả: %s | Trạng thái: %s\n",
                this.id, this.name, this.description, this.status ? "Hoạt động" : "Không hoạt động");
    }

    public static List<Category> readFromFile() {
        List<Category> categories = null;
        File file = new File("Categories.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            categories = (List<Category>) ois.readObject();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return categories;
    }

    public static void writeToFile(List<Category> categories) {
        File file = new File("Categories.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(categories);
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
