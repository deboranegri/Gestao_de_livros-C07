package com.gestaolivros.main;

import com.gestaolivros.dao.*;
import com.gestaolivros.model.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner          scanner       = new Scanner(System.in);
    private static final LivroDAO         livroDAO      = new LivroDAO();
    private static final ClienteDAO       clienteDAO    = new ClienteDAO();
    private static final PerfilUsuarioDAO perfilDAO     = new PerfilUsuarioDAO();
    private static final PedidoDAO        pedidoDAO     = new PedidoDAO();
    private static final ItemPedidoDAO    itemPedidoDAO = new ItemPedidoDAO();
    private static final PagamentoDAO     pagamentoDAO  = new PagamentoDAO();
    private static final SimpleDateFormat SDF           = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║   SISTEMA DE GESTÃO DE LIVROS        ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Gerenciar Livros                 ║");
            System.out.println("║  2. Gerenciar Clientes               ║");
            System.out.println("║  3. Gerenciar Perfis de Usuário      ║");
            System.out.println("║  4. Gerenciar Pedidos                ║");
            System.out.println("║  5. Ver Relatórios (JOINs)           ║");
            System.out.println("║  6. Gerenciar Itens de Pedido        ║");
            System.out.println("║  7. Gerenciar Pagamentos             ║");
            System.out.println("║  0. Sair                             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Escolha uma opção: ");

            opcao = lerInt();
            try {
                switch (opcao) {
                    case 1: menuLivros();       break;
                    case 2: menuClientes();     break;
                    case 3: menuPerfis();       break;
                    case 4: menuPedidos();      break;
                    case 5: menuRelatorios();   break;
                    case 6: menuItensPedido();  break;
                    case 7: menuPagamentos();   break;
                    case 0: System.out.println("\nAté logo!"); break;
                    default: System.out.println("  Opção inválida. Tente novamente.");
                }
            } catch (SQLException e) {
                System.err.println("\n  ✘ Erro no banco de dados: " + e.getMessage());
            }
        }
    }

    private static void menuLivros() throws SQLException {
        System.out.println("\n--- MENU LIVROS ---");
        System.out.println("  1. Listar Todos");
        System.out.println("  2. Inserir");
        System.out.println("  3. Atualizar");
        System.out.println("  4. Deletar");
        System.out.println("  5. Buscar por Título");
        System.out.println("  6. Adicionar ao Estoque");
        System.out.print("  Opção: ");
        switch (lerInt()) {
            case 1:
                List<Livro> livros = livroDAO.readAll();
                if (livros.isEmpty()) System.out.println("  (nenhum livro cadastrado)");
                else livros.forEach(l -> System.out.println("  " + l));
                break;
            case 2:
                System.out.print("  Título: ");   String titulo = lerLinha();
                System.out.print("  Preço: ");    double preco  = lerDouble();
                System.out.print("  Estoque: ");  int estoque   = lerInt();
                livroDAO.create(new Livro(0, titulo, preco, estoque));
                break;
            case 3:
                System.out.print("  ID do livro a atualizar: "); int idU = lerInt();
                System.out.print("  Novo Título: ");  String nt = lerLinha();
                System.out.print("  Novo Preço: ");   double np = lerDouble();
                System.out.print("  Novo Estoque: "); int ne    = lerInt();
                livroDAO.update(new Livro(idU, nt, np, ne));
                break;
            case 4:
                System.out.print("  ID do livro a deletar: "); livroDAO.delete(lerInt());
                break;
            case 5:
                System.out.print("  Título para busca: ");
                List<Livro> resultado = livroDAO.searchByTitulo(lerLinha());
                if (resultado.isEmpty()) System.out.println("  (nenhum resultado)");
                else resultado.forEach(l -> System.out.println("  " + l));
                break;
            case 6:
                livroDAO.readAll().forEach(l -> System.out.println("  " + l));
                System.out.print("  ID do livro: ");       int idE = lerInt();
                System.out.print("  Quantidade a adicionar: "); int qtd = lerInt();
                livroDAO.adicionarEstoque(idE, qtd);
                break;
            default: System.out.println("  Opção inválida.");
        }
    }

    private static void menuClientes() throws SQLException {
        System.out.println("\n--- MENU CLIENTES ---");
        System.out.println("  1. Listar Todos");
        System.out.println("  2. Inserir");
        System.out.println("  3. Atualizar");
        System.out.println("  4. Deletar");
        System.out.println("  5. Buscar por Nome");
        System.out.print("  Opção: ");
        switch (lerInt()) {
            case 1:
                List<Cliente> clientes = clienteDAO.readAll();
                if (clientes.isEmpty()) System.out.println("  (nenhum cliente cadastrado)");
                else clientes.forEach(c -> System.out.println("  " + c));
                break;
            case 2:
                System.out.print("  Nome: ");                    String nome  = lerLinha();
                System.out.print("  Email: ");                   String email = lerLinha();
                System.out.print("  Data Nasc (dd/MM/yyyy): ");  Date dataN   = lerData();
                System.out.print("  UserName (perfil): ");       String user2  = lerLinha();
                System.out.print("  Preferências de leitura: "); String prefs2 = lerLinha();
                int idPf = perfilDAO.create(new PerfilUsuario(0, user2, prefs2));
                clienteDAO.create(new Cliente(0, nome, email, dataN, idPf));
                break;
            case 3:
                System.out.print("  ID do cliente a atualizar: "); int idU = lerInt();
                System.out.print("  Novo Nome: ");                  String nn    = lerLinha();
                System.out.print("  Novo Email: ");                 String nem   = lerLinha();
                System.out.print("  Data Nasc (dd/MM/yyyy): ");     Date nd      = lerData();
                System.out.print("  Novo UserName (perfil): ");     String nu3   = lerLinha();
                System.out.print("  Novas Preferências: ");         String np3   = lerLinha();
                int nipf = perfilDAO.create(new PerfilUsuario(0, nu3, np3));
                clienteDAO.update(new Cliente(idU, nn, nem, nd, nipf));
                break;
            case 4:
                System.out.print("  ID do cliente a deletar: "); clienteDAO.delete(lerInt());
                break;
            case 5:
                System.out.print("  Nome para busca: ");
                List<Cliente> res = clienteDAO.searchByNome(lerLinha());
                if (res.isEmpty()) System.out.println("  (nenhum resultado)");
                else res.forEach(c -> System.out.println("  " + c));
                break;
            default: System.out.println("  Opção inválida.");
        }
    }

    private static void menuPerfis() throws SQLException {
        System.out.println("\n--- MENU PERFIS DE USUÁRIO ---");
        System.out.println("  1. Listar Todos");
        System.out.println("  2. Inserir");
        System.out.println("  3. Atualizar");
        System.out.println("  4. Deletar");
        System.out.println("  5. Buscar por UserName");
        System.out.print("  Opção: ");
        switch (lerInt()) {
            case 1:
                List<PerfilUsuario> perfis = perfilDAO.readAll();
                if (perfis.isEmpty()) System.out.println("  (nenhum perfil cadastrado)");
                else perfis.forEach(p -> System.out.println("  " + p));
                break;
            case 2:
                System.out.print("  UserName: ");      String user  = lerLinha();
                System.out.print("  Preferências: ");  String prefs = lerLinha();
                perfilDAO.create(new PerfilUsuario(0, user, prefs)); // ID gerado automaticamente pelo banco
                break;
            case 3:
                System.out.print("  ID do perfil a atualizar: "); int idU = lerInt();
                System.out.print("  Novo UserName: ");   String nu = lerLinha();
                System.out.print("  Novas Preferências: "); String np = lerLinha();
                perfilDAO.update(new PerfilUsuario(idU, nu, np));
                break;
            case 4:
                System.out.print("  ID do perfil a deletar: "); perfilDAO.delete(lerInt());
                break;
            case 5:
                System.out.print("  UserName para busca: ");
                List<PerfilUsuario> res = perfilDAO.searchByUserName(lerLinha());
                if (res.isEmpty()) System.out.println("  (nenhum resultado)");
                else res.forEach(p -> System.out.println("  " + p));
                break;
            default: System.out.println("  Opção inválida.");
        }
    }

    private static void menuPedidos() throws SQLException {
        System.out.println("\n--- MENU PEDIDOS ---");
        System.out.println("  1. Listar Todos");
        System.out.println("  2. Inserir");
        System.out.println("  3. Atualizar (trocar cliente)");
        System.out.println("  4. Deletar");
        System.out.println("  5. Buscar por ID");
        System.out.print("  Opção: ");
        switch (lerInt()) {
            case 1:
                List<Pedido> pedidos = pedidoDAO.readAll();
                if (pedidos.isEmpty()) System.out.println("  (nenhum pedido cadastrado)");
                else pedidos.forEach(p -> System.out.println("  " + p));
                break;
            case 2:
                System.out.println("  Clientes disponíveis:");
                clienteDAO.readAll().forEach(c -> System.out.println("    " + c));
                System.out.print("  ID do Cliente: "); int idC = lerInt();
                pedidoDAO.create(new Pedido(0, new Date(), idC));
                break;
            case 3:
                System.out.print("  ID do pedido a atualizar: "); int idU = lerInt();
                System.out.println("  Clientes disponíveis:");
                clienteDAO.readAll().forEach(c -> System.out.println("    " + c));
                System.out.print("  Novo ID do Cliente: ");       int idNC = lerInt();
                pedidoDAO.update(new Pedido(idU, new Date(), idNC));
                break;
            case 4:
                System.out.print("  ID do pedido a deletar: "); pedidoDAO.delete(lerInt());
                break;
            case 5:
                System.out.print("  ID do pedido: ");
                List<Pedido> res = pedidoDAO.searchById(lerInt());
                if (res.isEmpty()) System.out.println("  (não encontrado)");
                else res.forEach(p -> System.out.println("  " + p));
                break;
            default: System.out.println("  Opção inválida.");
        }
    }

    private static void menuRelatorios() throws SQLException {
        System.out.println("\n--- RELATÓRIOS (JOINs) ---");
        System.out.println("  1. Clientes e seus Perfis  (JOIN 1)");
        System.out.println("  2. Pedidos com Clientes    (JOIN 2)");
        System.out.println("  3. Relatório Geral de Vendas (JOIN 3 / VIEW)");
        System.out.print("  Opção: ");
        switch (lerInt()) {
            case 1: clienteDAO.listClientesComPerfil();   break;
            case 2: pedidoDAO.listPedidosComClientes();   break;
            case 3: pedidoDAO.listRelatorioVendas();      break;
            default: System.out.println("  Opção inválida.");
        }
    }

    private static void menuItensPedido() throws SQLException {
        System.out.println("\n--- MENU ITENS DE PEDIDO ---");
        System.out.println("  1. Listar Todos");
        System.out.println("  2. Inserir");
        System.out.println("  3. Atualizar (quantidade e preço)");
        System.out.println("  4. Deletar");
        System.out.println("  5. Buscar por ID do Pedido");
        System.out.print("  Opção: ");
        switch (lerInt()) {
            case 1:
                List<ItemPedido> itens = itemPedidoDAO.readAll();
                if (itens.isEmpty()) System.out.println("  (nenhum item cadastrado)");
                else itens.forEach(i -> System.out.println("  " + i));
                break;
            case 2:
                System.out.println("  Pedidos disponíveis:");
                pedidoDAO.readAll().forEach(p -> System.out.println("    " + p));
                System.out.print("  ID do Pedido: ");   int idP = lerInt();
                System.out.println("  Livros disponíveis:");
                livroDAO.readAll().forEach(l -> System.out.println("    " + l));
                System.out.print("  ID do Livro: ");    int idL  = lerInt();
                System.out.print("  Quantidade: ");     int qtd  = lerInt();
                System.out.print("  Preço Unitário: "); double pu = lerDouble();
                itemPedidoDAO.create(new ItemPedido(idP, idL, qtd, pu));
                break;
            case 3:
                System.out.print("  ID do Pedido: ");        int idPU = lerInt();
                System.out.print("  ID do Livro: ");         int idLU = lerInt();
                System.out.print("  Nova Quantidade: ");     int nq   = lerInt();
                System.out.print("  Novo Preço Unitário: "); double np = lerDouble();
                itemPedidoDAO.update(new ItemPedido(idPU, idLU, nq, np));
                break;
            case 4:
                System.out.print("  ID do Pedido: "); int idPD = lerInt();
                System.out.print("  ID do Livro: ");  int idLD = lerInt();
                itemPedidoDAO.delete(idPD, idLD);
                break;
            case 5:
                System.out.print("  ID do Pedido para busca: ");
                List<ItemPedido> res = itemPedidoDAO.searchByPedido(lerInt());
                if (res.isEmpty()) System.out.println("  (nenhum item encontrado)");
                else res.forEach(i -> System.out.println("  " + i));
                break;
            default: System.out.println("  Opção inválida.");
        }
    }

    private static void menuPagamentos() throws SQLException {
        System.out.println("\n--- MENU PAGAMENTOS ---");
        System.out.println("  1. Listar Todos");
        System.out.println("  2. Inserir");
        System.out.println("  3. Atualizar");
        System.out.println("  4. Deletar");
        System.out.println("  5. Buscar por Forma de Pagamento");
        System.out.print("  Opção: ");
        switch (lerInt()) {
            case 1:
                List<Pagamento> pags = pagamentoDAO.readAll();
                if (pags.isEmpty()) System.out.println("  (nenhum pagamento cadastrado)");
                else pags.forEach(p -> System.out.println("  " + p));
                break;
            case 2:
                System.out.println("  Pedidos disponíveis:");
                pedidoDAO.readAll().forEach(p -> System.out.println("    " + p));
                System.out.print("  ID do Pedido: ");          int idP  = lerInt();
                System.out.print("  Forma (PIX/Crédito/...): "); String forma = lerLinha();
                System.out.print("  Valor Total: ");           double vt = lerDouble();
                pagamentoDAO.create(new Pagamento(0, new Date(), forma, vt, idP));
                break;
            case 3:
                System.out.print("  ID do pagamento a atualizar: "); int idU = lerInt();
                System.out.println("  Pedidos disponíveis:");
                pedidoDAO.readAll().forEach(p -> System.out.println("    " + p));
                System.out.print("  ID do Pedido: ");             int nidP  = lerInt();
                System.out.print("  Nova Forma de Pagamento: ");  String nf = lerLinha();
                System.out.print("  Novo Valor Total: ");         double nvt = lerDouble();
                pagamentoDAO.update(new Pagamento(idU, new Date(), nf, nvt, nidP));
                break;
            case 4:
                System.out.print("  ID do pagamento a deletar: "); pagamentoDAO.delete(lerInt());
                break;
            case 5:
                System.out.print("  Forma de pagamento para busca: ");
                List<Pagamento> res = pagamentoDAO.searchByFormaPagamento(lerLinha());
                if (res.isEmpty()) System.out.println("  (nenhum resultado)");
                else res.forEach(p -> System.out.println("  " + p));
                break;
            default: System.out.println("  Opção inválida.");
        }
    }

    private static int lerInt() {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("  Valor inválido. Digite um número inteiro: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                String line = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("  Valor inválido. Digite um número (ex: 29.90): ");
            }
        }
    }

    private static String lerLinha() {
        String line = scanner.nextLine().trim();
        // Se vier vazia (resquício de nextLine anterior), lê de novo
        while (line.isEmpty()) {
            line = scanner.nextLine().trim();
        }
        return line;
    }

    private static Date lerData() {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                return SDF.parse(line);
            } catch (ParseException e) {
                System.out.print("  Formato inválido. Use dd/MM/yyyy: ");
            }
        }
    }
}