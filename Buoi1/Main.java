import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Book> listBook = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int chon = 0;

        do {
            System.out.println("1. Them 1 cuon sach");
            System.out.println("2. Xoa 1 cuon sach");
            System.out.println("3. Thay doi cuon sach");
            System.out.println("4. Xuat thong tin tat ca cac cuon sach");
            System.out.println("5. Tim sach co tua de chua chu 'Lap trinh'");
            System.out.println("6. Lay toi da K cuon sach co gia <= P");
            System.out.println("7. Tim sach theo danh sach tac gia");
            System.out.println("0. Thoat");
            System.out.print("Chọn chuc nang: ");

            chon = Integer.parseInt(sc.nextLine());

            switch (chon) {
                case 1 -> { // Thêm sách
                    Book b = new Book();
                    b.input();
                    listBook.add(b);
                    System.out.println("Da them sach thanh cong!");
                }
                case 2 -> {
                    System.out.print("Nhap ID sach can xoa: ");
                    int idDel = Integer.parseInt(sc.nextLine());
                    boolean removed = listBook.removeIf(b -> b.getId() == idDel);
                    System.out.println(removed ? "Da xoa thanh cong!" : "Khong tim thay ID!");
                }
                case 3 -> {
                    System.out.print("Nhap ID sach can sua: ");
                    int idEdit = Integer.parseInt(sc.nextLine());
                    listBook.stream()
                            .filter(b -> b.getId() == idEdit)
                            .findFirst()
                            .ifPresentOrElse(
                                    b -> {
                                        System.out.println("Nhap thong tin moi:");
                                        b.input();
                                    },
                                    () -> System.out.println("Khong tim thay sach!"));
                }
                case 4 -> {
                    System.out.println("--- Danh sach sach ---");
                    listBook.forEach(Book::output);
                }
                case 5 -> {
                    System.out.println("--- Sach lap trinh ---");
                    listBook.stream()
                            .filter(b -> b.getTitle().toLowerCase().contains("lap trinh"))
                            .forEach(Book::output);
                }
                case 6 -> {
                    System.out.print("Nhap so luong K: ");
                    int k = Integer.parseInt(sc.nextLine());
                    System.out.print("Nhap muc gia P: ");
                    double p = Double.parseDouble(sc.nextLine());

                    System.out.println("--- Ket qua loc ---");
                    listBook.stream()
                            .filter(b -> b.getPrice() <= p)
                            .limit(k)
                            .forEach(Book::output);
                }
                case 7 -> {
                    System.out.print("Nhap cac tac gia: ");
                    String inputAuthors = sc.nextLine();

                    Set<String> targetAuthors = Arrays.stream(inputAuthors.split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet());

                    System.out.println("--- Sach cua cac tac gia da chon ---");
                    listBook.stream()
                            .filter(b -> targetAuthors.contains(b.getAuthor()))
                            .forEach(Book::output);
                }
                case 0 -> System.out.println("Thoat chuong trinh.");
                default -> System.out.println("Chuc nang khong hop le!");
            }
        } while (chon != 0);
    }
}