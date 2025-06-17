package net.codejava.javaee.bookstore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // Importar esta anotação
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Importar para usar a sessão

/**
 * ControllerServlet.java
 * This servlet acts as a page controller for the application, handling all
 * requests from the user.
 * @author www.codejava.net
 */
@WebServlet("/") // Mapeia o Servlet para a raiz da aplicação (contexto)
public class ControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;

    // Método chamado quando o Servlet é inicializado
    public void init() {
        // Obtém os parâmetros de contexto do web.xml
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        // Instancia o BookDAO passando os parâmetros
        bookDAO = new BookDAO(jdbcURL, jdbcUsername, jdbcPassword);
        System.out.println("ControllerServlet inicializado. BookDAO criado com URL: " + jdbcURL); // Para depuração
    }

    // Lida com requisições POST (encaminha para GET para simplificar, como no tutorial)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // Lida com requisições GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Usa getServletPath() para obter a parte da URL após o contexto (ex: /new, /credits)
        String action = request.getServletPath();
        System.out.println("GET Request Action: " + action); // Para depuração

        HttpSession session = request.getSession(); // Obtém a sessão para mensagens

        try {
            switch (action) {
                case "/new": // Exibir formulário para novo livro
                    showNewForm(request, response);
                    break;
                case "/insert": // Processar inserção de livro
                    insertBook(request, response, session); // Passa a sessão
                    break;
                case "/delete": // Processar exclusão de livro
                    deleteBook(request, response, session); // Passa a sessão
                    break;
                case "/edit": // Exibir formulário para editar livro
                    showEditForm(request, response, session); // Passa a sessão
                    break;
                case "/update": // Processar atualização de livro
                    updateBook(request, response, session); // Passa a sessão
                    break;
                case "/credits": // Exibir página de créditos
                    showCreditsPage(request, response);
                    break;
                case "/list": // Listar todos os livros (URL explícita)
                default:      // Padrão (URL raiz '/')
                    listBook(request, response, session); // Passa a sessão
                    break;
            }
        } catch (SQLException ex) {
            // Em caso de erro SQL, loga e redireciona para a página de erro ou mostra mensagem
            System.err.println("Erro SQL no Servlet: " + ex.getMessage());
            session.setAttribute("errorMessage", "Erro no banco de dados: " + ex.getMessage());
            response.sendRedirect("list"); // Redireciona para a lista com a mensagem de erro
        }
    }

    /**
     * Exibe a lista de livros, buscando-os do DAO.
     */
    private void listBook(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws SQLException, IOException, ServletException {
        try {
            List<Book> listBook = bookDAO.listAllBooks();
            request.setAttribute("listBook", listBook);

            // Recupera mensagens da sessão para exibir uma única vez
            String message = (String) session.getAttribute("message");
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (message != null) {
                request.setAttribute("message", message);
                session.removeAttribute("message"); // Remove da sessão após usar
            }
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage"); // Remove da sessão após usar
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
            dispatcher.forward(request, response);
            System.out.println("Encaminhado para BookList.jsp."); // Para depuração
        } catch (SQLException e) {
            System.err.println("Erro SQL em listBook: " + e.getMessage());
            throw e; // Relança para ser pego pelo bloco catch principal
        }
    }

    /**
     * Exibe o formulário vazio para um novo livro.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
        dispatcher.forward(request, response);
        System.out.println("Encaminhado para BookForm.jsp (Novo Livro)."); // Para depuração
    }

    /**
     * Exibe o formulário preenchido para editar um livro existente.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Book existingBook = bookDAO.getBook(id);

            if (existingBook != null) {
                request.setAttribute("book", existingBook);
                RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
                dispatcher.forward(request, response);
                System.out.println("Encaminhado para BookForm.jsp (Editar Livro ID: " + id + ")."); // Para depuração
            } else {
                session.setAttribute("errorMessage", "Livro com ID " + id + " não encontrado para edição.");
                response.sendRedirect("list");
                System.err.println("Erro: Livro com ID " + id + " não encontrado para showEditForm."); // Para depuração
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID de livro inválido para edição.");
            response.sendRedirect("list");
            System.err.println("Erro de formato de número em showEditForm: " + e.getMessage()); // Para depuração
        } catch (SQLException e) {
             System.err.println("Erro SQL em showEditForm: " + e.getMessage());
             throw e; // Relança para ser pego pelo bloco catch principal
        }
    }

    /**
     * Insere um novo livro.
     */
    private void insertBook(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws SQLException, IOException {
        try {
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            float price = Float.parseFloat(request.getParameter("price"));

            Book newBook = new Book(title, author, price);
            boolean success = bookDAO.insertBook(newBook);
            if (success) {
                session.setAttribute("message", "Livro '" + title + "' adicionado com sucesso!");
            } else {
                session.setAttribute("errorMessage", "Falha ao adicionar o livro '" + title + "'.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Preço inválido. Use um número para o preço.");
            System.err.println("Erro de formato de número em insertBook: " + e.getMessage()); // Para depuração
        } catch (SQLException e) {
            System.err.println("Erro SQL em insertBook: " + e.getMessage());
            session.setAttribute("errorMessage", "Erro ao inserir livro: " + e.getMessage());
        } finally {
            response.sendRedirect("list"); // Redireciona para evitar reenvio do formulário
            System.out.println("Redirecionado para /list após inserção."); // Para depuração
        }
    }

    /**
     * Atualiza um livro existente.
     */
    private void updateBook(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws SQLException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            float price = Float.parseFloat(request.getParameter("price"));

            Book book = new Book(id, title, author, price);
            boolean success = bookDAO.updateBook(book);
            if (success) {
                session.setAttribute("message", "Livro com ID " + id + " atualizado com sucesso!");
            } else {
                session.setAttribute("errorMessage", "Falha ao atualizar o livro com ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID ou Preço inválido. Use números.");
            System.err.println("Erro de formato de número em updateBook: " + e.getMessage()); // Para depuração
        } catch (SQLException e) {
            System.err.println("Erro SQL em updateBook: " + e.getMessage());
            session.setAttribute("errorMessage", "Erro ao atualizar livro: " + e.getMessage());
        } finally {
            response.sendRedirect("list");
            System.out.println("Redirecionado para /list após atualização."); // Para depuração
        }
    }

    /**
     * Exclui um livro pelo ID.
     */
    private void deleteBook(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws SQLException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = bookDAO.deleteBook(id);
            if (success) {
                session.setAttribute("message", "Livro com ID " + id + " excluído com sucesso!");
            } else {
                session.setAttribute("errorMessage", "Falha ao excluir o livro com ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID de livro inválido para exclusão.");
            System.err.println("Erro de formato de número em deleteBook: " + e.getMessage()); // Para depuração
        } catch (SQLException e) {
            System.err.println("Erro SQL em deleteBook: " + e.getMessage());
            session.setAttribute("errorMessage", "Erro ao excluir livro: " + e.getMessage());
        } finally {
            response.sendRedirect("list");
            System.out.println("Redirecionado para /list após exclusão."); // Para depuração
        }
    }

    /**
     * Exibe a página de créditos.
     */
    private void showCreditsPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Credits.jsp");
        dispatcher.forward(request, response);
        System.out.println("Encaminhado para Credits.jsp."); // Para depuração
    }
}