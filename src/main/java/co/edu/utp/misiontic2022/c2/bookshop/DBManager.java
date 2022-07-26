package co.edu.utp.misiontic2022.c2.bookshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBManager implements AutoCloseable {
    private Connection connection;

    public DBManager() throws SQLException {
        connect();
    }

    private void connect() throws SQLException {
        //Salia error por que me falto la e en sqlite
        //Cambie la ruta para que pudieramos tener acceso a la base de datos
        var ruta="jdbc:sqlite:C:/Users/Ana Maria/Desktop/MISIONTIC 2022/Ruta 2/CICLO 2/Semana_4/G50/clase11-bookshop/BookShop.db";
        connection= DriverManager.getConnection(ruta);
    }

    /**
     * Close the connection to the database if it is still open.
     *
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = null;
    }

     /**
     * Return the number of units in stock of the given book.
     *
     * @param book The book object.
     * @return The number of units in stock, or 0 if the book does not
     *         exist in the database.
     * @throws SQLException If somthing fails with the DB.
     */
    public int getStock(Book book) throws SQLException {
        return getStock(book.getId());
    }

    /**
     * Return the number of units in stock of the given book.
     *
     * @param bookId The book identifier in the database.
     * @return The number of units in stock, or 0 if the book does not
     *         exist in the database.
     */
    public int getStock(int bookId) throws SQLException {
        // TODO: program this method
        int resp=0;
        try(var pstmt=connection.prepareStatement("Select existencia From unidades where id_unidades=?")){
        pstmt.setInt(1,bookId);
        try(var rs=pstmt.executeQuery()){
            if(rs.next()){
                resp=rs.getInt("existencia");
            }
        }
        }
        return resp;
    }

    /**
     * Search book by ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return The Book object, or null if not found.
     * @throws SQLException If somthing fails with the DB.
     */
    public Book searchBook(String isbn) throws SQLException {
        // TODO: program this method
        Book resp=null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt=connection.createStatement();
            rs=stmt.executeQuery("Select * From Book WHERE isbn='"+isbn+"'");
            if(rs.next()){
            resp=new Book();
            resp.setId(rs.getInt("id"));
            resp.setIsbn(rs.getString("isbn"));
            resp.setTitle(rs.getString("title"));
            resp.setYear(rs.getInt("year"));
        }
        }finally{
            if (rs != null){
                rs.close();
                }
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return resp;
    }

    /**
     * Sell a book.
     *
     * @param book The book.
     * @param units Number of units that are being sold.
     * @return True if the operation succeeds, or false otherwise
     *         (e.g. when the stock of the book is not big enough).
     * @throws SQLException If somthing fails with the DB.
     */
    public boolean sellBook(Book book, int units) throws SQLException {
        return sellBook(book.getId(), units);
    }

    /**
     * Sell a book.
     *
     * @param book The book's identifier.
     * @param units Number of units that are being sold.
     * @return True if the operation succeeds, or false otherwise
     *         (e.g. when the stock of the book is not big enough).
     * @throws SQLException If something fails with the DB.
     */
    public boolean sellBook(int book, int units) throws SQLException {
        // TODO: program this method
        return false;
    }

    /**
     * Return a list with all the books in the database.
     *
     * @return List with all the books.
     * @throws SQLException If something fails with the DB.
     */
    public List<Book> listBooks() throws SQLException {
        List<Book> resp= new ArrayList<>();
        try(var stmt=connection.createStatement();
        var rs=stmt.executeQuery("Select * From Book")){
            while (rs.next()) {
                var objBook=new Book();
                objBook.setId(rs.getInt("id"));
                objBook.setIsbn(rs.getString("isbn"));
                objBook.setTitle(rs.getString("title"));
                objBook.setYear(rs.getInt("year"));
                resp.add(objBook);
                }
        }
        return resp;
    }
}
