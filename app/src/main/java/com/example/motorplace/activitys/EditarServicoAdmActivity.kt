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
import com.example.motorplace.model.Servico
import com.example.motorplace.model.Usuario
import com.example.motorplace.util.Permissao
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_atualizar_carro.*
import kotlinx.android.synthetic.main.activity_editar_servico_adm.*
import java.io.ByteArrayOutputStream

class EditarServicoAdmActivity : AppCompatActivity() {
    private lateinit var data : Bundle
    private lateinit var titulo:TextView
    private lateinit var descricao:TextView
    private lateinit var valor:TextView
    private lateinit var custo:TextView
    private lateinit var foto : String
    private  var categoria : String = ""
    private lateinit var imagemPerfil: ImageView
    private val permisssaoCamera= arrayOf(Manifest.permission.CAMERA) //array com as permições que o app precisará (camera)
    private val permisssaoGaleria = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) //array com as permições que o app precisará (Galeria)
    private val SELECAO_CAMERA = 100
    private val SELECAO_GALERIA = 200
    private var imagem: Bitmap? = null
    private lateinit var storageReference : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_servico_adm)
        supportActionBar!!.title ="Editar Serviço"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        data = intent.extras!!
        inicializar()

        imagemPerfil.setOnClickListener {
            mudarFoto()
        }

        btn_alterar_servico.setOnClickListener {
            salvar()
        }

        btn_cancelar_servico.setOnClickListener {
            finish()
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
                                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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

    fun salvar(){
        val pd = ProgressDialog(this)
        pd.setMessage("Salvando...")
        pd.show()
        titulo = findViewById(R.id.txt_titulo_servico_editar)
        descricao = findViewById(R.id.txt_descricao_editar)
        valor = findViewById(R.id.txt_valor_editar)
        custo = findViewById(R.id.txt_custo_editar)

        val servico = Servico()
        servico.titulo = titulo.text.toString()
        servico.descricao = descricao.text.toString()
        servico.valor = valor.text.toString()
        servico.custo = custo.text.toString()
        servico.foto = foto
        servico.categoria = categoria
        servico.id = data.getString("id")!!

       val database = FirebaseDatabase.getInstance().reference

        database.child("servicos").child(data.getString("id")!!).setValue(servico).addOnCompleteListener {
            if(it.isSuccessful){
                if (imagem != null){
                    salvarFoto(database.child("servicos").child(data.getString("id")!!),data.getString("id")!!)
                }else{
                    Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this,HomeAdmActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent)
                }
            }
        }
    }

    fun salvarFoto(ref: DatabaseReference, id : String){
        //recuperar dados da imagem para o firebase
        val baos =  ByteArrayOutputStream()

        imagem?.compress(Bitmap.CompressFormat.JPEG, 70, baos)

        val dadosImagem = baos.toByteArray()

        //Salvar no Firebase
        val imagemRef = storageReference
            .child("imagens")
            .child("servicos")
            .child(id)
            .child("servico.jpeg")

        val uploadTask = imagemRef.putBytes(dadosImagem)
        uploadTask.addOnFailureListener{
            //Se houve erro no upload da imageFile
            Toast.makeText(this, "Erro ao salvar  a foto", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            imagemRef.downloadUrl.addOnSuccessListener {
                ref.child("foto").setValue(it.toString())
            }
            //Se o upload da imageFile foi realizado com sucesso
            Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this,HomeAdmActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
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
            } catch (e:java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun alertaPermissao(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissão Negada")
        builder.setMessage("Para poder acessar este recurso é neceessário aceitar a permissão")
        builder.setCancelable(false)
        builder.setPositiveButton("OK" ){ dialogInterface, i ->  }


        val dialog = builder.create()
        dialog.show()
    }

    fun inicializar(){
        titulo = findViewById(R.id.txt_titulo_servico_editar)
        descricao = findViewById(R.id.txt_descricao_editar)
        valor = findViewById(R.id.txt_valor_editar)
        custo = findViewById(R.id.txt_custo_editar)

        imagemPerfil = findViewById(R.id.image_servicos_editar)
        storageReference = FirebaseStorage.getInstance().reference


        titulo.text = data.get("titulo").toString()
        descricao.text = data.get("descricao").toString()
        valor.text = data.get("valor").toString()
        custo.text = data.get("custo").toString()
        foto = data.getString("foto")!!

        Picasso.get()
            .load(foto)
            .into(image_servicos_editar)

        //preenche os dados do spinner
        val spinnerCategoria: Spinner = findViewById(R.id.spinner_editar_servicos)
        ArrayAdapter.createFromResource(
            this, R.array.categoria, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerCategoria.adapter = adapter
        }

        //verifica o spinner selecionado
        spinnerCategoria.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                categoria = spinnerCategoria.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        val cat = data.getString("categoria")
        if(cat.equals("Revisão e Manutenção")){
           spinnerCategoria.setSelection(1)
            categoria = "Revisão e Manutenção"
        }else if(cat.equals("Pintura e Funilária")){
            spinnerCategoria.setSelection(2)
            categoria = "Pintura e Funilária"
        }else if(cat.equals("Eletrica")){
            spinnerCategoria.setSelection(3)
            categoria = "Eletrica"
        }
    }
}