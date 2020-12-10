package com.example.motorplace.model

class Usuario(
    var nome: String = "",
    var cpf: String = "",
    var telefone: String = "",
    var dataNasc: String = "",
    var foto: String = "",
    var email: String = "",
    var adm: String = ""
)

class Carro(
    var idUsuario: String = "",
    var placa: String = "",
    var modelo: String = "",
    var marca: String = "",
    var ano: String = "",
    var cor: String = "",
    var tamanho: String = ""
)

class Servico(
    var id : String = "",
    var titulo : String = "",
    var foto : String = "",
    var descricao : String = "",
    var categoria: String = "",
    var valor : String = "",
    var custo: String = "",
    var prazo: String = ""
)

class ServicosSolicitados(
    var idCliente : String = "",
    var nomeCliente : String = "",
    var idServico : String = "",
    var diaSemana : String = "",
    var data : String ="",
    var hora : String ="",
    var id:String = ""
)

class Produto(
    var id : String = "",
    var titulo : String = "",
    var foto : String = "",
    var descricao : String = "",
    var marca: String = "",
    var categoria: String ="",
    var qtdEstoque: String = "",
    var alertaQtdMin: String = "",
    var valor : String = "",
    var custo: String = ""
)

class Favorito(
    var favorito : String = "",
)
class Compras(
    var valor : String = "",
    var custo: String = ""
)

