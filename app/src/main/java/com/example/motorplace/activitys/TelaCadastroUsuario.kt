package com.example.motorplace.activitys

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.motorplace.R
import com.example.motorplace.model.Carro
import com.example.motorplace.model.Usuario
import com.example.motorplace.util.MyMaskEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_tela_cadastro_usuario.*
import kotlinx.android.synthetic.main.activity_tela_de_entrada.*

class TelaCadastroUsuario : AppCompatActivity() {
    private lateinit var linear1: LinearLayout
    private lateinit var linear2: LinearLayout
    private var pgtTamanho: String = ""
    private lateinit var auth:FirebaseAuth
    private lateinit var reference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_cadastro_usuario)
        supportActionBar!!.title ="Cadastro"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        inicializar()


        verificaEtapa()

    }

    fun inicializar(){
        linear1 = findViewById(R.id.step1)
        linear2 = findViewById(R.id.step2)
        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        //coloca a mascara nos editTexts
        editTextAno.myCustomMask("####")
        editTextDataN.myCustomMask("##/##/####")
        editTextTel.myCustomMask("(##) #####-####")
        editTextCpf.myCustomMask("###.###.###-##")


        val spinnerTamanho: Spinner = findViewById(R.id.spinner_tamanho)
        ArrayAdapter.createFromResource(
            this, R.array.tamanho, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerTamanho.adapter = adapter
        }

        spinnerTamanho.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtTamanho = spinnerTamanho.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    }

    fun verificaEtapa(){
        progresso.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

        //verifica se a barra de progresso esta ativa
        if (progresso != null) {
            btn_continuar.setOnClickListener() {
                //se a barra estiver na etapa 1, tornara a etapa 2 desativada

                if (progresso.currentStateNumber == 1) {

                    if(confirma(1)) {
                       btn_voltar.visibility = View.VISIBLE

                        progresso.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)

                        linear1.visibility = View.GONE
                        linear2.visibility = View.VISIBLE

                        btn_continuar.text = "Cadastrar"
                    }

                } else if (progresso.currentStateNumber == 2) {
                    confirma(2)
                }
            }

            btn_voltar.setOnClickListener() {

                progresso.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)

                linear1.visibility = View.VISIBLE
                linear2.visibility = View.GONE

                btn_continuar.text = "Continuar >"

                btn_voltar.visibility = View.GONE
            }
        }
    }

    //verifica campos conforme a etapa
    fun confirma(etapa: Int): Boolean{
        var nome = editTextNome.text.toString()
        var cpf = editTextCpf.text.toString()
        var tel = editTextTel.text.toString()
        var data = editTextDataN.text.toString()
        var email = editTextEmail.text.toString()
        var senha = editTextSenha.text.toString()
        var confirmarSenha = editTextSenhac.text.toString()

        var valido = true

        if(etapa == 1){
            if(nome.isEmpty()){
                editTextNome.error = "Campo obrigatório!"
                valido = false
            }
            if(cpf.isEmpty()){
                editTextCpf.error = "Campo obrigatório!"
                valido = false
            }
            if(tel.isEmpty()){
                editTextTel.error = "Campo obrigatório!"
                valido = false
            }
            if(data.isEmpty()){
                editTextDataN.error = "Campo obrigatório!"
                valido = false
            }
            if(email.isEmpty()){
                editTextEmail.error = "Campo obrigatório!"
                valido = false
            }
            if(senha.isEmpty()){
                editTextSenha.error = "Campo obrigatório!"
                valido = false
            }
            if(confirmarSenha.isEmpty()){
                editTextSenhac.error = "Campo obrigatório!"
                valido = false
            }

            if(!senha.isEmpty() && !confirmarSenha.isEmpty() && !senha.equals(confirmarSenha)){
                editTextSenha.error = "Senhas diferentes"
                editTextSenhac.error = "Senhas diferentes"

                valido = false
            }

            if(!valido){
                return false
            }
        } else if(etapa == 2){
            var placa = editTextPlacaCarro.text.toString()
            var modelo = editTextModelo.text.toString()
            var marca = editTextMarca.text.toString()
            var ano = editTextAno.text.toString()
            var cor = editTextCor.text.toString()
            //var tamanho = editTextTamanho.text.toString()

            if(placa.isEmpty()){
                editTextPlacaCarro.error = "Campo obrigatório!"
                valido = false
            }
            if(modelo.isEmpty()){
                editTextModelo.error = "Campo obrigatório!"
                valido = false
            }
            if (marca.isEmpty()){
                editTextMarca.error = "Campo obrigatório!"
                valido = false
            }
            if(ano.isEmpty()){
                editTextAno.error = "Campo obrigatório!"
                valido = false
            }
            if (cor.isEmpty()){
                editTextCor.error = "Campo obrigatório!"
                valido = false
            }

            //se tiver alguma informação faltando retorna false. Se não faz o cadastro
            if(!valido){
                return false
            }else{
                //verifica se o spinner foi preenchido
                if(pgtTamanho.isEmpty()){
                    Toast.makeText(this,"Preencha o tamanho",Toast.LENGTH_SHORT).show()
                    return false
                }

                //seta os valores para as classes usuario e carro
                var user = Usuario()
                var carro = Carro()

                user.nome = nome
                user.cpf = cpf
                user.telefone = tel
                user.dataNasc = data
                user.email = email

                carro.placa = placa
                carro.modelo = modelo
                carro.marca = marca
                carro.ano = ano
                carro.cor = cor
                carro.tamanho = pgtTamanho

                cadastrarUsuario(user,carro,senha)
            }
        }

        return true
    }

     private fun cadastrarUsuario(user:Usuario, carro: Carro,senha:String){

        var builder = android.app.AlertDialog.Builder(this).create()
        var view = layoutInflater.inflate(R.layout.progress_bar, null)
        builder.setView(view)
        builder.show()


        val auth = FirebaseAuth.getInstance()
        val reference = FirebaseDatabase.getInstance().reference


         auth.createUserWithEmailAndPassword(user.email, senha)
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     val usuarios = reference.child("usuarios")
                     val ref = usuarios.child(auth!!.currentUser!!.uid)
                     ref.setValue(user).addOnCompleteListener {
                         if (it.isSuccessful){
                             val carros = reference.child("carros")
                             val refCarros = carros.child(auth!!.currentUser!!.uid)
                             carro.idUsuario = auth!!.currentUser!!.uid
                             refCarros.setValue(carro).addOnCompleteListener {
                                 if(it.isSuccessful){
                                     Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                     auth.signOut()
                                     val intent = Intent(this, TelaDeLogin::class.java)
                                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                     startActivity(intent)
                                 }
                             }
                         }
                     }
                 } else {
                     try {
                         throw task.exception!!
                     } catch (e: FirebaseAuthWeakPasswordException) {
                         Toast.makeText(
                             this,
                             "Senha fraca!",
                             Toast.LENGTH_SHORT
                         ).show()
                     } catch (e: FirebaseAuthInvalidCredentialsException) {
                         Toast.makeText(
                             this,
                             "Email inválido!",
                             Toast.LENGTH_SHORT
                         ).show()
                     } catch (e: FirebaseAuthUserCollisionException) {
                         Toast.makeText(
                             this,
                             "Usuário já cadastrado!",
                             Toast.LENGTH_SHORT
                         ).show()
                     } catch (e: Exception) {
                         Toast.makeText(
                             this,
                             "" + e.message,
                             Toast.LENGTH_SHORT
                         ).show()
                     }
                     finish()
                 }
             }
    }

    //metodo de acesso a classe da maskara
    fun EditText.myCustomMask(mask: String) {
        addTextChangedListener(MyMaskEditText(this, mask))}
}

