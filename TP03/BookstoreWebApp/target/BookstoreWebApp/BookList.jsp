<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gerenciamento de Livros</title>
    <style>
        /* Estilos básicos para a tabela e o layout */
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4;
            color: #333;
        }
        .container {
            max-width: 960px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h2 {
            color: #0056b3;
            text-align: center;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #e9ecef;
            color: #495057;
        }
        tr:nth-child(even) {
            background-color: #f8f9fa;
        }
        .button {
            display: inline-block;
            padding: 8px 15px;
            margin: 5px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            color: white;
        }
        .button.add {
            background-color: #28a745; /* Verde para adicionar */
        }
        .button.edit {
            background-color: #007bff; /* Azul para editar (usado também para Créditos) */
        }
        .button.delete {
            background-color: #dc3545; /* Vermelho para deletar */
        }
        .actions {
            white-space: nowrap; /* Evita que os botões quebrem linha */
        }
        .message {
            text-align: center;
            color: green;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .error-message {
            text-align: center;
            color: red;
            font-weight: bold;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Gerenciamento de Livros</h2>

        <c:if test="${not empty message}">
            <p class="message">${message}</p>
            <c:remove var="message" scope="session"/> <%-- Remove a mensagem após exibir --%>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
            <c:remove var="errorMessage" scope="session"/> <%-- Remove a mensagem após exibir --%>
        </c:if>

        <h3>
            <a href="new" class="button add">Adicionar Novo Livro</a>
            <a href="credits" class="button edit">Créditos</a> <%-- LINK PARA A PÁGINA DE CRÉDITOS --%>
        </h3>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Autor</th>
                    <th>Preço</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty listBook}">
                        <tr>
                            <td colspan="5" style="text-align: center;">Nenhum livro encontrado.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="book" items="${listBook}">
                            <tr>
                                <td><c:out value="${book.id}" /></td>
                                <td><c:out value="${book.title}" /></td>
                                <td><c:out value="${book.author}" /></td>
                                <td><c:out value="${book.price}" /></td>
                                <td class="actions">
                                    <a href="edit?id=<c:out value='${book.id}' />" class="button edit">Editar</a>
                                    <a href="delete?id=<c:out value='${book.id}' />" class="button delete"
                                       onclick="return confirm('Tem certeza que deseja deletar este livro?');">Deletar</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</body>
</html>