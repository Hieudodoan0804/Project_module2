package ra.warehouse.design;

import java.util.Scanner;

public interface IProduct {
    float MIN_INTEREST_RATE = 0.2F;
    void inputData(Scanner scanner);
    void displayData();
    Double calProfit();
}
