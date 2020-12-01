package com.example.motorplace.activitys

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.example.motorplace.R
import com.example.motorplace.model.Produto
import com.example.motorplace.util.MoneyTextWatcher
import com.example.motorplace.util.Permissao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_cadastrar_produto.*
import java.io.ByteArrayOutputStream
import java.util.*

class CadastrarProdutoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var imagemPerfil: ImageView
    private val permisssaoCamera= arrayOf(Manifest.permission.CAMERA) //array com as permições que o app precisará (camera)
    private val permisssaoGaleria = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) //array com as permições que o app precisará (Galeria)
    private val SELECAO_CAMERA = 100
    private val SELECAO_GALERIA = 200
    private var imagem: Bitmap? = null
    private lateinit var storageReference : StorageReference
    private var pgtCategoria: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_produto)
        supportActionBar!!.title ="Cadastrar produto"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //criacao do spinner
        val spinnerFiltro = resources.getStringArray(R.array.categoria_produto) //recupera o array do string.xml
        spinner_cadastro_produto.setAdapter(
            ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item, spinnerFiltro
            )
        ) //seta o adapter no spinner
        inicializar()

        image_produto.setOnClickListener {
            mudarFoto()
        }
        btn_cadastrar_produto.setOnClickListener {
            veririfcaCampos()
        }
    }

    private fun inicializar(){
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        imagemPerfil = findViewById(R.id.image_produto)
        storageReference = FirebaseStorage.getInstance().reference

        spinner_cadastro_produto.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                pgtCategoria =  spinner_cadastro_produto.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        var mLocale = Locale("pt", "BR")

        txt_valor_produto.addTextChangedListener(MoneyTextWatcher(txt_valor_produto,mLocale))
        txt_custo_produto.addTextChangedListener(MoneyTextWatcher(txt_custo_produto,mLocale))

    }

    private fun veririfcaCampos(){
        var titulo = txt_titulo_produto.text.toString()
        var descricao = txt_descricao_produto.text.toString()
        var marca = txt_marca_produto.text.toString()
        var estoque = txt_qtd_estoque.text.toString()
        var alerta = txt_alerta.text.toString()
        var valor = txt_valor_produto.text.toString()
        var custo = txt_custo_produto.text.toString()


        var valido = true

        if(titulo.isEmpty()){
            txt_titulo_produto.error = "Campo obrigatório"
            valido = false
        }
        if(descricao.isEmpty()){
            txt_descricao_produto.error = "Campo obrigatório"
            valido = false
        }
        if(marca.isEmpty()){
            txt_marca_produto.error = "Campo obrigatório"
            valido = false
        }
        if(estoque.isEmpty()){
            txt_qtd_estoque.error = "Campo obrigatório"
            valido = false
        }
        if(alerta.isEmpty()){
            txt_alerta.error = "Campo obrigatório"
            valido = false
        }

        if(valor.isEmpty()){
            txt_valor_produto.error = "Campo obrigatório"
            valido = false
        }
        if(custo.isEmpty()){
            txt_custo_produto.error = "Campo obrigatório"
            valido = false
        }



        if(valido && pgtCategoria.isEmpty()){
            Toast.makeText(this, "Escolha uma categoria", Toast.LENGTH_SHORT).show()
        }else if(valido && imagem == null){
            Toast.makeText(this, "Selecione uma Foto", Toast.LENGTH_SHORT).show()
        }else if(valido && !pgtCategoria.isEmpty() && !(imagem == null)){
            val produto  = Produto()
            produto.titulo = titulo
            produto.descricao = descricao
            produto.marca = marca
            produto.categoria = pgtCategoria
            produto.qtdEstoque = estoque
            produto.alertaQtdMin = alerta
            produto.valor = valor
            produto.custo = custo

            salvar(produto)
        }

    }

    fun mudarFoto() {
        val modes = arrayOf("Tirar foto da câmera", "Selecionar foto da galeria")

        //Cria uma AlertDialog
        val builder = AlertDialog.Builder(this)

        //Seta o título
        builder.setTitle("Selecione uma opção") //Seta os itens de opção
            .setItems(modes) { dialogInterface, i ->

                //Verifica o índice do item
                when (i) {
                    0 -> //abre as permissoes para camera/ se o usuario tiver permissao irá abrir a camera
                        if (Permissao.validarPermissao(permisssaoCamera, this, SELECAO_CAMERA)) {
                            if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                if (intent.resolveActivity(packageManager) != null) {
                                    startActivityForResult(intent, SELECAO_CAMERA)
                                }
                            } else {
                                //caso não tenha acesso aos recursos da camera
                            }
                        }
                    1 -> {
                        //abre as permissoes para galeria/ se o usuario tiver permissao irá abrir a galeria
                        if (Permissao.validarPermissao(permisssaoGaleria, this, SELECAO_GALERIA)) {
                            val intent =
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivityForResult(intent, SELECAO_GALERIA)
                            }
                        }
                    }
                }
            }
            .create()
            .show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao()

            }else if (requestCode == SELECAO_GALERIA) { //se foi aceita a permissao ira abrir a opcao da camera ou galeria
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA)
                }

            } else if (requestCode == SELECAO_CAMERA) {
                if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivityForResult(intent, SELECAO_CAMERA)
                    }
                } else {
                    //notFound
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                when (requestCode) {
                    SELECAO_CAMERA -> {
                        imagem = data?.extras?.get("data") as Bitmap
                    }
                    SELECAO_GALERIA -> {
                        val localImagemSelecionada = data?.data
                        imagem = MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            localImagemSelecionada
                        )
                    }
                }
                if(imagem != null){
                    //recuperar dados da imagem para o firebase
                    imagemPerfil.setImageBitmap(imagem)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun alertaPermissao(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissão Negada")
        builder.setMessage("Para poder acessar este recurso é neceessário aceitar a permissão")
        builder.setCancelable(false)
        builder.setPositiveButton("OK"){ dialogInterface, i ->  }


        val dialog = builder.create()
        dialog.show()
    }

    fun salvar(produto: Produto){
        val pd = ProgressDialog(this)
        pd.setMessage("Salvando...")
        pd.show()

        val produtoRef = database.child("produtos")
        var id = produtoRef.push().key
        produtoRef.child(id!!).setValue(produto).addOnCompleteListener {
            if(it.isSuccessful){
                salvarFoto(produtoRef.child(id!!), id)
            }
        }
    }
    fun salvarFoto(ref: DatabaseReference, id: String){
        //recuperar dados da imagem para o firebase
        val baos =  ByteArrayOutputStream()

        imagem?.compress(Bitmap.CompressFormat.JPEG, 70, baos)

        val dadosImagem = baos.toByteArray()

        //Salvar no Firebase
        val imagemRef = storageReference
            .child("imagens")
            .child("produtos")
            .child(id)
            .child("produto.jpeg")

        val uploadTask = imagemRef.putBytes(dadosImagem)
        uploadTask.addOnFailureListener{
            //Se houve erro no upload da imageFile
            Toast.makeText(this, "Erro ao salvar  a foto", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            imagemRef.downloadUrl.addOnSuccessListener {
                ref.child("foto").setValue(it.toString())
                ref.child("id").setValue(id)
            }
            //Se o upload da imageFile foi realizado com sucesso
            Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, HomeAdmActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }
    }
}