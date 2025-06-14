package com.example.management.service;

import java.io.InputStream;

public interface GeneratorService {

    /**
     * Tạo ISBN duy nhất cho sách mới
     * @return ISBN string theo format "978-1-xxxx-xxxx-x"
     */
    String generateIsbn();

    /**
     * Kiểm tra xem ISBN đã tồn tại hay chưa
     * @param isbn ISBN string theo format "978-1-xxxx-xxxx-x"
     * @return true nếu ISBN đã tồn tại, false nếu chưa tồn tại
     */
    boolean isIsbnExists(String isbn);

    /**
     * Tạo QR Code unique cho sách
     * @param bookId ID của sách
     * @param title Tên sách
     * @param isbn ISBN của sách
     * @return URL đường dẫn đến ảnh QR Code
     */
    String generateUniqueQRCode(Long bookId, String title, String isbn);

    /**
     * Tạo mã QR temporary cho sách mới (chưa có ID)
     * @param title Tên sách
     * @param author Tác giả
     * @param isbn ISBN
     * @return Mã QR temporary
     */
    String generateTemporaryQRCode(String title, String author, String isbn);

    /**
     * Cập nhật QR Code từ temporary thành final
     * @param bookId ID sách
     * @param title Tên sách
     * @param isbn ISBN
     * @param temporaryQRCode Mã QR tạm thời
     * @return URL đường dẫn ảnh QR Code final
     */
    String updateToFinalQRCode(Long bookId, String title, String isbn, String temporaryQRCode);

    /**
     * Kiểm tra QR Code đã tồn tại chưa
     * @param qrCode Mã QR
     * @return true nếu đã tồn tại
     */
    boolean isQRCodeExists(String qrCode);

    /**
     * Xóa file QR Code ảnh
     * @param qrCodeUrl URL của QR Code
     */
    void deleteQRCodeImage(String qrCodeUrl);

    /**
     * Tạo ảnh QR Code và lưu vào storage
     */
   String createQRCodeImage(String qrData, Long bookId);

    /**
     * Chèn QR vào ảnh bìa và upload lên MinIO, trả về URL ảnh đã upload
     */
     String addQRCodeAndUploadToMinio(InputStream inputStream, String qrData, String objectName);
}
