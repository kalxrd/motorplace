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