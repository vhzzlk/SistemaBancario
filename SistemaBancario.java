package teste;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class SistemaBancario {
    private Map<String, Conta> contas;
    private Conta contaLogada;
    private JFrame frame;

    public SistemaBancario() {
        this.contas = new HashMap<>();
        this.contaLogada = null;

        criarTelaLogin();
    }

    private void criarTelaLogin() {
        frame = new JFrame("Sistema Bancário");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel labelNumero = new JLabel("Número da Conta:");
        JTextField campoNumero = new JTextField(20);

        JLabel labelSenha = new JLabel("Senha:");
        JPasswordField campoSenha = new JPasswordField(20);

        JButton botaoLogin = new JButton("Login");
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numero = campoNumero.getText();
                String senha = new String(campoSenha.getPassword());

                if (contas.containsKey(numero)) {
                    Conta conta = contas.get(numero);
                    if (conta.getSenha().equals(senha)) {
                        contaLogada = conta;
                        frame.dispose();
                        criarTelaPrincipal();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Senha incorreta.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Conta não encontrada.");
                }
            }
        });

        JButton botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                criarTelaCriarConta();
            }
        });

        panel.add(labelNumero);
        panel.add(campoNumero);
        panel.add(labelSenha);
        panel.add(campoSenha);
        panel.add(botaoLogin);
        panel.add(botaoCriarConta);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private boolean validarCPF(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }

        int[] pesos = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += Integer.parseInt(cpf.substring(i, i + 1)) * pesos[i];
        }

        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;

        soma = 0;
        int[] pesos2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 10; i++) {
            soma += Integer.parseInt(cpf.substring(i, i + 1)) * pesos2[i];
        }

        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;

        return Integer.parseInt(cpf.substring(9, 10)) == digito1 && Integer.parseInt(cpf.substring(10, 11)) == digito2;
    }

    private boolean validarDataNascimento(String dataNascimento) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dataNascimento, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void criarTelaCriarConta() {
        JFrame frameCriar = new JFrame("Criar Conta");
        frameCriar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameCriar.setSize(300, 350);

        JPanel panelCriar = new JPanel();
        panelCriar.setLayout(new BoxLayout(panelCriar, BoxLayout.Y_AXIS));

        JLabel labelNome = new JLabel("Nome do Cliente:");
        JTextField campoNome = new JTextField(20);

        JLabel labelCPF = new JLabel("CPF do Cliente:");
        JTextField campoCPF = new JTextField(20);

        JLabel labelDataNascimento = new JLabel("Data de Nascimento (dd/MM/yyyy):");
        JTextField campoDataNascimento = new JTextField(20);

        JLabel labelEndereco = new JLabel("Endereço do Cliente:");
        JTextField campoEndereco = new JTextField(20);

        JLabel labelNumero = new JLabel("Número da Conta:");
        JTextField campoNumero = new JTextField(20);

        JLabel labelSenha = new JLabel("Senha:");
        JPasswordField campoSenha = new JPasswordField(20);

        JLabel labelSaldo = new JLabel("Saldo Inicial:");
        JTextField campoSaldo = new JTextField(20);

        JButton botaoCriar = new JButton("Criar Conta");
        botaoCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = campoNome.getText();
                String cpf = campoCPF.getText();
                String dataNascimento = campoDataNascimento.getText();
                String endereco = campoEndereco.getText();
                String numero = campoNumero.getText();
                String senha = new String(campoSenha.getPassword());
                double saldo;

                try {
                    saldo = Double.parseDouble(campoSaldo.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frameCriar, "Valor inválido para saldo.");
                    return;
                }

                if (!validarCPF(cpf)) {
                    JOptionPane.showMessageDialog(frameCriar, "CPF inválido.");
                    return;
                }

                if (!validarDataNascimento(dataNascimento)) {
                    JOptionPane.showMessageDialog(frameCriar, "Data de nascimento inválida.");
                    return;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataNasc = LocalDate.parse(dataNascimento, formatter);
                    Cliente cliente = new Cliente(nome, cpf, dataNasc, endereco);

                    Conta conta = new Conta(numero, senha, saldo, cliente);

                    if (!contas.containsKey(numero)) {
                        contas.put(numero, conta);
                        JOptionPane.showMessageDialog(frameCriar, "Conta criada com sucesso!");
                        frameCriar.dispose();
                        criarTelaLogin();
                    } else {
                        JOptionPane.showMessageDialog(frameCriar, "Número de conta já existente.");
                    }
                } catch (DateTimeParseException ex) {
                    // Essa exceção já foi tratada na validação anterior
                }
            }
        });

        panelCriar.add(labelNome);
        panelCriar.add(campoNome);
        panelCriar.add(labelCPF);
        panelCriar.add(campoCPF);
        panelCriar.add(labelDataNascimento);
        panelCriar.add(campoDataNascimento);
        panelCriar.add(labelEndereco);
        panelCriar.add(campoEndereco);
        panelCriar.add(labelNumero);
        panelCriar.add(campoNumero);
        panelCriar.add(labelSenha);
        panelCriar.add(campoSenha);
        panelCriar.add(labelSaldo);
        panelCriar.add(campoSaldo);
        panelCriar.add(botaoCriar);

        frameCriar.getContentPane().add(panelCriar);
        frameCriar.setVisible(true);
    }

    private void criarTelaPrincipal() {
        frame = new JFrame("Sistema Bancário");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 350);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton botaoConsultarSaldo = new JButton("Consultar Saldo");
        botaoConsultarSaldo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Seu saldo atual é: R$" + contaLogada.getSaldo());
            }
        });

        JButton botaoDepositar = new JButton("Depositar");
        botaoDepositar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                criarTelaDepositar();
            }
        });

        JButton botaoSacar = new JButton("Sacar");
        botaoSacar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                criarTelaSacar();
            }
        });

        JButton botaoTransferir = new JButton("Transferir");
        botaoTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                criarTelaTransferir();
            }
        });

        JButton botaoVerificarDados = new JButton("Verificar Dados do Cliente");
        botaoVerificarDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cliente cliente = contaLogada.getCliente();
                String mensagem = "Nome: " + cliente.getNome() +
                        "\nCPF: " + cliente.getCpf() +
                        "\nData de Nascimento: " + cliente.getDataNascimento() +
                        "\nEndereço: " + cliente.getEndereco();

                JFrame frameDados = new JFrame("Dados do Cliente");
                frameDados.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameDados.setSize(300, 200);

                JTextArea areaDados = new JTextArea(mensagem, 10, 25);
                areaDados.setEditable(false);

                frameDados.getContentPane().add(new JScrollPane(areaDados));
                frameDados.setVisible(true);
            }
        });

        JButton botaoSair = new JButton("Sair");
        botaoSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contaLogada = null;
                frame.dispose();
                criarTelaLogin();
            }
        });

        panel.add(botaoConsultarSaldo);
        panel.add(botaoDepositar);
        panel.add(botaoSacar);
        panel.add(botaoTransferir);
        panel.add(botaoVerificarDados);
        panel.add(botaoSair);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void criarTelaDepositar() {
        JFrame frameDepositar = new JFrame("Depositar");
        frameDepositar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameDepositar.setSize(200, 150);

        JPanel panelDepositar = new JPanel();
        panelDepositar.setLayout(new BoxLayout(panelDepositar, BoxLayout.Y_AXIS));

        JLabel labelValor = new JLabel("Valor para Depositar:");
        JTextField campoValor = new JTextField(20);

        JButton botaoDepositar = new JButton("Depositar");
        botaoDepositar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valor = Double.parseDouble(campoValor.getText());
                    contaLogada.depositar(valor);
                    JOptionPane.showMessageDialog(frameDepositar, "Depósito realizado com sucesso!");
                    frameDepositar.dispose();
                    criarTelaPrincipal();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frameDepositar, "Valor inválido.");
                }
            }
        });

        panelDepositar.add(labelValor);
        panelDepositar.add(campoValor);
        panelDepositar.add(botaoDepositar);

        frameDepositar.getContentPane().add(panelDepositar);
        frameDepositar.setVisible(true);
    }

    private void criarTelaSacar() {
        JFrame frameSacar = new JFrame("Sacar");
        frameSacar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameSacar.setSize(200, 150);

        JPanel panelSacar = new JPanel();
        panelSacar.setLayout(new BoxLayout(panelSacar, BoxLayout.Y_AXIS));

        JLabel labelValor = new JLabel("Valor para Sacar:");
        JTextField campoValor = new JTextField(20);

        JButton botaoSacar = new JButton("Sacar");
        botaoSacar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valor = Double.parseDouble(campoValor.getText());
                    if (contaLogada.getSaldo() >= valor) {
                        contaLogada.sacar(valor);
                        JOptionPane.showMessageDialog(frameSacar, "Saque realizado com sucesso!");
                        frameSacar.dispose();
                        criarTelaPrincipal();
                    } else {
                        JOptionPane.showMessageDialog(frameSacar, "Saldo insuficiente.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frameSacar, "Valor inválido.");
                }
            }
        });

        panelSacar.add(labelValor);
        panelSacar.add(campoValor);
        panelSacar.add(botaoSacar);

        frameSacar.getContentPane().add(panelSacar);
        frameSacar.setVisible(true);
    }

    private void criarTelaTransferir() {
        JFrame frameTransferir = new JFrame("Transferir");
        frameTransferir.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameTransferir.setSize(250, 200);

        JPanel panelTransferir = new JPanel();
        panelTransferir.setLayout(new BoxLayout(panelTransferir, BoxLayout.Y_AXIS));

        JLabel labelNumeroDestino = new JLabel("Número da Conta Destino:");
        JTextField campoNumeroDestino = new JTextField(20);

        JLabel labelValor = new JLabel("Valor para Transferir:");
        JTextField campoValor = new JTextField(20);

        JButton botaoTransferir = new JButton("Transferir");
        botaoTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroDestino = campoNumeroDestino.getText();
                if (contas.containsKey(numeroDestino)) {
                    try {
                        double valor = Double.parseDouble(campoValor.getText());
                        if (contaLogada.getSaldo() >= valor) {
                            Conta contaDestino = contas.get(numeroDestino);
                            contaLogada.sacar(valor);
                            contaDestino.depositar(valor);
                            JOptionPane.showMessageDialog(frameTransferir, "Transferência realizada com sucesso!");
                            frameTransferir.dispose();
                            criarTelaPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(frameTransferir, "Saldo insuficiente.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frameTransferir, "Valor inválido.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frameTransferir, "Conta destino não encontrada.");
                }
            }
        });

        panelTransferir.add(labelNumeroDestino);
        panelTransferir.add(campoNumeroDestino);
        panelTransferir.add(labelValor);
        panelTransferir.add(campoValor);
        panelTransferir.add(botaoTransferir);

        frameTransferir.getContentPane().add(panelTransferir);
        frameTransferir.setVisible(true);
    }

    public static void main(String[] args) {
        new SistemaBancario();
    }
}
