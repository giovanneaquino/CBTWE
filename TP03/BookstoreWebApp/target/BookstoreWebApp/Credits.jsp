<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Créditos</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4;
            color: #333;
            line-height: 1.6;
        }
        .container {
            max-width: 800px;
            margin: 30px auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
        }
        h2 {
            color: #0056b3;
            margin-bottom: 20px;
        }
        p {
            margin-bottom: 10px;
        }
        .back-link {
            display: inline-block;
            margin-top: 30px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }
        .back-link:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Créditos da Aplicação Bookstore</h2>
        <p>Desenvolvido como parte de um tutorial de CRUD com JSP, Servlets e JDBC.</p>
        <p><strong>Autor Original do Tutorial:</strong> <a href="https://www.codejava.net" target="_blank">www.codejava.net</a></p>
        <p><strong>Adaptado e Implementado com:</strong></p>
        <ul>
            <li>Java Servlets & JSP</li>
            <li>JSP Standard Tag Library (JSTL)</li>
            <li>Java Database Connectivity (JDBC)</li>
            <li>MySQL Database</li>
            <li>Apache Tomcat Server</li>
            <li>Apache Maven</li>
            <li>WSL (Windows Subsystem for Linux)</li>
            <li>Visual Studio Code</li>
        </ul>
        <p>Obrigado por utilizar esta aplicação de exemplo!</p>
        <p>Elaborado por Giovanne Brandão de Aquino e Ricardo Queiroz Oliani</p>
        <a href="list" class="back-link">Voltar para a lista de livros</a>
    </div>
</body>
</html>