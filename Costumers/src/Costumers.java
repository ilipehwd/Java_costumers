import java.io.*;
import java.util.*;

public class Costumers {
	public static class Cliente {
		public char ativo;
		public String codCliente;
		public String nomeCliente;
		public String anoPrimeiraCompra;
		public float vlrCompra;
		public boolean emDia;
	}

	public static void main(String[] args) {
		Scanner leia = new Scanner(System.in);
		int escolha;
		String strEscolha;

		do {
			System.out.println("[ 0 ] Encerrar o programa");
			System.out.println("[ 1 ] Inclusão de cliente ");
			System.out.println("[ 2 ] Alteração de dados");
			System.out.println("[ 3 ] Consulta  de dados");
			System.out.println("[ 4 ] Exclusão de dados");
			do {
				System.out.print("Escolha a operação: ");
				strEscolha = leia.nextLine();

				escolha = Integer.parseInt(strEscolha);

				if (escolha < 0 || escolha > 4) {
					System.out.println("Escolha entre 0 e 4");
				}

			} while (escolha < 0 || escolha > 4);

			switch (escolha) {
			case 0:
				System.exit(0);
			case 1:
				incluirCliente();
				break;
			case 2:
				alterarDados();
				break;
			case 3:
				consultarClientes();
				break;
			case 4:
				excluirCliente();
				break;
			}

		} while (true);
	}

	public static void incluirCliente() {
		Cliente cliente = new Cliente();
		RandomAccessFile arquivo;
		Scanner leia = new Scanner(System.in);
		boolean encontrou;
		int codChave;
		String strCodChave;
		char confirmacao;
		int intAnoPrimeiraCompra;
		char emDia;
		do {
			do {
				System.out.println(" *************** INCLUSAO DE clienteS ***************** ");

				do {
					System.out.print("Digite o Código do Cliente(FIM para encerrar): ");
					strCodChave = leia.nextLine();

					if (strCodChave.equalsIgnoreCase("fim")) {
						break;
					}

					codChave = Integer.parseInt(strCodChave);

					if (codChave < 0) {
						System.out.println("O código do cliente deve ser número inteiro e maior que zero");
					}

				} while (codChave < 0);

				if (strCodChave.equalsIgnoreCase("fim")) {
					break;
				}

				encontrou = false;

				try {
					arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
					while (true) {
						cliente.ativo = arquivo.readChar();
						cliente.codCliente = arquivo.readUTF();
						cliente.nomeCliente = arquivo.readUTF();
						cliente.anoPrimeiraCompra = arquivo.readUTF();
						cliente.vlrCompra = arquivo.readFloat();
						cliente.emDia = arquivo.readBoolean();
						if (strCodChave.equals(cliente.codCliente) && cliente.ativo == 'S') {
							System.out.println("Matrícula já cadastrada, digite outro valor\n");
							encontrou = true;
							break;
						}
					}
				} catch (EOFException e) {
					encontrou = false;
				} catch (IOException e) {
					System.out.println("Erro na abertura do arquivo - programa será finalizado");
					System.exit(0);
				}
			} while (encontrou);
			if (strCodChave.equalsIgnoreCase("fim")) {
				System.out.println("\n ************ PROGRAMA ENCERRADO ************** \n");
				break;
			}
			cliente.ativo = 'S';
			cliente.codCliente = strCodChave;
			do {
				System.out.print("Digite o nome do cliente.........................: ");
				cliente.nomeCliente = leia.nextLine();

				if (cliente.nomeCliente.length() < 10) {
					System.out.println("O nome deve ter no minimo 10 caracteres");
				}

			} while (cliente.nomeCliente.length() < 10);

			do {
				System.out.print("Digite o ano de primeira compra.......: ");
				cliente.anoPrimeiraCompra = leia.nextLine();

				intAnoPrimeiraCompra = Integer.parseInt(cliente.anoPrimeiraCompra);

				if (intAnoPrimeiraCompra > 2013) {
					System.out.println("O ano da primeira compra dever ser menor ou igual a 2013");
				}

			} while (intAnoPrimeiraCompra > 2013);

			do {
				System.out.print("Digite o valor da compra..................: ");
				cliente.vlrCompra = leia.nextFloat();

				if (cliente.vlrCompra <= 0) {
					System.out.println("O valor da compra deve ser maior que zero");
				}

			} while (cliente.vlrCompra <= 0);

			do {
				System.out.print("Digite se o cliente está em dia com as contas (S/N)...: ");
				emDia = leia.next().charAt(0);

				if (emDia != 'S' && emDia != 'N') {
					System.out.println("Preencha apenas com S ou N");
				}

				if (emDia == 'S') {
					cliente.emDia = true;
				} else {
					cliente.emDia = false;
				}

			} while (emDia != 'S' && emDia != 'N');

			do {
				System.out.print("\nConfirma a gravação dos dados (S/N) ? ");
				confirmacao = leia.next().charAt(0);
				if (confirmacao == 'S') {
					try {
						arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
						arquivo.seek(arquivo.length());
						arquivo.writeChar(cliente.ativo);
						arquivo.writeUTF(cliente.codCliente);
						arquivo.writeUTF(cliente.nomeCliente);
						arquivo.writeUTF(cliente.anoPrimeiraCompra);
						arquivo.writeFloat(cliente.vlrCompra);
						arquivo.writeBoolean(cliente.emDia);
						arquivo.close();
						System.out.println("Dados gravados com sucesso !\n");
					} catch (IOException e) {
						System.out.println("Erro na gravaçao do registro - programa será finalizado");
						System.exit(0);
					}
				}
			} while (confirmacao != 'S' && confirmacao != 'N');
			leia.nextLine();
		} while (!cliente.codCliente.equals("FIM"));
	}

