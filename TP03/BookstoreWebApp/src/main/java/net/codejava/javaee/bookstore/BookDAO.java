package net.codejava.javaee.bookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * BookDAO.java
 * This DAO class provides CRUD database operations for the table book
 * in the database.
 * @author www.codejava.net
 */
public class BookDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection; // Esta variável será inicializada em connect()

    // Construtor que recebe as credenciais
    public BookDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername; // CORRIGIDO: Passa o parâmetro, não a string "root" diretamente
        this.jdbcPassword = jdbcPassword;
    }

    // Método para abrir a conexão
    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                // CORRIGIDO: Usar o driver mais recente para MySQL Connector/J 8.x
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // É importante lançar uma exceção mais informativa ou envolver uma RuntimeException
                // para que o problema seja capturado pelo servidor.
                throw new SQLException("Driver JDBC MySQL não encontrado.", e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!"); // Para depuração
        }
    }

    // Método para fechar a conexão
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
            System.out.println("Conexão com o banco de dados fechada."); // Para depuração
        }
    }

    /**
     * Insere um novo livro no banco de dados.
     * @param book O objeto Book a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public boolean insertBook(Book book) throws SQLException {
        String sql = "INSERT INTO book (title, author, price) VALUES (?, ?, ?)";
        PreparedStatement statement = null; // Declare statement aqui para o finally
        try {
            connect(); // Abre a conexão
            statement = jdbcConnection.prepareStatement(sql);
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setFloat(3, book.getPrice());

            boolean rowInserted = statement.executeUpdate() > 0;
            System.out.println("Livro '" + book.getTitle() + "' inserido com sucesso."); // Para depuração
            return rowInserted;
        } finally {
            if (statement != null) { statement.close(); } // Garante que o statement seja fechado
            disconnect(); // Fecha a conexão
        }
    }

    /**
     * Lista todos os livros do banco de dados.
     * @return Uma lista de objetos Book.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Book> listAllBooks() throws SQLException {
        List<Book> listBook = new ArrayList<>();
        String sql = "SELECT * FROM book";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connect(); // Abre a conexão
            statement = jdbcConnection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                float price = resultSet.getFloat("price");

                Book book = new Book(id, title, author, price);
                listBook.add(book);
            }
            System.out.println("Lista de livros obtida com sucesso."); // Para depuração
            return listBook;
        } finally {
            if (resultSet != null) { resultSet.close(); }
            if (statement != null) { statement.close(); }
            disconnect(); // Fecha a conexão
        }
    }

    /**
     * Exclui um livro do banco de dados pelo seu ID.
     * @param book O objeto Book com o ID a ser excluído.
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public boolean deleteBook(int id) throws SQLException { // Mude o parâmetro para int id, se o ControllerServlet passar apenas o ID
        String sql = "DELETE FROM book where book_id = ?";
        PreparedStatement statement = null;
        try {
            connect(); // Abre a conexão
            statement = jdbcConnection.prepareStatement(sql);
            statement.setInt(1, id); // Usa o ID diretamente

            boolean rowDeleted = statement.executeUpdate() > 0;
            System.out.println("Livro com ID " + id + " excluído."); // Para depuração
            return rowDeleted;
        } finally {
            if (statement != null) { statement.close(); }
            disconnect(); // Fecha a conexão
        }
    }

    /**
     * Atualiza as informações de um livro existente.
     * @param book O objeto Book com as informações atualizadas.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public boolean updateBook(Book book) throws SQLException {
        String sql = "UPDATE book SET title = ?, author = ?, price = ? WHERE book_id = ?";
        PreparedStatement statement = null;
        try {
            connect(); // Abre a conexão
            statement = jdbcConnection.prepareStatement(sql);
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setFloat(3, book.getPrice());
            statement.setInt(4, book.getId());

            boolean rowUpdated = statement.executeUpdate() > 0;
            System.out.println("Livro com ID " + book.getId() + " atualizado."); // Para depuração
            return rowUpdated;
        } finally {
            if (statement != null) { statement.close(); }
            disconnect(); // Fecha a conexão
        }
    }

    /**
     * Retorna um livro específico pelo seu ID.
     * @param id O ID do livro a ser buscado.
     * @return O objeto Book encontrado, ou null se não for encontrado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Book getBook(int id) throws SQLException {
        Book book = null;
        String sql = "SELECT * FROM book WHERE book_id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connect(); // Abre a conexão
            statement = jdbcConnection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                float price = resultSet.getFloat("price");

                book = new Book(id, title, author, price);
                System.out.println("Livro com ID " + id + " encontrado: " + title); // Para depuração
            } else {
                System.out.println("Livro com ID " + id + " não encontrado."); // Para depuração
            }
            return book;
        } finally {
            if (resultSet != null) { resultSet.close(); }
            if (statement != null) { statement.close(); }
            disconnect(); // Fecha a conexão
        }
    }
}