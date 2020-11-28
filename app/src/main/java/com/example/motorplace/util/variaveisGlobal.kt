package com.example.motorplace.util

import com.example.motorplace.model.Carro
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.model.Usuario

var userAtual : Usuario = Usuario()

var carroAtual : Carro = Carro()

var servicosSolicitadosGlobal = arrayListOf<ServicosSolicitados>()

var datasSolicitadas = arrayListOf<String>()

var servicosGlobal = arrayListOf<Servico>()