	public static void alterarDados() {
		Cliente cliente = new Cliente();
		RandomAccessFile arquivo;
		Scanner leia = new Scanner(System.in);
		boolean encontrou = false;
		String codChave;
		char confirmacao;
		long posicaoRegistro = 0;
		byte opcao;
		char emDia;

		do {
			do {
				System.out.println(" *************** ALTERAÇÃO DE clienteS ***************** ");
				System.out.print("Digite o Código do Cliente (FIM para encerrar): ");
				codChave = leia.nextLine();

				if (codChave.equalsIgnoreCase("fim")) {
					break;
				}
				encontrou = false;

				try {
					arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
					while (true) {
						posicaoRegistro = arquivo.getFilePointer();
						cliente.ativo = arquivo.readChar();
						cliente.codCliente = arquivo.readUTF();
						cliente.nomeCliente = arquivo.readUTF();
						cliente.anoPrimeiraCompra = arquivo.readUTF();
						cliente.vlrCompra = arquivo.readFloat();
						cliente.emDia = arquivo.readBoolean();
						if (codChave.equals(cliente.codCliente) && cliente.ativo == 'S') {
							encontrou = true;
							break;
						}
					}
					arquivo.close();
				} catch (EOFException e) {
					encontrou = false;
					System.out.println("Esta matrícula não foi encontrada no arquivo !\n");
				} catch (IOException e) {
					System.out.println("Erro na abertura do arquivo - programa será finalizado");
					System.exit(0);
				}
			} while (!encontrou);
			if (codChave.equalsIgnoreCase("fim")) {
				System.out.println("\n ************ PROGRAMA ENCERRADO ************** \n");
				break;
			}

			if (cliente.emDia) {
				emDia = 'S';
			} else {
				emDia = 'N';
			}

			do {
				System.out.println("[ 1 ] Nome do cliente.........: " + cliente.nomeCliente);
				System.out.println("[ 2 ] Ano de primeira compra..: " + cliente.anoPrimeiraCompra);
				System.out.println("[ 3 ] Valor da compra.........: " + cliente.vlrCompra);
				System.out.println("[ 4 ] Cliente em dia..........: " + emDia);

				do {
					System.out.print("Insira a opção que deseja alterar (0 para finalizar): ");
					opcao = leia.nextByte();

					if (opcao < 0 || opcao > 4) {
						System.out.println("Escolha apenas as opções citadas anteriormente");
					}

				} while (opcao < 0 || opcao > 4);
				leia.nextLine();

				switch (opcao) {
				case 1:
					System.out.print("Digite o novo Nome do cliente...........................: ");
					cliente.nomeCliente = leia.nextLine();
					break;
				case 2:
					System.out.print("Digite o novo Ano de primeira compra....................:");
					cliente.anoPrimeiraCompra = leia.nextLine();
					break;
				case 3:
					System.out.print("Digite o novo valor da compra...........................: ");
					cliente.vlrCompra = leia.nextFloat();
					break;
				case 4:
					System.out.println("Digite se o cliente está em dia com as contas (S/N)...: ");
					emDia = leia.next().charAt(0);
					break;
				}
				System.out.println();

			} while (opcao != 0);

			do {
				System.out.print("Confirma as Alterações (S/N) ? ");
				confirmacao = leia.next().charAt(0);
				if (confirmacao == 'S') {
					try {
						arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
						arquivo.seek(posicaoRegistro);
						arquivo.writeChar('N');
						arquivo.seek(arquivo.length());
						arquivo.writeChar(cliente.ativo);
						arquivo.writeUTF(cliente.codCliente);
						arquivo.writeUTF(cliente.nomeCliente);
						arquivo.writeUTF(cliente.anoPrimeiraCompra);
						arquivo.writeFloat(cliente.vlrCompra);
						arquivo.writeBoolean(cliente.emDia);
						arquivo.close();
						System.out.println("Dados alterados com sucesso !\n");
					} catch (IOException e) {
						System.out.println("Erro na gravaçao do registro - programa será finalizado");
						System.exit(0);
					}
				}
				System.out.println();
			} while (confirmacao != 'S' && confirmacao != 'N');
			leia.nextLine();
		} while (!cliente.codCliente.equals("FIM"));
	}

	public static void consultarClientes() {
		Scanner leia = new Scanner(System.in);
		Cliente cliente = new Cliente();
		RandomAccessFile arquivo;
		byte opcao;
		char escolha;
		String codChave;
		char emDia;
		long posicaoRegistro;
		boolean encontrou;
		do {
			System.out.println(" *************** CONSULTA DE clienteS ***************** ");
			System.out.println(" [0] SAIR");
			System.out.println(" [1] CONSULTAR APENAS 1 cliente ");
			System.out.println(" [2] LISTA DE TODOS OS clienteS ");
			System.out.println(" [3] LISTA SOMENTE CLIENTES EM DIA");

			do {
				System.out.print("Escolha sua opção: ");
				opcao = leia.nextByte();

				if (opcao < 0 || opcao > 3) {
					System.out.println("opcao Inválida, digite novamente.\n");
				}

			} while (opcao < 0 || opcao > 3);

			switch (opcao) {
			case 0:
				System.out.println("\n ************ PROGRAMA ENCERRADO ************** \n");
				break;
			case 1:
				leia.nextLine();
				System.out.print("Digite o código do cliente: ");
				codChave = leia.nextLine();
				encontrou = false;
				try {
					arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
					while (true) {
						posicaoRegistro = arquivo.getFilePointer();
						cliente.ativo = arquivo.readChar();
						cliente.codCliente = arquivo.readUTF();
						cliente.nomeCliente = arquivo.readUTF();
						cliente.anoPrimeiraCompra = arquivo.readUTF();
						cliente.vlrCompra = arquivo.readFloat();
						cliente.emDia = arquivo.readBoolean();

						if (codChave.equals(cliente.codCliente) && cliente.ativo == 'S') {
							encontrou = true;
							System.out.println("Código do Cliente.........:  " + cliente.codCliente);
							System.out.println("Nome do Cliente...........:  " + cliente.nomeCliente);
							System.out.println("Ano de primeira compra....:  " + cliente.anoPrimeiraCompra);
							System.out.println("Valor da compra...........:  " + cliente.vlrCompra);
							if (cliente.emDia) {
								emDia = 'S';
							} else {
								emDia = 'N';
							}
							System.out.println("Cliente em dia............:  " + emDia);
						}
					}
				} catch (EOFException e) {
					System.out.println("\n FIM DE RELATÓRIO - ENTER para continuar...\n");
					leia.nextLine();
				} catch (IOException e) {
					System.out.println("Erro na abertura arquivo - programa será finalizado");
					System.exit(0);
				}
				break;
			case 2:
				try {
					arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
					System.out.println("-Cod Cliente - Nome Cliente - ANO PRIMEIRA COMPRA - VALOR COMPRA - EM DIA");
					while (true) {
						cliente.ativo = arquivo.readChar();
						cliente.codCliente = arquivo.readUTF();
						cliente.nomeCliente = arquivo.readUTF();
						cliente.anoPrimeiraCompra = arquivo.readUTF();
						cliente.vlrCompra = arquivo.readFloat();
						cliente.emDia = arquivo.readBoolean();
						if (cliente.emDia) {
							emDia = 'S';
						} else {
							emDia = 'N';
						}
						if (cliente.ativo == 'S') {
							System.out.println(cliente.codCliente + "             " + cliente.nomeCliente + "          "
									+ cliente.anoPrimeiraCompra + "                  " + cliente.vlrCompra + "     "
									+ emDia);
						}
					}
				} catch (EOFException e) {
					System.out.println("\n FIM DE RELATÓRIO - ENTER para continuar...\n");
					leia.nextLine();
					codChave = leia.nextLine();
				} catch (IOException e) {
					System.out.println("\n Erro na abertura arquivo - programa será finalizado");
					System.exit(0);
				}
				break;
			case 3:
				do {
					System.out.print("Escolha a opção que deseja Em dia (S/N): ");
					escolha = leia.next().charAt(0);

					if (escolha != 'S' && escolha != 'N') {
						System.out.println("Opção inválido, escolha S ou N");
					}

				} while (escolha != 'S' && escolha != 'N');
				try {
					arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
					System.out.println("-Nome Cliente - Cod Cliente - ANO PRIMEIRA COMPRA - VALOR COMPRA - EM DIA");
					while (true) {
						cliente.ativo = arquivo.readChar();
						cliente.codCliente = arquivo.readUTF();
						cliente.nomeCliente = arquivo.readUTF();
						cliente.anoPrimeiraCompra = arquivo.readUTF();
						cliente.vlrCompra = arquivo.readFloat();
						cliente.emDia = arquivo.readBoolean();
						if (cliente.emDia) {
							emDia = 'S';
						} else {
							emDia = 'N';
						}
						if (emDia == escolha && cliente.ativo == 'S') {
							System.out.println(cliente.codCliente + "             " + cliente.nomeCliente + "          "
									+ cliente.anoPrimeiraCompra + "                  " + cliente.vlrCompra + "     "
									+ emDia);
						}
					}
				} catch (EOFException e) {
					System.out.println("\n FIM DE RELATÓRIO - ENTER para continuar...\n");
					leia.nextLine();
					codChave = leia.nextLine();
				} catch (IOException e) {
					System.out.println("\n Erro na abertura arquivo - programa será finalizado");
					System.exit(0);
				}
				break;
			}
		} while (opcao != 0);
	}

	public static void excluirCliente() {
		Cliente cliente = new Cliente();
		RandomAccessFile arquivo;
		Scanner leia = new Scanner(System.in);
		boolean encontrou;
		String codChave;
		char confirmacao;
		long posicaoRegistro = 0;
		char emDia;
		do {
			do {
				System.out.println(" *************** EXCLUSÃO DE clienteS ***************** ");
				System.out.print("Digite a Matrícula do cliente para excluir ( FIM para encerrar ): ");
				codChave = leia.nextLine();

				if (codChave.equals("FIM")) {
					break;
				}

				encontrou = false;
				try {
					arquivo = new RandomAccessFile("clienteS.DAT", "rw");
					while (true) {
						posicaoRegistro = arquivo.getFilePointer();
						cliente.ativo = arquivo.readChar();
						cliente.codCliente = arquivo.readUTF();
						cliente.nomeCliente = arquivo.readUTF();
						cliente.anoPrimeiraCompra = arquivo.readUTF();
						cliente.vlrCompra = arquivo.readFloat();
						cliente.emDia = arquivo.readBoolean();
						if (codChave.equals(cliente.codCliente) && cliente.ativo == 'S') {
							encontrou = true;
							break;
						}
					}
					arquivo.close();
				} catch (EOFException e) {
					encontrou = false;
					System.out.println("Esta matrícula não foi encontrada no arquivo !\n");
				} catch (IOException e) {
					System.out.println("Erro na abertura do arquivo - programa será finalizado");
					System.exit(0);
				}
			} while (!encontrou);
			if (codChave.equals("FIM")) {
				System.out.println("\n ************ PROGRAMA ENCERRADO ************** \n");
				break;
			}
			cliente.ativo = 'N';
			System.out.println("Nome do cliente.......: " + cliente.nomeCliente);
			System.out.println("Ano Primeira Compra...: " + cliente.anoPrimeiraCompra);
			System.out.println("Valor da compra.......: " + cliente.vlrCompra);
			if (cliente.emDia) {
				emDia = 'S';
			} else {
				emDia = 'N';
			}
			System.out.println("Em dia................: " + emDia);
			System.out.println();
			do {
				System.out.print("\nConfirma a exclusão deste cliente (S/N) ? ");
				confirmacao = leia.next().charAt(0);
				if (confirmacao == 'S') {
					try {
						arquivo = new RandomAccessFile("CLIENTES.DAT", "rw");
						arquivo.seek(posicaoRegistro);
						arquivo.writeChar(cliente.ativo);
						arquivo.close();
						System.out.println("cliente excluído com sucesso !\n");
					} catch (IOException e) {
						System.out.println("Erro na gravaçao do registro - programa será finalizado");
						System.exit(0);
					}
				}
			} while (confirmacao != 'S' && confirmacao != 'N');
			leia.nextLine();
		} while (!cliente.codCliente.equals("FIM"));

	}

}